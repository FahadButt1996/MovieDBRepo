package com.example.starzplayassignment.utilities

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.starzplayassignment.R
import com.example.starzplayassignment.databinding.CustomToolbarBinding

class CustomToolbar(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
    var titleText: String? = null
        set(value) {
            field = value
            textViewTitle?.text = field
        }
    var imageResource: Drawable? = null
        set(value) {
            field = value
            imageViewBack?.setImageDrawable(field)
        }
    var view: View? = null
    var textViewTitle: TextView? = null
    var imageViewBack: ImageView? = null
    var language: ImageView? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar, 0, 0)
        titleText = a.getString(R.styleable.CustomToolbar_titleText)
        imageResource = a.getDrawable(R.styleable.CustomToolbar_imageResource)
        a.recycle()
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: CustomToolbarBinding = CustomToolbarBinding.inflate(inflater, this, true)
        view = binding.root
        textViewTitle = binding.tvTitle
        imageViewBack = binding.ivBack
        language = binding.language
        textViewTitle?.text = titleText
        imageViewBack?.setImageDrawable(imageResource)
    }

    fun setImageClickListener(onClickListener: OnClickListener) {
        imageViewBack?.setOnClickListener(onClickListener)
    }
}