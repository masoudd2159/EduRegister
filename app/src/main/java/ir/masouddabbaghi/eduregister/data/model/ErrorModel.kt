package ir.masouddabbaghi.eduregister.data.model

import com.google.gson.annotations.SerializedName

data class ErrorModel(
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: List<Message>,
    @SerializedName("statusCode") val statusCode: Int,
) {
    data class Message(
        @SerializedName("error") val error: String,
        @SerializedName("field") val `field`: String,
    )
}
