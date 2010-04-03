package com.augb.autometer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;

public class LocationActivity extends Activity implements GpsNotificationListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        GpsLocationManager gMgr = GpsLocationManager.getGPSLocationManger();
        gMgr.start(this, this.getApplicationContext());
    }

	@Override
	public void onUpdate(GpsLocation loc) {
		Log.d("AUTO", loc.toString());
	}
}