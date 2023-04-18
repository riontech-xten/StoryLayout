package com.xtensolutions.sample.data

import com.xtensolutions.sample.room.dao.RestaurantDao
import com.xtensolutions.sample.room.model.Restaurant
import javax.inject.Inject


/**
 * Created by Vaghela Mithun R. on 10-02-2023 - 20:23.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/
class RestaurantRepository @Inject constructor(private val restaurantDao: RestaurantDao) {

    suspend fun insertRestaurants(list: List<Restaurant>) = restaurantDao.insertRestaurant(list)

    fun getRestaurant() = restaurantDao.getRestaurants()
}