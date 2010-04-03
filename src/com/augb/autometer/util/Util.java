/**
 * 
 */
package com.augb.autometer.util;

import android.content.Context;
import android.preference.PreferenceManager;

import com.augb.autometer.R;

/**
 * Util class with wrapper methods to Shared Preference
 * 
 * @author "Sudar Muthu (sudarm@)"
 *
 */
public final class Util {
	/**
	 * Returns the base fare
	 * 
	 * @param context
	 * @return
	 */
	public static float getBaseFare(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getFloat(context.getString(R.string.base_fare_key), 14);
	}

	/**
	 * Returns the Base Distance
	 * 
	 * @param context
	 * @return
	 */
	public static float getBaseDistance(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getFloat(context.getString(R.string.base_fare_distance_key), 2);
	}

	/**
	 * Returns the Fare per unit distance
	 * 
	 * @param context
	 * @return
	 */
	public static float getFarePerDistance(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getFloat(context.getString(R.string.fare_per_unit_distance_key), 7);
	}
	
}
