package grigoris.anastasios.punk

import android.arch.lifecycle.LiveData
import grigoris.anastasios.punk.Model.TheBeer

interface IRetrofitRepo{

    fun getBears(page : Int) : LiveData<ArrayList<TheBeer>>

    fun searchBears(name : String, page: Int) : LiveData<ArrayList<TheBeer>>

    fun showBear(beerID : Int) : LiveData<ArrayList<TheBeer>>

}