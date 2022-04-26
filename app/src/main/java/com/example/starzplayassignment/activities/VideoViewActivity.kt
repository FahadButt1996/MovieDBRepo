package com.example.starzplayassignment.activities

import android.content.res.Configuration
import android.content.res.Resources
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.example.starzplayassignment.databinding.ActivityVideoViewBinding
import com.example.starzplayassignment.utilities.*

class VideoViewActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityVideoViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar()
        binding = ActivityVideoViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initData()
        setListeners()
    }

    private fun initView() {
        showProgressDialog(this)
        val uri =
            Uri.parse(VIDEO_URL)
        binding.videoView.setMediaController(MediaController(this))
        binding.videoView.setVideoURI(uri)
        binding.videoView.start()

        val params = binding.videoView.layoutParams
        params.width = Resources.getSystem().displayMetrics.widthPixels
        params.height = Resources.getSystem().displayMetrics.heightPixels
        binding.videoView.layoutParams = params
    }

    private fun initData() {
        // do nothing
    }

    private fun setListeners() {
        binding.videoBackBtn.setOnClickListener(this)

        val info = MediaPlayer.OnInfoListener { _, what, _ ->
            when (what) {
                MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                    hideProgressDialog()
                    true
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                    showProgressDialog(this)
                    true
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                    hideProgressDialog()
                    true
                }
            }
            false
        }
        binding.videoView.setOnInfoListener(info)
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            binding.videoBackBtn.id -> {
                onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopVideo()
        LanguageConfigs.initializeLocale(this)
    }

    private fun stopVideo() {
        if (binding.videoView.isPlaying) {
            binding.videoView.stopPlayback()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        stopVideo()
    }
}