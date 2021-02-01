package com.karlosprojects.todomvvmapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.karlosprojects.todomvvmapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TaskDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: TaskDatabase
    private lateinit var dao: TaskDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TaskDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.taskDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertTaskItem() = runBlockingTest {
        val task = Task(
            "Make more kotlin projects",
            important = true,
            completed = false,
            created = System.currentTimeMillis(),
            id = 1
        )
        dao.insertTask(task)

        val allTaskItems =
            dao.getTasks("Make", SortOrder.BY_NAME, false).asLiveData().getOrAwaitValue()

        assertThat(allTaskItems).contains(task)
    }

    @Test
    fun updateTaskItem() = runBlockingTest {
        val task = Task(
            "Make more kotlin projects",
            important = true,
            completed = false,
            created = System.currentTimeMillis(),
            id = 1
        )
        dao.insertTask(task)

        val updatedTask = Task(
            "This is an updated task",
            important = false,
            completed = true,
            created = System.currentTimeMillis(),
            id = 1
        )
        dao.updateTask(updatedTask)

        val allTaskItems =
            dao.getTasks("updated", SortOrder.BY_NAME, false).asLiveData().getOrAwaitValue()

        assertThat(allTaskItems).doesNotContain(task)
    }

    @Test
    fun deleteTaskItem() = runBlockingTest {
        val task = Task(
            "Make more kotlin projects",
            important = true,
            completed = false,
            created = System.currentTimeMillis(),
            id = 1
        )
        val task2 = Task(
            "This task will be deleted",
            important = true,
            completed = false,
            created = System.currentTimeMillis(),
            id = 2
        )
        dao.insertTask(task)
        dao.insertTask(task2)

        dao.deleteTask(task2)

        val allTaskItems =
            dao.getTasks("This", SortOrder.BY_NAME, false).asLiveData().getOrAwaitValue()

        assertThat(allTaskItems).doesNotContain(task2)
    }

    @Test
    fun getTasksNotImportantSortedByName() = runBlockingTest {
        val task = Task(
            "This task will also be deleted",
            important = false,
            completed = false,
            created = System.currentTimeMillis(),
            id = 1
        )
        val task2 = Task(
            "This task will be deleted",
            important = false,
            completed = true,
            created = System.currentTimeMillis(),
            id = 2
        )
        val task3 = Task(
            "Make more kotlin projects",
            important = false,
            completed = true,
            created = System.currentTimeMillis(),
            id = 3
        )
        dao.insertTask(task)
        dao.insertTask(task2)
        dao.insertTask(task3)

        val allTaskItems =
            dao.getTasksSortedByName("", false).asLiveData().getOrAwaitValue()

        assertThat(allTaskItems[0].id).isEqualTo(3)
    }

    @Test
    fun deleteCompletedTasks() = runBlockingTest {
        val task = Task(
            "Make more kotlin projects",
            important = true,
            completed = false,
            created = System.currentTimeMillis(),
            id = 1
        )
        val task2 = Task(
            "This task will be deleted",
            important = false,
            completed = true,
            created = System.currentTimeMillis(),
            id = 2
        )
        val task3 = Task(
            "This task will also be deleted",
            important = false,
            completed = true,
            created = System.currentTimeMillis(),
            id = 3
        )
        dao.insertTask(task)
        dao.insertTask(task2)
        dao.insertTask(task3)

        dao.deleteCompletedTasks()

        val allTasksContainingThisName =
            dao.getTasks("This", SortOrder.BY_NAME, false).asLiveData().getOrAwaitValue()
        val allTasksItems =
            dao.getTasks("", SortOrder.BY_NAME, false).asLiveData().getOrAwaitValue()

        assertThat(allTasksContainingThisName).isEmpty()
        assertThat(allTasksItems.size).isEqualTo(1)
    }
}