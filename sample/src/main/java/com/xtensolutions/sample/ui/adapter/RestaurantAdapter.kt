package com.xtensolutions.sample.ui.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xtensolutions.sample.core.adapter.BaseRecyclerViewAdapter
import com.xtensolutions.sample.databinding.RowItemRestaurantBinding
import com.xtensolutions.sample.room.model.Restaurant
import com.xtensolutions.sample.ui.viewholder.RestaurantViewHolder


/**
 * Created by Vaghela Mithun R. on 13-02-2023 - 19:57.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/
class RestaurantAdapter(context: Context, restaurants: List<Restaurant>) :
    BaseRecyclerViewAdapter<Restaurant>(context, restaurants.toMutableList()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RowItemRestaurantBinding.inflate(inflater, parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RestaurantViewHolder) {
            viewHolderClickListener?.let { holder.setViewClickListener(it) }
            holder.bind(getItem(position))
        }
    }
}