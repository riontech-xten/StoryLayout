package com.xtensolutions.sample.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtensolutions.sample.data.DealStoryRepository
import com.xtensolutions.sample.room.model.DealStory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Vaghela Mithun R. on 10-02-2023 - 20:25.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/
@HiltViewModel
class DealStoryViewModel @Inject constructor(
    private val dealStoryRepository: DealStoryRepository
) : ViewModel() {

    init {
        insertRawDeals()
    }

    private fun insertRawDeals() {
        viewModelScope.launch {
            dealStoryRepository.insertDeals(DealStory.getRestaurantDeal())
        }
    }

    fun getRestaurantDeals(restaurantId: String) =
        dealStoryRepository.getRestaurantDeals(restaurantId)

}