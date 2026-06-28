package com.ghosttalk.domain.usecase

import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.repository.UserDiscoveryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiscoverUsersUseCase @Inject constructor(
    private val userDiscoveryRepository: UserDiscoveryRepository
) {
    operator fun invoke(query: String = ""): Flow<List<GhostUser>> =
        userDiscoveryRepository.discoverUsers(query)
}

class GetUserByIdUseCase @Inject constructor(
    private val userDiscoveryRepository: UserDiscoveryRepository
) {
    suspend operator fun invoke(userId: String): GhostUser? =
        userDiscoveryRepository.getUserById(userId)
}
