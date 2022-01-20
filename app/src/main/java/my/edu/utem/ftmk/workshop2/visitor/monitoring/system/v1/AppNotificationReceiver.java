// ATHIRAH'S MODULE
package my.edu.utem.ftmk.workshop2.visitor.monitoring.system.v1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class AppNotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "PQ";

    @Override
    public void onReceive(Context context, Intent intent) {
        String locationURL = intent.getStringExtra("pLocationURL");
        Log.d(TAG, "onReceive.locationURL: " + locationURL);

        Uri webpage = Uri.parse(locationURL);
        Log.d(TAG, "onReceive.webpage: " + webpage);
        Intent i = new Intent(Intent.ACTION_VIEW, webpage);
        context.startActivity(i);
    }
}
