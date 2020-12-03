package com.apolis.groceryapp.activities

import SessionManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.apolis.groceryapp.R
import com.apolis.groceryapp.adapters.CheckoutAdapter
import com.apolis.groceryapp.app.Endpoints
import com.apolis.groceryapp.database.DBHelper
import com.apolis.groceryapp.models.Address
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.activity_payment.recycler_view
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONArray
import org.json.JSONObject

class PaymentActivity : AppCompatActivity() {

    var address: Address? = null
    lateinit var dbHelper: DBHelper
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        init()
    }

    private fun init() {
        dbHelper = DBHelper(this)
        sessionManager = SessionManager(this)

        setupToolbar()
        //get the chosen address detail
        address = intent.getSerializableExtra(Address.KEY_ADDRESS_SELECTED) as Address
        //Set up the text views
        setUp()

        // Fill the recycler view with cart items
        checkedOut()

        // Post the address on confirm
        button_confirm.setOnClickListener {
            postOrderDetails()
        }
    }

    private fun postOrderDetails() {

        // create a json object for order
        var orderSummary = dbHelper.calculateReceipt()
        var order = HashMap<String, String>()
        order["discount"] = orderSummary.discount.toString()
        order["ourPrice"] = orderSummary.ourPrice.toString()
        order["totalAmount"] = orderSummary.totalAmount.toString()
        order["deliveryCharges"] = orderSummary.deliveryCharges.toString()

        var orderJsonObject = JSONObject(order as Map<*, *>)

        // create a json object for address
        var address = HashMap<String, String>()
        address["type"] = this.address!!.type
        address["houseNo"] = this.address!!.houseNo
        address["streetName"] = this.address!!.streetName
        address["city"] = this.address!!.city
        address["pincode"] = this.address!!.pincode.toString()
        var addressJsonObject = JSONObject(address as Map<*, *>)

        // create a json object for user
        var user = HashMap<String, String>()
        user["email"] = sessionManager.getUser().email!!
        var userJsonObject = JSONObject(user as Map<*, *>)

        //create a json object for products in the cart
        var cartItems = dbHelper.readItems()
        var jsonArray: ArrayList<Map<String, Any>> = ArrayList()
        for (i in 0 until cartItems.size) {
            var item = cartItems[i]
            var products = HashMap<String, String>()
            products["_id"] = item._id
            products["productName"] = item.productName
            products["quantity"] = item.quantity.toString()
            products["price"] = item.price.toString()
            jsonArray.add(products as Map<String, Any>)
        }

        // Parent object
        var params = HashMap<String, Any>()
        params["userId"] = sessionManager.getUserId().toString()
        params["orderStatus"] = "completed"
        params["products"] = jsonArray
        params["orderSummary"] = orderJsonObject
        params["shippingAddress"] = addressJsonObject
        params["user"] = userJsonObject

        var jsonObject = JSONObject(params as Map<*, *>)

        var request = JsonObjectRequest(
            Request.Method.POST,
            Endpoints.postOrder(),
            jsonObject,
            Response.Listener {
                dbHelper.clearCart()
                startActivity(Intent(this, ConfirmationActivity::class.java))
            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        )
        Volley.newRequestQueue(this).add(request)
    }

    private fun checkedOut() {
        var checkoutItems = dbHelper.readItems()
        var checkoutAdapter = CheckoutAdapter(this, checkoutItems)
        recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = checkoutAdapter

    }

    private fun setUp() {
        var sessionManager = SessionManager(this)
        text_view_userName.text = sessionManager.getUser().firstName
        text_view_home_no.text = address?.houseNo + ", "
        text_view_street_name.text = address?.streetName
        text_view_city.text = address?.city + ", "
        text_view_pincode.text = address?.pincode.toString()
        text_view_total_price.text = " ₹" + dbHelper.calculateReceipt().ourPrice
    }

    private fun setupToolbar() {
        toolbar.title = "Checkout"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}