package com.tech.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class UserDetails(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("pref")

    companion object{
        val USER_NAME = stringPreferencesKey("USER_NAME")
        val AGE = intPreferencesKey("AGE")
    }

    suspend fun storeUserData(name : String,age : Int){
        context.dataStore.edit {
            it[USER_NAME] = name
            it[AGE] = age
        }
    }
    fun getName() = context.dataStore.data.map {
        it[USER_NAME] ?: ""
    }
    fun getAge() = context.dataStore.data.map {
        it[AGE] ?: -1
    }
}