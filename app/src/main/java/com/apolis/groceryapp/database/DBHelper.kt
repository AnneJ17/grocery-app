package com.apolis.groceryapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.apolis.groceryapp.models.Product
import com.apolis.groceryapp.models.OrderSummary

class DBHelper(mContext: Context) :
    SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {

    var db: SQLiteDatabase = this.writableDatabase

    companion object {
        val DATABASE_NAME = "grocery_db"
        val DATABASE_VERSION = 1
        val TABLE_NAME = "CART"
        val COLUMN_ID = "id"
        val COLUMN_NAME = "product_name"
        val COLUMN_IMAGE = "product_image"
        val COLUMN_PRICE = "product_price"
        val COLUMN_MRP = "product_mrp"
        val COLUMN_QUANTITY = "product_quantity"
    }

    override fun onCreate(sqliteDatabase: SQLiteDatabase) {
        var createTable =
            "create table $TABLE_NAME ($COLUMN_ID char(50), $COLUMN_NAME char(50), $COLUMN_IMAGE char(50), $COLUMN_PRICE integer, $COLUMN_MRP integer, $COLUMN_QUANTITY integer)"
        sqliteDatabase.execSQL(createTable)
    }

    override fun onUpgrade(sqliteDatabase: SQLiteDatabase, p1: Int, p2: Int) {
        var dropTable = "drop table $TABLE_NAME"
        sqliteDatabase.execSQL(dropTable)
        onCreate(sqliteDatabase)
    }

    fun addItem(item: Product) {
        var contentValues = ContentValues()
        if (!isItemInCart(item)) {
            contentValues.put(COLUMN_ID, item._id)
            contentValues.put(COLUMN_NAME, item.productName)
            contentValues.put(COLUMN_IMAGE, item.image)
            contentValues.put(COLUMN_PRICE, item.price)
            contentValues.put(COLUMN_MRP, item.mrp)
            contentValues.put(COLUMN_QUANTITY, item.quantity)

            db.insert(TABLE_NAME, null, contentValues)
        } else {
            item.quantity++
            updateQuantity(item)
        }
    }

    fun readItems(): ArrayList<Product> {
        var list: ArrayList<Product> = ArrayList()
        var db = readableDatabase
        var columns =
            arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_IMAGE, COLUMN_PRICE, COLUMN_MRP, COLUMN_QUANTITY)
        var cursor = db.query(TABLE_NAME, columns, null, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                var id = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
                var name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                var image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
                var price = cursor.getInt(cursor.getColumnIndex(COLUMN_PRICE))
                var mrp = cursor.getInt(cursor.getColumnIndex(COLUMN_MRP))
                var quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
                list.add(
                    Product(
                        _id = id.toString(),
                        productName = name,
                        image = image,
                        price = price.toString().toDouble(),
                        mrp = mrp.toString().toDouble(),
                        quantity = quantity
                    )
                )
            } while (cursor.moveToNext())
        }
        return list
    }

    fun deleteItem(id: String) {
        var whereClause = "$COLUMN_ID = ?"
        var whereArgs = arrayOf(id)
        db.delete(TABLE_NAME, whereClause, whereArgs)
    }

    fun updateQuantity(updateItem: Product) {
        var contentValues = ContentValues()
        contentValues.put(COLUMN_QUANTITY, updateItem.quantity)
        var whereClause = "$COLUMN_ID = ?"
        var whereArgs = arrayOf(updateItem._id)
        db.update(TABLE_NAME, contentValues, whereClause, whereArgs)
    }

    fun isItemInCart(product: Product): Boolean {
        var query = "select * from $TABLE_NAME where $COLUMN_ID = ?"
        var whereArgs = arrayOf(product._id)
        var cursor = db.rawQuery(query, whereArgs)
        var count = cursor.count
        return count != 0
    }

    fun itemsInCart(): Int {
        var query = "select * from $TABLE_NAME"
        var cursor = db.rawQuery(query, null)
        return cursor.count
    }

    fun isCartEmpty(): Boolean {
        var query = "select * from $TABLE_NAME"
        var cursor = db.rawQuery(query, null)
        return !cursor.moveToFirst()
    }

    fun clearCart() {
        var query = "delete from $TABLE_NAME"
        db.execSQL(query)
    }

    fun calculateReceipt(): OrderSummary {
        var query = "select * from $TABLE_NAME"
        var cursor = db.rawQuery(query, null)
        var subtotal = 0;
        var discount = 0;
        var total = 0
        var deliveryCharges = 0
        if (cursor != null && cursor.moveToFirst()) {
            var totalPrice = 0
            do {
                var quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
                subtotal += cursor.getInt(cursor.getColumnIndex(COLUMN_MRP)) * quantity
                totalPrice += cursor.getInt(cursor.getColumnIndex(COLUMN_PRICE)) * quantity
            } while (cursor.moveToNext())
            discount = subtotal - totalPrice
            total = subtotal - discount
        }
        if (total < 300) {
            deliveryCharges = 30
            total += 30
        }
        return OrderSummary(totalAmount = subtotal, discount = discount, ourPrice = total, deliveryCharges = deliveryCharges)
    }
}