package ir.masouddabbaghi.eduregister.data.model

import com.google.gson.annotations.SerializedName

data class HomeList(
    @SerializedName("result") val result: List<Result>,
) {
    data class Result(
        @SerializedName("color") val color: String,
        @SerializedName("icon") val icon: String,
        @SerializedName("id") val id: Int,
        @SerializedName("title") val title: String,
    )
}
