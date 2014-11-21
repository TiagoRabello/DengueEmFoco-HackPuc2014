package com.example.projectangra.controller;

import org.osmdroid.bonuspack.clustering.GridMarkerClusterer;
import org.osmdroid.bonuspack.clustering.StaticCluster;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.views.MapView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;

import com.example.projectangra.R;

public class MultilevelGridMarkerClusterer extends GridMarkerClusterer {

	private Context m_context;
	
	public MultilevelGridMarkerClusterer(Context ctx) {
		super(ctx);
		m_context = ctx;
		mTextPaint.setShadowLayer(2, 0, 0, m_context.getResources().getColor(R.color.highlight_text_shadow_color));
		mTextPaint.setColor(m_context.getResources().getColor(R.color.highlight_text_color));
	}
	
	@Override public Marker buildClusterMarker(StaticCluster cluster, MapView mapView){
		// Create marker.
        Marker m = new Marker(mapView);
        m.setPosition(cluster.getPosition());
        m.setInfoWindow(null);
        m.setAnchor(0.5f, 0.5f);
        
        // Calculate dimensions.
        int numMarkers = cluster.getSize();
        int zoomLevel = mapView.getZoomLevel();
        
        //int radius = (int) Math.sqrt(2 * numMarkers * zoomLevel * zoomLevel / Math.PI);
        int radius = (int)Math.sqrt((float)numMarkers / 100000 * zoomLevel * zoomLevel * zoomLevel * zoomLevel * zoomLevel * zoomLevel);
        int width = radius*2 + 10;
        int height = radius*2 + 10;
        
        Bitmap finalIcon = Bitmap.createBitmap(width, height, mClusterIcon.getConfig());
        Canvas iconCanvas = new Canvas(finalIcon);
        Paint circlePaint = new Paint();
        
        // Draw highlight fill.
        circlePaint.setColor(m_context.getResources().getColor(R.color.highlight_circle_fill_color));
        circlePaint.setAlpha(40);
        iconCanvas.drawCircle(width / 2.0f, height / 2.0f, radius, circlePaint);

        // Draw highlight stroke.
        circlePaint.setColor(m_context.getResources().getColor(R.color.highlight_circle_stroke_color));
        circlePaint.setAlpha(100);
        circlePaint.setStyle(Style.STROKE);
        circlePaint.setStrokeWidth(4.0f);
        iconCanvas.drawCircle(width / 2.0f, height / 2.0f, radius, circlePaint);
        
        // Draw number of markers.
        String text = Integer.toString(cluster.getSize());
        int textHeight = (int) (mTextPaint.descent() + mTextPaint.ascent());
        iconCanvas.drawText(text, 
                        mTextAnchorU*finalIcon.getWidth(), 
                        mTextAnchorV*finalIcon.getHeight() - textHeight/2, 
                        mTextPaint);
        
        m.setIcon(new BitmapDrawable(mapView.getContext().getResources(), finalIcon));
        return m;
	}
}
