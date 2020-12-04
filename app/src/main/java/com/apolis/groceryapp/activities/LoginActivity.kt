package com.apolis.groceryapp.activities

import SessionManager
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.apolis.groceryapp.R
import com.apolis.groceryapp.app.Endpoints
import com.apolis.groceryapp.models.LoginResponse
import com.apolis.groceryapp.models.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var sessionManager = SessionManager(this)

        if(sessionManager.isLoggedIn()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        init()
    }

    private fun init() {
        button_login.setOnClickListener {
            var sessionManager = SessionManager(this)
            var params = HashMap<String, String>()
            var email = edit_text_username.text.toString()
            var password = edit_text_password.text.toString()
            params["email"] = email
            params["password"] = password
            var jsonObject = JSONObject(params as Map<*, *>)

            var request = JsonObjectRequest(
                Request.Method.POST,
                Endpoints.getLogin(),
                jsonObject,
                Response.Listener {
                    var gson = Gson()
                    var loginResponse = gson.fromJson(it.toString(), LoginResponse::class.java)
                    var name = loginResponse.user.firstName
                    var email = loginResponse.user.email
                    var password = loginResponse.user.password
                    var userId = loginResponse.user._id
                    var user = User(userId, name, email, password)
                    sessionManager.register(user)
                    setResult(Activity.RESULT_OK)
                    startActivity(Intent(this, HomeActivity::class.java))
                },
                Response.ErrorListener {
                    Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                }
            )
            Volley.newRequestQueue(this).add(request)
        }

        text_view_signup.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}