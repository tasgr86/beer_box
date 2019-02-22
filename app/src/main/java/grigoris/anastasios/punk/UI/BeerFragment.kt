package grigoris.anastasios.punk.UI

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_fragment.*
import android.support.v7.widget.RecyclerView
import android.widget.SearchView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.squareup.picasso.Picasso
import grigoris.anastasios.punk.Adapters.BeersAdapter
import grigoris.anastasios.punk.Model.TheBeer
import grigoris.anastasios.punk.R
import grigoris.anastasios.punk.RetrofitRepo
import kotlinx.android.synthetic.main.beer_sheet.*

class BeerFragment : Fragment() {

    private lateinit var adapter        : BeersAdapter
    private lateinit var sheet          : BottomSheetBehavior<ConstraintLayout>
    private lateinit var repo           : RetrofitRepo
    private var startLoading            = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.main_fragment, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startLoading = System.currentTimeMillis()

        showLoadingState()

        sheet = BottomSheetBehavior.from(beer_sheet)
        sheet.isHideable = true

        repo = RetrofitRepo()
        getBeers()

        setUpSearchWidget()

    }


    private fun loadAdapter(beers : ArrayList<TheBeer> ?){

        if (beers == null) {

            no_beers.visibility = View.VISIBLE

        } else {

            no_beers.visibility = View.GONE

            val lm = LinearLayoutManager(requireContext())
            adapter = BeersAdapter(requireContext(), beers, lm, rv, this)
            rv.layoutManager = lm
            rv.adapter = adapter

            rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    sheet.state = BottomSheetBehavior.STATE_HIDDEN;
                    undim()

                }

            })


            adapter.listener = {

                Picasso.get().load(it.image_url).into(beer_sheet_icon)
                dim()
                sheet.state = BottomSheetBehavior.STATE_EXPANDED;
                beer_sheet_title.text = it.name
                beer_sheet_tagline.text = it.tagline
                beer_sheet_desc.text = it.description

            }

        }

    }

    private fun dim (){

        main_fragment_content.alpha = 0.2F

    }

    private fun undim (){

        main_fragment_content.alpha = 1F

    }

    private fun showLoadingState(){

        Glide.with(context!!).load(R.drawable.loading).into(DrawableImageViewTarget(main_fragment_loading))
        main_fragment_loading.visibility = View.VISIBLE
        main_fragment_content.visibility = View.GONE

    }

    private fun showLoadedState(){

        main_fragment_loading.visibility = View.GONE
        main_fragment_content.visibility = View.VISIBLE

    }


    private fun setUpSearchWidget() {

        val searchManager = context!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        search_view.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
        search_view.maxWidth = Integer.MAX_VALUE

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {

                if (!query.isEmpty()) {

                    repo.searchBeers(query).observe(this@BeerFragment, Observer<ArrayList<TheBeer>>{

                        loadAdapter(it)

                    })

                } else {

                    getBeers()

                }

                return false
            }
        })

    }

    private fun getBeers(){

        repo.getBeers(1).observe(this, Observer<ArrayList<TheBeer>>{

            val diff = System.currentTimeMillis() - startLoading

            if (diff < 1000){

                Handler().postDelayed({

                    showLoadedState()
                    loadAdapter(it)


                }, 1300 - diff)

            }else{

                showLoadedState()
                loadAdapter(it)

            }

        })

    }

}