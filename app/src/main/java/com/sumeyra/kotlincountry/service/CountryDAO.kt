package com.sumeyra.kotlincountry.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sumeyra.kotlincountry.model.Country

@Dao
interface CountryDAO {

    // Data Access Object

    @Insert
    suspend fun insertAll( vararg countries : Country) : List<Long>

    /*
    Insert : INSERT INTO
    suspend fun : coroutine , pause & resume
    vararg : multiple country object
    List<Long> : primary key
     */

    @Query("SELECT * FROM  country")
    suspend fun getAllCountries() : List<Country>

    @Query("SELECT * FROM  country WHERE uuid = :countryId")
    suspend fun getCountry(countryId : Int) : Country

    @Query("DELETE FROM country")
    suspend fun deleteAllCountry()

}
