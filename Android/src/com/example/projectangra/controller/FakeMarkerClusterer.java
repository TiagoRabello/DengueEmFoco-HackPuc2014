package com.example.projectangra.controller;

import java.util.ArrayList;

import org.osmdroid.bonuspack.clustering.MarkerClusterer;
import org.osmdroid.bonuspack.clustering.StaticCluster;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.views.MapView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import com.example.projectangra.R;

public class FakeMarkerClusterer extends MarkerClusterer {

	Context m_context;
	
	public FakeMarkerClusterer(Context ctx) {
		super(ctx);
		m_context = ctx;
	}

	@Override
	public ArrayList<StaticCluster> clusterer(MapView mapView) {
		ArrayList<StaticCluster> result = new ArrayList<StaticCluster>();
		for (Marker item : mItems) {
			StaticCluster cluster = new StaticCluster(item.getPosition());
			cluster.add(item);
			result.add(cluster);
		}
		return result;
	}

	@Override
	public Marker buildClusterMarker(StaticCluster cluster, MapView mapView) {
		// Create marker.
        Marker m = new Marker(mapView);
        m.setPosition(cluster.getPosition());
        m.setInfoWindow(null);
        m.setAnchor(0.5f, 0.5f);
        
        int radius = 30;
        int width = radius * 2 + 4;
        int height = radius * 2 + 4;
        
        Bitmap finalIcon = Bitmap.createBitmap(width, height, mClusterIcon.getConfig());
        Canvas iconCanvas = new Canvas(finalIcon);
        Paint circlePaint = new Paint();
        
        // Draw highlight fill.
        circlePaint.setColor(m_context.getResources().getColor(R.color.highlight_circle_fill_color));
        circlePaint.setAlpha(13);
        iconCanvas.drawCircle(width / 2.0f, height / 2.0f, radius, circlePaint);
        
        m.setIcon(new BitmapDrawable(mapView.getContext().getResources(), finalIcon));
        return m;
	}

	@Override
	public void renderer(ArrayList<StaticCluster> clusters, Canvas canvas,
			MapView mapView) {
		for  (StaticCluster cluster:clusters){
			Marker m = buildClusterMarker(cluster, mapView);
			cluster.setMarker(m);
		}

	}

}
