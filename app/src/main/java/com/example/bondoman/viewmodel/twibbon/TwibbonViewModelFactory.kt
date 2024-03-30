package com.example.bondoman.viewmodel.twibbon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TwibbonViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TwibbonViewModel() as T
    }
}