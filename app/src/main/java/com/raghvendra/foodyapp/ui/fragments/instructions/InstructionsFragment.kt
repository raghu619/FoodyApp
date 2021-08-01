package com.raghvendra.foodyapp.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.raghvendra.foodyapp.R
import com.raghvendra.foodyapp.models.Result
import com.raghvendra.foodyapp.util.Constants
import kotlinx.android.synthetic.main.fragment_instructions.view.*

class InstructionsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_instructions,container,false)
        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)
        view.instructions_webView.webViewClient = object :WebViewClient(){}
        val websiteUrl :String = myBundle!!.sourceUrl
        view.instructions_webView.loadUrl(websiteUrl)

        return view
    }
}