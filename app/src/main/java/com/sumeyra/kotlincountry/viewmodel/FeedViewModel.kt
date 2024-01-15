package com.sumeyra.kotlincountry.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sumeyra.kotlincountry.model.Country
import com.sumeyra.kotlincountry.service.CountryAPIServices
import com.sumeyra.kotlincountry.service.CountryDatabase
import com.sumeyra.kotlincountry.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Async.Schedule

class FeedViewModel(application: Application) : BaseViewModel(application) {

    private val countryApiService = CountryAPIServices()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 10*60*1000*1000*1000L



    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()

    fun refreshData(){

        val updateTime = customPreferences.getTime()
        if (updateTime !=null && updateTime != 0L  && System.nanoTime() - refreshTime < refreshTime){
            getDataFromSQLite()
        }else{
            getDataFromAPI()         //verileri çekmek için refresh etmek için
        }

    }

    private fun getDataFromSQLite() {
        countryLoading.value= true
        launch {
            val countries = CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
        }
    }

    fun getDataFromAPI(){
        countryLoading.value= true
        disposable.add(
            countryApiService.getData()
                .subscribeOn(Schedulers.io()) // Örnek olarak IO Scheduler
                .observeOn(AndroidSchedulers.mainThread()) // Android için main thread scheduler
                .subscribeWith( object : DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {
                        storeInSQLite(t)
                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value= false
                        countryError.value = true
                        e.printStackTrace()
                    }

                }))

    }

    private fun showCountries(countryList : List<Country>){

        countries.value= countryList
        countryError.value =false
        countryLoading.value = false

    }

    private fun storeInSQLite(list: List<Country>){
        launch {
            val dao = CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountry()
            val listLong = dao.insertAll(*list.toTypedArray()) // list ten individiual hale getiriyor
            var i =0
            while (i < list.size){
                list[i].uuid = listLong[i].toInt()
                i = i+1

            }

            showCountries(list)
        }
        customPreferences.saveTime(System.nanoTime())

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}