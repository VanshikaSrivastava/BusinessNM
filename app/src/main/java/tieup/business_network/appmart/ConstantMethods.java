package tieup.business_network.appmart;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;


public class ConstantMethods {

private static AlertDialog alert;


public final static boolean showAlertDialog(Context context, String title, String message,
                                            Boolean status) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle(title);
    builder.setMessage(message);

    if (status != null) {

        builder.setTitle(title)
                .setMessage(message)
                .setNegativeButton("Ok..", null)
                .create()
                .show();
    }
return false;
}
    public static void showOKMessageandBack(String title, String message, final Context context, final Class activityClass, Boolean status) {
        AlertDialog.Builder builder;  builder = new AlertDialog.Builder(context);
        //  if (message.length() <= 0)
        builder.setMessage(message);
        builder.setTitle(title);

        if(status != null)

       //     builder.setIcon((status) ? R.drawable.sucess : R.mipmap.inv);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alert.hide();
                Intent intent = new Intent(context, activityClass);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        alert = builder.create();
        alert.show();
    }
}
