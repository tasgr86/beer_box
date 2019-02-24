package grigoris.anastasios.punk.Dagger

import android.support.v7.app.AppCompatActivity
import dagger.Component
import grigoris.anastasios.punk.Adapters.BeersAdapter
import grigoris.anastasios.punk.UI.BeerFragment
import grigoris.anastasios.punk.UI.MainActivity
import grigoris.anastasios.punk.UI.ShowBeer
import grigoris.anastasios.punk.UI.SplashScreen
import javax.inject.Singleton

@Singleton
@Component(modules = [MyModule::class])
interface MyComponent {

    fun injectShowRecipe(activity: ShowBeer)

    fun injectBeerFragment(fr: BeerFragment)

    fun injectSplash(activity: SplashScreen)

    fun injectMainActivity(activity: MainActivity)


}