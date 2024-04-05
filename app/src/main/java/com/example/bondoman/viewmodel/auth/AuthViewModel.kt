package com.example.bondoman.viewmodel.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bondoman.api.RetrofitClient
import com.example.bondoman.types.util.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AuthViewModel: ViewModel() {
    val isAuthorized = MutableLiveData<Boolean?>(null)
    val errMessage = MutableLiveData("")
    val isConnectionTimeout = MutableLiveData(false)
    val removeToken = MutableLiveData(false)

    fun validate(token: String?, isDelay: Boolean) {
        viewModelScope.launch {
            if (token == null) {
                if (isDelay) delay(1000)
                isAuthorized.value = false
            } else {
                try {
                    val response = RetrofitClient.authInstance.authToken("Bearer $token")

                    if (response.code() == 200) {
                        isAuthorized.value = true
                    } else if (response.code() == 401) {
                        isAuthorized.value = false
                        removeToken.value = true
                    } else {
                        errMessage.value = "Auth server error"
                    }
                } catch (_: UnknownHostException) {
                    errMessage.value = "Connection timeout"
                    isConnectionTimeout.value = true
                } catch (e: Exception) {
                    errMessage.value = "An error occurred"
                }
            }
        }
    }
}