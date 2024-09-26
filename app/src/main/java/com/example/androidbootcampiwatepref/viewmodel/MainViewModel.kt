import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataStore: AppDataStore
) : ViewModel() {
    val count get() = dataStore.count

    fun setCount(count: Int) {
        viewModelScope.launch {
            dataStore.setCount(count)
        }
    }
}