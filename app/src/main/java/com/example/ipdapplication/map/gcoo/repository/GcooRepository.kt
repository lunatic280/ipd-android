package com.example.ipdapplication.map.gcoo.repository

import com.example.ipdapplication.map.gcoo.api.GcooApi
import com.example.ipdapplication.map.gcoo.dto.GcooDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GcooRepository @Inject constructor(
    private val api: GcooApi,
    private val json: Json
) {
    /**
     * 전체 Gcoo 목록 조회 (모든 사용자 가능)
     */
    suspend fun getGcoos(): List<GcooDto> = api.getGcoos()

    /**
     * 특정 Gcoo 조회 (모든 사용자 가능)
     */
    suspend fun getGcooById(gcooId: Long): GcooDto = api.getGcooById(gcooId)

    /**
     * Gcoo 생성 (OWNER만 가능)
     * @param dto Gcoo 정보
     * @param imageFile 킥보드 이미지 파일
     */
    suspend fun createGcoo(dto: GcooDto, imageFile: File?): GcooDto {
        // 1) 요청 DTO를 JSON -> RequestBody로 변환
        val jsonBody: RequestBody = json.encodeToString(dto)
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        // 2) 이미지 파일을 MultipartBody.Part로 변환
        val imagePart: MultipartBody.Part = if (imageFile != null && imageFile.exists()) {
            MultipartBody.Part.createFormData(
                name = "image",
                filename = imageFile.name,
                body = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            )
        } else {
            // 더미 이미지 파트 (서버에서 required = true인 경우를 대비)
            MultipartBody.Part.createFormData("image", "")
        }

        return api.createGcoo(request = jsonBody, image = imagePart)
    }

    /**
     * Gcoo 수정 (OWNER만 가능)
     * @param gcooId 수정할 Gcoo ID
     * @param dto 수정할 Gcoo 정보
     * @param imageFile 새 이미지 파일 (선택 사항)
     */
    suspend fun updateGcoo(gcooId: Long, dto: GcooDto, imageFile: File?): GcooDto {
        // 1) 요청 DTO를 JSON -> RequestBody로 변환
        val jsonBody: RequestBody = json.encodeToString(dto)
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        // 2) 이미지 파일이 있으면 MultipartBody.Part로 변환
        val imagePart: MultipartBody.Part? = if (imageFile != null && imageFile.exists()) {
            MultipartBody.Part.createFormData(
                name = "image",
                filename = imageFile.name,
                body = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            )
        } else {
            null
        }

        return api.updateGcoo(gcooId = gcooId, request = jsonBody, image = imagePart)
    }

    /**
     * Gcoo 삭제 (OWNER만 가능)
     * @param gcooId 삭제할 Gcoo ID
     */
    suspend fun deleteGcoo(gcooId: Long) {
        api.deleteGcoo(gcooId)
    }
}
