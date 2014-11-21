package com.example.projectangra.view;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.bonuspack.clustering.MarkerClusterer;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.projectangra.R;
import com.example.projectangra.controller.DatabaseHandler;
import com.example.projectangra.controller.DatabaseHandler.OnFetchDoneListener;
import com.example.projectangra.controller.DatabaseHandler.OnSaveDoneListener;
import com.example.projectangra.controller.FakeMarkerClusterer;
import com.example.projectangra.controller.GoogleMapsTileSource;
import com.example.projectangra.controller.MultilevelGridMarkerClusterer;
import com.gc.materialdesign.views.ButtonFloat;
import com.parse.ParseException;


public class HomeActivity extends ActionBarActivity {

	private MapView m_map;
	private ButtonFloat m_buttonCreate;
	private DatabaseHandler m_database;
	private MarkerClusterer m_markers;
	private MyLocationNewOverlay m_userLocationOverlay;
	private ButtonFloat m_buttonCenterCamera;
	private ButtonFloat m_buttonSwitch;
	
	private boolean usingGridClusterer;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        m_buttonSwitch = (ButtonFloat) findViewById(R.id.switch_scheme);
        m_buttonSwitch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<Marker> markers = m_markers.getItems();
				m_map.getOverlays().remove(m_markers);
				if (usingGridClusterer){
					m_markers = createFakeMarkerClusterer();
				} else {
					m_markers = createGridMarkerClusterer();
				}
				m_markers.getItems().addAll(markers);
				m_map.getOverlays().add(0, m_markers);
				usingGridClusterer = !usingGridClusterer;
				
				notifyDataChanged();
			}
		});
        
        m_database = new DatabaseHandler(this);
        
        m_map = configMap();
        
        m_userLocationOverlay = configMyLocation(m_map);
        m_map.getOverlays().add(m_userLocationOverlay);

        m_markers = createFakeMarkerClusterer();
        m_map.getOverlays().add(m_markers);
        usingGridClusterer = false;
        
        m_buttonCreate = configButtonCreate(m_userLocationOverlay, m_database);
        m_buttonCenterCamera = configButtonCenter();

        syncData();
    }
    
    private MapView configMap() {
    	 MapView map = (MapView) findViewById(R.id.display_map);
    	 map.setTileSource(new GoogleMapsTileSource());
    	 map.setBuiltInZoomControls(false);
    	 map.setMultiTouchControls(true);
    	 map.getController().setZoom(18);
    	 
    	 return map;
    }
    
    private MyLocationNewOverlay configMyLocation(MapView map) {
    	MyLocationNewOverlay myLocation = new MyLocationNewOverlay(this, map);
    	myLocation.enableMyLocation();
    	myLocation.enableFollowLocation();
    	myLocation.setDrawAccuracyEnabled(true);
    	myLocation.setOptionsMenuEnabled(false);
    	
    	return myLocation;
    }
    
    private ButtonFloat configButtonCenter() {
    	ButtonFloat buttonCenterCamera = (ButtonFloat) findViewById(R.id.center_camera);
        buttonCenterCamera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				centerCamera();
			}
		});
        
        return buttonCenterCamera;
    }
    
    private ButtonFloat configButtonCreate(final MyLocationNewOverlay myLocation, final DatabaseHandler database) {
        ButtonFloat buttonCreate = (ButtonFloat) findViewById(R.id.create_report);
        buttonCreate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final ProgressDialog dialog = ProgressDialog.show(HomeActivity.this, "", "Registrando", true); 
				Location location = new Location(myLocation.getMyLocationProvider().toString());
				GeoPoint point = myLocation.getMyLocation();
				location.setLatitude(point.getLatitude());
				location.setLongitude(point.getLongitude());	
				database.saveLocationInBackground(location, new OnSaveDoneListener() {
					
					@Override
					public void onSaveDoneListener(ParseException e) {
						dialog.dismiss();
						if (e == null) {
							Toast.makeText(HomeActivity.this, "Registro salvo", Toast.LENGTH_SHORT).show();
							syncData();
						}
						else {
							e.printStackTrace();
						}
					}
				});
			}
		});
        
        return buttonCreate;
    }
    
    private MarkerClusterer createGridMarkerClusterer() {
        MultilevelGridMarkerClusterer markers = new MultilevelGridMarkerClusterer(this);
        markers.setGridSize(100);
        Drawable clusterIconD = getResources().getDrawable(R.drawable.m1);
        Bitmap clusterIcon = ((BitmapDrawable)clusterIconD).getBitmap();
        markers.setIcon(clusterIcon);
        
        return markers;
    }
    
    private MarkerClusterer createFakeMarkerClusterer() {
    	FakeMarkerClusterer markers = new FakeMarkerClusterer(this);
        Drawable clusterIconD = getResources().getDrawable(R.drawable.m1);
        Bitmap clusterIcon = ((BitmapDrawable)clusterIconD).getBitmap();
        markers.setIcon(clusterIcon);
        
        return markers;
    }
    
    private void notifyDataChanged() {
    	m_markers.invalidate();
    	m_map.postInvalidate();
    }
    
    private void syncData() {    	
        m_database.fetchLocationsInBackground(new OnFetchDoneListener() {
			
			@Override
			public void onFetchDone(List<Location> locations) {
				m_markers.getItems().clear();
				
				for (Location location : locations) {					
					Marker marker = new Marker(m_map);
					marker.setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));
					marker.setIcon(getResources().getDrawable(R.drawable.m1));
					m_markers.add(marker);
				}
				
				notifyDataChanged();
			}
		});
    }
    
    private void centerCamera() {
    	m_map.getController().animateTo(m_userLocationOverlay.getMyLocation());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        //if (id == R.id.action_settings) {
        //	Intent i = new Intent(this, Preference.class);
        // 	startActivity(i);
        //    return true;
        //}
        return super.onOptionsItemSelected(item);
    }
}
