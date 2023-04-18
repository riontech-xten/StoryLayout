package com.xtensolutions.sample.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Created by Vaghela Mithun R. on 10-02-2023 - 17:09.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/

@Entity(tableName = "restaurant")
data class Restaurant(
    @PrimaryKey
    val id: String,
    val name: String,
    val address: String,
    val rating: Float,
    val image: String
) : java.io.Serializable {
    companion object {
        fun getRestaurantList() = arrayListOf<Restaurant>().apply {
            add(
                Restaurant(
                    id = "RES001",
                    name = "Food City",
                    address = "Godhaniya Collage Road, Madhav Arcade 1st Floor Porbandar, Porbandar Locality, Porbandar, Gujarat",
                    rating = 4.0f,
                    image = "https://res.cloudinary.com/swiggy/image/upload/f_auto,q_auto,fl_lossy/l5hcl3g22dbshsdnkjw8"
                )
            )
            add(
                Restaurant(
                    id = "RES002",
                    name = "William Johns Pizza",
                    address = "Shreeji Arcade, Above Kotak Bank, MG Road, Porbandar Locality, Porbandar, Gujarat",
                    rating = 5.0f,
                    image = "https://b.zmtcdn.com/data/reviews_photos/85a/97e3f0de588a5d4161c91036bf91185a_1605775715.jpg"
                )
            )
            add(
                Restaurant(
                    id = "RES003",
                    name = "Pranam Gola and Waffles House",
                    address = "Wadi Plot, Near Aashta Bakery, Porbandar, Porbandar Locality, Porbandar, Gujarat",
                    rating = 4.0f,
                    image = "https://b.zmtcdn.com/data/reviews_photos/abd/a9926b5d741fa33ef50a497d62db2abd_1591265576.jpg"
                )
            )
            add(
                Restaurant(
                    id = "RES004",
                    name = "Shree Kansar Restaurant",
                    address = "MG Road, Above SBI NRI Branch, 1st Floor Porbandar, Porbandar Locality, Porbandar, Gujarat",
                    rating = 3.5f,
                    image = "https://media-cdn.tripadvisor.com/media/photo-o/1c/97/73/a1/unlimited-gujarati-thali.jpg"
                )
            )
            add(
                Restaurant(
                    id = "RES005",
                    name = "Shree Parcel Point",
                    address = "N/R Kamlabaug, 3-Vadi Plot, Porbandar, Porbandar Locality, Porbandar, Gujarat",
                    rating = 4.0f,
                    image = "https://media-cdn.tripadvisor.com/media/photo-o/1c/97/73/a1/unlimited-gujarati-thali.jpg"
                )
            )
        }
    }
}
