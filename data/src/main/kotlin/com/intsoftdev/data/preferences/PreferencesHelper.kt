package com.intsoftdev.data.preferences

import android.content.Context
import android.content.SharedPreferences

internal class PreferencesHelper(context: Context) {

    companion object {
        private val PREF_PACKAGE_NAME = "com.intsoftdev.postsdemo"

        private val PREF_KEY_LAST_UPDATE = "last_update"
    }

    private val sharedPref: SharedPreferences

    init {
        sharedPref = context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Store and retrieve the last time data was cached
     */
    var lastUpdateTime: Long
        get() = sharedPref.getLong(PREF_KEY_LAST_UPDATE, 0)
        set(lastUpdate) = sharedPref.edit().putLong(PREF_KEY_LAST_UPDATE, lastUpdate).apply()
}
