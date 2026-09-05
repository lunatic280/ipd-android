package com.example.ipdapplication.map.menu.api

import com.example.ipdapplication.map.menu.dto.MenuDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface MenuApi {

    @GET("menus")
    suspend fun getMenus(): List<MenuDto>

    @GET("menus/place/{placeId}")
    suspend fun getMenusByPlaceId(
        @Path("placeId") placeId: Long
    ): List<MenuDto>

    @GET("menus/{id}")
    suspend fun getMenuById(
        @Path("id") menuId: Long
    ): MenuDto

    @Multipart
    @POST("menus")
    suspend fun createMenu(
        @Part image: MultipartBody.Part,
        @Part("request") request: RequestBody
    ): MenuDto

    @Multipart
    @PUT("menus/{id}")
    suspend fun updateMenu(
        @Path("id") menuId: Long,
        @Part image: MultipartBody.Part?,
        @Part("request") request: RequestBody
    ): MenuDto

    @DELETE("menus/{id}")
    suspend fun deleteMenu(
        @Path("id") menuId: Long
    )
}
