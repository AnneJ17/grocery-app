package com.apolis.groceryapp.activities

import SessionManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.apolis.groceryapp.R
import com.apolis.groceryapp.app.Endpoints
import com.apolis.groceryapp.models.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONObject

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        init()
    }

    private fun init() {
        setupToolbar()

        var sessionManager = SessionManager(this)
        //get user data
        getUserData(sessionManager)
        // On edit icon click view should show edit fields with existing values
        text_view_edit.setOnClickListener {
            text_view_layout.visibility = View.GONE
            edit_layout.visibility = View.VISIBLE
            fillUpEditText(sessionManager)
        }
        // On save update the text views
        button_save.setOnClickListener {
            updateUserData(sessionManager)
            edit_layout.visibility = View.GONE
            text_view_layout.visibility = View.VISIBLE
        }
    }

    private fun updateUserData(sessionManager: SessionManager) {
        var params = HashMap<String, String>()
        params["firstName"] = edit_text_first_name.text.toString()
        params["email"] = edit_text_email.text.toString()
        params["mobile"] = edit_text_mobile.text.toString()
        var jsonObject = JSONObject(params as Map<*, *>)

        var id = sessionManager.getUserId()
        var request = JsonObjectRequest(
            Request.Method.PUT,
            Endpoints.putUser(id!!),
            jsonObject,
            Response.Listener {
                var gson = Gson()
                var userResponse = gson.fromJson(it.toString(), User::class.java)
                var id = userResponse._id
                var name = userResponse.firstName
                var email = userResponse.email
                var mobile = userResponse.mobile
                var password = userResponse.password
                var user = User(id, name, email, password, mobile)
                sessionManager.register(user)
                getUserData(sessionManager)
            },
            Response.ErrorListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            }
        )
        Volley.newRequestQueue(this).add(request)
    }

    private fun fillUpEditText(sessionManager: SessionManager) {
        var user = sessionManager.getUser()
        edit_text_first_name.setText(user.firstName)
        edit_text_email.setText(user.email)
        edit_text_mobile.setText(user.mobile)
    }

    private fun getUserData(sessionManager: SessionManager) {
        var user = sessionManager.getUser()
        text_view_name.text = user.firstName
        text_view_first_name.text = user.firstName
        text_view_email.text = user.email
        text_view_mobile.text = user.mobile
    }

    private fun setupToolbar(){
        toolbar.title = "My Profile"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}