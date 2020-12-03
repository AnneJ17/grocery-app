package com.apolis.groceryapp.models

data class Order(
    val __v: Int,
    val _id: String,
    val date: String,
    val orderStatus: String,
    val orderSummary: OrderSummary,
    val products: ArrayList<Any>,
    val shippingAddress: ShippingAddress,
    val user: UserInfo,
    val userId: String
)


data class OrderSummary(
    val _id: String? = null,
    val deliveryCharges: Int? = null,
    val discount: Int,
    val orderAmount: Int? = null,
    val ourPrice: Int,
    val totalAmount: Int
)

data class ShippingAddress(
    val _id: String,
    val city: String,
    val houseNo: String,
    val pincode: Int,
    val type: String
)

data class UserInfo(
    val _id: String? = null,
    val email: String,
    val mobile: String? = null
)