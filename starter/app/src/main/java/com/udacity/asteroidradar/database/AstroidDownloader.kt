package com.udacity.asteroidradar.database

import android.util.Log
import com.udacity.asteroidradar.Constants.NASA_API_KEY
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AstroidDownloader(private val database: AstroidDatabase) {

    val astroidList = database.astroidDao.getAsteroids()

    val todaysAstroidList = database.astroidDao.getsAstroidsByDay(LocalDateTime.now().format(
        DateTimeFormatter.ISO_DATE))
    val lastWeeksAstroidList = database.astroidDao.getAstroidsFromDateToDate(
        LocalDateTime.now().minusDays(7).format(DateTimeFormatter.ISO_DATE),
        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE),
    )

    suspend fun getAstroidsFromNasa(){
        withContext(Dispatchers.IO){
            try{
                val asteroids = NasaApi.retrofitService.getAstroids(NASA_API_KEY)
                Log.i("bla",asteroids)
                val parsedResult = parseAsteroidsJsonResult(JSONObject(asteroids))
                database.astroidDao.insertAll(*parsedResult.toTypedArray())
            }catch (exc : Exception){
                Log.e("downloader",exc.message.toString())
            }
        }
    }
}