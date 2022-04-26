package com.example.starzplayassignment.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.starzplayassignment.utilities.LanguageConfigs

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageConfigs(this)
        LanguageConfigs.initializeLocale(this)
    }
}