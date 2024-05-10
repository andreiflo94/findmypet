package com.heixss.findmypet.common

import android.content.Context
import com.heixss.findmypet.data.common.AuthInterceptor
import com.heixss.findmypet.data.common.FakeRemoteApi
import com.heixss.findmypet.data.common.RemoteApi
import com.heixss.findmypet.data.common.SessionManager
import com.heixss.findmypet.data.login.LoginRepositoryImpl
import com.heixss.findmypet.data.profile.UserRepositoryImpl
import com.heixss.findmypet.data.searchpets.PetRepositoryImpl
import com.heixss.findmypet.domain.login.LoginRepository
import com.heixss.findmypet.domain.profile.UserRepository
import com.heixss.findmypet.domain.searchpets.PetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideRemoteApi(sessionManager: SessionManager): RemoteApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .build()

        return Retrofit.Builder()
            .baseUrl(RemoteApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideLoginRepository(remoteApi: RemoteApi, sessionManager: SessionManager): LoginRepository {
        return LoginRepositoryImpl(remoteApi, sessionManager)
    }


    @Provides
    @Singleton
    fun providePetRepository(remoteApi: RemoteApi): PetRepository {
        return PetRepositoryImpl(remoteApi)
    }

    @Provides
    @Singleton
    fun provideUserRepository(remoteApi: FakeRemoteApi, sessionManager: SessionManager): UserRepository {
        return UserRepositoryImpl(remoteApi, sessionManager)
    }

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext applicationContext: Context): SessionManager = SessionManager(applicationContext)
}