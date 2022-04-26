package com.example.starzplayassignment.activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import com.example.restapi.models.ResultResponse
import com.example.starzplayassignment.R
import com.example.starzplayassignment.adapters.MultiSearchMovieAdapter
import com.example.starzplayassignment.databinding.ActivityMoviesBinding
import com.example.starzplayassignment.interfaces.GenericAdapterCallback
import com.example.starzplayassignment.repository.MovieDBRepository
import com.example.starzplayassignment.utilities.*
import com.example.starzplayassignment.viewModels.MovieDBViewModel
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.coroutines.CoroutineContext


class MoviesActivity : BaseActivity(), CoroutineScope, GenericAdapterCallback,
    View.OnClickListener {

    private val compositeJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + compositeJob

    lateinit var viewModel: MovieDBViewModel
    private lateinit var binding: ActivityMoviesBinding
    var queryTextChangedJob: Job? = null
    lateinit var hashMap: HashMap<String, ArrayList<ResultResponse>>
    var pageNo: Int = 1
    var totalPages: Int? = null
    lateinit var languageConfigs: LanguageConfigs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        setListeners()
        initView()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LanguageConfigs.initializeLocale(this)
    }

    private fun initView() {
        showNoDataView(R.drawable.searching, getString(R.string.write_something_for_search))
        binding.customToolbar.imageViewBack?.visibility = View.INVISIBLE

        if (!intent.getStringExtra(QUERY).isNullOrEmpty()) {
            binding.movieSearch.setQuery(intent.getStringExtra("query"), true)
        }
    }

    private fun initData() {
        viewModel = ViewModelProviders.of(
            this,
            MovieDBViewModel.UserViewModelFactory(MovieDBRepository())
        ).get(MovieDBViewModel::class.java)
    }

    private fun setListeners() {
        binding.loadMore.setOnClickListener(this)
        binding.customToolbar.language?.setOnClickListener(this)

        binding.movieSearch.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    showNoDataView(
                        R.drawable.searching,
                        getString(R.string.write_something_for_search)
                    )
                    getMovieDataHashMap().clear()
                    updateMovieData()
                    hideKeyboard()
                } else {
                    queryTextChangedJob?.cancel()
                    queryTextChangedJob = launch(Dispatchers.Main) {
                        pageNo = 1
                        totalPages = null
                        delay(500)
                        getMovieDataHashMap().clear()
                        getMultiSearchData(query, pageNo)
                        hideKeyboard()
                        binding.movieSearch.clearFocus()
                    }
                }
                return true
            }
        })
    }

    fun getMovieDataHashMap(): HashMap<String, ArrayList<ResultResponse>> {
        if (!::hashMap.isInitialized) {
            hashMap = HashMap()
        }
        return hashMap
    }

    private fun getMultiSearchData(query: String, page: Int) {
        if (checkNetworkConnectivity(this)) {
            launch {
                showProgressDialog(this@MoviesActivity)
                val multiSearchData = viewModel.getMultiSearchData(query, page)
                hideProgressDialog()

                multiSearchData?.let {
                    totalPages = it.totalPages
                    handleLoadMoreBtnVisibility()
                    if (it.results.size > 0) {
                        if (binding.multiSearchRecyclerView.visibility == View.GONE) {
                            showDataView()
                        }
                        viewModel.convertListToHashMap(it, getMovieDataHashMap())
                        updateMovieData()
                    } else {
                        showNoDataView(R.drawable.no_data, getString(R.string.no_data_found))

                    }
                } ?: run {
                    showNoDataView(R.drawable.no_data, getString(R.string.something_went_wrong))
                }
            }
        } else {
            showNoDataView(R.drawable.no_internet, getString(R.string.no_connection))
        }
    }

    // this is for pagination
    private fun handleLoadMoreBtnVisibility() {
        if (totalPages.nonNullValue() > pageNo) {
            binding.loadMore.visibility = View.VISIBLE
        } else {
            binding.loadMore.visibility = View.GONE
        }
    }

    private fun showNoDataView(drawable: Int, message: String) {
        binding.multiSearchRecyclerView.visibility = View.GONE
        binding.noDataImage.visibility = View.VISIBLE
        binding.noDataImage.setImageResource(drawable)
        binding.noDataText.visibility = View.VISIBLE
        binding.noDataText.text = message
    }

    private fun showDataView() {
        binding.multiSearchRecyclerView.visibility = View.VISIBLE
        binding.noDataImage.visibility = View.GONE
        binding.noDataText.visibility = View.GONE
    }

    private fun updateMovieData() {
        if (binding.multiSearchRecyclerView.adapter == null) {
            val adapter =
                MultiSearchMovieAdapter(
                    this@MoviesActivity,
                    ArrayList(getMovieDataHashMap().keys.toList().sorted()),
                    getMovieDataHashMap(),
                    this@MoviesActivity
                )
            binding.multiSearchRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        } else {
            if ((binding.multiSearchRecyclerView.adapter as MultiSearchMovieAdapter).getEntries()
                    .isEmpty()
            ) {
                (binding.multiSearchRecyclerView.adapter as MultiSearchMovieAdapter).updateEntries(
                    getMovieDataHashMap().keys.toList().sorted()
                )
            }
            binding.multiSearchRecyclerView.adapter?.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.movieSearch.clearFocus()
    }

    override fun <T> getClickedObject(clickedObj: T, position: T, callingID: String) {
        clickedObj as ResultResponse
        navigate<MovieDetailScreen>(ArrayList<IntentParams>().apply {
            if (clickedObj.mediaType == MediaTypeEnum.PERSON.type) {
                add(
                    IntentParams(
                        IMAGE_URL,
                        clickedObj.profilePath.nonNullValue()
                    )
                )
                add(IntentParams(TITLE, clickedObj.name.nonNullValue()))
                add(
                    IntentParams(
                        OVERVIEW,
                        clickedObj.overview.nonNullValue()
                    )
                )
                add(
                    IntentParams(
                        MEDIA_TYPE,
                        clickedObj.mediaType.nonNullValue()
                    )
                )

            } else {
                add(IntentParams(IMAGE_URL, clickedObj.posterPath.nonNullValue()))
                add(IntentParams(TITLE, clickedObj.title.nonNullValue()))
                add(IntentParams(OVERVIEW, clickedObj.overview.nonNullValue()))
                add(IntentParams(MEDIA_TYPE, clickedObj.mediaType.nonNullValue()))
            }
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.loadMore.id -> {
                if (pageNo <= totalPages.nonNullValue()) {
                    pageNo += 1
                    getMultiSearchData(binding.movieSearch.query.toString(), pageNo)
                }
            }
            binding.customToolbar.language?.id -> {
                languageConfigs = LanguageConfigs(this)
                if (languageConfigs.language == LanguageEnum.ARABIC) {
                    languageConfigs.language = LanguageEnum.ENGLISH

                } else {
                    languageConfigs.language = LanguageEnum.ARABIC

                }
                LanguageConfigs.initializeLocale(this)
                restartActivity()
            }
        }
    }

    private fun restartActivity() {
        val intent = intent
        intent.putExtra(QUERY, binding.movieSearch.query.toString())
        finish()
        startActivity(intent)
    }

}