package com.xtensolutions.sample.di

import android.content.Context
import androidx.room.Room
import com.xtensolutions.sample.room.AppDatabase
import com.xtensolutions.sample.utils.Constant.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


/**
 * Created by Vaghela Mithun R. on 10-02-2023 - 20:06.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()

    @Provides
    fun provideRestaurantDao(appDatabase: AppDatabase) = appDatabase.restaurantDao()

    @Provides
    fun provideDealDao(appDatabase: AppDatabase) = appDatabase.dealDao()
}