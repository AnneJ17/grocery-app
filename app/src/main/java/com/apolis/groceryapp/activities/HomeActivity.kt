package com.apolis.groceryapp.activities

import SessionManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.apolis.groceryapp.R
import com.apolis.groceryapp.adapters.CategoryAdapter
import com.apolis.groceryapp.adapters.ViewPagerAdapter
import com.apolis.groceryapp.app.Endpoints
import com.apolis.groceryapp.database.DBHelper
import com.apolis.groceryapp.models.CategoryResponse
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.home_content.*
import kotlinx.android.synthetic.main.nav_header.view.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    lateinit var categoryAdapter: CategoryAdapter
    private val images = arrayOf(R.drawable.placeholder, R.drawable.placeholder, R.drawable.placeholder)
    private var textViewItemCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        init()
    }

    private fun init() {
        setupToolbar()
        getData()

        drawerLayout = drawer_layout
        navView = nav_view
        var headerView = navView.getHeaderView(0)
        var logout_menu = navView.menu.findItem(R.id.menu_logout)
        var login_menu = navView.menu.findItem(R.id.menu_login)
        var menu_profile = navView.menu.findItem(R.id.menu_profile)
        var menu_order = navView.menu.findItem(R.id.menu_order_summary_list)
        var sessionManager = SessionManager(this)
        if(sessionManager.isLoggedIn()) {
            headerView.text_view_name.text = sessionManager.getUser().firstName
            headerView.text_view_email.text = sessionManager.getUser().email
            logout_menu.isVisible = true
            login_menu.isVisible = false
            menu_profile.isVisible = true
            menu_order.isVisible = true

        } else {
            headerView.text_view_name.text = "Guest"
            headerView.text_view_email.text = ""
            logout_menu.isVisible = false
            login_menu.isVisible = true
            menu_profile.isVisible = false
            menu_order.isVisible = false
        }

        var toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        categoryAdapter = CategoryAdapter(this)
        recycler_view_home.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_home.adapter = categoryAdapter

        imageSlider()
    }

    private fun imageSlider() {
        var adapter = ViewPagerAdapter(this, images)
        view_pager.adapter = adapter
        // connecting with tab layout
        tab_layout.setupWithViewPager(view_pager, true)
    }

    private fun getData() {
        var requestQueue = Volley.newRequestQueue(this)
        var request = StringRequest(Request.Method.GET, Endpoints.getCategory(), Response.Listener {
            progress_bar.visibility = View.GONE
            var gson = Gson()
            var categoryResponse = gson.fromJson(it.toString(), CategoryResponse::class.java)
            categoryAdapter.setData(categoryResponse.data)
        },
            Response.ErrorListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            })
        requestQueue.add(request)
    }

    private fun setupToolbar(){
        toolbar.title = "Categories"
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        var menuItem = menu?.findItem(R.id.cart_badge)
        var actionView = menuItem?.actionView
        textViewItemCount = actionView?.findViewById(R.id.cart_badge)

        setUpBadge()

        actionView?.setOnClickListener {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
            onOptionsItemSelected(menuItem!!)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_cart -> startActivity(Intent(this, CartActivity::class.java))
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var sessionManager = SessionManager(applicationContext)
        when(item.itemId) {
            R.id.menu_profile -> startActivity(Intent(this, UserProfileActivity::class.java))
            R.id.menu_login -> startActivity(Intent(this, LoginActivity::class.java))
            R.id.menu_logout -> {
                sessionManager.logOut()
                finish()
                startActivity(intent)
            }
            R.id.menu_order_summary_list -> startActivity(Intent(applicationContext, ReceiptActivity::class.java))
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setUpBadge() {
        var dbHelper = DBHelper(this)
        if(dbHelper.itemsInCart() != null) {
            if (dbHelper.itemsInCart() == 0) {
                if(textViewItemCount?.visibility == View.VISIBLE) {
                    textViewItemCount?.visibility = View.GONE
                }
            } else {
                if(textViewItemCount?.visibility == View.GONE) {
                    textViewItemCount?.visibility = View.VISIBLE
                    textViewItemCount?.text = dbHelper.itemsInCart().toString()
                }
            }
        }
    }
}