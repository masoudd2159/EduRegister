package ir.masouddabbaghi.eduregister.data.model

import com.google.gson.annotations.SerializedName

data class Slider(
    @SerializedName("result") val result: List<Result>,
) {
    data class Result(
        @SerializedName("id") val id: Int,
        @SerializedName("image") val image: String,
        @SerializedName("title") val title: String,
    )
}
