package com.xtensolutions.sample.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xtensolutions.sample.room.model.DealStory


/**
 * Created by Vaghela Mithun R. on 10-02-2023 - 18:51.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/
@Dao
interface DealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeals(deals: List<DealStory>)

    @Query("SELECT * FROM deal WHERE restaurantId = :restaurantId")
    fun getDeals(restaurantId: String): LiveData<List<DealStory>?>

    @Insert
    suspend fun addDeal(deal: DealStory)
}