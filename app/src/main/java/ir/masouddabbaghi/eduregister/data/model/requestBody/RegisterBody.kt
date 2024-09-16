package ir.masouddabbaghi.eduregister.data.model.requestBody

import com.google.gson.annotations.SerializedName

data class RegisterBody(
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("password") val password: String,
)
