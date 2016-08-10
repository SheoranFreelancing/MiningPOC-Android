package com.drill.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.drill.db.MerchantReaderContract.DealEntry;
import com.drill.db.MerchantReaderContract.LocationEntry;
import com.drill.db.MerchantReaderContract.MerchantEntry;
import com.drill.responsemodel.Address;
import com.drill.responsemodel.Contact;
import com.drill.responsemodel.Contract;
import com.drill.responsemodel.CrawledMerchant;
import com.drill.responsemodel.DailyLimits;
import com.drill.responsemodel.Deal;
import com.drill.responsemodel.FinalValue;
import com.drill.responsemodel.Image;
import com.drill.responsemodel.Management;
import com.drill.responsemodel.Merchant;
import com.drill.responsemodel.MerchantLocation;
import com.drill.responsemodel.MerchantTimings;
import com.drill.responsemodel.Metadata;
import com.drill.responsemodel.OriginalValue;
import com.drill.responsemodel.Price;
import com.drill.responsemodel.RedemptionTimings;
import com.drill.responsemodel.Special;
import com.drill.utils.LTLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by groupon on 8/10/15.
 */
public class MerchantDatabase {

    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "Merchant.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_MERCHANT_ENTRIES =
            "CREATE TABLE " + MerchantEntry.TABLE_NAME + " ( " +
                    MerchantEntry.COLUMN_NAME_MERCHANT_ID + " TEXT PRIMARY KEY," +
                    MerchantEntry.COLUMN_NAME_MERCHANT_NAME_ENGLISH + TEXT_TYPE + COMMA_SEP +
                    MerchantEntry.COLUMN_NAME_MERCHANT_TAXONOMY + TEXT_TYPE + COMMA_SEP +
                    MerchantEntry.COLUMN_NAME_MERCHANT_TAGS + TEXT_TYPE + COMMA_SEP +
                    MerchantEntry.COLUMN_NAME_MERCHANT_CREATEDAT + TEXT_TYPE + COMMA_SEP +
                    MerchantEntry.COLUMN_NAME_MERCHANT_UPDATEDAT + TEXT_TYPE + COMMA_SEP +
                    MerchantEntry.COLUMN_NAME_MERCHANT_WEBSITE + TEXT_TYPE + COMMA_SEP +
                    MerchantEntry.COLUMN_NAME_MERCHANT_META_DATA + TEXT_TYPE + COMMA_SEP +
                    MerchantEntry.COLUMN_NAME_MERCHANT_DESCRIPTION + TEXT_TYPE +
                    " )";
    private static final String SQL_CREATE_LOCATION_ENTRIES =
            "CREATE TABLE " + LocationEntry.TABLE_NAME + " ( " +
                    LocationEntry.COLUMN_NAME_LOCATION_ID + " TEXT PRIMARY KEY," +
                    LocationEntry.COLUMN_NAME_LOCATION_MERCHANT_ID + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LOCATION_HALO_ID + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LOCATION_NAME + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LOCATION_CREATEDAT + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LOCATION_UPDATEDAT + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LOCATION_WEBSITE + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LOCATION_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LOCATION_LQS + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LOCATION_STATUS + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LOCATION_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LOCATION_GEOCOORDINATES + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LOCATION_TIMINGS + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LOCATION_META_DATA + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LOCATION_CONTACT + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LOCATION_CONTRACT + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LOCATION_MANAGEMENT_CONTACT + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LOCATION_IMAGE_URLS + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_DEAL_ENTRIES =
            "CREATE TABLE " + DealEntry.TABLE_NAME + " ( " +
                    DealEntry.COLUMN_NAME_DEAL_ID + " TEXT PRIMARY KEY," +
                    DealEntry.COLUMN_NAME_DEAL_TITLE + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_HALO + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_MERCHANT_LOCATION + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_SHORT_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_LONG_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_CREATEDAT + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_UPDATEDAT + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_EXPIRESAT + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_STARTAT + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_ENDAT + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_ACTIVATESAT + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_TAXONOMY + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_ISSOLDOUT + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_TOTAL_REDEMPTIONS + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_TOTAL_PURCHASES + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_TYPE + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_STATUS + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_REDEMPTION_TIMINGS + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_FEATURE_LIMIT + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_USER_LIMIT + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_FINAL_VALUE + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_ORIGINAL_VALUE + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_PRICE + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_META_DATA + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_TAGS + TEXT_TYPE + COMMA_SEP +
                    DealEntry.COLUMN_NAME_DEAL_IMAGES + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_MERCHANT_ENTRIES =
            "DROP TABLE IF EXISTS " + MerchantEntry.TABLE_NAME;
    private static final String SQL_DELETE_LOCATION_ENTRIES =
            "DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME;
    private static final String SQL_DELETE_DEAL_ENTRIES =
            "DROP TABLE IF EXISTS " + DealEntry.TABLE_NAME;

    public ArrayList<Object> aggregateMerchants = null;


    private class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_MERCHANT_ENTRIES);
            db.execSQL(SQL_CREATE_LOCATION_ENTRIES);
            db.execSQL(SQL_CREATE_DEAL_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_MERCHANT_ENTRIES);
            db.execSQL(SQL_DELETE_LOCATION_ENTRIES);
            db.execSQL(SQL_DELETE_DEAL_ENTRIES);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    private static MerchantDatabase instance = null;
    private final DbHelper dbHelper;

    public static void initializeMerchantDatabaseHandler(Context context) {
        synchronized (context) {
            if (instance == null) {
                instance = new MerchantDatabase(context);
            }
        }
    }

    public void close() {
        this.dbHelper.close();
    }

    private static SQLiteDatabase db;

    public SQLiteDatabase getWritableDB() {
        return db;
    }
    public SQLiteDatabase getReadableDB() {
        return db;
    }
    private MerchantDatabase(Context context) {
        this.dbHelper = new DbHelper(context);
        db = this.dbHelper.getWritableDatabase();
    }

    public static MerchantDatabase getInstance() {
        return instance;
    }

    public void createMerchant(ContentValues values) {
        long rows = db.insertWithOnConflict(MerchantEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        LTLog.d("createMerchant:", "Inserted rows: " + rows);
    }

    public Merchant getMerchantFromId(String merchantId) {
        Cursor cursor = db.query(MerchantEntry.TABLE_NAME,
                null, MerchantEntry.COLUMN_NAME_MERCHANT_ID + " = ?",
                new String[]{merchantId}, null, null, null, null);

        Gson gson = new Gson();

        if (cursor.moveToFirst()) {
            Merchant merchant = new Merchant();
            merchant.set_id(cursor.getString(cursor.getColumnIndex(MerchantEntry.COLUMN_NAME_MERCHANT_ID)));
            merchant.setName(cursor.getString(cursor.getColumnIndex(MerchantEntry.COLUMN_NAME_MERCHANT_NAME_ENGLISH)));
            merchant.setDescription(cursor.getString(cursor.getColumnIndex(MerchantEntry.COLUMN_NAME_MERCHANT_DESCRIPTION)));
            merchant.setTaxonomy(cursor.getString(cursor.getColumnIndex(MerchantEntry.COLUMN_NAME_MERCHANT_TAXONOMY)));
            merchant.setWebsite(cursor.getString(cursor.getColumnIndex(MerchantEntry.COLUMN_NAME_MERCHANT_WEBSITE)));
            merchant.setMetadata(gson.fromJson(cursor.getString(cursor.getColumnIndex(MerchantEntry.COLUMN_NAME_MERCHANT_META_DATA)),Metadata.class));

            return merchant;
        }
        return null;
    }

    public ArrayList<Merchant> getAllMerchants() {
        ArrayList<Merchant> merchants = new ArrayList<Merchant>();
        String query = "SELECT  * FROM " + MerchantEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        Gson gson = new Gson();

        Merchant merchant = null;
        if(cursor.moveToFirst()) {
            do {
                merchant = new Merchant();
                merchant.set_id(cursor.getString(cursor.getColumnIndex(MerchantEntry.COLUMN_NAME_MERCHANT_ID)));
                merchant.setName(cursor.getString(cursor.getColumnIndex(MerchantEntry.COLUMN_NAME_MERCHANT_NAME_ENGLISH)));
                merchant.setDescription(cursor.getString(cursor.getColumnIndex(MerchantEntry.COLUMN_NAME_MERCHANT_DESCRIPTION)));
                merchant.setTaxonomy(cursor.getString(cursor.getColumnIndex(MerchantEntry.COLUMN_NAME_MERCHANT_TAXONOMY)));
                merchant.setWebsite(cursor.getString(cursor.getColumnIndex(MerchantEntry.COLUMN_NAME_MERCHANT_WEBSITE)));

                merchant.setMetadata(gson.fromJson(cursor.getString(cursor.getColumnIndex(MerchantEntry.COLUMN_NAME_MERCHANT_META_DATA)),Metadata.class));
                merchants.add(merchant);
            } while (cursor.moveToNext());
        }
        return merchants;
    }

    public void deleteMerchant(long merchantId) {
        db.delete(MerchantEntry.TABLE_NAME, MerchantEntry.COLUMN_NAME_MERCHANT_ID + " = ?", new String[]{String.valueOf(merchantId)});
        db.close();
    }

    public void createLocation(ContentValues values) {
        long rows = db.insertWithOnConflict(LocationEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        LTLog.d("createLocation:", "Inserted rows: " + rows);
    }

    public MerchantLocation getLocationFromId(String locationId) {
        Cursor cursor = db.query(LocationEntry.TABLE_NAME,
                null, LocationEntry.COLUMN_NAME_LOCATION_ID + " = ?",
                new String[]{locationId}, null, null, null, null);

        Gson gson = new Gson();

        if (cursor.moveToFirst()) {
            MerchantLocation location = new MerchantLocation();
            location.set_id(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_ID)));
            location.setMerchant(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_MERCHANT_ID)));
            location.setHalo(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_HALO_ID)));
            location.setName(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_NAME)));
            location.setCreatedAt(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_CREATEDAT)));
            location.setUpdatedAt(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_UPDATEDAT)));
            location.setWebsite(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_WEBSITE)));
            location.setDescription(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_DESCRIPTION)));
            //    location.setLqs(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_LQS)));
            location.setStatus(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_STATUS)));
            location.setAddress(gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_ADDRESS)), Address.class));
            List<Double> geoList = gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_GEOCOORDINATES)), new TypeToken<List<Double>>() {
            }.getType());
            location.setGeolocation(geoList);
            //location.setGeolocation(gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_GEOCOORDINATES)), List.class));
            location.setMerchantTimings(gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_TIMINGS)), MerchantTimings.class));
            location.setContact(gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_CONTACT)), Contact.class));
            location.setContract(gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_CONTRACT)), Contract.class));
            location.setManagement(gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_MANAGEMENT_CONTACT)), Management.class));
            List<Image> imageList = gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_IMAGE_URLS)), new TypeToken<List<Image>>(){}.getType());
            location.setImages(imageList);
            location.setMetadata(gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_META_DATA)), Metadata.class));

            return location;
        }
        return null;
    }

    public ArrayList<MerchantLocation> getAllLocationsForMerchantId(String merchantId) {
        ArrayList<MerchantLocation> locations = new ArrayList<MerchantLocation>();
        Cursor cursor = db.query(LocationEntry.TABLE_NAME,
                null, LocationEntry.COLUMN_NAME_LOCATION_MERCHANT_ID + " = ?",
                new String[]{merchantId}, null, null, null, null);

        Gson gson = new Gson();
        MerchantLocation location = null;
        if(cursor.moveToFirst()) {
            do {
                location = new MerchantLocation();
                location.set_id(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_ID)));
                location.setMerchant(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_MERCHANT_ID)));
                location.setHalo(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_HALO_ID)));
                location.setName(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_NAME)));
                location.setCreatedAt(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_CREATEDAT)));
                location.setUpdatedAt(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_UPDATEDAT)));
                location.setWebsite(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_WEBSITE)));
                location.setDescription(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_DESCRIPTION)));
                //            location.setLqs(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_LQS)));
                location.setStatus(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_STATUS)));
                location.setAddress(gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_ADDRESS)), Address.class));

                List<Double> geoList = gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_GEOCOORDINATES)), new TypeToken<List<Double>>() {
                }.getType());
                location.setGeolocation(geoList);
                location.setMerchantTimings(gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_TIMINGS)), MerchantTimings.class));
                location.setContact(gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_CONTACT)), Contact.class));
                location.setContract(gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_CONTRACT)), Contract.class));
                location.setManagement(gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_MANAGEMENT_CONTACT)), Management.class));
                location.setMetadata(gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_META_DATA)), Metadata.class));

                List<Image> imageList = gson.fromJson(cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LOCATION_IMAGE_URLS)), new TypeToken<List<Image>>(){}.getType());
                location.setImages(imageList);

                locations.add(location);
            } while (cursor.moveToNext());
        }
        return locations;
    }
    public void createDeal(ContentValues values) {
        long rows = db.insertWithOnConflict(DealEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        LTLog.d("createDeal:", "Inserted rows: " + rows);
    }

    public Deal getDealFromId(String dealId) {
        Cursor cursor = db.query(DealEntry.TABLE_NAME,
                null, DealEntry.COLUMN_NAME_DEAL_ID + " = ?",
                new String[]{dealId}, null, null, null, null);

        if (cursor.moveToFirst()) {
            Deal deal = new Deal();
            deal.set_id(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_ID)));


            /*Gson merchantLocationJson = new Gson();
            MerchantLocation merchantLocation = merchantLocationJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_MERCHANT_LOCATION)),MerchantLocation.class);
            deal.setMerchantLocation(merchantLocation);*/

            MerchantLocation merchantLocation = new MerchantLocation();
            merchantLocation.set_id(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_MERCHANT_LOCATION)));
            deal.setMerchantLocation(merchantLocation);


            deal.setTitle(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_TITLE)));
            deal.setHalo(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_HALO)));
            //        deal.setTitle(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_SHORT_DESCRIPTION)));
            deal.setLongDescription(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_LONG_DESCRIPTION)));
            deal.setTaxonomy(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_TAXONOMY)));
            deal.setCreatedAt(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_CREATEDAT)));
            deal.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_UPDATEDAT)));
            deal.setExpiresAt(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_EXPIRESAT)));
            deal.setStartAt(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_STARTAT)));
            deal.setEndAt(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_ENDAT)));

            Gson redemptionJson = new Gson();
            RedemptionTimings redemptionTimings = redemptionJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_REDEMPTION_TIMINGS)),RedemptionTimings.class);
            deal.setRedemptionTimings(redemptionTimings);

            deal.setIsSoldOut(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_ISSOLDOUT)).equals("true") ? true : false);

            Gson limitJson = new Gson();
            DailyLimits dailyLimits = limitJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_FEATURE_LIMIT)),DailyLimits.class);
            deal.setDailyLimits(dailyLimits);


            Gson imageJson = new Gson();
            List<Image> imageList = imageJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_IMAGES)), new TypeToken<List<Image>>(){}.getType());
            deal.setImages(imageList);



            Gson finalValueJson = new Gson();
            FinalValue finalValue = finalValueJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_ORIGINAL_VALUE)),FinalValue.class);
            deal.setFinalValue(finalValue);


            Gson originalValueJson = new Gson();
            OriginalValue originalValue = originalValueJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_ORIGINAL_VALUE)), OriginalValue.class);
            deal.setOriginalValue(originalValue);

            Gson priceJson = new Gson();
            Price price = priceJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_PRICE)), Price.class);
            deal.setPrice(price);


            deal.setTotalRedemptions(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_TOTAL_REDEMPTIONS))));
            deal.setTotalPurchases(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_TOTAL_PURCHASES))));
            deal.setType(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_TYPE)));
            deal.setStatus(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_STATUS)));

            Gson metadataJson = new Gson();
            Metadata metadata = metadataJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_META_DATA)), Metadata.class);
            deal.setMetadata(metadata);


            Gson dealTagJson = new Gson();
            List<String> tagList = dealTagJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_TAGS)), new TypeToken<List<String>>(){}.getType());
            deal.setTags(tagList);


            return deal;
        }
        return null;
    }

    public ArrayList<Deal> getAllDealsForLocationId(String locationId) {
        ArrayList<Deal> deals = new ArrayList<Deal>();
        Cursor cursor = db.query(DealEntry.TABLE_NAME,
                null, DealEntry.COLUMN_NAME_DEAL_MERCHANT_LOCATION + " = ?",
                new String[]{locationId}, null, null, null, null);

        Deal deal = null;
        if(cursor.moveToFirst()) {
            do {
                deal = new Deal();
                deal.set_id(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_ID)));

                /*Gson merchantLocationJson = new Gson();
                MerchantLocation merchantLocation = merchantLocationJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_MERCHANT_LOCATION)),MerchantLocation.class);
                deal.setMerchantLocation(merchantLocation);*/

                MerchantLocation merchantLocation = new MerchantLocation();
                merchantLocation.set_id(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_MERCHANT_LOCATION)));
                deal.setMerchantLocation(merchantLocation);

                deal.setTitle(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_TITLE)));
                deal.setHalo(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_HALO)));
                //   deal.setTitle(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_SHORT_DESCRIPTION)));
                deal.setLongDescription(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_LONG_DESCRIPTION)));
                deal.setTaxonomy(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_TAXONOMY)));
                deal.setCreatedAt(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_CREATEDAT)));
                deal.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_UPDATEDAT)));
                deal.setExpiresAt(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_EXPIRESAT)));
                deal.setStartAt(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_STARTAT)));
                deal.setEndAt(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_ENDAT)));

                Gson redemptionJson = new Gson();
                RedemptionTimings redemptionTimings = redemptionJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_REDEMPTION_TIMINGS)),RedemptionTimings.class);
                deal.setRedemptionTimings(redemptionTimings);

                deal.setIsSoldOut(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_ISSOLDOUT)).equals("true") ? true : false);

                Gson limitJson = new Gson();
                DailyLimits dailyLimits = limitJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_FEATURE_LIMIT)),DailyLimits.class);
                deal.setDailyLimits(dailyLimits);



                Gson imageJson = new Gson();
                List<Image> imageList = imageJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_IMAGES)), new TypeToken<List<Image>>(){}.getType());
                deal.setImages(imageList);



                Gson finalValueJson = new Gson();
                FinalValue finalValue = finalValueJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_ORIGINAL_VALUE)),FinalValue.class);
                deal.setFinalValue(finalValue);


                Gson originalValueJson = new Gson();
                OriginalValue originalValue = originalValueJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_ORIGINAL_VALUE)), OriginalValue.class);
                deal.setOriginalValue(originalValue);

                Gson priceJson = new Gson();
                Price price = priceJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_PRICE)), Price.class);
                deal.setPrice(price);


                deal.setTotalRedemptions(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_TOTAL_REDEMPTIONS))));
                deal.setTotalPurchases(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_TOTAL_PURCHASES))));
                deal.setType(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_TYPE)));
                deal.setStatus(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_STATUS)));

                Gson metadataJson = new Gson();
                Metadata metadata = metadataJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_META_DATA)), Metadata.class);
                deal.setMetadata(metadata);

                Gson dealTagJson = new Gson();
                List<String> tagList = dealTagJson.fromJson(cursor.getString(cursor.getColumnIndex(DealEntry.COLUMN_NAME_DEAL_TAGS)), new TypeToken<List<String>>(){}.getType());
                deal.setTags(tagList);

                deals.add(deal);
            } while (cursor.moveToNext());
        }
        return deals;
    }

    public void clearAllTable(String tableName) {
        db.execSQL("DELETE FROM "+ tableName);
    }

    public void clearAllTables() {
        db.execSQL("DELETE FROM "+ MerchantEntry.TABLE_NAME);
        db.execSQL("DELETE FROM "+ LocationEntry.TABLE_NAME);
        db.execSQL("DELETE FROM "+ DealEntry.TABLE_NAME);
    }

    public void saveMerchants(List<Merchant> merchants) {
        aggregateMerchants = new ArrayList<Object>();
        for (int i=0;i<merchants.size();i++){
            Merchant merchant = merchants.get(i);
            saveMerchantinDB(merchant);
            aggregateMerchants.add(merchant);
        }
    }

    public void saveCrawledMerchantList(List<CrawledMerchant> crawledMerchantList) {
        if(aggregateMerchants==null){
            aggregateMerchants = new ArrayList<Object>();
        }
        for (int i=0;i<crawledMerchantList.size();i++){
            CrawledMerchant crawledMerchant = crawledMerchantList.get(i);
            aggregateMerchants.add(crawledMerchant);
        }
    }

    public void saveMerchantinDB(Merchant merchant) {
        ContentValues cv = new ContentValues();
        Gson gson = new Gson();
        cv.put(MerchantReaderContract.MerchantEntry.COLUMN_NAME_MERCHANT_ID, merchant.get_id());

        String metadataJson = gson.toJson(merchant.getMetadata());
        cv.put(MerchantReaderContract.MerchantEntry.COLUMN_NAME_MERCHANT_META_DATA, metadataJson);
        cv.put(MerchantReaderContract.MerchantEntry.COLUMN_NAME_MERCHANT_NAME_ENGLISH, merchant.getName());
        cv.put(MerchantReaderContract.MerchantEntry.COLUMN_NAME_MERCHANT_DESCRIPTION, merchant.getDescription());

        cv.put(MerchantReaderContract.MerchantEntry.COLUMN_NAME_MERCHANT_TAXONOMY, merchant.getTaxonomy());
        cv.put(MerchantReaderContract.MerchantEntry.COLUMN_NAME_MERCHANT_CREATEDAT,merchant.getCreatedAt());
        cv.put(MerchantReaderContract.MerchantEntry.COLUMN_NAME_MERCHANT_UPDATEDAT,merchant.getUpdatedAt());
        cv.put(MerchantReaderContract.MerchantEntry.COLUMN_NAME_MERCHANT_WEBSITE, merchant.getWebsite());
        MerchantDatabase.getInstance().createMerchant(cv);

        List<MerchantLocation> locationList = merchant.getLocations();

        if(locationList.size()>0){
            for (int i=0;i<locationList.size();i++)
                saveMerchantLocationsinDB(locationList);
        }
    }


    public void saveMerchantsinDB(ArrayList<Merchant> merchantArrayList) {
        Gson gson = new Gson();
        for (int i=0;i<merchantArrayList.size();i++){
            Merchant merchant = merchantArrayList.get(i);
            saveMerchantinDB(merchant);
        }
    }

    public void saveMerchantLocationsinDB(List<MerchantLocation> locationList) {

        if(aggregateMerchants==null){
            aggregateMerchants = new ArrayList<Object>();
        }

        for(int i=0;i<locationList.size();i++){
            ContentValues cv = new ContentValues();
            MerchantLocation location = locationList.get(i);
            aggregateMerchants.add(location);
            saveMerchantLocationinDB(location);
        }

    }

    public void saveMerchantLocationinDB(MerchantLocation location) {

        Gson gson = new Gson();
        ContentValues cv = new ContentValues();

        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_ID, location.get_id());
        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_MERCHANT_ID, location.getMerchant());
        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_NAME, location.getName());
        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_HALO_ID, location.getHalo());
        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_WEBSITE, location.getWebsite());
        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_DESCRIPTION, location.getDescription());
        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_STATUS, location.getStatus());
        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_CREATEDAT,location.getCreatedAt());
        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_UPDATEDAT,location.getUpdatedAt());
        String addressJson = gson.toJson(location.getAddress());

        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_ADDRESS,addressJson);

        String geoLocationJson = gson.toJson(location.getGeolocation());
        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_GEOCOORDINATES,geoLocationJson);

        String timingJson = gson.toJson(location.getMerchantTimings());
        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_TIMINGS,timingJson);

        String metadataJson = gson.toJson(location.getMetadata());
        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_META_DATA,metadataJson);

        String contactJson = gson.toJson(location.getContact());
        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_CONTACT,contactJson);

        String contractJson = gson.toJson(location.getContract());
        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_CONTRACT,contractJson);

        String managementJson = gson.toJson(location.getManagement());
        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_MANAGEMENT_CONTACT,managementJson);

        String imageJson = gson.toJson(location.getImages());
        cv.put(MerchantReaderContract.LocationEntry.COLUMN_NAME_LOCATION_IMAGE_URLS,imageJson);
        createLocation(cv);
    }




    public void saveDeal(Deal deal) {

        Gson gson = new Gson();
        ContentValues cv = new ContentValues();
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_ID, deal.get_id());

        MerchantLocation merchantLocation = deal.getMerchantLocation();
        // String merchantLocationJson = gson.toJson(merchantLocation);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_MERCHANT_LOCATION, merchantLocation.get_id());

        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_TITLE, deal.getTitle());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_HALO, deal.getHalo());
        //    cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_SHORT_DESCRIPTION, deal.getLongDescription());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_LONG_DESCRIPTION, deal.getLongDescription());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_TAXONOMY, deal.getTaxonomy());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_CREATEDAT,deal.getCreatedAt());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_UPDATEDAT,deal.getUpdatedAt());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_EXPIRESAT,deal.getExpiresAt());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_STARTAT,deal.getStartAt());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_ENDAT,deal.getEndAt());

        RedemptionTimings redemptionTimings = deal.getRedemptionTimings();

        String redemptionJson = gson.toJson(redemptionTimings);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_REDEMPTION_TIMINGS,redemptionJson);


        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_ISSOLDOUT,deal.getIsSoldOut());


        DailyLimits dailyLimits = deal.getDailyLimits();
        String dailyLimitJson = gson.toJson(dailyLimits);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_FEATURE_LIMIT,dailyLimitJson);


        //    cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_USER_LIMIT,userLimit);

        List<Image> imageList = deal.getImages();
        String imageJson = gson.toJson(imageList);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_IMAGES,imageJson);

        FinalValue finalValue = deal.getFinalValue();
        String finalValueJson = gson.toJson(finalValue);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_FINAL_VALUE,finalValueJson);


        OriginalValue originalValue = deal.getOriginalValue();
        String originalValueJson = gson.toJson(originalValue);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_ORIGINAL_VALUE,originalValueJson);

        Price price = deal.getPrice();
        String priceJson = gson.toJson(price);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_PRICE,priceJson);


        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_TOTAL_REDEMPTIONS,deal.getTotalRedemptions());

        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_TOTAL_PURCHASES,deal.getTotalPurchases());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_TYPE,deal.getType());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_STATUS,deal.getStatus());


        List<String> tagList = deal.getTags();
        String tagJson = gson.toJson(tagList);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_TAGS, tagJson);


        Metadata metadata = deal.getMetadata();
        String metadataJson = gson.toJson(metadata);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_META_DATA,metadataJson);
        createDeal(cv);
    }

    public void saveSpecial(Special deal) {

        Gson gson = new Gson();
        ContentValues cv = new ContentValues();
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_ID, deal.get_id());



        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_MERCHANT_LOCATION, deal.getMerchantLocation());

        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_TITLE, deal.getTitle());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_HALO, deal.getHalo());
        //    cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_SHORT_DESCRIPTION, deal.getLongDescription());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_LONG_DESCRIPTION, deal.getLongDescription());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_TAXONOMY, deal.getTaxonomy());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_CREATEDAT,deal.getCreatedAt());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_UPDATEDAT,deal.getUpdatedAt());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_EXPIRESAT,deal.getExpiresAt());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_STARTAT,deal.getStartAt());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_ENDAT,deal.getEndAt());

        RedemptionTimings redemptionTimings = deal.getRedemptionTimings();

        String redemptionJson = gson.toJson(redemptionTimings);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_REDEMPTION_TIMINGS,redemptionJson);


        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_ISSOLDOUT,deal.getIsSoldOut());


        DailyLimits dailyLimits = deal.getDailyLimits();
        String dailyLimitJson = gson.toJson(dailyLimits);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_FEATURE_LIMIT,dailyLimitJson);


        //    cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_USER_LIMIT,userLimit);

        List<Image> imageList = deal.getImages();
        String imageJson = gson.toJson(imageList);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_IMAGES,imageJson);

        FinalValue finalValue = deal.getFinalValue();
        String finalValueJson = gson.toJson(finalValue);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_FINAL_VALUE,finalValueJson);


        OriginalValue originalValue = deal.getOriginalValue();
        String originalValueJson = gson.toJson(originalValue);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_ORIGINAL_VALUE,originalValueJson);

        Price price = deal.getPrice();
        String priceJson = gson.toJson(price);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_PRICE,priceJson);


        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_TOTAL_REDEMPTIONS,deal.getTotalRedemptions());

        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_TOTAL_PURCHASES,deal.getTotalPurchases());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_TYPE,deal.getType());
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_STATUS,deal.getStatus());

        List<String> tagList = deal.getTags();
        String tagJson = gson.toJson(tagList);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_TAGS, tagJson);

        Metadata metadata = deal.getMetadata();
        String metadataJson = gson.toJson(metadata);
        cv.put(MerchantReaderContract.DealEntry.COLUMN_NAME_DEAL_META_DATA,metadataJson);
        createDeal(cv);
    }


    public ArrayList<Object> getAggregateMerchants() {
        return aggregateMerchants;
    }


}