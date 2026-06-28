package com.ghosttalk.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ghosttalk.data.local.entity.PendingMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingMessageDao {
    @Query("SELECT * FROM pending_messages ORDER BY createdAt ASC")
    fun getAllPending(): Flow<List<PendingMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pending: PendingMessageEntity)

    @Query("DELETE FROM pending_messages WHERE clientMessageId = :clientMessageId")
    suspend fun delete(clientMessageId: String)

    @Update
    suspend fun update(pending: PendingMessageEntity)
}
