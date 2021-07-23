package com.example.native42

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

class HomeViewModel : ViewModel() {

    private val logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    private val _welcomeMessage = MutableStateFlow("Hello world!")
    val welcomeMessage: StateFlow<String> = _welcomeMessage

    private val _eventMessages = MutableStateFlow<List<String>>(listOf())
    val eventMessages: StateFlow<List<String>> = _eventMessages

    init {
        viewModelScope.launch(Dispatchers.IO) {
            (0..100).forEach {
                delay(1000)
                _welcomeMessage.value = "$it"
            }
        }
    }

    fun start() {
        logger.debug("start")
        viewModelScope.launch(Dispatchers.IO) {
            messages.collect {
                logger.debug("collect $it")
                val list = _eventMessages.value.toMutableList()
                list.add(it)
                if (list.size > 5) {
                    list.removeAt(0)
                }
                _eventMessages.value = list
            }
        }
    }

    private val messages: Flow<String> = flow {
        (0..100).forEach {
            emit("$it")
            delay(3000)
        }
    }
}