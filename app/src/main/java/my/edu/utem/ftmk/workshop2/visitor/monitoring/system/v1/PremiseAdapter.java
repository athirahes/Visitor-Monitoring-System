package my.edu.utem.ftmk.workshop2.visitor.monitoring.system.v1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class PremiseAdapter extends RecyclerView.Adapter<PremiseViewHolder> {

    private final Vector<Premise> premiseList;
    private final Context context;
    private static final String TAG = "PQ";

    public static final int REQUEST_CODE = 275;

    public PremiseAdapter(Context context, Vector<Premise> mPremiseData) {
        Log.d(TAG, "PremiseAdapter.PremiseAdapter");
        this.premiseList = mPremiseData;
        this.context = context;
    }

    //Required method for creating the viewholder objects.
    @NonNull
    @Override
    public PremiseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "PremiseAdapter.PremiseViewHolder");
        return new PremiseViewHolder(LayoutInflater.from(context).inflate(R.layout.premise_list, parent, false), context, premiseList);
    }

    //Required method that binds the data to the viewholder.
    @Override
    public void onBindViewHolder(@NonNull PremiseViewHolder holder, int position) {
        // Get current Premise.
        Log.d(TAG, "PremiseAdapter.onBindViewHolder.START: " + position);
        Premise currentPremise = premiseList.get(position);

        setBgColor(holder, currentPremise);
        launchPremiseDetail(holder, currentPremise, position);

        // Populate the textviews with data.

        holder.bindTo(currentPremise);
        Log.d(TAG, "PremiseAdapter.onBindViewHolder.END: " + currentPremise.getPremiseName());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "PremiseAdapter.getItemCount");
        return premiseList.size();
    }

    private void setBgColor(@NonNull PremiseViewHolder holder, Premise thisPremise) {
        int TrackVisitorInput = thisPremise.getTrackVisitorCount();
        Drawable isNotTracking = ContextCompat.getDrawable(context, R.drawable.premise_box);
        Drawable isTracking = ContextCompat.getDrawable(context, R.drawable.premise_box_track);

        if (TrackVisitorInput != 0) {
            holder.itemView.setBackground(isTracking);
            Log.d(TAG, "PremiseAdapter.onBindViewHolder.TrackVisitorInput != 0: setBackground(isTracking)");
        } else {
            holder.itemView.setBackground(isNotTracking);
            Log.d(TAG, "PremiseAdapter.onBindViewHolder.TrackVisitorInput == 0: setBackground(isNotTracking)");
        }
    }

    private void launchPremiseDetail(@NonNull PremiseViewHolder holder, Premise thisPremise, int index){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookingIntent = new Intent(context, Booking.class);
                String pName = thisPremise.getPremiseName(),
                        pType = thisPremise.getPremiseType(),
                        pDesc = thisPremise.getPremiseDescription(),
                        pLocation = thisPremise.getPremiseLocation(),
                        pPosition = String.valueOf(index),
                        pTrack = String.valueOf(thisPremise.getTrackVisitorCount()),
                        pMax = String.valueOf(thisPremise.getVisitorAllowed());
                Float pRating = Float.parseFloat(String.valueOf(thisPremise.getRating()));

                Log.d(TAG, "PremiseViewHolder.onClick.pTrack: " + pTrack);

                bookingIntent.putExtra("name", pName);
                bookingIntent.putExtra("type", pType);
                bookingIntent.putExtra("desc", pDesc);
                bookingIntent.putExtra("location", pLocation);
                bookingIntent.putExtra("maxVisitor", pMax);
                bookingIntent.putExtra("trackVisitor", pTrack);
                bookingIntent.putExtra("position", pPosition);
                bookingIntent.putExtra("rating", pRating);

                ((Activity) context).startActivityForResult(bookingIntent,REQUEST_CODE);

                Log.d(TAG, "PremiseViewHolder.onClick.startActivity(bookingIntent)");
            }
        });
    }
}
