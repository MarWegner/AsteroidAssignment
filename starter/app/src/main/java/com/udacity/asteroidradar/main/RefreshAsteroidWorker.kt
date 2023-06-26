package com.udacity.asteroidradar.main

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AstroidDatabase
import com.udacity.asteroidradar.database.AstroidDownloader
import retrofit2.HttpException

class RefreshAsteroidWorker(appContext: Context,params: WorkerParameters):CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshAsteroidsWorker"
    }

    override suspend fun doWork(): Result {
        val database = AstroidDatabase.getInstance(applicationContext)
        val downloader = AstroidDownloader(database)

        return try {
            downloader.getAstroidsFromNasa()
            Result.success()
        }catch (e: HttpException){
            Result.retry()
        }
    }
}