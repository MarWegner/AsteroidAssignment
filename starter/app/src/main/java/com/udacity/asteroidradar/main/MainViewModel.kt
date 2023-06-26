package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.NASA_API_KEY
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.AstroidDatabase
import com.udacity.asteroidradar.database.AstroidDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class Filter {TODAY, WEEK, ALL}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _picOfTheDay= MutableLiveData<PictureOfDay>()
    private val database = AstroidDatabase.getInstance(application)
    private val downloader = AstroidDownloader(database)
    private val activeFilter = MutableLiveData(Filter.ALL)

   val asteroidList  = activeFilter.switchMap{ when(it) {
        Filter.TODAY -> downloader.todaysAstroidList
        Filter.WEEK -> downloader.lastWeeksAstroidList
        else -> downloader.astroidList
    } }

    val picOfTheDay : LiveData<PictureOfDay>
        get()= _picOfTheDay

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetails : LiveData<Asteroid?>
    get() = _navigateToAsteroidDetails


    init{
        viewModelScope.launch {
            updatePicOfTheDay()
            downloader.getAstroidsFromNasa()
        }
    }
    class Factory(val app: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw java.lang.IllegalArgumentException("Unable to construct viewModel")
        }
    }

    private suspend fun updatePicOfTheDay(){
        withContext(Dispatchers.IO){
            try {
                _picOfTheDay.postValue(
                    NasaApi.retrofitService.getPicOfTheDay(NASA_API_KEY)
                )
            } catch (err: Exception) {
                Log.e("updatePic", err.printStackTrace().toString())
            }
        }
    }

    fun onFilterClicked(filter: Filter) {
        activeFilter.postValue(filter)
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroidDetails.value = asteroid
    }

    fun navigateFinished(){
        _navigateToAsteroidDetails.value = null
    }
}