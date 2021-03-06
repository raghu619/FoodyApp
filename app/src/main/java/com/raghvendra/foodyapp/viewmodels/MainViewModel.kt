package com.raghvendra.foodyapp.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.raghvendra.foodyapp.data.Repository
import com.raghvendra.foodyapp.data.database.entities.FavoritesEntity
import com.raghvendra.foodyapp.data.database.entities.FoodJokeEntity
import com.raghvendra.foodyapp.data.database.entities.RecipesEntity
import com.raghvendra.foodyapp.models.FoodJoke
import com.raghvendra.foodyapp.models.FoodRecipe
import com.raghvendra.foodyapp.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
) :
    AndroidViewModel(application) {

    /** ROOM DATABASE */

    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavoriteRecipes: LiveData<List<FavoritesEntity>> =
        repository.local.readFavoriteRecipes().asLiveData()

    val readFoodJoke: LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipes(favoritesEntity)
        }

    fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodJoke(foodJokeEntity)
        }

    fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favoritesEntity)
        }

    fun deleteAllFavoriteRecipes() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteRecipes()
        }

    /** RETROFIT */
    private var _recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    val recipesResponse get() = _recipesResponse

    private var _searchedRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> =
        MutableLiveData()
    val searchedRecipesResponse get() = _searchedRecipesResponse

    private var _foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()
    val foodJokeResponse get() = _foodJokeResponse

    fun getFoodJoke(apiKey: String) = viewModelScope.launch {
        getFoodJokeSafeCall(apiKey)
    }

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {

        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        _searchedRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                _searchedRecipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                _searchedRecipesResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            _searchedRecipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }


    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        _recipesResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                _recipesResponse.value = handleFoodRecipesResponse(response)
                val foodRecipe = _recipesResponse.value!!.data
                if (foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }
            } catch (e: Exception) {

                _recipesResponse.value = NetworkResult.Error("Recipes not found")
            }

        } else {
            _recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }


    private suspend fun getFoodJokeSafeCall(apiKey: String) {
        _foodJokeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getFoodJoke(apiKey)
                _foodJokeResponse.value = handleFoodJokeResponse(response)
                val foodJoke = _foodJokeResponse.value!!.data
                if (foodJoke != null) {
                    offlineCacheFoodJoke(foodJoke)
                }
            } catch (e: Exception) {
                _foodJokeResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            _foodJokeResponse.value = NetworkResult.Error("No Internet Connection.")
        }

    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }


    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }

    }


    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                NetworkResult.Error("API Key Limited.")
            }
            response.isSuccessful -> {
                val foodJoke = response.body()
                NetworkResult.Success(foodJoke!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }

    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }
}