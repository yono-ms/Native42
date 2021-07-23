package com.example.native42

import android.text.format.DateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {

    private val logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    private val _progress = MutableStateFlow(false)
    val progress: StateFlow<Boolean> = _progress

    private val _welcomeMessage = MutableStateFlow("Hello world!")
    val welcomeMessage: StateFlow<String> = _welcomeMessage

    private val _eventMessages = MutableStateFlow<List<String>>(listOf())
    val eventMessages: StateFlow<List<String>> = _eventMessages

    init {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                kotlin.runCatching {
                    val locale = Locale.getDefault()
                    val cal = Calendar.getInstance(locale).apply { time = Date() }
                    val pattern = DateFormat.getBestDateTimePattern(locale, "yyyyMMMdEEEHHmmss")
                    SimpleDateFormat(pattern, locale).format(cal.time)
                }.onSuccess {
                    _welcomeMessage.value = it
                }.onFailure {
                    logger.error("SimpleDateFormat", it)
                }
                delay(1000)
            }
        }
    }

    fun start() {
        logger.debug("start")
        if (_progress.value) {
            logger.warn("ignore.")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _progress.value = true

                messages.collect {
                    logger.debug("collect $it")
                    val list = _eventMessages.value.toMutableList()
                    list.add(it)
                    if (list.size > 5) {
                        list.removeAt(0)
                    }
                    _eventMessages.value = list
                }
            }.onFailure {
                logger.error("start", it)
            }.apply {
                _progress.value = false
            }
        }
    }

    private val messages: Flow<String> = flow {
        (0..10).forEach {
            emit("$it")
            delay(2000)
        }
    }
}