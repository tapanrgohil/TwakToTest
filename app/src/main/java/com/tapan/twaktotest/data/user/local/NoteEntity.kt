package com.tapan.twaktotest.data.user.local

import androidx.room.*

@Entity(
    tableName = "NOTE",
    indices = [Index("USER_ID", "NOTE", unique = true)],
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = arrayOf("ID"),
        childColumns = arrayOf("USER_ID")
    )]
)
data class NoteEntity(
    @ColumnInfo(name = "USER_ID")
    var userId: Int,
    @ColumnInfo(name = "NOTE")
    var note: String
) {
    @PrimaryKey
    var index: Long? = null
}