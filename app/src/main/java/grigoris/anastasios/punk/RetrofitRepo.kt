package grigoris.anastasios.punk

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import grigoris.anastasios.punk.Model.TheBeer

class RetrofitRepo(_apiServices: RetrofitAPI = RetrofitClient.createClient().create(RetrofitAPI::class.java)) {

    val apiServices = _apiServices

    fun getBeers(page : Int): LiveData<ArrayList<TheBeer>> {

        val beers = MutableLiveData<ArrayList<TheBeer>>()
        val call = apiServices.getBeers(page)

        call.enqueue(object : Callback<ArrayList<TheBeer>> {
            override fun onResponse(call: Call<ArrayList<TheBeer>>, response: Response<ArrayList<TheBeer>>) {

                if (response.isSuccessful)
                    beers.value = response.body()

                for(beer in beers.value!!){

                    println("BEER: ".plus(beer.id).plus(" ").plus(beer.name).plus(" ").plus(beer.first_brewed))


                }

            }

            override fun onFailure(call: Call<ArrayList<TheBeer>>?, t: Throwable?) {

                println("Get Beers Failure ".plus(t.toString()))
                beers.value = null

            }
        })

        return beers

    }




    fun searchBeers(name : String): LiveData<ArrayList<TheBeer>> {

        val beers = MutableLiveData<ArrayList<TheBeer>>()
        val call = apiServices.searchBeer(name)

        call.enqueue(object : Callback<ArrayList<TheBeer>> {
            override fun onResponse(call: Call<ArrayList<TheBeer>>, response: Response<ArrayList<TheBeer>>) {

                if (response.isSuccessful)
                    beers.value = response.body()

            }

            override fun onFailure(call: Call<ArrayList<TheBeer>>?, t: Throwable?) {

                println("Get Beers Failure ".plus(t.toString()))
                beers.value = null

            }
        })

        return beers

    }




    fun showBeer(beerID : Int): LiveData<ArrayList<TheBeer>> {

        val beers = MutableLiveData<ArrayList<TheBeer>>()
        val call = apiServices.showBeer(beerID)

        call.enqueue(object : Callback<ArrayList<TheBeer>> {
            override fun onResponse(call: Call<ArrayList<TheBeer>>, response: Response<ArrayList<TheBeer>>) {

                if (response.isSuccessful)
                    beers.value = response.body()

                println("URLLLL: ".plus(response.raw().request().url()))

                for(beer in beers.value!!){

                    println("BEER: ".plus(beer.id).plus(" ").plus(beer.name).plus(" ").plus(beer.first_brewed))

                }
            }

            override fun onFailure(call: Call<ArrayList<TheBeer>>?, t: Throwable?) {

                println("Get Beers Failure ".plus(t.toString()))
                beers.value = null

            }
        })

        return beers

    }

}