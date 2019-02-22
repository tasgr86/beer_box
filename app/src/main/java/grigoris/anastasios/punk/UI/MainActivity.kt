package grigoris.anastasios.punk.UI

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import grigoris.anastasios.punk.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Using a fragment is probably redundant for the project requirements.
        // Still i will a use fragment for demonstration purposes as well as for the possibility
        // of adding in a future update a NavigationView menu

        loadFragment(BeerFragment())

    }

    private fun loadFragment(fragment: Fragment) {

        val fManager = supportFragmentManager
        val mFragmentTransaction = fManager.beginTransaction()
        mFragmentTransaction.replace(main_frame.id, fragment).commit()

    }


}
