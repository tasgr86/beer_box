package grigoris.anastasios.punk

import android.util.Log

class MyLog{

    companion object {

        val isLogEnabled = false

    }

    fun showLog(tag : String, message : String){

        if (!isLogEnabled)
            return

        Log.d(tag, message)

    }

}