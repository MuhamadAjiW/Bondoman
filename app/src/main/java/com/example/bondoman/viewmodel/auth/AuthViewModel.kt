package com.example.bondoman.viewmodel.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bondoman.api.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    val isAuthorized = MutableLiveData<Boolean?>(null)
    val removeToken = MutableLiveData(false)

    fun validate(token: String?) {
        viewModelScope.launch {
            if (token == null) {
                delay(1000)
                isAuthorized.value = false
            } else {
                try {
                    val response = RetrofitClient.authInstance.authToken("Bearer $token")

                    if (response.code() == 200) {
                        isAuthorized.value = true
                    } else if (response.code() == 401) {
                        isAuthorized.value = false
                        removeToken.value = true
                    }
                } catch (_: Exception) {
                }
            }
        }
    }
}