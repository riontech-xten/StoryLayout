package com.xtensolutions.sample.data

import com.xtensolutions.sample.room.dao.DealDao
import com.xtensolutions.sample.room.model.DealStory
import javax.inject.Inject


/**
 * Created by Vaghela Mithun R. on 10-02-2023 - 20:23.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/
class DealStoryRepository @Inject constructor(private val dealDao: DealDao) {
    suspend fun insertDeals(list: List<DealStory>) = dealDao.insertDeals(list)

    fun getRestaurantDeals(restaurantId: String) = dealDao.getDeals(restaurantId)

    suspend fun addDeal(deal: DealStory) = dealDao.addDeal(deal)
}