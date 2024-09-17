package ir.masouddabbaghi.eduregister.data.model.requestBody

import com.google.gson.annotations.SerializedName

data class StudentRegisterBody(
    @SerializedName("birthDate") val birthDate: String,
    @SerializedName("departmentId") val departmentId: Int,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("major") val major: String,
    @SerializedName("middleName") val middleName: String,
)
