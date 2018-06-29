package co.mobiwise.materialintro.prefs

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by mertsimsek on 29/01/16.
 */
class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun isDisplayed(id: String): Boolean = sharedPreferences.getBoolean(id, false)

    fun setDisplayed(id: String) {
        sharedPreferences.edit().putBoolean(id, true).apply()
    }

    fun reset(id: String) {
        sharedPreferences.edit().putBoolean(id, false).apply()
    }

    fun resetAll() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private val PREFERENCES_NAME = "material_intro_preferences"
    }
}
