package com.travel.`in`.db.entities

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
    @ColumnInfo(name = "photo") var photo: String?,
    @ColumnInfo(name = "photos") var photos: String?,
    @ColumnInfo(name = "description") val description: String?)
{
    override fun toString(): String {
        return "Photo(id=$id, date=$date, label=$label, long=$long, lat=$lat, photo=$photo, photos=$photos, description=$description)"
    }


}