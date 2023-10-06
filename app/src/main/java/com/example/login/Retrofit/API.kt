package com.example.login.Retrofit

import com.example.login.Models.MyData
import retrofit2.Call
import retrofit2.http.GET

interface API {

    @GET("gimme")
    fun getMeme() : Call<MyData>
}



//interface  API{
//
//    @GET(" ")
//    fun getMeme() : Call<MyData>
//}
//
//interface  API{
//    @GET(" ")
//    fun getMeme(): Call<MyData>
//}

