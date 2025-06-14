package tech.clique.android.test.utils

import com.google.gson.Gson


object GsonUtil {
    val inst: Gson = Gson()
    fun <T> fromJson(json: String, classOfT: Class<T>): T {
        return inst.fromJson(json, classOfT)
    }

    fun toJson(obj: Any): String? {
        return inst.toJson(obj)
    }
}