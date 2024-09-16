package ir.masouddabbaghi.eduregister.data.model

import com.google.gson.annotations.SerializedName

sealed class ErrorResponse {
    data class Model1(
        @SerializedName("status") val status: Int,
        @SerializedName("title") val title: String,
        @SerializedName("traceId") val traceId: String,
        @SerializedName("type") val type: String,
    ) : ErrorResponse()

    data class Model2(
        @SerializedName("errors") val errors: List<String>,
    ) : ErrorResponse()

    data class Model3(
        @SerializedName("errors") val errors: Errors,
        @SerializedName("status") val status: Int,
        @SerializedName("title") val title: String,
        @SerializedName("traceId") val traceId: String,
        @SerializedName("type") val type: String,
    ) : ErrorResponse() {
        data class Errors(
            @SerializedName("Password") val password: List<String>,
        )
    }
}
