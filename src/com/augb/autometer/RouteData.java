package com.augb.autometer;

import static android.provider.BaseColumns._ID;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.augb.autometer.GpsLocation;

public class RouteData extends SQLiteOpenHelper {
	
	Long tripId;

	private static final String DATABASE_NAME = "autometer1.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String ROUTE_TABLE_TRIP = "TRIP";
	private static final String ROUTE_TABLE_ROUTE = "ROUTE";
	
	private static final String TRIP_ID = "TRIP_ID";	
	private static final String LATITUDE = "LATITUDE";
	private static final String LONGITUDE = "LONGITUDE";
	private static final String LOCATION_ACCURACY = "LOCATION_ACCURACY";
	private static final String TOTAL_DISTANCE = "TOTAL_DISTANCE";
	private static final String SPEED = "SPEED";
	private static final String BEARING = "BEARING";
	private static final String LAST_LOCATION_DT = "LAST_LOCATION_DT";
	private static final String TOTAL_WAITING_DT = "TOTAL_WAITING_DT";
	private static final String TOTAL_TRIP_DT = "TOTAL_TRIP_DT";
	
	private static final String START_TIME = "START_TIME";
	private static final String START_LAT = "START_LAT";
	private static final String START_LONG = "START_LONG";
	private static final String END_TIME = "END_TIME";
	private static final String END_LAT = "END_LAT";
	private static final String END_LONG = "END_LONG";
	private static final String FARE = "FARE";
	private static final String CALCULATED_FARE = "CALCULATED_FARE";
	
	
	private static String ORDER_BY = _ID + " ASC";
	
	private static final String CREATE_SQL_ROUTE = 
		"CREATE TABLE " + ROUTE_TABLE_ROUTE + " (" + _ID
	+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
	+ TRIP_ID + " LONG,"  
	+ LATITUDE + " DOUBLE," 
	+ LONGITUDE + " DOUBLE, " 
	+ LOCATION_ACCURACY + " DOUBLE, "
	+ TOTAL_DISTANCE + " DOUBLE, "
	+ SPEED + " DOUBLE, "	
	+ BEARING + " DOUBLE, "
	+ LAST_LOCATION_DT + " LONG, "
	+ TOTAL_WAITING_DT + " LONG, "
	+ TOTAL_TRIP_DT + " LONG "
	+ " );";
	
	private static final String CREATE_SQL_TRIP = 
		"CREATE TABLE " + ROUTE_TABLE_TRIP + " (" + _ID
	+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
	+ START_TIME + " LONG,"  
	+ START_LAT + " DOUBLE," 
	+ START_LONG + " DOUBLE, " 
	+ END_TIME + " LONG, "
	+ END_LAT + " DOUBLE," 
	+ END_LONG + " DOUBLE, " 
	+ TOTAL_DISTANCE + " DOUBLE, "
	+ FARE + " DOUBLE, " 
	+ CALCULATED_FARE + " DOUBLE "
	+ " );";
	
	/** Create a helper object for the AutoMeter database */
	public RouteData(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_SQL_ROUTE);
		db.execSQL(CREATE_SQL_TRIP);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + ROUTE_TABLE_TRIP);
		db.execSQL("DROP TABLE IF EXISTS " + ROUTE_TABLE_ROUTE);
		onCreate(db);
	}
	
	public long createTrip( Long startTime, Double startLat, Double startLong  )
	{
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(START_TIME, startTime);
		values.put(START_LAT, startLat);
		values.put(START_LONG, startLong);
		
		tripId = db.insertOrThrow(ROUTE_TABLE_TRIP, null, values);	

		return tripId;
		
	}
	
	public long updateTrip( Long endTime,Double endLat, Double endLong, Double totalDistance, Double fare, Double calculatedFare, Long id )
	{
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();


		values.put(END_TIME, endTime);
		values.put(END_LAT, endLat);
		values.put(END_LONG, endLong);
		values.put(TOTAL_DISTANCE, totalDistance);
		values.put(FARE, fare);
		values.put(CALCULATED_FARE, calculatedFare);

		return db.update(ROUTE_TABLE_TRIP, values, _ID, new String[] {  id.toString() } );
		
	}
	
	public long createPositionInfo( GpsLocation gpsLoc )		
	{
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(TRIP_ID, tripId);		
		values.put(LATITUDE, gpsLoc.getLat());
		values.put(LONGITUDE, gpsLoc.getLon());
		values.put(LOCATION_ACCURACY, gpsLoc.getLocAccuracy());
		values.put(TOTAL_DISTANCE, gpsLoc.getTotalDistance());
		
		values.put(SPEED, gpsLoc.getSpeed());
		values.put(BEARING, gpsLoc.getBearing());
		values.put(LAST_LOCATION_DT, gpsLoc.getLastLocationUpdateDT());
		values.put(TOTAL_WAITING_DT, gpsLoc.getTotalWaitingDT());
		values.put(TOTAL_TRIP_DT, gpsLoc.getTotalTripDT());
		
		return db.insertOrThrow(ROUTE_TABLE_ROUTE, null, values);
		
	}

	public Cursor getTripInfo() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(ROUTE_TABLE_TRIP, null, null, null, null,
				null, ORDER_BY);
		// startManagingCursor(cursor); //startManagingCursor to be added
		// defined in Activity
		return cursor;
	}
	
	public Cursor getRouteInfo() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(ROUTE_TABLE_ROUTE, null, null, null, null,
				null, ORDER_BY);
		// startManagingCursor(cursor); //startManagingCursor to be added
		// defined in Activity
		return cursor;
	}

}
