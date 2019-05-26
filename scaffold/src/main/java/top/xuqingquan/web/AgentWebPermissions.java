package top.xuqingquan.web;

import android.Manifest;

class AgentWebPermissions {
	static final String[] CAMERA;
	static final String[] LOCATION;
	static final String[] STORAGE;
	static final String ACTION_CAMERA = "Camera";
	static final String ACTION_LOCATION = "Location";
	static final String ACTION_STORAGE = "Storage";
	static {
		CAMERA = new String[]{
				Manifest.permission.CAMERA};
		LOCATION = new String[]{
				Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.ACCESS_COARSE_LOCATION};
		STORAGE = new String[]{
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE};
	}
}
