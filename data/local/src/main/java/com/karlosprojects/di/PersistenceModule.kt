package com.karlosprojects.di

import android.app.Application
import androidx.room.Room
import com.karlosprojects.local.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object PersistenceModule {

    private const val DATABASE_NAME: String = "task_database"

    @Singleton
    @Provides
    fun provideRoomDatabase(
        context: Application,
        callback: TaskDatabase.Callback
    ) = Room.databaseBuilder(context, TaskDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @Singleton
    @Provides
    fun provideTaskDao(taskDatabase: TaskDatabase) = taskDatabase.taskDao()

    @ApplicationScope
    @Singleton
    @Provides
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope