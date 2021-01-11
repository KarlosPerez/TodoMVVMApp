package com.karlosprojects.todomvvmapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.karlosprojects.todomvvmapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao() : TaskDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            //db operations
            val dao = database.get().taskDao()

            //Coroutine is a lightweight thread
            applicationScope.launch {
                dao.insertTask(Task("Wash the dishes"))
                dao.insertTask(Task("Do the laundry"))
                dao.insertTask(Task("Buy Groceries", important = true))
                dao.insertTask(Task("Prepare Food", completed = true))
                dao.insertTask(Task("Call mom", important = true))
                dao.insertTask(Task("Visit Grandma", completed = true))
            }
        }
    }
}