package com.apolis.groceryapp.activities

import SessionManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.apolis.groceryapp.R
import com.apolis.groceryapp.app.Endpoints
import com.apolis.groceryapp.models.Address
import com.apolis.groceryapp.models.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONObject

class AddAddressActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        init()
    }

    private fun init() {
        sessionManager = SessionManager(this)

        setupToolbar()


        button_save.setOnClickListener {
            var userId = sessionManager.getUserId().toString()
            var type = edit_text_type.text.toString()
            var houseNo = edit_text_house_no.text.toString()
            var street = edit_text_street.text.toString()
            var city = edit_text_city.text.toString()
            var pincode = edit_text_pincode.text.toString()

            // create a json object to pass
            var params = HashMap<String, String>()
            params["userId"] = userId
            params["type"] = type
            params["houseNo"] = houseNo
            params["streetName"] = street
            params["city"] = city
            params["pincode"] = pincode
            var jsonObject = JSONObject(params as Map<*, *>)

            var request = JsonObjectRequest(
                Request.Method.POST,
                Endpoints.postAddress(),
                jsonObject,
                Response.Listener {
                    finish()
                    startActivity(Intent(this, AddressListActivity::class.java))
                },
                Response.ErrorListener {
                    Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            )
            Volley.newRequestQueue(this).add(request)
        }
    }

    private fun setupToolbar() {
        toolbar.title = "Add Address"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}