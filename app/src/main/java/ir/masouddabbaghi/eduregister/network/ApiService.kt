package ir.masouddabbaghi.eduregister.network

import ir.masouddabbaghi.eduregister.data.model.DepartmentList
import ir.masouddabbaghi.eduregister.data.model.Login
import ir.masouddabbaghi.eduregister.data.model.StudentList
import ir.masouddabbaghi.eduregister.data.model.requestBody.LoginBody
import ir.masouddabbaghi.eduregister.data.model.requestBody.RegisterBody
import ir.masouddabbaghi.eduregister.data.model.requestBody.StudentRegisterBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/Auth/login")
    suspend fun login(
        @Body loginBody: LoginBody,
    ): Response<Login>

    @POST("api/Auth/register")
    suspend fun register(
        @Body registerBody: RegisterBody,
    ): Response<String>

    @GET("api/Students")
    suspend fun studentList(): Response<StudentList>

    @POST("api/Students")
    suspend fun registerStudent(
        @Body studentRegisterBody: StudentRegisterBody,
    ): Response<String>

    @GET("api/Departments")
    suspend fun departmentList(): Response<DepartmentList>
}
