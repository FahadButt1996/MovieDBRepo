package com.example.starzplayassignment.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun String?.appendImageBaseURL(): String {
    return "http://image.tmdb.org/t/p/w500$this"
}

inline fun <reified T : Activity> Activity.navigate(params: List<IntentParams> = emptyList()) {
    val intent = Intent(this, T::class.java)
    for (parameter in params) {
        intent.putExtra(parameter.key, parameter.value)
    }
    startActivity(intent)
}

fun String?.nonNullValue(): String {
    return this ?: ""
}

fun Int?.nonNullValue(): Int {
    return this ?: -1
}

fun Activity.hideKeyboard() {
    val inputManager =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocusedView = currentFocus
    if (currentFocusedView != null) {
        inputManager.hideSoftInputFromWindow(currentFocusedView.windowToken, 0)
    }
}

fun Activity.hideStatusBar() {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.hide(WindowInsets.Type.statusBars())
    } else {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}
