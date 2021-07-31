package com.tapan.twaktotest.data.note.local

import com.tapan.twaktotest.data.user.local.NoteDao
import com.tapan.twaktotest.data.user.local.NoteEntity
import javax.inject.Inject

interface NoteLocalSource {
    fun insertNote(note: NoteEntity)
}

class NoteLocalSourceImpl @Inject constructor(private val noteDao: NoteDao) : NoteLocalSource {
    override fun insertNote(note: NoteEntity) {
        noteDao.insertNote(note)
    }

}