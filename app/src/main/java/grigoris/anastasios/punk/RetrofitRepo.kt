package grigoris.anastasios.punk

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import grigoris.anastasios.punk.Model.TheBeer

class RetrofitRepo(_apiServices: RetrofitAPI = RetrofitClient.createClient().create(RetrofitAPI::class.java)) : IRetrofitRepo {

    val apiServices = _apiServices

    override fun searchBears(name: String, page: Int): LiveData<ArrayList<TheBeer>> {

        val beers = MutableLiveData<ArrayList<TheBeer>>()
        val call = apiServices.searchBeer(name, page)

        call.enqueue(object : Callback<ArrayList<TheBeer>> {
            override fun onResponse(call: Call<ArrayList<TheBeer>>, response: Response<ArrayList<TheBeer>>) {

                MyLog().showLog("QUERY SEARCH URL", response.raw().request().url().toString())

                if (response.isSuccessful)
                    beers.value = response.body()

            }

            override fun onFailure(call: Call<ArrayList<TheBeer>>?, t: Throwable?) {

                beers.value = null

            }
        })

        return beers

    }

    override fun showBear(beerID: Int): LiveData<ArrayList<TheBeer>> {

        val beers = MutableLiveData<ArrayList<TheBeer>>()
        val call = apiServices.showBeer(beerID)

        call.enqueue(object : Callback<ArrayList<TheBeer>> {
            override fun onResponse(call: Call<ArrayList<TheBeer>>, response: Response<ArrayList<TheBeer>>) {

                MyLog().showLog("SHOW BEER SEARCH URL", response.raw().request().url().toString())

                if (response.isSuccessful)
                    beers.value = response.body()

            }

            override fun onFailure(call: Call<ArrayList<TheBeer>>?, t: Throwable?) {

                beers.value = null

            }
        })

        return beers

    }

    override fun getBears(page: Int): LiveData<ArrayList<TheBeer>> {

        val beers = MutableLiveData<ArrayList<TheBeer>>()
        val call = apiServices.getBeers(page)

        call.enqueue(object : Callback<ArrayList<TheBeer>> {
            override fun onResponse(call: Call<ArrayList<TheBeer>>, response: Response<ArrayList<TheBeer>>) {

                MyLog().showLog("BEERS SEARCH URL", response.raw().request().url().toString())

                if (response.isSuccessful)
                    beers.value = response.body()

            }

            override fun onFailure(call: Call<ArrayList<TheBeer>>?, t: Throwable?) {

                beers.value = null

            }
        })

        return beers

    }

}