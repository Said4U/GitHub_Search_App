package com.example.githubsearchapp.repository

import com.example.githubsearchapp.Constants
import com.example.githubsearchapp.data.Content
import com.example.githubsearchapp.data.General
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface Api {

    @GET("/search/users")
    @Headers("accept: application/json",
        "X-GitHub-Api-Version: 2022-11-28",
        "Authorization: " + Constants.API_KEY )
    fun getUsers(@Query("q") name : String) : Call<General>

    @GET("/search/repositories")
    @Headers("accept: application/json",
        "X-GitHub-Api-Version: 2022-11-28",
        "Authorization: " + Constants.API_KEY )
    fun getRepo(@Query("q") name : String) : Call<General>


    @GET("/repos/{owner}/{repo}/contents/{path}")
    @Headers("accept: application/json",
        "X-GitHub-Api-Version: 2022-11-28",
        "Authorization: " + Constants.API_KEY )
    fun getContent(@Path("owner") owner: String,
                   @Path("repo") repo: String,
                   @Path("path") path : String) : Call<List<Content>>


    companion object {

        private var BASE_URL = "https://api.github.com/"

        fun create() : Api {

            val okHttpClient: OkHttpClient = OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS).readTimeout(100, TimeUnit.SECONDS).build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(Api::class.java)

        }
    }

}