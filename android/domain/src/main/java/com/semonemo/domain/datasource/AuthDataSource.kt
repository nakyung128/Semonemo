package com.semonemo.domain.datasource

interface AuthDataSource {
    suspend fun saveWalletAddress(address: String): Unit

    suspend fun getWalletAddress(): String?

    suspend fun savePassword(password: String): Unit

    suspend fun getPassword(): String?

    suspend fun saveNickname(nickname: String): Unit

    suspend fun getNickname(): String?

    suspend fun saveProfileImage(profileImage: String): Unit

    suspend fun getProfileImage(): String?

    suspend fun deleteAuthData(): Unit

    suspend fun saveUserId(userId: Long): Unit

    suspend fun getUserId(): String?

    suspend fun saveCurrentKeyword(keyword: String): Unit

    suspend fun getCurrentKeyword(): List<String>

    suspend fun removeKeyword(keyword: String): Unit
}
