package com.example.starzplayassignment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.restapi.models.MultiSearchMovieDataResponse
import com.example.starzplayassignment.interfaces.GenericAdapterCallback
import com.example.starzplayassignment.databinding.CarousalViewChildBinding
import com.example.starzplayassignment.utilities.nonNullValue
import java.util.*

class MultiSearchMovieAdapter(
    private val context: Context,
    private val entries: List<String>,
    private val hashMap: HashMap<String, ArrayList<MultiSearchMovieDataResponse.Result>>,
    private val genericAdapterCallback: GenericAdapterCallback
) :
    Adapter<MultiSearchMovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CarousalViewChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.mediaType.text =
            entries[position].nonNullValue().toUpperCase(Locale.getDefault())
        hashMap[entries[position]]?.let {
            val adapter =
                MoviesItemAdapter(
                    context,
                    list = it,
                    genericAdapterCallback
                )
            holder.binding.moviesRecyclerview.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    inner class ViewHolder(val binding: CarousalViewChildBinding) :
        RecyclerView.ViewHolder(binding.root)
}