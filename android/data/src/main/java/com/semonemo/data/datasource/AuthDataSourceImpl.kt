package com.semonemo.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.semonemo.domain.datasource.AuthDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthDataSourceImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : AuthDataSource {
        override suspend fun saveWalletAddress(address: String) {
            dataStore.edit { prefs ->
                prefs[WALLET_ADDRESS_KEY] = address
            }
        }

        override suspend fun getWalletAddress(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[WALLET_ADDRESS_KEY]
                }.first()

        override suspend fun savePassword(password: String) {
            dataStore.edit { prefs ->
                prefs[PASSWORD_KEY] = password
            }
        }

        override suspend fun getPassword(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[PASSWORD_KEY]
                }.first()

        override suspend fun saveNickname(nickname: String) {
            dataStore.edit { prefs ->
                prefs[NICKNAME_KEY] = nickname
            }
        }

        override suspend fun getNickname(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[NICKNAME_KEY]
                }.first()

        override suspend fun saveProfileImage(profileImage: String) {
            dataStore.edit { prefs ->
                prefs[PROFILE_IMAGE_KEY] = profileImage
            }
        }

        override suspend fun getProfileImage(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[PROFILE_IMAGE_KEY]
                }.first()

        override suspend fun deleteAuthData() {
            dataStore.edit { prefs ->
                prefs.remove(WALLET_ADDRESS_KEY)
                prefs.remove(PASSWORD_KEY)
                prefs.remove(USER_ID_KEY)
                prefs.remove(PROFILE_IMAGE_KEY)
            }
        }

        override suspend fun saveUserId(userId: Long) {
            dataStore.edit { prefs ->
                prefs[USER_ID_KEY] = userId.toString()
            }
        }

        override suspend fun getUserId(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[USER_ID_KEY]
                }.first()

        override suspend fun saveCurrentKeyword(keyword: String) {
            dataStore.edit { prefs ->
                val currentKeywords =
                    prefs[CURRENT_KEYWORD_KEY]?.split(",")?.toMutableList() ?: mutableListOf()

                if (currentKeywords.contains(keyword)) {
                    currentKeywords.remove(keyword)
                    currentKeywords.add(0, keyword)
                } else {
                    currentKeywords.add(0, keyword)
                }

                prefs[CURRENT_KEYWORD_KEY] = currentKeywords.joinToString(",")
            }
        }

        override suspend fun getCurrentKeyword(): List<String> =
            dataStore.data
                .map { prefs ->
                    prefs[CURRENT_KEYWORD_KEY]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
                }.first()

        override suspend fun removeKeyword(keyword: String) {
            dataStore.edit { prefs ->
                val currentKeywords =
                    prefs[CURRENT_KEYWORD_KEY]?.split(",")?.toMutableList() ?: mutableListOf()
                currentKeywords.remove(keyword)

                prefs[CURRENT_KEYWORD_KEY] = currentKeywords.joinToString(",")
            }
        }

        companion object {
            val WALLET_ADDRESS_KEY = stringPreferencesKey("WALLET_ADDRESS_KEY")
            val PASSWORD_KEY = stringPreferencesKey("PASSWORD_KEY")
            val NICKNAME_KEY = stringPreferencesKey("NICKNAME_KEY")
            val USER_ID_KEY = stringPreferencesKey("USER_ID_KEY")
            val PROFILE_IMAGE_KEY = stringPreferencesKey("PROFILE_IMAGE_KEY")
            val CURRENT_KEYWORD_KEY = stringPreferencesKey("CURRENT_KEYWORD_KEY")
        }
    }
