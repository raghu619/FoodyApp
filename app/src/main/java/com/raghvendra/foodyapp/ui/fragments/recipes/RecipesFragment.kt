package com.raghvendra.foodyapp.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.raghvendra.foodyapp.viewmodels.MainViewModel
import com.raghvendra.foodyapp.R
import com.raghvendra.foodyapp.adapters.RecipesAdapter
import com.raghvendra.foodyapp.util.Constants.Companion.API_KEY
import com.raghvendra.foodyapp.util.NetworkResult
import com.raghvendra.foodyapp.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*


@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private lateinit var mView: View
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel :RecipesViewModel
    private val mAdapter by lazy {
        RecipesAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel =ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(R.layout.fragment_recipes, container, false)

        setupRecyclerView()
        requestApiData()
        return mView
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner,{ response ->
            when(response){
                is NetworkResult.Success ->{
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error ->{
                    hideShimmerEffect()
                    Toast.makeText(requireContext(),response.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }



    private fun setupRecyclerView() {
        mView.recyclerview.adapter = mAdapter
        mView.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        mView.recyclerview.showShimmer()
    }

    private fun hideShimmerEffect() {
        mView.recyclerview.hideShimmer()
    }


}