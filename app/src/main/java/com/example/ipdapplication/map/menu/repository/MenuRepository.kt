package com.example.ipdapplication.map.menu.repository

import com.example.ipdapplication.map.menu.api.MenuApi
import com.example.ipdapplication.map.menu.dto.MenuCreateRequest
import com.example.ipdapplication.map.menu.dto.MenuDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuRepository @Inject constructor(
    private val api: MenuApi,
    private val json: Json
){

    suspend fun getMenus(): List<MenuDto> = api.getMenus()

    suspend fun getMenuById(menuId: Long): MenuDto = api.getMenuById(menuId)

    suspend fun getMenusByPlaceId(placeId: Long): List<MenuDto> {
        return runCatching { api.getMenusByPlaceId(placeId) }
            .getOrElse {
                val allMenus = api.getMenus()
                allMenus.filter { it.placeId == placeId }
            }
    }

    suspend fun createMenu(
        placeId: Long,
        name: String,
        price: Int,
        description: String,
        imageFile: File
    ): MenuDto {
        val request = MenuCreateRequest(
            placeId = placeId,
            name = name,
            price = price,
            description = description,
            imageUrl = ""
        )

        val jsonBody = json.encodeToString(request)
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        val imagePart = MultipartBody.Part.createFormData(
            name = "image",
            filename = imageFile.name,
            body = imageFile
                .asRequestBody("image/*".toMediaTypeOrNull())
        )

        return api.createMenu(image = imagePart, request = jsonBody)
    }

    suspend fun updateMenu(
        menuId: Long,
        placeId: Long,
        name: String,
        price: Int,
        description: String,
        imageFile: File?
    ): MenuDto {
        val request = MenuCreateRequest(
            placeId = placeId,
            name = name,
            price = price,
            description = description,
            imageUrl = ""
        )

        val jsonBody = json.encodeToString(request)
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        val imagePart = imageFile?.let {
            MultipartBody.Part.createFormData(
                name = "image",
                filename = it.name,
                body = it.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        return api.updateMenu(menuId = menuId, image = imagePart, request = jsonBody)
    }

    suspend fun deleteMenu(menuId: Long) {
        api.deleteMenu(menuId)
    }
}
