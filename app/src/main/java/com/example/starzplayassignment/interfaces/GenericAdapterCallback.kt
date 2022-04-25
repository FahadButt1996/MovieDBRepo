package com.example.starzplayassignment.interfaces

interface GenericAdapterCallback {
    fun <T> getClickedObject(clickedObj: T, position: T, callingID: String = "")
}