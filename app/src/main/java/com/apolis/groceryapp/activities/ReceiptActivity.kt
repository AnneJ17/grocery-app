package com.apolis.groceryapp.activities

import SessionManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.apolis.groceryapp.R
import com.apolis.groceryapp.adapters.ReceiptAdapter
import com.apolis.groceryapp.app.Endpoints
import com.apolis.groceryapp.models.ReceiptResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_receipt.*
import kotlinx.android.synthetic.main.app_bar.*

class ReceiptActivity : AppCompatActivity() {

    lateinit var receiptAdapter: ReceiptAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)

        init()
    }

    private fun init() {
        setupToolbar()

        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        receiptAdapter = ReceiptAdapter(this)
        recycler_view.adapter = receiptAdapter

        getOrderSummary()
    }

    private fun getOrderSummary() {
        var sessionManager = SessionManager(this)
        var userId = sessionManager.getUserId()
        var requestQueue = Volley.newRequestQueue(this)
        var request = StringRequest(
            Request.Method.GET,
            Endpoints.getOrderSummary(userId!!),
            Response.Listener {
                progress_bar.visibility = View.GONE
                var gson = Gson()
                var receiptResponse = gson.fromJson(it, ReceiptResponse::class.java)
                receiptAdapter.setData(receiptResponse.data)

            },
            Response.ErrorListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            }
        )
        requestQueue.add(request)
    }

    private fun setupToolbar() {
        toolbar.title = "Order Summary"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}