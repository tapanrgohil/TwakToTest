package com.tapan.twaktotest.data.user.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.tapan.twaktotest.data.db.BaseDao

@Dao
abstract class NoteDao : BaseDao<NoteEntity>() {

    @Transaction
    open fun insertNote(noteEntity: NoteEntity) {
        val insertResult = insert(noteEntity)
        if (insertResult == -1L) {
            update(noteEntity)
        }
    }
}