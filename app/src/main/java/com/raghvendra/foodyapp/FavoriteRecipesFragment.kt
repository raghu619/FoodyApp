package com.raghvendra.foodyapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_recipes.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteRecipesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteRecipesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

         val view =  inflater.inflate(R.layout.fragment_favorite_recipes, container, false)

        return view
    }


}