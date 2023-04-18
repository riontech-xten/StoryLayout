package com.xtensolutions.sample.ui.viewholder

import com.xtensolutions.sample.core.adapter.BaseViewHolder
import com.xtensolutions.sample.databinding.RowItemRestaurantBinding
import com.xtensolutions.sample.room.model.Restaurant


/**
 * Created by Vaghela Mithun R. on 13-02-2023 - 20:06.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/
class RestaurantViewHolder(private val binding: RowItemRestaurantBinding) :
    BaseViewHolder(binding.root) {

    fun bind(restaurant: Restaurant) {
        binding.restaurant = restaurant
        binding.clRowRestaurantRoot.setOnClickListener(this)
    }
}