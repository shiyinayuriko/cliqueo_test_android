package tech.clique.android.test.utils

import android.util.Log

const val TAG = "CliqueTestTag"
fun getClassName(obj: Any?): String {
    if (obj == null) return ""
    obj.javaClass.simpleName.also {
        if (it.isNotBlank()) return it
    }

    obj.javaClass.name.also {
        val lastName = it.substringAfterLast(".")
        if (lastName.isNotBlank()) return lastName
        return it
    }
}

inline fun <reified T> T.logD(message: String) {
    Log.d(TAG, "${getClassName(this)} : $message")
}


inline fun <reified T> T.logE(message: String) {
    Log.e(TAG, "${getClassName(this)} : $message")
}

inline fun <reified T> T.logE(message: String, t: Throwable) {
    Log.e(TAG, "${getClassName(this)} : $message", t)
}