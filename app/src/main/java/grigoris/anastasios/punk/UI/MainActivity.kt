package grigoris.anastasios.punk.UI

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import grigoris.anastasios.punk.*
import grigoris.anastasios.punk.Model.TheBeer
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var dialogs : IMyDialogs

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyApplication.getComponent().injectMainActivity(this@MainActivity)

        val beers = intent.getParcelableArrayListExtra<TheBeer>("beers")

        // Using a fragment is probably redundant for the project requirements.
        // Still i will a use fragment for demonstration purposes as well as for the possibility
        // of adding in a future update a NavigationView menu


        if (beers == null)
            dialogs.showMessage(this, getString(R.string.beers_unavailable))

        else{

            val fr = BeerFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("beers", beers)
            fr.arguments = bundle
            loadFragment(fr)

        }

    }

    private fun loadFragment(fragment: Fragment) {

        val fManager = supportFragmentManager
        val mFragmentTransaction = fManager.beginTransaction()
        mFragmentTransaction.replace(main_frame.id, fragment).commit()

    }

}
