package app.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences


actual fun getAppDataStore(): DataStore<Preferences> = createIOSDataStore()


@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
private fun createIOSDataStore(): DataStore<Preferences> = getDataStore(
    producePath = {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        requireNotNull(documentDirectory).path + "/$dataStoreFileName"
    }
)