package com.egg.jumpadventures.data.di

import com.egg.jumpadventures.data.progress.PlayerProgressRepository
import com.egg.jumpadventures.data.progress.PlayerProgressRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProgressModule {

    @Binds
    @Singleton
    abstract fun bindPlayerProgressRepository(
        impl: PlayerProgressRepositoryImpl
    ): PlayerProgressRepository
}
