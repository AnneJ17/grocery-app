package com.apolis.groceryapp.activities

import SessionManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apolis.groceryapp.R
import com.apolis.groceryapp.helpers.SwipeToDelete
import com.apolis.groceryapp.adapters.CartAdapter
import com.apolis.groceryapp.database.DBHelper
import com.apolis.groceryapp.models.Product
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.cart_empty.*

class CartActivity : AppCompatActivity(), CartAdapter.OnAdapterListener {

    var cartItems: ArrayList<Product> = ArrayList()
    lateinit var dbHelper: DBHelper
    lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        dbHelper = DBHelper(this)

        init()
    }

    private fun init() {
        var sessionManager = SessionManager(this)
        setupToolbar()

        // read data from the database
        cartItems = dbHelper.readItems()

        cartAdapter = CartAdapter(this)
        cartAdapter.setOnAdapterListener(this)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = cartAdapter
        cartAdapter.setData(cartItems)

        onSwipeDelete()

        updateUI()

        button_checkout.setOnClickListener {
            if (sessionManager.isLoggedIn()) {
                startActivity(Intent(this, AddressListActivity::class.java))
            }
            else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        // Show empty cart view
        isCartEmpty()
    }

    private fun onSwipeDelete() {
        // swipe to delete callback
        val item = object : SwipeToDelete(this, 0, ItemTouchHelper.RIGHT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                cartAdapter.deleteItem(viewHolder.adapterPosition)
                updateUI()
                isCartEmpty()
            }
        }
        val itemTouchHelper = ItemTouchHelper(item)
        itemTouchHelper.attachToRecyclerView(recycler_view)
    }

    private fun updateUI() {
        // receipt for the in cart items
        var receipt = dbHelper.calculateReceipt()
        text_view_subtotal_value.text = "₹${receipt.totalAmount}.00"
        text_view_discount_value.text = "- ₹${receipt.discount}.00"
        text_view_total_value.text = "₹${receipt.ourPrice}.00"
        text_view_delivery_charges.text = "₹${receipt.deliveryCharges}.00"
    }

    private fun setupToolbar() {
        toolbar.title = "My Cart"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun updateItemChanged(item: Product, position: Int) {
        dbHelper.updateQuantity(item)
        cartItems[position] = item
        cartAdapter.notifyItemChanged(position, item)
    }

    private fun isCartEmpty() {
        if(dbHelper.isCartEmpty()) {
            main_layout.visibility = View.GONE
            button_back_to_menu.setOnClickListener {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }

    override fun onButtonClicked(view: View, position: Int) {
        var item = cartItems[position]
        when(view.id) {
            R.id.button_add -> {
                item.quantity++
                updateItemChanged(item, position)
                updateUI()
            }
            R.id.button_minus -> {
                if(item.quantity > 1) {
                    item.quantity--
                    updateItemChanged(item, position)
                    updateUI()
                }
            }
        }
    }
}