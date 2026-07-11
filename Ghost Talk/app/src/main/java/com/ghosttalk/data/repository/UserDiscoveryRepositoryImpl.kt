package com.ghosttalk.data.repository

import com.ghosttalk.data.local.dao.UserDao
import com.ghosttalk.data.local.toDomain
import com.ghosttalk.data.local.toEntity
import com.ghosttalk.data.remote.api.ConversationApi
import com.ghosttalk.data.remote.toGhostUser
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.repository.UserDiscoveryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDiscoveryRepositoryImpl @Inject constructor(
    private val conversationApi: ConversationApi,
    private val userDao: UserDao
) : UserDiscoveryRepository {

    override fun discoverUsers(query: String): Flow<List<GhostUser>> {
        val flow = if (query.isBlank()) userDao.getAllUsers() else userDao.searchUsers(query)
        return flow.map { list -> list.map { it.toDomain() } }
            .onStart {
                try {
                    syncUsers(query)
                } catch (_: Exception) {
                    // Show cached users when offline
                }
            }
    }

    override suspend fun getUserById(userId: String): GhostUser? =
        userDao.getUserById(userId)?.toDomain()

    private suspend fun syncUsers(query: String) {
        val response = conversationApi.searchUsers(query)
        val body = response.body() ?: return
        if (!body.success || body.data == null) return
        body.data!!.forEach { userDao.insertUser(it.toGhostUser().toEntity()) }
    }
}
