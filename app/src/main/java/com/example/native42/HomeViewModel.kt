package com.example.native42

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _welcomeMessage = MutableStateFlow("Hello world!")
    val welcomeMessage: StateFlow<String> = _welcomeMessage

    init {
        viewModelScope.launch(Dispatchers.IO) {
            (0..100).forEach {
                delay(1000)
                _welcomeMessage.value = "$it"
            }
        }
    }
}