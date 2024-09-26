import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class AppDataStore(
    private val context: Context
) {
    companion object {
        private const val USER_PREFERENCES_NAME = "user_preferences"
        val COUNT_KEY = intPreferencesKey("count")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    // PreferenceDataStoreからFlowでcountを取得するためのプロパティ
    val count: Flow<Int>
        get() = context.dataStore.data.map { preferences ->
            preferences[COUNT_KEY] ?: 0
        }

    // PreferenceDataStoreにcountを書き込む処理
    suspend fun setCount(count: Int) {
        context.dataStore.edit { preferences ->
            preferences[COUNT_KEY] = count
        }
    }
}