package com.apolis.groceryapp.models

import java.io.Serializable

data class AddressResponse(
    val count: Int,
    val data: ArrayList<Address>,
    val error: Boolean
)

data class Address(
    val __v: Int? = null,
    val _id: String? = null,
    val city: String,
    val houseNo: String,
    val pincode: Int,
    val streetName: String,
    val type: String,
    val userId: String? = null
) : Serializable {
    companion object {
        val KEY_ADDRESS_EDIT = "address_edit"
        val KEY_ADDRESS_SELECTED = "address_selected"
    }
}