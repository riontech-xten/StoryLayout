package com.xtensolutions.sample.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtensolutions.sample.data.RestaurantRepository
import com.xtensolutions.sample.room.model.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Vaghela Mithun R. on 10-02-2023 - 20:21.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/
@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {

    init {
        insertDummyRestaurant()
    }

    private fun insertDummyRestaurant() {
        viewModelScope.launch {
            restaurantRepository.insertRestaurants(Restaurant.getRestaurantList())
        }
    }

    fun getRestaurants() = restaurantRepository.getRestaurant()
}