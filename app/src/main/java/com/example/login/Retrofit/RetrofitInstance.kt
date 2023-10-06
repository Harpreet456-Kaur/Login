package com.example.login.Retrofit

import com.example.login.Models.MyData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    //Log.d
    fun retrofitInstance(): Retrofit {
        return Retrofit.Builder().baseUrl("https://meme-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}


//fun retrofitInstance() : Retrofit{
//
//    return Retrofit.Builder.baseUrl(" ")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//}
//
//fun retrofitInstance(): Retrofit{
//    return Retrofit.Builder().BaseUrl(" ")
//        .addConvertorFactory(GsonConverterFactory.create())
//        .build()
//}
//
//fun retrofitInstance():Retrofit{
//    return  Retrofit.Builder().baseUrl(" ")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//}