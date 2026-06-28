package com.ghosttalk.di

import android.content.Context
import androidx.room.Room
import com.ghosttalk.BuildConfig
import com.ghosttalk.core.encryption.DeviceRegistration
import com.ghosttalk.core.encryption.EncryptionProvider
import com.ghosttalk.core.encryption.KeyManager
import com.ghosttalk.core.encryption.MessageEncryptionLayer
import com.ghosttalk.core.encryption.NoOpDeviceRegistration
import com.ghosttalk.core.encryption.NoOpEncryptionProvider
import com.ghosttalk.core.encryption.NoOpKeyManager
import com.ghosttalk.core.encryption.NoOpMessageEncryptionLayer
import com.ghosttalk.core.encryption.NoOpSecureSessionManager
import com.ghosttalk.core.encryption.SecureSessionManager
import com.ghosttalk.core.session.SessionManager
import com.ghosttalk.core.session.SessionManagerImpl
import com.ghosttalk.data.local.GhostTalkDatabase
import com.ghosttalk.data.local.dao.ChatDao
import com.ghosttalk.data.local.dao.MessageDao
import com.ghosttalk.data.local.dao.UserDao
import com.ghosttalk.data.remote.FakeAuthApi
import com.ghosttalk.data.remote.FakeChatApi
import com.ghosttalk.data.remote.FakeUserApi
import com.ghosttalk.data.remote.api.AuthApi
import com.ghosttalk.data.remote.api.ChatApi
import com.ghosttalk.data.remote.api.UserApi
import com.ghosttalk.data.repository.AuthRepositoryImpl
import com.ghosttalk.data.repository.ChatRepositoryImpl
import com.ghosttalk.data.repository.UserDiscoveryRepositoryImpl
import com.ghosttalk.domain.repository.AuthRepository
import com.ghosttalk.domain.repository.ChatRepository
import com.ghosttalk.domain.repository.UserDiscoveryRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.ghosttalk.app/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GhostTalkDatabase =
        Room.databaseBuilder(
            context,
            GhostTalkDatabase::class.java,
            GhostTalkDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideUserDao(db: GhostTalkDatabase): UserDao = db.userDao()

    @Provides
    fun provideChatDao(db: GhostTalkDatabase): ChatDao = db.chatDao()

    @Provides
    fun provideMessageDao(db: GhostTalkDatabase): MessageDao = db.messageDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun bindUserDiscoveryRepository(impl: UserDiscoveryRepositoryImpl): UserDiscoveryRepository

    @Binds
    @Singleton
    abstract fun bindSessionManager(impl: SessionManagerImpl): SessionManager
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

    @Binds
    @Singleton
    abstract fun bindAuthApi(impl: FakeAuthApi): AuthApi

    @Binds
    @Singleton
    abstract fun bindChatApi(impl: FakeChatApi): ChatApi

    @Binds
    @Singleton
    abstract fun bindUserApi(impl: FakeUserApi): UserApi
}

@Module
@InstallIn(SingletonComponent::class)
abstract class EncryptionModule {

    @Binds
    @Singleton
    abstract fun bindEncryptionProvider(impl: NoOpEncryptionProvider): EncryptionProvider

    @Binds
    @Singleton
    abstract fun bindKeyManager(impl: NoOpKeyManager): KeyManager

    @Binds
    @Singleton
    abstract fun bindSecureSessionManager(impl: NoOpSecureSessionManager): SecureSessionManager

    @Binds
    @Singleton
    abstract fun bindDeviceRegistration(impl: NoOpDeviceRegistration): DeviceRegistration

    @Binds
    @Singleton
    abstract fun bindMessageEncryptionLayer(impl: NoOpMessageEncryptionLayer): MessageEncryptionLayer
}
