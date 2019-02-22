package grigoris.anastasios.punk.UI

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.squareup.picasso.Picasso
import grigoris.anastasios.punk.Adapters.IngredientsAdapter
import grigoris.anastasios.punk.Model.TheBeer
import grigoris.anastasios.punk.R
import grigoris.anastasios.punk.RetrofitRepo
import kotlinx.android.synthetic.main.show_beer.*

class ShowBeer : AppCompatActivity(){

    private lateinit var theBeer        : TheBeer
    private var startLoading            = 0L

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_beer)

        startLoading = System.currentTimeMillis()
        showLoadingState()

        show_beer_toolbar.title = ""
        setSupportActionBar(show_beer_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val beerID = intent.getIntExtra("beer_id", 0)

        RetrofitRepo().showBeer(beerID)
            .observe(this, Observer<ArrayList<TheBeer>>{

                val diff = System.currentTimeMillis() - startLoading

                if (diff < 1000){

                    Handler().postDelayed({

                        theBeer = it!![0]
                        setUpViews()

                    }, 1300 - diff)


                }else{

                    theBeer = it!![0]
                    setUpViews()

                }

        })

    }

    private fun showLoadingState(){

        Glide.with(applicationContext).load(R.drawable.loading).into(DrawableImageViewTarget(show_beer_loading))
        show_beer_loading.visibility = View.VISIBLE
        show_beer_content.visibility = View.GONE

    }

    private fun showLoadedState(){

        show_beer_loading.visibility = View.GONE
        show_beer_content.visibility = View.VISIBLE

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        onBackPressed()

        return super.onOptionsItemSelected(item)

    }


    private fun setUpViews(){

        showLoadedState()

        Picasso.get().load(theBeer.image_url).into(show_beer_img)

        show_beer_toolbar.title             = theBeer.name
        show_beer_tagline.text              = theBeer.tagline
        show_beer_desc.text                 = theBeer.description
        show_beer_brewed.text               = getString(R.string.first_brewed).plus(" ").plus(theBeer.first_brewed)
        show_beer_tips.text                 = getString(R.string.tips).plus(" ").plus(theBeer.brewers_tips)
        show_beer_ingredients_label.text    = getString(R.string.ingredients)

        loadIngredientsMalt()
        loadIngredientsHops()

    }


    private fun loadIngredientsMalt(){

        val lm = LinearLayoutManager(applicationContext)
        val adapter = IngredientsAdapter(applicationContext, theBeer, 1)
        show_beer_ingredients_malt_rv.layoutManager = lm
        show_beer_ingredients_malt_rv.adapter = adapter
        show_beer_ingredients_hops_rv.isNestedScrollingEnabled = false

    }

    private fun loadIngredientsHops(){

        val lm = LinearLayoutManager(applicationContext)
        val adapter = IngredientsAdapter(applicationContext, theBeer, 0)
        show_beer_ingredients_hops_rv.layoutManager = lm
        show_beer_ingredients_hops_rv.adapter = adapter
        show_beer_ingredients_hops_rv.isNestedScrollingEnabled = false

    }

}