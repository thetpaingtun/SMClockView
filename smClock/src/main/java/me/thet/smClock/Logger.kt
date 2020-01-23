package me.thet.smClock

import android.util.Log


class Logger {
    companion object {
        private val loggable = false
        private val TAG = "TAG_SMCLOCK"

        fun log(msg: String) {
            if (loggable) {
                Log.d(TAG, msg)
            }
        }
    }
}