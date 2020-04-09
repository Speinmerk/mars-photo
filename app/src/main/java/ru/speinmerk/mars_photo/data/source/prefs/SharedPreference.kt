package ru.speinmerk.mars_photo.data.source.prefs

import android.content.Context
import androidx.preference.PreferenceManager

class SharedPreference(context: Context) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    var lastPageLoaded by prefs.int()
    var isFullLoaded by prefs.boolean()

}