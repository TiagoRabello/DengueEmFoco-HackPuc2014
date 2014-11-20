package com.example.projectangra.view;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.projectangra.R;
import com.gc.materialdesign.views.ButtonFloat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


public class HomeActivity extends ActionBarActivity {

	private GoogleMap m_map;
	private ButtonFloat m_buttonCreate;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.display_map);
        m_map = mapFragment.getMap();
        
        if (m_map != null) {
        	m_map.setMyLocationEnabled(true);
        	m_map.getUiSettings().setCompassEnabled(true);
            m_map.getUiSettings().setZoomControlsEnabled(false);
            m_map.getUiSettings().setMyLocationButtonEnabled(false);
             
            m_map.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
                 
                @Override
                public void onMyLocationChange(Location location) {
                    centerCameraAt(location);
                }
            });
        }
        
        m_buttonCreate = (ButtonFloat) findViewById(R.id.create_report);
        m_buttonCreate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(HomeActivity.this, "TODO", Toast.LENGTH_LONG).show();
			}
		});
    }
    
    private void centerCameraAt(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        m_map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
