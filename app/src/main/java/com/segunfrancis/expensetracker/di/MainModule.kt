package com.segunfrancis.expensetracker.di

import android.content.Context
import com.segunfrancis.expensetracker.data.ExpenseTrackerDao
import com.segunfrancis.expensetracker.data.ExpenseTrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideDao(@ApplicationContext context: Context): ExpenseTrackerDao {
        return ExpenseTrackerDatabase.getDatabase(context).getDao()
    }
}
