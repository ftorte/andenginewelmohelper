package com.welmo.andengine.utility.inappbilling;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.welmo.andengine.ui.SimpleWelmoActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;


/**
* Provides convenience methods for in-app billing. You can create one instance of this
* class for your application and use it to process in-app billing operations.
* It provides synchronous (blocking) and asynchronous (non-blocking) methods for
* many common in-app billing operations, as well as automatic signature
* verification.
*
* After instantiating, you must perform setup in order to start using the object.
* To perform setup, call the {@link #startSetup} method and provide a listener;
* that listener will be notified when setup is complete, after which (and not before)
* you may call other methods.
*
* After setup is complete, you will typically want to request an inventory of owned
* items and subscriptions. See {@link #queryInventory}, {@link #queryInventoryAsync}
* and related methods.
*
* When you are done with this object, don't forget to call {@link #dispose}
* to ensure proper cleanup. This object holds a binding to the in-app billing
* service, which will leak unless you dispose of it correctly. If you created
* the object on an Activity's onCreate method, then the recommended
* place to dispose of it is the Activity's onDestroy method.
*
* A note about threading: When using this object from a background thread, you may
* call the blocking versions of methods; when using from a UI thread, call
* only the asynchronous versions and handle the results via callbacks.
* Also, notice that you can only call one asynchronous operation at a time;
* attempting to start a second asynchronous operation while the first one
* has not yet completed will result in an exception being thrown.
*
* @author Bruno Oliveira (Google)
*
*/
public class PurchasingManager {
	
	//Static strings
	final static String 	TAG = "PurchasingManager";
	
	// Interface
	public interface IAPurchasing {
		/**
		 * Called to notify that a consumption has finished.
		 *
		 * @param purchase The purchase that was (or was to be) consumed.
		 * @param result The result of the consumption operation.
		 */
		public void onConsumeFinished(Purchase purchase, IabResult result);
		/**
		 * Called to notify that a consumption of multiple items has finished.
		 *
		 * @param purchases The purchases that were (or were to be) consumed.
		 * @param results The results of each consumption operation, corresponding to each
		 *     sku.
		 */
		public void onConsumeMultiFinished(List<Purchase> purchases, List<IabResult> results);
		/**
		 * Called to notify that an in-app purchase finished. If the purchase was successful,
		 * then the sku parameter specifies which item was purchased. If the purchase failed,
		 * the sku and extraData parameters may or may not be null, depending on how far the purchase
		 * process went.
		 *
		 * @param result The result of the purchase.
		 * @param info The purchase information (null if purchase failed)
		 */
		public void onIabPurchaseFinished(IabResult result, Purchase info);
		/**
		 * Called to notify that setup is complete.
		 *
		 * @param result The result of the setup process.
		 */
		public void onIabSetupFinished(IabResult result);
		/**
		 * Called to notify that an inventory query operation completed.
		 *
		 * @param result The result of the operation.
		 * @param inv The inventory.
		 */
		public void onQueryInventoryFinished(IabResult result, Inventory inv);
	}
	   
	//hnadle sku product list
	public enum SKUS_TYPES{CONSUMABLE,NOT_CONSUMABLE,SUBSCRIPTION};
	
	//inventoris
	public Inventory 							mInventory					= null;
	public Inventory 							mDefaultInventory			= null;
	
	//constants 
	ArrayList<String>							catalogueConsumable			= null;
	ArrayList<String>							catalogueNotConsumable		= null;
	ArrayList<String>							catalogueSubscription		= null;
	
		
	//Map<Pair<SKUS_TYPES,String>, Purchase>		puchasedProducts			= null;
	
	//The activity that use this purchasing manager
	protected Activity 							theActivity					= null;
	// A wrapper class including all code to manage the Purchasing
	protected IabHelper							mHelper						= null;
	
	//The response Interface
	protected IAPurchasing						mIAPurchasing				= null;
	
	public PurchasingManager(Activity activity, IAPurchasing IIAPurchasing){
		theActivity 				= activity;
		catalogueConsumable			= new ArrayList<String>();
		catalogueNotConsumable		= new ArrayList<String>();
		catalogueSubscription		= new ArrayList<String>();
		mIAPurchasing				= IIAPurchasing;
		mInventory					= new Inventory();
		mDefaultInventory			= new Inventory();
	}
	
	//add product to the product list
	public boolean addProductCatalogue(SKUS_TYPES theType, String skuID){
		switch(theType){
		
		case CONSUMABLE:
			if(catalogueConsumable.contains(skuID))
				return false;
			return catalogueConsumable.add(skuID);
			
		case NOT_CONSUMABLE:
			if(catalogueNotConsumable.contains(skuID))
				return false;
			return catalogueNotConsumable.add(skuID);
		case SUBSCRIPTION:
			if(catalogueSubscription.contains(skuID))
				return false;
			return catalogueSubscription.add(skuID);
			
		default:
			return false;
		}
	}
	
	public void addPurchasedProduct(Purchase pProduct){
		mInventory.addPurchase(pProduct);
	}
	
	public void connectService(Activity activity, String strPubblicKey){

		// Create the helper, passing it our context and the public key to verify signatures with
		Log.d(TAG, "Connect to IAB Service.");

		mHelper = new IabHelper(activity, strPubblicKey);

		// enable debug logging (for a production application, you should set this to false).
		mHelper.enableDebugLogging(true);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		mHelper.startSetup(mIAPurchasing);
	}
    
	public void getInventory(Activity activity, String strPubblicKey){
			mHelper.queryInventoryAsync(mIAPurchasing);
	}
	
	public void queryInventoryAsync(final boolean querySkuDetails, final IAPurchasing listener) {
		this.mHelper.queryInventoryAsync(querySkuDetails,listener);
	}
	public boolean launchPurchaseFlow(Activity act, String sku, String itemType, int requestCode,
			IAPurchasing listener, String extraData) {
		
		if (!mHelper.mSetupDone){
			return false;
	
		}
		this.mHelper.launchPurchaseFlow(act, sku, itemType,requestCode,listener,"");
		return true;
	}
	public void  handleActivityResult(int requestCode, int resultCode, Intent data){
		this.mHelper.handleActivityResult(requestCode, resultCode, data);
		
	}
	/**********************************************************************************
	 * add a product to the list of products owned by default
	 **********************************************************************************/
	public void addDefaultProductOwned(Purchase product){
		if(mDefaultInventory.hasPurchase(product.getSku()))
			mDefaultInventory.erasePurchase(product.getSku());
		mDefaultInventory.addPurchase(product);
	}
	/**********************************************************************************
	 * store the list of owned products in the prefrence file
	 **********************************************************************************/
	public void storeProductOwned(Editor mPreferencesEditor){
		if(mDefaultInventory != null) storeProductOwned(mPreferencesEditor,mDefaultInventory);
		if(mInventory != null) storeProductOwned(mPreferencesEditor,mInventory);
	}
	private void storeProductOwned(Editor mPreferencesEditor, Inventory theInv){
		
		//get updated inventory to update share preferences => This includes the default products
		List<String> allOwnedSkus = theInv.getAllOwnedSkus();
		//get iterator on product owned
		ListIterator<String> it = allOwnedSkus.listIterator();

		boolean hasproduct = false;		//by default consider that there are no product owned				
		if(it.hasNext()) hasproduct = true;

		while(it.hasNext()){
			String sku = it.next();
			mPreferencesEditor.putBoolean(sku, true);
		}
		if(hasproduct) mPreferencesEditor.commit();
	}
	/**********************************************************************************
	 * ensure that for each product in teh catalog nothing is stored in the preference file
	 * except for the default products
	 **********************************************************************************/
	public void clearStoredInventory(Editor mPreferencesEditor) {
		
		boolean prodcatalogue = false;
		
		//reset preference file for consumables
		Iterator<String> it = this.catalogueConsumable.iterator();
		if (it.hasNext()) prodcatalogue = true;
		while (it.hasNext()){
			mPreferencesEditor.putBoolean(it.next(), false);
		}

		//reset preference file for NON consumables
		it = this.catalogueNotConsumable.iterator();
		if (it.hasNext()) prodcatalogue = true;
		while (it.hasNext()){
			mPreferencesEditor.putBoolean(it.next(), false);
		}
		
		//reset preference file for services
		it = this.catalogueSubscription.iterator();
		if (it.hasNext()) prodcatalogue = true;
		while (it.hasNext()){
			mPreferencesEditor.putBoolean(it.next(), false);
		}
		
		if(prodcatalogue)
			mPreferencesEditor.commit();
		
		//ensure default product are stored in the preference file
		storeProductOwned(mPreferencesEditor);	
	}
	public void updateInventory(Inventory inv) {
		mInventory = inv;
	}
	public boolean hasPurchase(String sku){
		if(this.mDefaultInventory.hasPurchase(sku))
			return true;
		
		return this.mInventory.hasPurchase(sku);
	}
	
}