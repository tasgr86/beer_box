package grigoris.anastasios.punk.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import grigoris.anastasios.punk.Model.TheBeer
import grigoris.anastasios.punk.R
import kotlinx.android.synthetic.main.ingredient_header.view.*
import kotlinx.android.synthetic.main.ingredient_item.view.*

class IngredientsAdapter(val context : Context, val beer : TheBeer, val type : Int)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER                         = 0
    private val TYPE_ITEM                           = 1

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == TYPE_HEADER)
            HeaderHolder(LayoutInflater.from(context).inflate(R.layout.ingredient_header, p0, false))
        else
            ItemHolder(LayoutInflater.from(context).inflate(R.layout.ingredient_item, p0, false))

    }

    override fun getItemCount(): Int {

        if (type == 0)
            return beer.ingredients.hops.size + 1
        else
            return beer.ingredients.malt.size + 1

    }

    override fun getItemViewType(position: Int): Int {

        return if (position == 0) TYPE_HEADER else TYPE_ITEM

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val ingredient = beer.ingredients

        if (type == 0){

            if (position == TYPE_HEADER)
                (holder as HeaderHolder).name.text = context.getString(
                    R.string.hops
                )
            else {

                (holder as ItemHolder).name.text = ingredient.hops[position - 1].amount.value.toString().plus(" ")
                    .plus(ingredient.hops[position - 1].amount.unit).plus(" ").plus(ingredient.hops[position - 1].name)
            }
        }else{

            if (position == TYPE_HEADER)
                (holder as HeaderHolder).name.text = context.getString(
                    R.string.malt
                )
            else {

                (holder as ItemHolder).name.text = ingredient.malt[position - 1].amount.value.toString().plus(" ")
                    .plus(ingredient.malt[position - 1].amount.unit).plus(" ").plus(ingredient.malt[position - 1].name)

            }

        }

    }

    inner class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.ingredient_header_title

    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.ingredient_item_title

    }

}