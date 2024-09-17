package ir.masouddabbaghi.eduregister.data.model

import com.google.gson.annotations.SerializedName

class DepartmentList : ArrayList<DepartmentList.DepartmentListItem>() {
    data class DepartmentListItem(
        @SerializedName("description") val description: String,
        @SerializedName("name") val name: String,
        @SerializedName("yearFounded") val yearFounded: String,
    )
}
