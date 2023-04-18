package com.xtensolutions.sample.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xtensolutions.sample.room.dao.DealDao
import com.xtensolutions.sample.room.dao.RestaurantDao
import com.xtensolutions.sample.room.model.DealStory
import com.xtensolutions.sample.room.model.Restaurant


/**
 * Created by Vaghela Mithun R. on 10-02-2023 - 19:34.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/
@Database(entities = [Restaurant::class, DealStory::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao

    abstract fun dealDao(): DealDao
}