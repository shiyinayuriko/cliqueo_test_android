package tech.clique.android.test.application

import android.app.Application
import android.content.Context

class ContextApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: ContextApplication
        fun getAppContext(): Context = instance.applicationContext
    }
}