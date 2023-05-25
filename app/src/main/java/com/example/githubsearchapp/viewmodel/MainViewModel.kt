package com.example.githubsearchapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubsearchapp.data.Content
import com.example.githubsearchapp.data.General
import com.example.githubsearchapp.repository.Api
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private  val _users = MutableLiveData<General>()
    val users : LiveData<General> = _users

    private  val _repo = MutableLiveData<General>()
    val repo : LiveData<General> = _repo

    private  val _content = MutableLiveData<List<Content>>()
    val content : LiveData<List<Content>> = _content

    private var api = Api.create()

    suspend fun getUsers(name: String){
        viewModelScope.launch {
            api.getUsers(name).enqueue(object : Callback<General> {
                override fun onResponse(call: Call<General>, response: Response<General>) {
                    _users.value  = response.body()
                }

                override fun onFailure(call: Call<General>, t: Throwable) {
                    Log.e("Debug", t.message.toString())
                    _users.postValue(null)

                }
            })
        }

    }

    suspend fun getRepo(name: String){
        viewModelScope.launch {
            api.getRepo(name).enqueue(object : Callback<General> {
                override fun onResponse(call: Call<General>, response: Response<General>) {
                    _repo.postValue(response.body())
                }

                override fun onFailure(call: Call<General>, t: Throwable) {
                    Log.e("Debug", t.message.toString())
                    _repo.postValue(null)

                }
            })
        }
    }

    suspend fun getContent(owner: String, repo: String, path: String){
        viewModelScope.launch {
            api.getContent(owner, repo, path).enqueue(object : Callback<List<Content>> {
                override fun onResponse(call: Call<List<Content>>, response: Response<List<Content>>) {
                    Log.i("postValue", response.body().toString())
                    _content.postValue(response.body())
                }

                override fun onFailure(call: Call<List<Content>>, t: Throwable) {
                    Log.e("Debug", t.message.toString())
                }
            })
        }


    }

}