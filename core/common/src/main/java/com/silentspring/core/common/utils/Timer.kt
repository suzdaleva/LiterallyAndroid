package com.silentspring.core.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SimpleTimer {

    private var timerJob: Job? = null

    fun start(scope: CoroutineScope, onTick: (String) -> Unit, onStop: () -> Unit) {
        timerJob?.cancel()
        timerJob = scope.launch(Dispatchers.IO) {
            var counter = COUNT_DOWN_MILLIS
            while (counter != 0L) {
                onTick(counter.toTimeDateString(TIMER_PATTERN))
                delay(1000L)
                counter -= 1000L
            }
            onStop()
        }
    }

    fun stop() {
        timerJob?.cancel()
    }

    companion object {
        private const val COUNT_DOWN_MILLIS = 20000L
        private const val TIMER_PATTERN = "mm:ss"
    }
}