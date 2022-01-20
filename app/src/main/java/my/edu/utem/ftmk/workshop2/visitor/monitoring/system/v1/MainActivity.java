package my.edu.utem.ftmk.workshop2.visitor.monitoring.system.v1;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Vector<Premise>> {

    private static final String TAG = "PQ";
    public static final int REQUEST = 275;
    private Vector<Premise> mPremiseV;

    private NotificationManagerCompat notificationManager;
    private LoaderManager loaderManager;
    private DrawerLayout drawerLayout;
    private RecyclerView rcvPremises;
    private EditText premiseSearch;

    private ProgressBar progressBar;

    String pTrackVisitor_s, pPosition_s, pPremiseName, pLocation;
    int pTrackVisitor, pPosition, maxCapacity, currentCapacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity.onCreate");

        drawerLayout = findViewById(R.id.drawer_layout);
        loaderManager = LoaderManager.getInstance(this);
        premiseSearch = findViewById(R.id.search);
        rcvPremises = findViewById(R.id.rcvPremise);
        progressBar = findViewById(R.id.progressBar);
        notificationManager = NotificationManagerCompat.from(this);

        // AISAH [START]
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.light_blue));
        }
        // AISAH [END]

        RequestLocationPermission();
        ConnectivityCheck();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
        Log.d(TAG, "MainActivity.onPause: close drawer");
    }

    @NonNull
    @Override
    public Loader<Vector<Premise>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(TAG, "MainActivity.Loader<Vector<Premise>> ");
        return new PremisesListTaskLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Vector<Premise>> loader, Vector<Premise> vPremise) {
        loaderManager.destroyLoader(0);

        mPremiseV = PrefConfig.readListFromPref_mPremiseV(this);
        rcvPremises.setAdapter(new PremiseAdapter(this, mPremiseV));
        if (mPremiseV == null)
            this.mPremiseV = vPremise;

        Log.d(TAG, "MainActivity.onLoadFinished.pPosition_s: " + pPosition_s + ", pTrackVisitor_s: " + pTrackVisitor_s);

        if (!vPremise.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            Log.d(TAG, "MainActivity.onLoadFinished.vPremise: " + vPremise.size());
            rcvPremises.setAdapter(new PremiseAdapter(this, vPremise));
            rcvPremises.setLayoutManager(new LinearLayoutManager(this));
            premiseSearchBar(premiseSearch, vPremise);
        } else
            Toast.makeText(this, "Unable to load data from iiotisme.com", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Vector<Premise>> loader) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "MainActivity.onActivityResult.requestCode: " + requestCode + ", resultCode: " + resultCode + "data: " + data);
        if (data == null)
            return;

        if (requestCode == REQUEST) {
            Log.d(TAG, "MainActivity.onActivityResult: if (requestCode == REQUEST)");
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "MainActivity.onActivityResult: if (resultCode == RESULT_OK)");
                pTrackVisitor_s = data.getStringExtra("trackVisitor");
                pPosition_s = data.getStringExtra("position");

                Log.d(TAG, "MainActivity.onActivityResult.pPosition_s: " + pPosition_s + ", pTrackVisitor_s: " + pTrackVisitor_s);

                if (pTrackVisitor_s != null) {
                    Log.d(TAG, "MainActivity.onActivityResult: if (pTrackVisitor_s != null)");
                    pTrackVisitor = Integer.parseInt(pTrackVisitor_s);
                    pPosition = Integer.parseInt(pPosition_s);
                    Premise p = mPremiseV.get(pPosition);

                    p.setTrackVisitorCount(pTrackVisitor);
                    maxCapacity = p.getVisitorAllowed();
                    currentCapacity = p.getCurrentCount();
                    pLocation = p.getPremiseLocation();
                } else {
                    Log.d(TAG, "MainActivity.onActivityResult: else");
                    pTrackVisitor = 0;
                    pPosition = 0;
                }
                rcvPremises.setAdapter(new PremiseAdapter(this, mPremiseV));
                PrefConfig.writeListInPref(this, mPremiseV);

                sendNotification();
            }
        }
        Log.d(TAG, "MainActivity.onActivityResult.pPosition_s: " + pPosition_s + ", pTrackVisitor_s: " + pTrackVisitor_s);
    }

    // AISAH [START]
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void RequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
    // AISAH [END]

    private void ConnectivityCheck() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        Log.d("Athirah:", "MainActivity.ConnectivityCheck.connectivityManager: " + connectivityManager);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Log.d("Athirah:", "MainActivity.ConnectivityCheck.networkInfo: " + networkInfo);
        if (networkInfo != null && networkInfo.isConnected())
            loaderManager.initLoader(0, null, this);
        else
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
    }

    private void premiseSearchBar(EditText txtPremiseSearch, Vector<Premise> vPremiseData) {
        txtPremiseSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim().toLowerCase();

                if (!input.isEmpty()) {
                    Vector<Premise> filtered = new Vector<>();
                    for (Premise premise : vPremiseData)
                        if (premise.getPremiseName().toLowerCase().contains(input))
                            filtered.add(premise);
                    rcvPremises.setAdapter(new PremiseAdapter(MainActivity.this, filtered));
                } else
                    rcvPremises.setAdapter(new PremiseAdapter(MainActivity.this, vPremiseData));
            }
        });
    }

    private void sendNotification() {
        Log.d(TAG, "MainActivity.sendNotification");

        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);

        Intent broadcastIntent = new Intent(Intent.ACTION_VIEW);
        broadcastIntent.setData(Uri.parse(pLocation));

        PendingIntent actionIntent = PendingIntent.getActivity(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int free = maxCapacity - currentCapacity;
        Log.d(TAG, "MainActivity.sendNotification.free: " + free);

        if (free > pTrackVisitor){
            String nText = pPremiseName + " is ready for you now!";
            Log.d(TAG, "MainActivity.sendNotification.nText: " + nText);

            Notification notification = new NotificationCompat.Builder(this, AppNotification.CHANNEL_1_ID)
                    .setSmallIcon(R.mipmap.launcher_icon_pq)
                    .setContentTitle("Premise Queue")
                    .setContentText(nText)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setColor(Color.DKGRAY)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .addAction(R.mipmap.launcher_icon_pq, "Open Navigation", actionIntent)
                    .build();

            Log.d(TAG, "MainActivity.sendNotification.notification");
            notificationManager.notify(1, notification);
        }
        else if (free < pTrackVisitor) {
            String nText = pPremiseName + " is unable to accommodate your visit right now. We’ll notify you again when our premise is ready for you";
            Log.d(TAG, "MainActivity.sendNotification.nText: " + nText);

            Notification notification = new NotificationCompat.Builder(this, AppNotification.CHANNEL_2_ID)
                    .setSmallIcon(R.mipmap.launcher_icon_pq)
                    .setContentTitle("Premise Queue")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(nText))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setColor(Color.DKGRAY)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .addAction(R.mipmap.launcher_icon_pq, "Close Notification", actionIntent)
                    .addAction(R.mipmap.launcher_icon_pq, "Open Navigation", actionIntent)
                    .build();

            Log.d(TAG, "MainActivity.sendNotification.notification");
            notificationManager.notify(1, notification);
        }
    }

    // AISAH [START]
    // Navigation Pane
    public static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
        Log.d(TAG, "MainActivity.openDrawer: Open drawer start");
    }

    public void ClickMenu(View view) {
        //Open drawer
        openDrawer(drawerLayout);
        Log.d(TAG, "MainActivity.ClickMenu: Open drawer");
    }

    public void ClickLogo(View view) {
        //Close drawer
        closeDrawer(drawerLayout);
        Log.d(TAG, "MainActivity.ClickLogo: Close drawer with logo");
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Close drawer layout
        //Check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //When drawer is open, close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view) {
        //Recreate activity
        recreate();
    }

    public void ClickAboutUs(View view) {
        //Redirect activity to about us
        redirectActivity(this, AboutUs.class);
        Log.d(TAG, "MainActivity.ClickAboutUs: redirect activity to about us");
    }

    public void ClickRating(View view) {
        //Redirect activity to rating
        //redirectActivity(this,AppRating.class);
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity, aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);
        Log.d(TAG, "redirect activity");
    }
    // AISAH [END]
}