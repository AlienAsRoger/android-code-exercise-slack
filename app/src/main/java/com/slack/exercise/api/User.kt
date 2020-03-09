package com.slack.exercise.api

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * User model returned by the API.
 */
@Entity(tableName = "search_user_table")
data class User(
    @PrimaryKey
    val username: String,
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("avatar_url")
    val avatarUrl: String)

/*
 {
      "avatar_url": "https://randomuser.me/api/portraits/women/52.jpg",
      "display_name": "Brooklyn Huffman",
      "id": 1,
      "username": "bhuffman"
    },

 */