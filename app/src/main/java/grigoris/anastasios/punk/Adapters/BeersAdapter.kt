package grigoris.anastasios.punk.Adapters

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Context;
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar
import android.widget.SearchView
import com.squareup.picasso.Picasso;
import grigoris.anastasios.punk.*
import grigoris.anastasios.punk.Model.TheBeer
import grigoris.anastasios.punk.UI.BeerFragment
import grigoris.anastasios.punk.UI.ShowBeer
import kotlinx.android.synthetic.main.beer_row.view.*

class BeersAdapter (_context: Context, _array: ArrayList<TheBeer>, llm: LinearLayoutManager,
                    val rv : RecyclerView, val lifecycleOwner : LifecycleOwner, val repo : IRetrofitRepo, val dialogs : IMyDialogs)
                    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_ITEM                           = 0
    private val TYPE_LOADING                        = 1
    private var isLoading                           = false
    var array                                       = _array
    val context                                     = _context
    var listener : ((TheBeer) -> Unit)?             = null
    var nextPage                                    = 2
    private var hasMorePages                        = true

    init {

        rv.addOnScrollListener(MyScroll(llm))
        hasMorePages()

    }

    private fun hasMorePages(){

        // Check whether more beers are available for download

        hasMorePages = array.size % 25 == 0

        MyLog().showLog("SHOULD SCROLL UPDATE", hasMorePages.toString().plus(" ").plus(array.size))

    }


    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == TYPE_ITEM)
            Holder(LayoutInflater.from(context).inflate(R.layout.beer_row, p0, false))
        else
            LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.loading_bar, p0, false))

    }

    override fun getItemCount(): Int {
        return array.size + 1
    }

    fun resetNextPage(){

        nextPage = 2

    }


    fun updateAdapter(movies: ArrayList<TheBeer>) {

        this.array = movies
        hasMorePages()
        notifyDataSetChanged()

    }

    private fun startLoading(bar: ProgressBar) {

        isLoading = true
        bar.visibility = View.VISIBLE

    }

    private fun finishLoading(bar: ProgressBar) {

        isLoading = false
        bar.visibility = View.GONE

    }

    override fun getItemViewType(position: Int): Int {

        return if (array.size == position) TYPE_LOADING else TYPE_ITEM

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {

        if (holder is Holder) {

            holder.title.text = array[pos].name
            holder.tagline.text = array[pos].tagline
            holder.description.text = array[pos].description
            Picasso.get().load(array[pos].image_url).into(holder.image)

            if (pos == array.size - 1)
                holder.divider.visibility = View.GONE
            else
                holder.divider.visibility = View.VISIBLE
        }

    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title = itemView.beer_row_title
        val tagline = itemView.beer_row_tagline
        val description = itemView.beer_row_description
        val image = itemView.beer_row_icon
        val divider = itemView.beer_row_divider
        val more_info = itemView.beer_row_more

        init {

            more_info.setOnClickListener {

                listener?.invoke(array[layoutPosition])

            }

            itemView.setOnClickListener {

                val intent = Intent(context, ShowBeer::class.java).apply {

                    putExtra("beer_id", array[layoutPosition].id)

                }
                context.startActivity(intent)

            }

        }

    }


    inner class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var progressBar: ProgressBar = view.findViewById(R.id.progress_bar)

        init {

            progressBar.isIndeterminate = true
            progressBar.visibility = View.GONE

        }
    }


    private inner class MyScroll internal constructor(internal var llm: LinearLayoutManager) :

        RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val lastViewedItem = llm.findLastCompletelyVisibleItemPosition()

            if (lastViewedItem == array.size && !isLoading && hasMorePages) {

                val holder = rv.findViewHolderForAdapterPosition(lastViewedItem)
                val pBar = (holder as LoadingViewHolder).progressBar

                startLoading(pBar)

                if (BeerFragment.type == 0) {

                    repo.getBears(nextPage).observe(lifecycleOwner, Observer<ArrayList<TheBeer>> {

                        updateOnScrollFinish(it, pBar)

                    })

                }else{

                    val query = ((context as Activity).findViewById(R.id.search_view) as SearchView).query.toString()

                    repo.searchBears(query, nextPage).observe(lifecycleOwner, Observer<ArrayList<TheBeer>>{

                        updateOnScrollFinish(it, pBar)

                    })

                }

            }
        }


        private fun updateOnScrollFinish(beers : ArrayList<TheBeer>?, pBar : ProgressBar){

            if (beers == null)
                dialogs.showMessage(context, context.getString(R.string.beers_unavailable))

            else{

                array.addAll(beers)
                updateAdapter(array)
                finishLoading(pBar)
                nextPage++

            }

        }

    }
}