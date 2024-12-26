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
    fun uploadImageFront(
        @Part image: MultipartBody.Part,
        @Part("transid") transId: RequestBody,
        @Part("side") docSide: RequestBody
    ): Observable<ApiImageFrontResponse>

    @Multipart
    @POST("/api/request-ocr-driver-license/")
    fun uploadImageBack(
        @Part image: MultipartBody.Part,
        @Part("transid") transId: RequestBody,
        @Part("side") docSide: RequestBody
    ): Observable<ApiImageBackResponse>


    @Multipart
    @POST("/api/request-face-compare/")
    fun uploadFace(
        @Part("transid") transId: RequestBody,
        @Part("image_doc_path") imageDocPath: RequestBody,
        @Part image_selfie_path: MultipartBody.Part
    ): Observable<FaceResponse>
}


data class ApiImageFrontResponse(
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

data class ApiImageBackResponse(
    val trans_id: String,
    val passed: Boolean,
    val gw_status: String,
    val gw_message: String,
    val gw_message_fra: String,
    val gw_body: GwBodyBack
)

data class GwBodyBack(
    val doc_type: Int,
    val doc_side: Int,
    val confidence: ConfidenceBack,
    val value: ValueBack,
)

data class ConfidenceBack(
    val auth: Double,
    val card: Double,
    val issued_at: Double,
    val issued_on: Double,
    val license: LicenseNum
)

data class LicenseNum(
    val A: Double,
    val B: Double,
    val C: Double,
    val D1: Double,
    val D2: Double,
    val E: Double,
    val F: Double
)
data class ValueBack(
    val auth: String,
    val card: String,
    val issued_at: String,
    val issued_on: String,
    val license: LicenseText,
    val path_image: String
)

data class LicenseText(
    val A: String,
    val B: String,
    val C: String,
    val D1: String,
    val D2: String,
    val E: String,
    val F: String
)



data class FaceResponse(
    val trans_id: String,
    val passed: Boolean,
    val gw_status: String,
    val gw_message: String,
    val gw_message_fra: String,
    val gw_body: GwBodyFace
)
data class GwBodyFace(
    val face: Face
)
data class Face(
    val match: Boolean,
    val percent_match: Double,
    val liveness: Boolean,
    val percent_liveness: Double
)
