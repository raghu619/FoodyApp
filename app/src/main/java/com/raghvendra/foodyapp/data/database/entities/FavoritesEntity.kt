package com.raghvendra.foodyapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.raghvendra.foodyapp.models.Result
import com.raghvendra.foodyapp.util.Constants.Companion.FAVORITE_RECIPES_TABLE


@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id :Int,
    var result: Result
)