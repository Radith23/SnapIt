package sns.example.snapit.utils

import android.content.Context
import android.content.SharedPreferences
import sns.example.snapit.utils.ConstVal.KEY_EMAIL
import sns.example.snapit.utils.ConstVal.KEY_IS_LOGIN
import sns.example.snapit.utils.ConstVal.KEY_TOKEN
import sns.example.snapit.utils.ConstVal.KEY_USER_NAME
import sns.example.snapit.utils.ConstVal.PREFS_NAME

class SessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    fun setStringPreference(prefKey: String, value: String) {
        editor.putString(prefKey, value)
        editor.apply()
    }

    fun clearPreferences() {
        editor.clear().apply()
    }

    fun setBooleanPreference(prefKey: String, value: Boolean) {
        editor.putBoolean(prefKey, value)
        editor.apply()
    }

    val getToken = prefs.getString(KEY_TOKEN, "")
    val isLogin = prefs.getBoolean(KEY_IS_LOGIN, false)
    val getUserName = prefs.getString(KEY_USER_NAME, "")
    val getEmail = prefs.getString(KEY_EMAIL, "")
}