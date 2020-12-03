package com.apolis.groceryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.apolis.groceryapp.R
import com.apolis.groceryapp.app.Endpoints
import com.apolis.groceryapp.models.Address
import com.apolis.groceryapp.models.Address.Companion.KEY_ADDRESS_EDIT
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONObject

class EditAddressActivity : AppCompatActivity() {

    var address: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        address = intent.getSerializableExtra(KEY_ADDRESS_EDIT) as Address
        init()
    }

    private fun init() {
        setupToolbar()
        // update the views in this layout
        text_view_title.text = "Update Address"

        //get data and fill up the edit texts
        inputData()

        button_save.setOnClickListener {
            updateAddress(address?._id)
            startActivity(Intent(this, AddressListActivity::class.java))
            finish()
        }
    }

    private fun inputData() {
        edit_text_type.setText(address?.type)
        edit_text_house_no.setText(address?.houseNo)
        edit_text_street.setText(address?.streetName)
        edit_text_city.setText(address?.city)
        edit_text_pincode.setText(address?.pincode.toString())
    }

    private fun updateAddress(id: String?) {
        var params = HashMap<String, String>()
        params["type"] = edit_text_type.text.toString()
        params["houseNo"] = edit_text_house_no.text.toString()
        params["streetName"] = edit_text_street.text.toString()
        params["city"] = edit_text_city.text.toString()
        params["pincode"] = edit_text_pincode.text.toString()
        var jsonObject = JSONObject(params as Map<*, *>)

        var requestQueue = Volley.newRequestQueue(this)
        var request = JsonObjectRequest(
            Request.Method.PUT,
            Endpoints.getAddress(id!!),
            jsonObject,
            Response.Listener {
                Toast.makeText(this, "Address updated", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            })
        requestQueue.add(request)
    }

    private fun setupToolbar() {
        toolbar.title = "Delivery Address"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this, AddressListActivity::class.java))
        return true
    }
}