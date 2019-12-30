package com.bolo.bolomap.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo")
class Photo (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "label") val label: String?,
    @ColumnInfo(name = "long") val long: Double?,
    @ColumnInfo(name = "lat") val lat: Double?,
    @ColumnInfo(name = "photo") val photo: String?,
    @ColumnInfo(name = "description") val description: String?)
{
    override fun toString(): String {
        return "AlbumDao(id=$id, date=$date, label=$label, long=$long, lat=$lat, photo=$photo, description=$description)"
    }
}