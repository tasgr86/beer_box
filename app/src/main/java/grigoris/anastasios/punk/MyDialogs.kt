package grigoris.anastasios.punk

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.my_dialog.view.*

class MyDialogs : IMyDialogs{

    override fun showMessage(context : Context, message : String){

        val myView = LayoutInflater.from(context).inflate(R.layout.my_dialog, null)
        myView.ad_message.text = message

        AlertDialog.Builder(context).setNegativeButton(context.getString(R.string.close)) { dialogInterface: DialogInterface, i: Int ->
            (context as Activity).finish()
        }.setView(myView).create().show()

    }

}