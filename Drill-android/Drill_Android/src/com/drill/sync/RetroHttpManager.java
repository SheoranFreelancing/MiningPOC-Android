package com.drill.sync;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.drill.db.MerchantDatabase;
import com.drill.android.R;
import com.drill.responsemodel.AggregateMerchantList;
import com.drill.responsemodel.Contract;
import com.drill.responsemodel.CrawledMerchant;
import com.drill.responsemodel.CreatedDeal;
import com.drill.responsemodel.Deal;
import com.drill.responsemodel.Deals;
import com.drill.responsemodel.Merchant;
import com.drill.responsemodel.MerchantDetails;
import com.drill.responsemodel.MerchantList;
import com.drill.responsemodel.MerchantLocation;
import com.drill.responsemodel.MerchantLocations;
import com.drill.responsemodel.Merchants;
import com.drill.responsemodel.Results;
import com.drill.responsemodel.Special;
import com.drill.ui.LT_BaseActivity;
import com.drill.utils.LT_Utils;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by GROUPON on 25/09/15.
 */
public class RetroHttpManager{

    private int requestType;
    private LT_BaseActivity responseListener;

    public List<Merchant> searchList = null;


    static Context context = null;


    private static String BASE_SERVICE_URL;

    public static final int REQUEST_GET_MERCHANTS = 5;
    public static final int REQUEST_CREATE_MERCHANT = 6;
    public static final int REQUEST_UPDATE_MERCHANT = 7;

    public static final int REQUEST_GET_MERCHANT_LOCATIONS = 8;
    public static final int REQUEST_CREATE_MERCHANT_LOCATION = 9;
    public static final int REQUEST_UPDATE_MERCHANT_LOCATION = 10;

    public static final int REQUEST_GET_LOCATIONS_DEALS = 11;
    public static final int REQUEST_CREATE_LOCATION_DEALS = 12;
    public static final int REQUEST_UPDATE_LOCATION_DEALS = 13;

    public static final int REQUEST_GET_SEARCH_CRAWL_MERCHANTS = 14;
    public static final int REQUEST_GET_SEARCH_MERCHANTS = 15;

    public static final int REQUEST_CREATE_MERCHANT_AND_UPDATE_CLAIM_STATUS = 16;
    public static final int REQUEST_UPDATE_CONTRACT_STATUS = 17;
    public static final int REQUEST_GET_NEARBY_MERCHANTS = 18;

    public static final int REQUEST_CLOUDINARY_DOWNLOAD = 101;
    public static final int REQUEST_CLOUDINARY_UPLOAD = 102;


    public static RetroHttpManager create(LT_BaseActivity responseListener, int requestType) {
        RetroHttpManager manager =  new RetroHttpManager();
        manager.responseListener = responseListener;
        manager.requestType = requestType;
        context = responseListener;
        return manager;
    }

    public static void setBaseServiceUrl(String baseServiceUrl) {
        BASE_SERVICE_URL = baseServiceUrl;
    }

    public static boolean isNetworkAvailable() {
        if (null == LT_Utils.appContext)
            return true;

        ConnectivityManager cm = (ConnectivityManager) LT_Utils.appContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }



    public static OkClient getOkClient (){
        OkHttpClient client = new OkHttpClient();
        client = StubSSLTrust.getUnsafeOkHttpClient();
        OkClient _client = new OkClient(client);
        return _client;
    }



    public boolean sendRequest(String search){

        OkClient okClient = getOkClient();

        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setClient(okClient).setEndpoint(BASE_SERVICE_URL).build();
        L3HttpService httpService = restAdapter.create(L3HttpService.class);

        if(!isNetworkAvailable()) {
            Toast.makeText(responseListener, R.string.network_disconnected_error, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            responseListener.showProgressView();
        }

        switch (requestType){
            case REQUEST_GET_SEARCH_CRAWL_MERCHANTS:
                httpService.getCrawledMerchant(search, new Callback<AggregateMerchantList>() {
                    @Override
                    public void success(AggregateMerchantList merchantList, Response response) {
                        Log.d("L3Http", "response: " + response);
                        Results resultList = merchantList.getResults();
                        List<Merchant> merchants = resultList.getMerchants();
                        MerchantDatabase.getInstance().saveMerchants(merchants);
                        List<CrawledMerchant> crawledMerchantList = resultList.getCrawledmerchants();
                        MerchantDatabase.getInstance().saveCrawledMerchantList(crawledMerchantList);

                        onSuccess(REQUEST_GET_SEARCH_CRAWL_MERCHANTS);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("L3Http", "error: " + error.getCause());
                        responseListener.onFailure(error);

                    }
                });
                break;

            case REQUEST_GET_MERCHANTS:

                httpService.getMerchant(new Callback<MerchantList>() {
                    @Override
                    public void success(MerchantList merchantList, Response response) {

                        searchList = merchantList.getMerchants();
                        MerchantDatabase.getInstance().saveMerchantsinDB((ArrayList<Merchant>) searchList);
                        onSuccess(REQUEST_GET_MERCHANTS);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("L3Http", "error: " + error.getCause());
                        responseListener.onFailure(error);
                    }
                });

                break;

            case REQUEST_GET_MERCHANT_LOCATIONS:    //Get details
                httpService.getMerchantDetails(search, new Callback<MerchantDetails>() {
                    @Override
                    public void success(MerchantDetails merchantDetails, Response response) {
                        onSuccess(REQUEST_GET_MERCHANT_LOCATIONS, merchantDetails);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        responseListener.onFailure(error);
                    }
                });

                break;

            case REQUEST_GET_LOCATIONS_DEALS:

                httpService.getLocationDeals(search, new Callback<Deals>() {
                    @Override
                    public void success(Deals deals, Response response) {
                        onSuccess(REQUEST_GET_LOCATIONS_DEALS, deals);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        responseListener.onFailure(error);
                    }
                });

                break;

            default: return false;
        }


        return false;
    }


    public boolean sendRequest(Merchant merchant) {
        OkClient okClient = getOkClient();

        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setClient(okClient).setEndpoint(BASE_SERVICE_URL).build();
        L3HttpService httpService = restAdapter.create(L3HttpService.class);

        if(!isNetworkAvailable()) {
            Toast.makeText(responseListener, R.string.network_disconnected_error, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            responseListener.showProgressView();
        }

        switch (requestType){

            case REQUEST_CREATE_MERCHANT:


                httpService.createMerchant(merchant, new Callback<Merchants>() {

                    @Override
                    public void success(Merchants merchants, Response response) {
                        Merchant merchantObj = merchants.getMerchant();
                        MerchantDatabase.getInstance().saveMerchantinDB(merchantObj);
                        onSuccess(REQUEST_CREATE_MERCHANT, merchantObj);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("L3Http", "error: " + error.getCause());
                        responseListener.onFailure(error);
                    }
                });



            default: return false;
        }

    }


    public boolean sendRequest(String search, Merchant merchant){
        OkClient okClient = getOkClient();

        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setClient(okClient).setEndpoint(BASE_SERVICE_URL).build();
        L3HttpService httpService = restAdapter.create(L3HttpService.class);

        if(!isNetworkAvailable()) {
            Toast.makeText(responseListener, R.string.network_disconnected_error, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            responseListener.showProgressView();
        }
        switch (requestType){
            case REQUEST_UPDATE_MERCHANT:
                httpService.updateMerchant(search, merchant, new Callback<Merchants>() {
                    @Override
                    public void success(Merchants merchants, Response response) {
                        Merchant merchantObj = merchants.getMerchant();
                        MerchantDatabase.getInstance().saveMerchantinDB(merchantObj);
                        onSuccess(REQUEST_UPDATE_MERCHANT, merchantObj);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        responseListener.onFailure(error);
                    }
                });

                break;

            case REQUEST_CREATE_MERCHANT_AND_UPDATE_CLAIM_STATUS:
                httpService.updateClaimStatus(search, merchant, new Callback<Merchants>() {
                    @Override
                    public void success(Merchants merchants, Response response) {
                        Merchant merchantObj = merchants.getMerchant();
                        MerchantDatabase.getInstance().saveMerchantinDB(merchantObj);
                        onSuccess(REQUEST_CREATE_MERCHANT_AND_UPDATE_CLAIM_STATUS, merchantObj);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        responseListener.onFailure(error);
                    }
                });
        }

        return false;
    }

    //REQUEST_CREATE_MERCHANT_LOCATION
    public boolean sendRequest(String key, MerchantLocation location){
        OkClient okClient = getOkClient();

        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setClient(okClient).setEndpoint(BASE_SERVICE_URL).build();
        L3HttpService httpService = restAdapter.create(L3HttpService.class);

        if(!isNetworkAvailable()) {
            Toast.makeText(responseListener, R.string.network_disconnected_error, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            responseListener.showProgressView();
        }

        httpService.createLocationDetails(key, location, new Callback<MerchantLocations>() {
            @Override
            public void success(MerchantLocations merchantLocations, Response response) {
                onSuccess(REQUEST_CREATE_MERCHANT_LOCATION, merchantLocations.getMerchantLocation());

            }

            @Override
            public void failure(RetrofitError error) {
                responseListener.onFailure(error);
            }
        });
        return false;
    }

    public boolean sendRequest(String key1, String key2, MerchantLocation location){
        OkClient okClient = getOkClient();

        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setClient(okClient).setEndpoint(BASE_SERVICE_URL).build();
        L3HttpService httpService = restAdapter.create(L3HttpService.class);

        if(!isNetworkAvailable()) {
            Toast.makeText(responseListener, R.string.network_disconnected_error, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            responseListener.showProgressView();
        }

        httpService.updateLocationDetails(key1, key2, location, new Callback<MerchantLocations>() {
            @Override
            public void success(MerchantLocations merchantLocations, Response response) {
                onSuccess(REQUEST_UPDATE_MERCHANT_LOCATION, merchantLocations.getMerchantLocation());
            }

            @Override
            public void failure(RetrofitError error) {
                responseListener.onFailure(error);
            }
        });

        return false;
    }

    public boolean sendRequest(Deal deal){

        OkClient okClient = getOkClient();

        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setClient(okClient).setEndpoint(BASE_SERVICE_URL).build();
        L3HttpService httpService = restAdapter.create(L3HttpService.class);

        if(!isNetworkAvailable()) {
            Toast.makeText(responseListener, R.string.network_disconnected_error, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            responseListener.showProgressView();
        }
        switch (requestType) {
            case REQUEST_CREATE_LOCATION_DEALS:

                httpService.createDeals(deal, new Callback<CreatedDeal>() {
                    @Override
                    public void success(CreatedDeal createdDeal, Response response) {
                        MerchantDatabase.getInstance().saveSpecial(createdDeal.getDeal());
                        onSuccess(requestType, createdDeal.getDeal());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        responseListener.onFailure(error);
                    }
                });
                break;
        }
        return false;
    }

    public boolean sendRequest(String dealId, Deal deal){
        OkClient okClient = getOkClient();

        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setClient(okClient).setEndpoint(BASE_SERVICE_URL).build();
        L3HttpService httpService = restAdapter.create(L3HttpService.class);

        if(!isNetworkAvailable()) {
            Toast.makeText(responseListener, R.string.network_disconnected_error, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            responseListener.showProgressView();
        }
        switch (requestType) {
            case REQUEST_UPDATE_LOCATION_DEALS:

                httpService.updateDeals(dealId, deal, new Callback<CreatedDeal>() {
                    @Override
                    public void success(CreatedDeal createdDeal, Response response) {
                        onSuccess(requestType, createdDeal.getDeal());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        responseListener.onFailure(error);
                    }
                });
                break;
        }
        return false;
    }

    public boolean sendRequest(String merchantId, String locationId, Contract contract){
        OkClient okClient = getOkClient();

        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setClient(okClient).setEndpoint(BASE_SERVICE_URL).build();
        L3HttpService httpService = restAdapter.create(L3HttpService.class);

        if(!isNetworkAvailable()) {
            Toast.makeText(responseListener, R.string.network_disconnected_error, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            responseListener.showProgressView();
        }
        switch (requestType) {
            case REQUEST_UPDATE_CONTRACT_STATUS:
                httpService.updateContractStatus(merchantId, locationId, contract, new Callback<MerchantLocations>() {
                    @Override
                    public void success(MerchantLocations merchantLocations, Response response) {
                        onSuccess(requestType);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        responseListener.onFailure(error);
                    }
                });
                break;
        }

        return false;
    }

    public boolean sendRequest(String longitude, String latitude){
        OkClient okClient = getOkClient();

        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setClient(okClient).setEndpoint(BASE_SERVICE_URL).build();
        L3HttpService httpService = restAdapter.create(L3HttpService.class);

        if(!isNetworkAvailable()) {
            Toast.makeText(responseListener, R.string.network_disconnected_error, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            responseListener.showProgressView();
        }
        switch (requestType) {
            case REQUEST_GET_NEARBY_MERCHANTS:
                httpService.getNearbyMerchants(longitude, latitude, new Callback<AggregateMerchantList>() {
                    @Override
                    public void success(AggregateMerchantList merchantList, Response response) {
                        Results resultList = merchantList.getResults();
                        List<CrawledMerchant> crawledMerchantList = resultList.getCrawledmerchants();
                        MerchantDatabase.getInstance().saveCrawledMerchantList(crawledMerchantList);
                        List<Merchant> merchants = resultList.getMerchants();
                        MerchantDatabase.getInstance().saveMerchants(merchants);
                        onSuccess(requestType);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        responseListener.onFailure(error);
                    }
                });
                break;
        }
        return false;
    }

    private void onSuccess(int requestType) {
        switch (requestType) {
            case REQUEST_GET_SEARCH_CRAWL_MERCHANTS:
                responseListener.onSuccessResponse(this);
                break;
            case REQUEST_GET_MERCHANTS:
                responseListener.onSuccessResponse(this);
                break;
            case REQUEST_CREATE_MERCHANT:
                responseListener.onSuccessResponse(this);
                break;
            case REQUEST_UPDATE_CONTRACT_STATUS:
                responseListener.onSuccessResponse(this);
                break;
            case REQUEST_GET_NEARBY_MERCHANTS:
                responseListener.onSuccessResponse(this);
                break;

        }
    }

    private void onSuccess(int requestType, Object object) {
        switch (requestType) {

            case REQUEST_CREATE_MERCHANT:
                if(object instanceof Merchant) {
                    responseListener.onSuccessResponse(this, (Merchant)object);
                }
                break;

            case REQUEST_GET_MERCHANT_LOCATIONS:

                MerchantDetails details = null;

                if(object instanceof MerchantDetails){

                    details = (MerchantDetails) object;
                    MerchantDatabase.getInstance().saveMerchantLocationsinDB(details.getMerchantLocations());
                    responseListener.onSuccessResponse(this);
                }
                break;

            case REQUEST_UPDATE_MERCHANT:
                if(object instanceof Merchant) {
                    responseListener.onSuccessResponse(this, (Merchant)object);
                }
                break;

            case REQUEST_CREATE_MERCHANT_AND_UPDATE_CLAIM_STATUS:
                if(object instanceof Merchant) {
                    responseListener.onSuccessResponse(this, (Merchant)object);
                }
                break;

            case REQUEST_CREATE_MERCHANT_LOCATION:
                if(object instanceof MerchantLocation){
                    MerchantDatabase.getInstance().saveMerchantLocationinDB((MerchantLocation) object);
                    responseListener.onSuccessResponse(this, null, requestType, (MerchantLocation) object);


                }
                break;

            case REQUEST_UPDATE_MERCHANT_LOCATION:
                if(object instanceof MerchantLocation){
                    MerchantDatabase.getInstance().saveMerchantLocationinDB((MerchantLocation) object);
                    responseListener.onSuccessResponse(this, null, requestType, (MerchantLocation) object);

                }

                break;

            case REQUEST_GET_LOCATIONS_DEALS:

                Deals deals = null;
                Deal deal = null;
                if(object instanceof Deals) {
                    deals = (Deals)object;
                    for (int i=0;i<deals.getDeals().size();i++){
                        deal = deals.getDeals().get(i);
                        MerchantDatabase.getInstance().saveDeal(deal);
                    }
                    responseListener.onSuccessResponse(this,deals);

                }else{
                    responseListener.onSuccessResponse(this, object);
                }
                break;

            case REQUEST_CREATE_LOCATION_DEALS:
            case REQUEST_UPDATE_LOCATION_DEALS:
                MerchantDatabase.getInstance().saveSpecial((Special) object);
                responseListener.onSuccessResponse(this, null, requestType, (Special) object);

                break;



        }
    }




}

class StubSSLTrust {

    public static OkHttpClient configureClient(final OkHttpClient client) {
        final TrustManager[] certs = new TrustManager[]{new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
            }
        }};

        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, certs, new SecureRandom());
        } catch (final java.security.GeneralSecurityException ex) {
            Log.e("StubSSLTrust", "Exception: " + ex);
        }

        try {
            final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(final String hostname, final SSLSession session) {
                    return true;
                }
            };
            client.setHostnameVerifier(hostnameVerifier);
            client.setSslSocketFactory(ctx.getSocketFactory());
        } catch (final Exception e) {
            Log.e("StubSSLTrust", "Exception: " + e);
        }

        return client;
    }

    public static OkHttpClient getIgnoreSslOKHttpClient() {
        final OkHttpClient client = new OkHttpClient();
        return configureClient(client);
    }

    public static OkHttpClient allowAllSSL(){
        OkHttpClient okHttpClient = new OkHttpClient();
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
        } catch (GeneralSecurityException e) {
            throw new AssertionError(); // The system has no TLS. Just give up.
        }
        okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
        return okHttpClient;
    }

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OkHttpClient getCertifiedOKHttpClient(Context context) {
        OkHttpClient okHttpClient = new OkHttpClient();

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // TODO add a certificate in raw folder
            InputStream cert = null;//context.getResources().openRawResource(R.raw.halo_cert);

            Certificate ca = cf.generateCertificate(cert);
            // creating a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // creating a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // creating an SSLSocketFactory that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());

            //TODO put cert.close in finally block

            cert.close();
        } catch (CertificateException | KeyStoreException | KeyManagementException | NoSuchAlgorithmException | IOException se) {
            Log.e("StubSSLTrust", "Exception: " + se);
        }
        return okHttpClient;
    }
}