package com.example.starzplayassignment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.restapi.models.ResultResponse
import com.example.starzplayassignment.databinding.MovieItemBinding
import com.example.starzplayassignment.interfaces.GenericAdapterCallback
import com.example.starzplayassignment.utilities.MediaTypeEnum
import com.example.starzplayassignment.utilities.appendImageBaseURL
import com.example.starzplayassignment.utilities.loadImage
import com.example.starzplayassignment.utilities.nonNullValue

class MoviesItemAdapter(
    val context: Context,
    val list: ArrayList<ResultResponse>,
    private val genericAdapterCallback: GenericAdapterCallback
) :
    Adapter<MoviesItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].mediaType == MediaTypeEnum.PERSON.type) {
            loadImage(
                context,
                list[position].profilePath.nonNullValue().appendImageBaseURL(),
                holder.binding.movieImage
            )

        } else {
            loadImage(
                context,
                list[position].posterPath.appendImageBaseURL(),
                holder.binding.movieImage
            )
        }
        holder.binding.movieImageContainer.setOnClickListener {
            genericAdapterCallback.getClickedObject(list[position], position = position)
        }


    }

    inner class ViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}