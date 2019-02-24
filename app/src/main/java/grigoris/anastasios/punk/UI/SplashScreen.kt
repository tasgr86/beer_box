package grigoris.anastasios.punk.UI

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import grigoris.anastasios.punk.IRetrofitRepo
import grigoris.anastasios.punk.Model.TheBeer
import grigoris.anastasios.punk.MyApplication
import grigoris.anastasios.punk.R
import grigoris.anastasios.punk.RetrofitRepo
import kotlinx.android.synthetic.main.splash_screen.*
import javax.inject.Inject

class SplashScreen : AppCompatActivity(){

    private var startLoading            = 0L
    @Inject lateinit var retrofitRepo   : IRetrofitRepo

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        MyApplication.getComponent().injectSplash(this@SplashScreen)

        Glide.with(applicationContext).load(R.drawable.loading).into(DrawableImageViewTarget(splash_img))

        startLoading = System.currentTimeMillis()

        getBeers()

    }

    private fun getBeers(){

        retrofitRepo.getBears(1).observe(this, Observer<ArrayList<TheBeer>>{

            val diff = System.currentTimeMillis() - startLoading

            if (diff < 1000){

                Handler().postDelayed({

                    startMainActivity(it)

                }, 1000 - diff)

            }else{

                startMainActivity(it)

            }

        })

    }

    private fun startMainActivity(beers : ArrayList<TheBeer>?){

        val intent = Intent(this, MainActivity::class.java).apply {

            putParcelableArrayListExtra("beers", beers)

        }

        startActivity(intent)
        finish()

    }

}