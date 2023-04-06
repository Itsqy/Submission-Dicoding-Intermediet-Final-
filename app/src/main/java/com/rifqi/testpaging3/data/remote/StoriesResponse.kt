package com.rifqi.testpaging3.data.remote

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class StoriesResponse(
    @field:SerializedName("error") val error: Boolean,

    @field:SerializedName("message") val message: String,

    @field:SerializedName("listStory") val listStory: ArrayList<ListStoryData>,
)

@Parcelize
@Entity(tableName = "listStoryData")
data class ListStoryData(

    @PrimaryKey
    @field:SerializedName("id") val id: String,

    @field:SerializedName("name") val name: String,

    @field:SerializedName("description") val description: String,

    @field:SerializedName("photoUrl") val photoUrl: String,

    @field:SerializedName("createdAt") val createdAt: String,

    @field:SerializedName("lat") val lat: Double = 1.0,

    @field:SerializedName("lon") val lon: Double = 1.0,
) : Parcelable
