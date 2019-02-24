package grigoris.anastasios.punk

import grigoris.anastasios.punk.Model.TheBeer
import retrofit2.Call
import retrofit2.http.*

interface RetrofitAPI {

    @GET("/v2/beers/")
    fun getBeers(@Query("page") page : Int): Call<ArrayList<TheBeer>>

    @GET("/v2/beers/")
    fun searchBeer(@Query("beer_name") name : String, @Query("page") page: Int): Call<ArrayList<TheBeer>>

    @GET("/v2/beers/{beerID}")
    fun showBeer(@Path("beerID") beerID : Int): Call<ArrayList<TheBeer>>


}