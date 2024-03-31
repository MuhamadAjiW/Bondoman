package com.example.bondoman.viewmodel.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bondoman.api.RetrofitClient
import com.example.bondoman.models.Credential
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    val loginMessage = MutableLiveData("")
    val loginToken = MutableLiveData<String?>(null)

    fun loginUser(credential: Credential) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.loginInstance.login(credential)

                if (response.code() == 200) {
                    loginMessage.value = "Login success"
                    loginToken.value = response.body()?.token
                } else if (response.code() / 100 == 4){
                    loginMessage.value = "Invalid email or password"
                    loginToken.value = null
                } else {
                    loginMessage.value = "Internal server error"
                    loginToken.value = null
                }
            } catch (ex: Exception) {
                loginMessage.value = "An error occurred"
                loginToken.value = null
            }
        }
    }
}