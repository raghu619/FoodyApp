package com.raghvendra.foodyapp.data

import com.raghvendra.foodyapp.data.network.FoodRecipesApi
import com.raghvendra.foodyapp.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val foodRecipesApi: FoodRecipesApi) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
         return  foodRecipesApi.getRecipes(queries)
    }
}