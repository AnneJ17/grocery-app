package com.apolis.groceryapp.activities

import SessionManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.apolis.groceryapp.R
import com.apolis.groceryapp.adapters.AddressAdapter
import com.apolis.groceryapp.app.Endpoints
import com.apolis.groceryapp.helpers.SwipeToDelete
import com.apolis.groceryapp.models.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.activity_address_list.recycler_view
import kotlinx.android.synthetic.main.app_bar.*

class AddressListActivity : AppCompatActivity(), AddressAdapter.OnAdapterListener {

    lateinit var addressAdapter: AddressAdapter
    var addressList: ArrayList<Address> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)

        setupToolbar()
        init()
    }

    private fun init() {
        getAddresses()

        // decorating recycler view
        recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recycler_view.layoutManager = LinearLayoutManager(this)
        addressAdapter = AddressAdapter(this)
        addressAdapter.setOnAdapterListener(this)
        recycler_view.adapter = addressAdapter

        onSwipeDelete()

        text_view_delivery_address.setOnClickListener {
            finish()
            startActivity(Intent(this, AddAddressActivity::class.java))
        }

        button_proceed.setOnClickListener {
            // send the address to payment activity
            if (intent.hasExtra(Address.KEY_ADDRESS_SELECTED)) {
                startActivity(Intent(this, PaymentActivity::class.java))
            } else {
                Toast.makeText(this, "Please select the address", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getAddresses() {
        var sessionManager = SessionManager(this)
        var userId = sessionManager.getUserId()
        // getting data from API
        var requestQueue = Volley.newRequestQueue(this)
        var request = StringRequest(
            Request.Method.GET,
            Endpoints.getAddress(userId!!),
            Response.Listener {
                progress_bar.visibility = View.GONE
                var gson = Gson()
                var addressResponse =
                    gson.fromJson(it, AddressResponse::class.java)
                addressList = addressResponse.data
                addressAdapter.setData(addressList)
            },
            Response.ErrorListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            })
        requestQueue.add(request)
    }

    private fun deleteAddress(id: String?) {
        // deleting data from API
        var requestQueue = Volley.newRequestQueue(this)
        var request = StringRequest(
            Request.Method.DELETE,
            Endpoints.getAddress(id!!),
            Response.Listener {
                Toast.makeText(this, "Address deleted", Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            })
        requestQueue.add(request)
    }

    private fun onProceedToCheckout(address: Address) {
        button_proceed.setOnClickListener {
            var intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra(Address.KEY_ADDRESS_SELECTED, address)
            startActivity(intent)
        }
    }

    private fun onEditButtonClicked(address: Address) {
        // pass the data of that particular address
        var intent = Intent(this, EditAddressActivity::class.java)
        intent.putExtra(Address.KEY_ADDRESS_EDIT, address)
        finish()
        startActivity(intent)
    }

    private fun onSwipeDelete() {
        // swipe to delete callback
        val item = object : SwipeToDelete(this, 0, ItemTouchHelper.RIGHT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var item = addressList[viewHolder.adapterPosition]
                deleteAddress(item._id)
                addressAdapter.deleteItem(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(item)
        itemTouchHelper.attachToRecyclerView(recycler_view)
    }

    private fun setupToolbar() {
        toolbar.title = "Delivery Address"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }

    override fun onButtonClicked(view: View, position: Int) {
        var address = addressList[position]
        when (view.id) {
            R.id.button_edit -> onEditButtonClicked(address)
            R.id.radio_button -> onProceedToCheckout(address)
        }
    }
}