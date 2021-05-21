package com.kuldeepsocialmedia.indianic.repository

import com.kuldeepsocialmedia.indianic.model.City
import com.kuldeepsocialmedia.indianic.network.ApiServiceImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apiServiceImp: ApiServiceImp) {

    fun getCityData(city:String): Flow<City> = flow {
        val response= apiServiceImp.getCity(city,"ecdcb353d38fc0579247407543d8d2f7")
        emit(response)
    }.flowOn(Dispatchers.IO)
        .conflate()

}