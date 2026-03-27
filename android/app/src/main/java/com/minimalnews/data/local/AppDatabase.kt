package com.minimalnews.data.local

import android.content.Context
import androidx.room.*
import com.minimalnews.data.models.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    fun getAll(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    suspend fun getAllSync(): List<TodoItem>

    @Insert
    suspend fun insert(todo: TodoItem)

    @Update
    suspend fun update(todo: TodoItem)

    @Delete
    suspend fun delete(todo: TodoItem)

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Database(entities = [TodoItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "minimalnews.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
