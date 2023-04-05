package link.magic.android.utils

import android.os.Handler
import android.os.Looper

class Debouncer {
    internal val handler = Handler(Looper.getMainLooper())
    private var timeoutId: Int? = null
    private var lastExecutionTime: Long? = null

    fun debounce(wait: Long, immediate: Boolean = false, block: () -> Unit) {
        val currentTime = System.currentTimeMillis()

        if (immediate && (lastExecutionTime == null || currentTime - lastExecutionTime!! >= wait)) {
            lastExecutionTime = currentTime
            block()
        } else {
            timeoutId?.let { clearTimeout() }

            timeoutId = setTimeout({
                block()
            }, wait)
        }
    }

    fun cancel() {
        timeoutId?.let { clearTimeout() }
        timeoutId = null
    }

    private fun setTimeout(block: () -> Unit, delayMillis: Long): Int {
        val runnable = Runnable {
            block()
        }
        handler.postDelayed(runnable, delayMillis)
        return runnable.hashCode()
    }

    private fun clearTimeout() {
        handler.removeCallbacksAndMessages(null)
    }
}