package com.ghosttalk.data.repository

import com.ghosttalk.data.local.dao.UserDao
import com.ghosttalk.data.local.entity.UserEntity
import com.ghosttalk.data.local.toDomain
import com.ghosttalk.data.local.toEntity
import com.ghosttalk.data.remote.api.UserApi
import com.ghosttalk.data.remote.toDomain
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.repository.UserDiscoveryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDiscoveryRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userDao: UserDao
) : UserDiscoveryRepository {

    override fun discoverUsers(query: String): Flow<List<GhostUser>> {
        val userFlow = if (query.isBlank()) {
            userDao.getAllUsers()
        } else {
            userDao.searchUsers(query)
        }
        return userFlow
            .map { list -> list.map { it.toDomain() } }
            .onStart { syncUsersFromRemote(query) }
    }

    override suspend fun getUserById(userId: String): GhostUser? {
        val local = userDao.getUserById(userId)?.toDomain()
        if (local != null) return local
        val response = userApi.getUser(userId)
        if (response.isSuccessful && response.body() != null) {
            val user = response.body()!!.toDomain()
            userDao.insertUser(user.toEntity())
            return user
        }
        return null
    }

    private suspend fun syncUsersFromRemote(query: String) {
        val response = userApi.discoverUsers(query)
        if (response.isSuccessful) {
            response.body()?.forEach { userDao.insertUser(it.toUserEntity()) }
        }
    }
}

private fun com.ghosttalk.data.remote.dto.UserDto.toUserEntity() = UserEntity(
    ghostId = ghostId,
    nickname = nickname,
    avatarResId = avatarId,
    isOnline = isOnline,
    lastSeen = lastSeen
)
