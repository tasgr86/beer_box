package grigoris.anastasios.punk.Dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import grigoris.anastasios.punk.*

@Module
class MyModule (private val context: Context) {

    @Provides
    internal fun provideRetrofitRepo(): IRetrofitRepo {

        return RetrofitRepo()

    }

    @Provides
    internal fun provideMyDialogs(): IMyDialogs {

        return MyDialogs()

    }

}
