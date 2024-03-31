package com.example.bondoman.viewmodel.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bondoman.api.RetrofitClient
import com.example.bondoman.models.Credential
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class AuthViewModel: ViewModel() {
    val isAuthorized = MutableLiveData(false)
    val removeToken = MutableLiveData(false)

    fun validate(token: String) {
        viewModelScope.launch {
            try {
                Log.d("bondoman cuy", "validating...")
                val response = RetrofitClient.authInstance.authToken("Bearer $token")

                if (response.code() == 200) {
                    isAuthorized.value = true
                } else if (response.code() == 401){
                    Log.d("bondoman cuy", token)
                    response.errorBody()?.string()?.let { Log.d("bondoman cuy", it) }
                    removeToken.value = true
                }
            } catch (_: SocketTimeoutException) { }
        }
    }
}