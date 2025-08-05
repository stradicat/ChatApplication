package dev.dmayr.chatapplication.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.dmayr.chatapplication.data.database.AppDatabase
import dev.dmayr.chatapplication.data.database.dao.ChatMessageDao
import dev.dmayr.chatapplication.data.database.dao.UserDao
import dev.dmayr.chatapplication.data.datasource.websocket.WebSocketDataSource
import dev.dmayr.chatapplication.data.repository.AuthRepository
import dev.dmayr.chatapplication.data.repository.AuthRepositoryImpl
import dev.dmayr.chatapplication.data.repository.ChatRepository
import dev.dmayr.chatapplication.data.repository.ChatRepositoryImpl
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideWebSocketDataSource(okHttpClient: OkHttpClient): WebSocketDataSource {
        return WebSocketDataSource(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideChatMessageDao(appDatabase: AppDatabase): ChatMessageDao {
        return appDatabase.chatMessageDao()
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        webSocketDataSource: WebSocketDataSource,
        chatMessageDao: ChatMessageDao,
        gson: Gson
    ): ChatRepository {
        return ChatRepositoryImpl(webSocketDataSource, chatMessageDao, gson)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return AuthRepositoryImpl()
    }
}
