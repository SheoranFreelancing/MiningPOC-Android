package com.drill.sync;

import com.drill.responsemodel.AggregateMerchantList;
import com.drill.responsemodel.Contract;
import com.drill.responsemodel.CreatedDeal;
import com.drill.responsemodel.Deal;
import com.drill.responsemodel.Deals;
import com.drill.responsemodel.Merchant;
import com.drill.responsemodel.MerchantDetails;
import com.drill.responsemodel.MerchantList;
import com.drill.responsemodel.MerchantLocation;
import com.drill.responsemodel.MerchantLocations;
import com.drill.responsemodel.Merchants;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by GROUPON on 25/09/15.
 */
public interface L3HttpService {


    //List CrawledMerchants

    @GET("/v1/search/crawledmerchants")
    public void getCrawledMerchant(@Query("q") String search, Callback<AggregateMerchantList> response);


    //List Merchants

    @GET("/v1/merchants")
    public void getMerchant(Callback<MerchantList> response);



    // Create/Update Merchants

    @PUT("/v1/merchants/{merchantId}")
    public void updateMerchant(@Path("merchantId") String merchantId, @Body Merchant merchant, Callback<Merchants> response);


    @POST("/v1/merchants")
    public void createMerchant(@Body Merchant merchant, Callback<Merchants> response);


    @POST("/v1/merchants")
    public void updateClaimStatus(@Query("crawledMerchantLocationId") String crawledMerchantLocationId,@Body Merchant merchant, Callback<Merchants> response);


    //Create/Update Deals

    @POST("/v1/deals/")
    public void createDeals(@Body Deal deal, Callback<CreatedDeal> response);


    @PUT("/v1/deals/{dealId}")
    public void updateDeals(@Path("dealId") String dealId, @Body Deal deal, Callback<CreatedDeal> response);


    @GET("/v1/deals/")
    public void getLocationDeals(@Query("merchantLocationId") String merchantLocationId, Callback<Deals> response);

    //List Merchant Details

    @GET("/v1/merchants/{merchantId}/locations")
    public void getMerchantDetails(@Path("merchantId") String merchantId, Callback<MerchantDetails> response);

    //Location

    @POST("/v1/merchants/{merchantId}/locations")
    public void createLocationDetails(@Path("merchantId") String merchantId, @Body MerchantLocation merchantLocation, Callback<MerchantLocations> response);

    @PUT("/v1/merchants/{merchantId}/locations/{locationId}")
    public void updateLocationDetails(@Path("merchantId") String merchantId, @Path("locationId") String locationId, @Body MerchantLocation merchantLocation, Callback<MerchantLocations> response);


   //Contract
    @PUT("/v1/merchants/{merchantId}/locations/{locationId}/contract")
    public void updateContractStatus(@Path("merchantId") String merchantId, @Path("locationId") String locationId, @Body Contract contract, Callback<MerchantLocations> response);


    //Nearby
    @GET("/v1/search/crawledmerchants")
    public void getNearbyMerchants(@Query("longitude") String longitude, @Query("latitude") String latitude, Callback<AggregateMerchantList> response);


}
