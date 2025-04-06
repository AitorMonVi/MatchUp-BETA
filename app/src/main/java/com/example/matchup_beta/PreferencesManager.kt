package com.example.matchup_beta

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_preferences")



object PreferencesKeys {
    val ITEMS_ADDED = intPreferencesKey("items_added")
    val ITEMS_REMOVED = intPreferencesKey("items_removed")
    val FRAGMENT_ENTRIES = intPreferencesKey("fragment_entries")

}

class PreferencesManager(context: Context) {
    private val dataStore = context.dataStore

    val itemsAdded: Flow<Int> = dataStore.data
        .map { prefs -> prefs[PreferencesKeys.ITEMS_ADDED] ?: 0 }

    val itemsRemoved: Flow<Int> = dataStore.data
        .map { prefs -> prefs[PreferencesKeys.ITEMS_REMOVED] ?: 0 }

    suspend fun incrementItemsAdded() {
        dataStore.edit { prefs ->
            val current = prefs[PreferencesKeys.ITEMS_ADDED] ?: 0
            prefs[PreferencesKeys.ITEMS_ADDED] = current + 1
        }
    }

    suspend fun incrementItemsRemoved() {
        dataStore.edit { prefs ->
            val current = prefs[PreferencesKeys.ITEMS_REMOVED] ?: 0
            prefs[PreferencesKeys.ITEMS_REMOVED] = current + 1
        }
    }
    val fragmentEntries: Flow<Int> = dataStore.data
        .map { prefs -> prefs[PreferencesKeys.FRAGMENT_ENTRIES] ?: 0 }

    suspend fun incrementFragmentEntries() {
        dataStore.edit { prefs ->
            val current = prefs[PreferencesKeys.FRAGMENT_ENTRIES] ?: 0
            prefs[PreferencesKeys.FRAGMENT_ENTRIES] = current + 1
        }
    }
}
