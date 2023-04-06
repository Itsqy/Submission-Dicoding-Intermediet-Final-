package com.rifqi.testpaging3

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rifqi.testpaging3.data.local.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPrefferences(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { user ->
            UserModel(

                user[stringPreferencesKey("name")] ?: "",
                user[stringPreferencesKey("email")] ?: "",
                user[stringPreferencesKey("token")] ?: "",
                user[booleanPreferencesKey("login")] ?: false,


                )
        }
    }

    suspend fun logout() {
        dataStore.edit { user ->
            user[stringPreferencesKey("name")] = ""
            user[stringPreferencesKey("email")] = ""
            user[stringPreferencesKey("token")] = ""
            user[booleanPreferencesKey("login")] = false

        }
    }

     suspend fun saveUser(data: UserModel) {

        dataStore.edit { user ->
            user[stringPreferencesKey("name")] = data.userName
            user[stringPreferencesKey("email")] = data.userId
            user[stringPreferencesKey("token")] = data.userToken
            user[booleanPreferencesKey("login")] = data.userLogin

        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPrefferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPrefferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPrefferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}