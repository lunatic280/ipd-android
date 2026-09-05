package com.example.ipdapplication.map.repository

import com.example.ipdapplication.map.api.PlaceApi
import com.example.ipdapplication.map.dto.PlaceCreateRequest
import com.example.ipdapplication.map.dto.PlaceDto
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@Singleton
class PlaceRepository @Inject constructor(
    private val api: PlaceApi,
    private val json: Json
) {
    suspend fun getPlaces(): List<PlaceDto> = api.getPlaces()

    suspend fun createPlace(
        request: PlaceCreateRequest,
        imageFiles: List<File> = emptyList()
    ): PlaceDto {
        val jsonBody: RequestBody = json.encodeToString(request)
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        val imageParts: List<MultipartBody.Part> = imageFiles
            .filter { it.exists() }
            .map { file ->
                MultipartBody.Part.createFormData(
                    name = "images",
                    filename = file.name,
                    body = file.asRequestBody("image/*".toMediaTypeOrNull())
                )
            }

        return api.createPlaceWithImage(images = imageParts, request = jsonBody)
    }

    suspend fun updatePlace(
        id: Long,
        request: PlaceCreateRequest
    ): PlaceDto {
        val body = PlaceDto(
            id = id,
            title = request.title,
            description = request.description,
            latitude = request.latitude,
            longitude = request.longitude,
            telNumber = request.telNumber,
            type = request.type
        )
        return api.updatePlace(id = id, request = body)
    }

    suspend fun deletePlace(id: Long) {
        api.deletePlace(id)
    }
}
