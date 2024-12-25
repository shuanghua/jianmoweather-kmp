package app.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.SynchronizedObject
import kotlinx.coroutines.internal.synchronized
import okio.Path.Companion.toPath


expect fun getAppDataStore(): DataStore<Preferences> // koin di 中调用

private lateinit var dataStore: DataStore<Preferences>

internal var dataStoreFileName = "app.preferences_pb"

@OptIn(InternalCoroutinesApi::class)
private val lock = SynchronizedObject()


/**
 * 单例
 */
@OptIn(InternalCoroutinesApi::class)
internal fun getDataStore(
    producePath: () -> String
): DataStore<Preferences> = synchronized(lock) {
    if (::dataStore.isInitialized) {
        dataStore
    } else {
        PreferenceDataStoreFactory
            .createWithPath(
                produceFile = { producePath().toPath() }
            )
            .also {
                dataStore = it
            }
    }
}




