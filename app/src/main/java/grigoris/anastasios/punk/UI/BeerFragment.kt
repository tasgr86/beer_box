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
import com.squareup.picasso.Picasso
import grigoris.anastasios.punk.*
import grigoris.anastasios.punk.Adapters.BeersAdapter
import grigoris.anastasios.punk.Model.TheBeer
import kotlinx.android.synthetic.main.beer_sheet.*
import javax.inject.Inject

class BeerFragment : Fragment() {

    private lateinit var adapter        : BeersAdapter
    private lateinit var sheet          : BottomSheetBehavior<ConstraintLayout>
    @Inject lateinit var repo           : IRetrofitRepo
    @Inject lateinit var dialogs           : IMyDialogs
    private var startLoading            = 0L
    private lateinit var beers          : ArrayList<TheBeer>

    companion object {

        // Type becomes 0 when downloading more pages with beers and 1 when searching beers by name

        var type = 0

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.main_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MyApplication.getComponent().injectBeerFragment(this@BeerFragment)

        beers = arguments!!.getParcelableArrayList<TheBeer>("beers")!!

        sheet = BottomSheetBehavior.from(beer_sheet)
        sheet.isHideable = true

        loadAdapter(beers)
        setUpSearchWidget()

    }


    private fun loadAdapter(beers : ArrayList<TheBeer> ?){

        if (beers == null) {

            no_beers.visibility = View.VISIBLE

        } else {

            no_beers.visibility = View.GONE

            val lm = LinearLayoutManager(requireContext())
            adapter = BeersAdapter(requireContext(), beers, lm, rv, this, repo, dialogs)
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


    private fun setUpSearchWidget() {

        val searchManager = context!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        search_view.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
        search_view.maxWidth = Integer.MAX_VALUE

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {

                adapter.resetNextPage()

                if (!query.isEmpty()) {

                    type = 1

                    repo.searchBears(query, 1).observe(this@BeerFragment, Observer<ArrayList<TheBeer>>{

                       onBeersFetched(it)

                    })

                } else {

                    type = 0
                    getBeers()

                }

                return false
            }
        })

    }

    private fun onBeersFetched(newBeers : ArrayList<TheBeer>?){

        if (newBeers == null)
            dialogs.showMessage(requireContext(), getString(R.string.beers_unavailable))
        else{

            beers = newBeers
            adapter.updateAdapter(beers)

        }


    }

    private fun getBeers(){

        repo.getBears(1).observe(this, Observer<ArrayList<TheBeer>>{

            val diff = System.currentTimeMillis() - startLoading

            if (diff < 1000){

                Handler().postDelayed({

                    onBeersFetched(it)

                }, 1000 - diff)

            }else{

                onBeersFetched(it)
            }

        })
    }
}