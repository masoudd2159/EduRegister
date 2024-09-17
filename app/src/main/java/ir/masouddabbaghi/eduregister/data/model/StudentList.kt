package ir.masouddabbaghi.eduregister.data.model

import com.google.gson.annotations.SerializedName

class StudentList : ArrayList<StudentList.StudentListItem>() {
    data class StudentListItem(
        @SerializedName("birthDate") val birthDate: String,
        @SerializedName("firstName") val firstName: String,
        @SerializedName("gender") val gender: String,
        @SerializedName("lastName") val lastName: String,
        @SerializedName("major") val major: String,
        @SerializedName("middleName") val middleName: String,
    )
}
