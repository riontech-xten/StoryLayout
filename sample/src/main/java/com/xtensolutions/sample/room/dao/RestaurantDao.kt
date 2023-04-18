package com.xtensolutions.sample.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xtensolutions.sample.room.model.Restaurant


/**
 * Created by Vaghela Mithun R. on 10-02-2023 - 18:51.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/
@Dao
interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurant(restaurants: List<Restaurant>)

    @Query("SELECT * FROM restaurant")
    fun getRestaurants(): LiveData<List<Restaurant>>
}