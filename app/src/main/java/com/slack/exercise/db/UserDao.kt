package com.slack.exercise.db

import android.content.Context
import androidx.room.*
import com.slack.exercise.api.User
import io.reactivex.Single

/**
 * Data Access Object interface for [User] entity to communicate with Room DB, named "search_user_table"
 */
@Dao
interface UserDao {

    @Query("SELECT * from search_user_table")
    fun getAllUsers(): Single<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<User>)
}

/**
 * Database that stores information about [User] received from [com.slack.exercise.api.SlackApi]
 */
@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "UserSearch_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}