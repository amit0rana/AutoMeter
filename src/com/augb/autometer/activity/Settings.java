/**
 * 
 */
package com.augb.autometer.activity;

import com.augb.autometer.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Settings Activity
 * 
 * @author "Sudar Muthu (sudarm@)"
 *
 */
public class Settings extends PreferenceActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
}