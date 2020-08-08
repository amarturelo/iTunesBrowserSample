package com.turelo.itunesbrowsersample.ui.browser

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.turelo.itunesbrowsersample.R
import com.turelo.itunesbrowsersample.extensions.listen
import com.turelo.itunesbrowsersample.ui.browser.models.SongItemViewModel
import com.turelo.itunesbrowsersample.ui.common.CellClickListener

class SongResultAdapter(private var listener: CellClickListener<SongItemViewModel>? = null) :
    PagedListAdapter<SongItemViewModel, SongViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val vh = SongViewHolder(parent)
        vh.listen { position, _ ->
            getItem(position)?.also { item ->
                this.listener?.onCellClickListener(view = vh.itemView, data = item)
            }
        }
        return vh
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<SongItemViewModel>() {
            override fun areItemsTheSame(
                oldItem: SongItemViewModel,
                newItem: SongItemViewModel
            ): Boolean =
                oldItem.trackId == newItem.trackId

            override fun areContentsTheSame(
                oldItem: SongItemViewModel,
                newItem: SongItemViewModel
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}

class SongViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.song_recycler_view_item, parent, false)
) {

    private var model: SongItemViewModel? = null

    private val collectionName: TextView = itemView.findViewById(R.id.collectionName)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artwork: ImageView = itemView.findViewById(R.id.artwork)

    fun bindTo(model: SongItemViewModel?) {
        this.model = model
        model?.also {

            artwork.transitionName = "artwork${model.trackId}"
            collectionName.transitionName = "collectionName${model.trackId}"

            collectionName.text = it.collectionName
            trackName.text = it.trackName

            Glide.with(itemView)
                .load(it.artworkUrl100)
                .circleCrop()
                .into(artwork)
        }
    }

}
