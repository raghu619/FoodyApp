package com.raghvendra.foodyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.raghvendra.foodyapp.R
import com.raghvendra.foodyapp.adapters.PagerAdapter
import com.raghvendra.foodyapp.ui.fragments.ingredients.IngredientsFragment
import com.raghvendra.foodyapp.ui.fragments.instructions.InstructionsFragment
import com.raghvendra.foodyapp.ui.fragments.overview.OverViewFragment
import com.raghvendra.foodyapp.util.Constants
import com.raghvendra.foodyapp.util.Constants.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
   private val args by navArgs<DetailsActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverViewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY,args.result)

        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager
        )
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}