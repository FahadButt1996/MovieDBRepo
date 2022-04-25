package com.example.starzplayassignment.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.starzplayassignment.R
import com.example.starzplayassignment.databinding.ActivityMovieDetailScreenBinding
import com.example.starzplayassignment.utilities.*
import java.util.*

class MovieDetailScreen : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMovieDetailScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initData()
        setListeners()
    }

    private fun initView() {
        binding.customToolbar.language?.visibility = View.GONE
        loadImage(
            this,
            intent.getStringExtra(IMAGE_URL).appendImageBaseURL(),
            binding.movieImage
        )

        binding.movieTitle.text = if (!intent.getStringExtra("title").isNullOrEmpty()) {
            intent.getStringExtra(TITLE)
        } else {
            getString(R.string.no_title_available)

        }
        binding.movieOverview.text = if (!intent.getStringExtra("overview").isNullOrEmpty()) {
            intent.getStringExtra(OVERVIEW)
        } else {
            getString(R.string.no_overview_available)
        }

        setPlayButtonVisibility(intent.getStringExtra(MEDIA_TYPE).nonNullValue())
    }

    private fun setPlayButtonVisibility(mediaType: String) {
        when (mediaType.toLowerCase(Locale.getDefault())) {
            MediaTypeEnum.MOVIE.type -> {
                binding.playBtn.visibility = View.VISIBLE
            }
            MediaTypeEnum.TV.type -> {
                binding.playBtn.visibility = View.VISIBLE
            }
            else -> {
                binding.playBtn.visibility = View.GONE
            }
        }
    }

    private fun initData() {
        // do nothing
    }

    private fun setListeners() {
        binding.playBtn.setOnClickListener(this)
        binding.customToolbar.imageViewBack?.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.playBtn.id -> {
                navigate<VideoViewActivity>()
            }
            binding.customToolbar.imageViewBack?.id -> {
                onBackPressed()
            }
        }
    }
}