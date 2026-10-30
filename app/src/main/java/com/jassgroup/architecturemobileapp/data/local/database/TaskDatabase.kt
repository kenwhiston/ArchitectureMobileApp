package com.jassgroup.architecturemobileapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jassgroup.architecturemobileapp.data.local.dao.TaskDao
import com.jassgroup.architecturemobileapp.data.local.entity.TaskEntity

/**
 * Base de datos principal de la aplicación utilizando Room.
 *
 * Contiene la lista de entidades y DAOs asociados. Se implementa
 * el patrón Singleton para asegurar que solo exista una instancia activa de la base de datos.
 */
@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        /**
         * Retorna la instancia única de [TaskDatabase].
         * Si no existe, se crea utilizando [Room.databaseBuilder].
         */
        fun getDatabase(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                )
                // En producción podrías agregar migraciones (.addMigrations(...))
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
