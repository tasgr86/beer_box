package grigoris.anastasios.punk

import android.app.Application
import grigoris.anastasios.punk.Dagger.DaggerMyComponent
import grigoris.anastasios.punk.Dagger.MyComponent
import grigoris.anastasios.punk.Dagger.MyModule

class MyApplication : Application() {

    companion object {

        private var component: MyComponent? = null

        fun getComponent(): MyComponent { return component!! }

    }

    override fun onCreate() {
        super.onCreate()

        component = createMyComponent()

    }

    private fun createMyComponent(): MyComponent {

        return DaggerMyComponent
            .builder()
            .myModule(MyModule(baseContext))
            .build()

    }
}