package com.sumeyra.kotlincountry.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.sumeyra.kotlincountry.model.Country
import com.sumeyra.kotlincountry.service.CountryDatabase
import kotlinx.coroutines.launch
import java.util.UUID

class CountryViewModel(application: Application) : BaseViewModel(application) {

    val countryLiveData = MutableLiveData<Country>()

    fun getDataFromRoom(uuid: Int) {
        launch {

            val dao = CountryDatabase(getApplication()).countryDao()
            val country = dao.getCountry(uuid)
            countryLiveData.value = country
        }

    }
}