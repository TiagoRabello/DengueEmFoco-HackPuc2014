package com.example.projectangra.controller;

import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;

public class GoogleMapsTileSource extends OnlineTileSourceBase {
	
	public GoogleMapsTileSource() {
		super("Google Maps"
		    , ResourceProxy.string.unknown
		    , 1, 20, 256
		    , ".png"
		    , new String[] {"http://mt3.google.com/vt/v=w2.97"});
	}

	@Override
	public String getTileURLString(MapTile tile) {
		/*
		 * GOOGLE MAPS URL looks like
		 * 	base url	    const	x	y    zoom
		 * http://mt3.google.com/vt/v=w2.97&x=74327&y=50500&z=17
		 */
		return getBaseUrl() + "&x=" + tile.getX() + "&y=" + tile.getY() + "&z=" + tile.getZoomLevel();
	}

}
