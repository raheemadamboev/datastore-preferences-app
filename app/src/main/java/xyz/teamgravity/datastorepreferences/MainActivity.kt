package xyz.teamgravity.datastorepreferences

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.teamgravity.datastorepreferences.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize
        dataStore = createDataStore(name = "Prefs")


        binding.apply {

            // save button
            saveB.setOnClickListener {
                lifecycleScope.launch {
                    save(key = keyField.text.toString().trim(), value = valueField.text.toString().trim())
                }
            }

            // read button
            readB.setOnClickListener {
                lifecycleScope.launch {
                    val value = read(requestField.text.toString().trim())
                    valueT.text = value ?: getString(R.string.no_value)
                }
            }
        }
    }

    // save to data store
    private suspend fun save(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { prefs ->
            prefs[dataStoreKey] = value
        }
    }

    // read from data store - no type safe
    private suspend fun read(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
}