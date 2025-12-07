package com.egg.jumpadventures.data.di

import android.content.Context
import android.content.SharedPreferences
import com.egg.jumpadventures.data.settings.AudioPrefsRepository
import com.egg.jumpadventures.data.settings.AudioPrefsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsDataModule {

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: AudioPrefsRepositoryImpl
    ): AudioPrefsRepository
}