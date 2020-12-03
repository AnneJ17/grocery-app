import android.content.Context
import com.apolis.groceryapp.models.Address
import com.apolis.groceryapp.models.User
import com.google.gson.Gson

class SessionManager(mContext: Context) {
    private val FILE_NAME = "my_pref"
    private val KEY_USER_ID = "userID"
    private val KEY_NAME = "name"
    private val KEY_EMAIL = "email"
    private val KEY_MOBILE = "mobile"
    private val KEY_PASSWORD = "password"
    private var KEY_IS_LOGGED_IN = "isLoggedIn"


    var sharedPreference = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    var editor = sharedPreference.edit()

    fun register(user: User) {      //fun login(name: String, email: String, password: String) {
        editor.putString(KEY_USER_ID, user._id)
        editor.putString(KEY_NAME, user.firstName)
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_PASSWORD, user.password)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.commit()
    }

    fun isLoggedIn(): Boolean{
        return sharedPreference.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun logOut() {
        editor.clear()
        editor.commit()
    }

    fun getUserId(): String? {
        return sharedPreference.getString(KEY_USER_ID, null)
    }

    fun getUser(): User {
        var name = sharedPreference.getString(KEY_NAME, null)
        var email = sharedPreference.getString(KEY_EMAIL, null)
        var mobile = sharedPreference.getString(KEY_MOBILE, null)
        return User(firstName = name, email = email, mobile = mobile)
    }
}