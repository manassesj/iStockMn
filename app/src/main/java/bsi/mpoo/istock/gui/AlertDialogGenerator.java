package bsi.mpoo.istock.gui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import bsi.mpoo.istock.R;
import bsi.mpoo.istock.services.Constants;

public class AlertDialogGenerator {
    private Activity activity;
    private String message;
    private boolean closeAfter;

    public AlertDialogGenerator(Activity activity, String message, boolean closeAfter) {
        this.activity = activity;
        this.message = message;
        this.closeAfter = closeAfter;
    }

    public void invoke() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setPositiveButton(activity.getString(R.string.okay), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (closeAfter){
                    activity.finish();
                }
            }
        });
        builder.show();
    }

    public void invokeShare(final String extraMessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(message);
        builder.setMessage(extraMessage);
        builder.setPositiveButton(activity.getString(R.string.share), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (closeAfter){
                    activity.finish();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, extraMessage);
                    intent.setType(Constants.Activity.TEXT_PLAIN);
                    activity.startActivity(intent);

                }
            }
        });
        builder.setNegativeButton(activity.getString(R.string.okay), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (closeAfter){
                    activity.finish();
                }
            }
        });
        builder.show();
    }
}
