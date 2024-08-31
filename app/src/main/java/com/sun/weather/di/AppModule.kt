package com.sun.weather.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.example.weather.utils.dispatchers.BaseDispatcherProvider
import com.example.weather.utils.dispatchers.DispatcherProvider
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sun.weather.data.repository.source.local.AppDatabase
import com.sun.weather.data.repository.source.remote.api.middleware.BooleanAdapter
import com.sun.weather.data.repository.source.remote.api.middleware.DoubleAdapter
import com.sun.weather.data.repository.source.remote.api.middleware.IntegerAdapter
import com.sun.weather.utils.DateTimeUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val AppModule =
    module {
        single { provideResources(get()) }
        single { provideBaseDispatcherProvider() }
        single { provideAppDatabase(androidContext()) }
        factory { get<AppDatabase>().weatherDao() }
        single { provideGson() }
    }

fun provideResources(app: Application): Resources {
    return app.resources
}

fun provideAppDatabase(context: Context): AppDatabase {
    return AppDatabase.getInstance(context)
}

fun provideBaseDispatcherProvider(): BaseDispatcherProvider {
    return DispatcherProvider()
}

fun provideGson(): Gson {
    val booleanAdapter = BooleanAdapter()
    val integerAdapter = IntegerAdapter()
    val doubleAdapter = DoubleAdapter()
    return GsonBuilder()
        .registerTypeAdapter(Boolean::class.java, booleanAdapter)
        .registerTypeAdapter(Int::class.java, integerAdapter)
        .registerTypeAdapter(Double::class.java, doubleAdapter)
        .setDateFormat(DateTimeUtils.DATE_TIME_FORMAT_UTC)
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .excludeFieldsWithoutExposeAnnotation()
        .create()
}
