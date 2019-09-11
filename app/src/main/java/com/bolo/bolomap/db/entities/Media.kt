package com.bolo.bolomap.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "media")
data class Media (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "label") val label: String?,
    @ColumnInfo(name = "long") val long: Float?,
    @ColumnInfo(name = "lat") val lat: Float?,
    @ColumnInfo(name = "photos") val photos: String?,
    @ColumnInfo(name = "description") val description: String?
)