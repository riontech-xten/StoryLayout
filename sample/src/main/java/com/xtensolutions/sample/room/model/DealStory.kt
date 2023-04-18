package com.xtensolutions.sample.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xtensolutions.sample.utils.StoryType


/**
 * Created by Vaghela Mithun R. on 08-02-2023 - 23:40.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/
@Entity(tableName = "deal")
data class DealStory(
    @PrimaryKey
    val id: String,
    val restaurantId: String,
    val name: String,
    val description: String,
    val likes: Int,
    val total: Int,
    val purchased: Int,
    val image: String,
    val price: String,
    val storyType: Int = StoryType.IMAGE.value
) : java.io.Serializable {
    companion object {
        fun getRestaurantDeal() = arrayListOf<DealStory>().apply {
            add(
                DealStory(
                    id = "DID001",
                    restaurantId = "RES001",
                    name = "2 Pizzas at ₹99 each",
                    price = "₹99",
                    description = "Select any 2 Regular Pizzas Worth ₹165 @ ₹99 each",
                    likes = 1050,
                    total = 10,
                    purchased = 4,
                    image = "https://player.vimeo.com/external/479817948.hd.mp4?s=858a82cb50573d69db11b723a8f36850a628744b&profile_id=174&oauth2_token_id=57447761",
                    storyType = StoryType.VIDEO.value
                )
            )
            add(
                DealStory(
                    id = "DID002",
                    restaurantId = "RES001",
                    name = "2 Pizzas at ₹139 each",
                    price = "₹139",
                    description = "Select any 2 Regular Pizzas Worth ₹205 @ ₹139 each",
                    likes = 1050,
                    total = 10,
                    purchased = 4,
                    image = "https://images.pexels.com/photos/2147491/pexels-photo-2147491.jpeg",
                    storyType = StoryType.IMAGE.value
                )
            )
            add(
                DealStory(
                    id = "DID003",
                    restaurantId = "RES001",
                    name = "2 Pizzas at ₹179 each",
                    price = "₹179",
                    description = "Select any 2 Regular Pizzas Worth ₹235 @ ₹179 each",
                    likes = 1050,
                    total = 10,
                    purchased = 4,
                    storyType = StoryType.VIDEO.value,
                    image = "https://player.vimeo.com/external/479817456.hd.mp4?s=06580fc7c18f3546d1af86852630115f2c609e55&profile_id=174&oauth2_token_id=57447761"
                )
            )
            add(
                DealStory(
                    id = "DID004",
                    restaurantId = "RES001",
                    name = "2 Pizzas at ₹219 each",
                    price = "₹219",
                    description = "Select any 2 Regular Pizzas Worth ₹295 @ ₹219 each",
                    likes = 1050,
                    total = 10,
                    purchased = 4,
                    storyType = StoryType.IMAGE.value,
                    image = "https://www.itl.cat/pngfile/big/297-2975386_pizza-for-mobile-pizza-wallpaper-for-mobile.jpg"
                )
            )
        }
    }
}

//player.vimeo.com/video/479817948?title=0&portrait=0&byline=0&autoplay=1&muted=true
//https://player.vimeo.com/8c61236b-7ef4-4531-a520-bd2af631c400
// https://player.vimeo.com/external/479817456.hd.mp4?s=06580fc7c18f3546d1af86852630115f2c609e55&profile_id=174&oauth2_token_id=57447761
// https://player.vimeo.com/external/479817948.hd.mp4?s=858a82cb50573d69db11b723a8f36850a628744b&profile_id=174&oauth2_token_id=57447761