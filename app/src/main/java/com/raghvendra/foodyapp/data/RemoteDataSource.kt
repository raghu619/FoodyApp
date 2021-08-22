package com.raghvendra.foodyapp.data

import com.raghvendra.foodyapp.data.network.FoodRecipesApi
import com.raghvendra.foodyapp.models.FoodJoke
import com.raghvendra.foodyapp.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val foodRecipesApi: FoodRecipesApi) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.searchRecipes(searchQuery)
    }

    suspend fun getFoodJoke(apikey:String):Response<FoodJoke>{
        return foodRecipesApi.getFoodJoke(apikey)
    }
}