package app.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.mp.KoinPlatform.getKoin


actual fun getAppDataStore(): DataStore<Preferences> = createAndroidDataStore(context = getKoin().get<Context>())

private fun createAndroidDataStore(context: Context): DataStore<Preferences> = getDataStore(
    producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
)