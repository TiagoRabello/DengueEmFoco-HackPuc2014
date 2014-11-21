package com.example.projectangra.controller;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.util.GeoPoint;

import android.content.Context;
import android.location.Location;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class DatabaseHandler {

	private static final String APPLICATION_ID = "xTCMviuSWCcRWP4CBcSyKSBZerdo0OjqDgMZnWRO";
	private static final String CLIENT_ID = "9UwbdOwiGJmcldZzxTe90GkxzyYzraAZYSyvQl5s";
	
	private static final String LOCATION_CLASS = "RioData";
	private static final String LOCATION_DATA = "pos";
	private static final String DATE_DATA = "date";
	
	public interface OnFetchDoneListener {
		public void onFetchDone(List<Location> locations);
	}
	
	public interface OnSaveDoneListener {
		public void onSaveDoneListener(ParseException e);
	}
	
	public DatabaseHandler(Context context) {
		Parse.initialize(context, APPLICATION_ID, CLIENT_ID);
	}
	
	public void fetchLocationsInBackground(final OnFetchDoneListener onDone) {
		
		if (onDone == null) {
			throw new IllegalArgumentException("onDone cannot be null!");
		}

		ParseQuery.clearAllCachedResults();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery(LOCATION_CLASS);
		query.setLimit(1000);
		query.orderByDescending(DATE_DATA);
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e != null) {
					e.printStackTrace();
					return;
				}
				
				ArrayList<Location> locations = new ArrayList<Location>();
				
				for (ParseObject obj : objects) {
					Location location = new Location("Parse.ProjectAngra");
					
					ParseGeoPoint pos = obj.getParseGeoPoint(LOCATION_DATA);
					
					location.setLatitude(pos.getLatitude());
					location.setLongitude(pos.getLongitude());
					
					locations.add(location);
				}
				
				onDone.onFetchDone(locations);
			}
		});
	}
	
	public void fetchNearestLocationsInBackground(GeoPoint location, final OnFetchDoneListener onDone) {
		
		if (onDone == null) {
			throw new IllegalArgumentException("onDone cannot be null!");
		}

		ParseQuery.clearAllCachedResults();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery(LOCATION_CLASS);
		query.setLimit(1000);
		query.whereNear(LOCATION_DATA, new ParseGeoPoint(location.getLatitude(), location.getLongitude()));
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e != null) {
					e.printStackTrace();
					return;
				}
				
				ArrayList<Location> locations = new ArrayList<Location>();
				
				for (ParseObject obj : objects) {
					Location location = new Location("Parse.ProjectAngra");
					
					ParseGeoPoint pos = obj.getParseGeoPoint(LOCATION_DATA);
					
					location.setLatitude(pos.getLatitude());
					location.setLongitude(pos.getLongitude());
					
					locations.add(location);
				}
				
				onDone.onFetchDone(locations);
			}
		});
	}
	
	public void saveLocationInBackground(Location location) {
		
		ParseObject obj = new ParseObject(LOCATION_CLASS);
		ParseGeoPoint point = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
		obj.put(LOCATION_DATA, point);
		obj.saveInBackground();
	}
	
	public void saveLocationInBackground(Location location, final OnSaveDoneListener onDone) {
		
		final ParseObject obj = new ParseObject(LOCATION_CLASS);
		ParseGeoPoint point = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
		obj.put(LOCATION_DATA, point);
		obj.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if (onDone != null) {
					obj.put(DATE_DATA, obj.getCreatedAt());
					obj.saveInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException e) {
							if (onDone != null) {
								onDone.onSaveDoneListener(e);
							}
						}
					});
				}
			}
		});
	}
}
