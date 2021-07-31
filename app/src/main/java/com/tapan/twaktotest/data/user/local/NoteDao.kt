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
            if (noteEntity.note.isEmpty()) {
                deleteNoteByUserId(noteEntity.userId)
            } else {
                updateNote(noteEntity.note, noteEntity.userId)
            }
        }
    }

    @Query("UPDATE NOTE SET NOTE = :note where USER_ID =:id")
    abstract fun updateNote(note: String, id: Int): Int

    @Query("DELETE FROM NOTE WHERE USER_ID =:id")
    abstract fun deleteNoteByUserId(id: Int): Int
}