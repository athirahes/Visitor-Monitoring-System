package my.edu.utem.ftmk.workshop2.visitor.monitoring.system.v1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import my.edu.utem.ftmk.workshop2.visitor.monitoring.system.v1.interfaces.DialogCallback;
import my.edu.utem.ftmk.workshop2.visitor.monitoring.system.v1.utils.GlobalUtils;

public class Booking extends AppCompatActivity implements QueueDialog.QueueDialogInterFace {

    private static final String TAG = "PQ";
    private String pLocation, pMaxVisitor_s, pPosition_s, pTrackVisitor_s;
    int pMaxVisitor, pTrackVisitor, pPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // AISAH [START]
        if (Build.VERSION.SDK_INT >= 21) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));
        }
        // AISAH [END]


        String pName = getIntent().getStringExtra("name");
        String pType = getIntent().getStringExtra("type");
        String pDesc = getIntent().getStringExtra("desc");
        pLocation = getIntent().getStringExtra("location");
        pMaxVisitor_s = getIntent().getStringExtra("maxVisitor");
        pTrackVisitor_s = getIntent().getStringExtra("trackVisitor");
        pPosition_s = getIntent().getStringExtra("position");

        pMaxVisitor = Integer.parseInt(pMaxVisitor_s);
        pTrackVisitor = Integer.parseInt(pTrackVisitor_s);
        pPosition = Integer.parseInt(pPosition_s);
        Log.d(TAG, "Booking.onCreate.Vector(i=" + pPosition + "): " + pName + " [" + pTrackVisitor_s + "/" + pMaxVisitor_s + "]");

        TextView premiseName = findViewById(R.id.premiseName_lbl2);
        TextView premiseType = findViewById(R.id.premiseType_lbl2);
        TextView premiseDescription = findViewById(R.id.premiseDescription_lbl);
        Button btnNavigation = findViewById(R.id.navigate_btn);
        Button btnQueue = findViewById(R.id.queue_btn);

        premiseName.setText(pName);
        premiseType.setText(pType);
        premiseDescription.setText(pDesc);
        Log.d(TAG, "Booking.LaunchBooking.pLocation: " + pLocation);

        if (pLocation.equals("null"))
            btnNavigation.setVisibility(View.INVISIBLE);

        // condition for the Queue Button
        // if premise is tracked
        if (pTrackVisitor != 0) {
            String btnStop = "PLAN TO VISIT: " + pTrackVisitor_s + " VISITOR(S)";
            btnQueue.setText(btnStop);
            btnQueue.setTextColor(Color.parseColor("#A93226"));
            btnQueue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LaunchStopTrackingDialog(v);
                }
            });
            // if premise is NOT tracked
        } else {
            btnQueue.setText(R.string.queue_to_visit);
            btnQueue.setTextColor(Color.parseColor("#6176FF"));
            btnQueue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LaunchQueueDialog();
                }
            });
        }
    }

    public void LaunchQueueDialog() {
        Log.d(TAG, "Booking.LaunchQueueDialog");
        QueueDialog queueDialog = new QueueDialog();

        Log.d(TAG, "Booking.LaunchQueueDialog.queueDialog");

        Bundle args = new Bundle();
        args.putString("maxVisitor_s", pMaxVisitor_s);
        args.putInt("position", pPosition);
        queueDialog.setArguments(args);

        Log.d(TAG, "Booking.LaunchQueueDialog.queueDialog.show(.... , ...)");
        queueDialog.show(getSupportFragmentManager(), "Queue Dialog");
    }

    public void LaunchStopTrackingDialog(View itemView) {
        Log.d(TAG, "Booking.LaunchStopTrackingDialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        builder.setTitle("Confirm to stop tracking?");
        builder.setMessage("If you have safely arrived to this premise, do rate our service by clicking Rate. Click Confirm to stop tracking this premise visitor, Cancel to cancel action.");

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    // if click btn Confirm
                    case DialogInterface.BUTTON_POSITIVE:
                        Log.d(TAG, "Booking.LaunchStopTrackingDialog.DialogInterface.BUTTON_POSITIVE");
                        // set trackQInput = 0
                        pTrackVisitor = 0;
                        launchMainActivity(String.valueOf(pTrackVisitor));
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        Log.d(TAG, "Booking.LaunchStopTrackingDialog.DialogInterface.BUTTON_NEGATIVE");
                        break;
                    case DialogInterface.BUTTON_NEUTRAL:
                        showDialog();
                        break;
                }
            }
        };

        builder.setPositiveButton("Confirm", dialogClickListener);
        builder.setNegativeButton("Cancel", dialogClickListener);
        builder.setNeutralButton("Rate", dialogClickListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void OpenPremiseLocation(View view) {
        Toast toast = Toast.makeText(this, "No Location available for navigation", Toast.LENGTH_SHORT);
        String url = pLocation;
        /*Uri webpage = Uri.parse(url);*/
        Uri webpage = Uri.parse("google.navigation:q=" + Uri.parse(url));
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        String contentt = String.valueOf(intent.resolveActivity(getPackageManager()));
        Log.d(TAG, "Booking.OpenPremiseLocation.contentt: " + contentt);

        if (intent.resolveActivity(getPackageManager()) != null) {
            Log.d(TAG, "Booking.OpenPremiseLocation.getPackageManager(): " + getPackageManager());
            startActivity(intent);
        } else {
            toast.show();
            Log.d(TAG, "Booking.OpenPremiseLocation.getPackageManager(): NULL");
        }
    }

    // AISAH [START]
    public void showDialog() {
        //create and show the dialog
        GlobalUtils.showDialog(this, new DialogCallback() {
            @Override
            public void callback(int rating) {
                String result = String.valueOf(rating);
                Log.d(TAG, "The result " + result);
                pTrackVisitor = 0;
                launchMainActivity(String.valueOf(pTrackVisitor));
            }
        });
    }
    // AISAH [END]

    @Override
    public void applyTexts(String text_eQueueInput) {
        String toastMessage = text_eQueueInput + " person will be tracked.";
        Toast toast = Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT);
        toast.show();
        Log.d(TAG, "Booking.applyTexts.text_eQueueInput: " + text_eQueueInput);

        launchMainActivity(text_eQueueInput);
    }

    private void launchMainActivity(String QueueInput) {
        pTrackVisitor_s = QueueInput;

        Intent mainIntent = new Intent();
        mainIntent.putExtra("trackVisitor", pTrackVisitor_s);
        mainIntent.putExtra("position", pPosition_s);

        Log.d(TAG, "Booking.launchMainActivity.pPosition_s: " + pPosition_s + ", pTrackVisitor_s: " + pTrackVisitor_s);

        setResult(RESULT_OK, mainIntent);
        finish();
        Log.d(TAG, "Booking.launchMainActivity");
    }
}