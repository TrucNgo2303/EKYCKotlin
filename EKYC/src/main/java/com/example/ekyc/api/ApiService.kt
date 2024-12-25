package com.example.ekyc.api

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/api/request-ocr-driver-license/")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("transid") transId: RequestBody,
        @Part("side") docSide: RequestBody
    ): Observable<ApiResponse>
}


data class ApiResponse(
    val trans_id: String,
    val passed: Boolean,
    val gw_status: String,
    val gw_message: String,
    val gw_message_fra: String,
    val gw_body: GwBody
)

data class GwBody(
    val doc_type: Int,
    val doc_side: Int,
    val confidence: Confidence,
    val value: Value,
    val nearest_ocr: NearestOcr
)

data class Confidence(
    val address: Double,
    val date_place_of_birth: Double,
    val drive_licence: Double,
    val name: Double,
    val surname: Double
)

data class Value(
    val address: String,
    val date_place: String,
    val date_birth: String,
    val drive_licence: String,
    val name: String,
    val surname: String,
    val path_image: String
)

data class NearestOcr(
    val path_image: String,
    val face_recognition: String
)
