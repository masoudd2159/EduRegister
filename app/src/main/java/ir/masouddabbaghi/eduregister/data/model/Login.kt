package ir.masouddabbaghi.eduregister.data.model

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("token") val token: String,
)
