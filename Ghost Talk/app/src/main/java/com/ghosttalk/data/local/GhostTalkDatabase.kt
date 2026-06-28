package com.ghosttalk.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ghosttalk.data.local.dao.ChatDao
import com.ghosttalk.data.local.dao.MessageDao
import com.ghosttalk.data.local.dao.PendingMessageDao
import com.ghosttalk.data.local.dao.UserDao
import com.ghosttalk.data.local.entity.ChatEntity
import com.ghosttalk.data.local.entity.MessageEntity
import com.ghosttalk.data.local.entity.PendingMessageEntity
import com.ghosttalk.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, ChatEntity::class, MessageEntity::class, PendingMessageEntity::class],
    version = 2,
    exportSchema = false
)
abstract class GhostTalkDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
    abstract fun pendingMessageDao(): PendingMessageDao

    companion object {
        const val DATABASE_NAME = "ghost_talk_db"
    }
}
