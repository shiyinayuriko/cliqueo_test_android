package tech.clique.android.test.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

object DateStoreUtil {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")
}