package com.apolis.groceryapp.activities

import SessionManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.apolis.groceryapp.R
import com.apolis.groceryapp.app.Endpoints
import com.apolis.groceryapp.models.LoginResponse
import com.apolis.groceryapp.models.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
    }

    private fun init() {
        button_register.setOnClickListener{
            var name = edit_text_name.text.toString()
            var email = edit_text_email.text.toString()
            var password = edit_text_password.text.toString()

            // create a json object to pass
            var params = HashMap<String, String>()
            params["firstName"] = name      // use the same key the backend application is expecting
            params["email"] = email
            params["password"] = password
            params["mobile"] = "1"
            var jsonObject = JSONObject(params as Map<*, *>)

            var request = JsonObjectRequest(
                Request.Method.POST,
                Endpoints.getRegister(),
                jsonObject,
                Response.Listener {
                    startActivity(Intent(this, LoginActivity::class.java))
                },
                Response.ErrorListener {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                }
            )
            Volley.newRequestQueue(this).add(request)

        }

        text_view_registered.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}