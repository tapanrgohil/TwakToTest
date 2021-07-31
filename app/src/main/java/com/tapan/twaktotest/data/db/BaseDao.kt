package com.tapan.twaktotest.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
abstract class BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(data: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertAll(data: List<T>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(data: T): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateAll(data: List<T>)


}