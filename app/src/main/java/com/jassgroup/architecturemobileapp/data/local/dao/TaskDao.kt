package com.jassgroup.architecturemobileapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jassgroup.architecturemobileapp.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para realizar operaciones CRUD en la tabla 'tasks'.
 *
 * En la arquitectura MVVM:
 * El DAO abstrae las consultas SQL directas. El retorno de [Flow] permite que
 * la UI (a través del ViewModel) observe cambios en tiempo real en la base de datos.
 */
@Dao
interface TaskDao {

    /**
     * Obtiene todas las tareas ordenadas por fecha de creación descendente (las más recientes primero).
     * Devuelve un [Flow] para que cualquier cambio en la base de datos actualice la UI automáticamente.
     */
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    /**
     * Obtiene una tarea específica por su ID.
     */
    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Int): Flow<TaskEntity?>

    /**
     * Inserta una nueva tarea. Si ocurre un conflicto de ID, se reemplaza la existente.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    /**
     * Actualiza los datos de una tarea existente.
     */
    @Update
    suspend fun updateTask(task: TaskEntity)

    /**
     * Elimina una tarea específica de la base de datos.
     */
    @Delete
    suspend fun deleteTask(task: TaskEntity)

    /**
     * Elimina una tarea por su ID.
     */
    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Int)

    /**
     * Obtiene los títulos de las tareas que coincidan con ('1','2','3').
     */
    @Query("SELECT title FROM tasks WHERE title IN ('1','2','3')")
    fun getAllTitles(): Flow<List<String>>
}
