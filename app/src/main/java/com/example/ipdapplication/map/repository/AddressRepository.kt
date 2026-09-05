package com.example.ipdapplication.map.repository

import com.example.ipdapplication.map.api.AddressApi
import com.example.ipdapplication.map.dto.AddressDto
import javax.inject.Inject

class AddressRepository @Inject constructor(
    private val api: AddressApi
) {

    suspend fun getAddress(lat: Double, lng: Double): AddressDto =
        api.getAddress(lat, lng)
}
