package com.bolo.bolomap.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "media")
class Media (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "label") val label: String?,
    @ColumnInfo(name = "long") val long: Double?,
    @ColumnInfo(name = "lat") val lat: Double?,
    @ColumnInfo(name = "photos") val photos: String?,
    @ColumnInfo(name = "description") val description: String?
)
{
    override fun toString(): String {
        return "Media(id=$id, date=$date, label=$label, long=$long, lat=$lat, photos=$photos, description=$description)"
    }
}