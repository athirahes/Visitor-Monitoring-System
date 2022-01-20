package my.edu.utem.ftmk.workshop2.visitor.monitoring.system.v1;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

class PremiseViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "PQ";

    // Member Variables for the TextViews
    private final TextView pName, pType, pVisiting, pAllowed;
    private final RatingBar pRating;
    Vector<Premise> premiseItem;

    private Context context;
    private Premise premise;

    public static final int REQUEST_CODE = 275;

    //Constructor for the ViewHolder, used in onCreateViewHolder().
    PremiseViewHolder(View itemView, Context context, Vector<Premise> premiseVector) {
        super(itemView);
        this.context = context;
        this.premiseItem = premiseVector;

        Log.d(TAG, "PremiseViewHolder.PremiseViewHolder");
        // Initialize the views.
        pName = itemView.findViewById(R.id.premiseName_lbl);
        pType = itemView.findViewById(R.id.premiseType_lbl);
        pVisiting = itemView.findViewById(R.id.visiting_count);
        pAllowed = itemView.findViewById(R.id.visitorAllowed);
        pRating = itemView.findViewById(R.id.ratingBar2);

        // 6: Set the OnClickListener to the entire view.
        Log.d(TAG, "PremiseViewHolder.ViewHolder.setOnClickListener");
        /*itemView.setOnClickListener(this);*/
    }

    void bindTo(Premise thisPremise) {
        // Populate the textviews with data.
        Log.d(TAG, "PremiseViewHolder.bindTo");

        this.premise = thisPremise;
        pName.setText(thisPremise.getPremiseName());
        pType.setText(thisPremise.getPremiseType());
        pVisiting.setText(String.valueOf(premise.getCurrentCount()));
        pAllowed.setText(String.valueOf(premise.getVisitorAllowed()));
        pRating.setRating(Float.parseFloat(String.valueOf(premise.getRating())));
        Log.d(TAG, "PremiseViewHolder.bindTo.Name: " + thisPremise.getPremiseName());
    }
}