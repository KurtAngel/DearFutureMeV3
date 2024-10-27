package com.example.dearfutureme.APIResponse

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okio.BufferedSink
import okio.BufferedSource
import java.io.File

data class UploadResponse (
    val file: File,
    @SerializedName("profile_pic_url")
    val profilePicUrl: String?,
): ResponseBody() {
    override fun contentLength() = file.length()

    override fun contentType() = "$profilePicUrl/*".toMediaTypeOrNull()

    override fun source(): BufferedSource {
        TODO("Not yet implemented")
    }

}