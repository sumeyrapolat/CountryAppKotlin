package com.sumeyra.kotlincountry.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sumeyra.kotlincountry.model.Country

@Database(entities = arrayOf(Country::class), version = 1)
abstract class CountryDatabase: RoomDatabase() {

    abstract fun countryDao() : CountryDAO

   //Singleton
    companion object{ //farklı threadlerden ulaılabilecek uygulama
        private var instance : CountryDatabase? = null
       private val lock = Any()
       operator fun invoke(context: Context) = instance ?: synchronized(lock)
       {
           instance ?: countryDatabase(context).also {
               instance = it
           }
       }


       private fun countryDatabase(context: Context) = Room.databaseBuilder(
           context.applicationContext,
           CountryDatabase:: class.java,
           "countrydatabase"
       ).build()

   }
}