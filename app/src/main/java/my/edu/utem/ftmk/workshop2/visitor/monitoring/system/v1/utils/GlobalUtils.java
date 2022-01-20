package my.edu.utem.ftmk.workshop2.visitor.monitoring.system.v1.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hsalf.smilerating.SmileRating;
import com.hsalf.smileyrating.SmileyRating;
import com.hsalf.smileyrating.smileys.Okay;

import my.edu.utem.ftmk.workshop2.visitor.monitoring.system.v1.R;
import my.edu.utem.ftmk.workshop2.visitor.monitoring.system.v1.interfaces.DialogCallback;
import my.edu.utem.ftmk.workshop2.visitor.monitoring.system.v1.widgets.CustomDialog;

import static android.content.ContentValues.TAG;

public class GlobalUtils {
    private static final String TAG = "PQ";
    public static int rating;

    public static void showDialog(Context context, final DialogCallback dialogCallback){
        //create the dialog
        final CustomDialog dialog = new CustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.rating_dialog, null);

        dialog.setContentView(v);

        TextView btnDone = dialog.findViewById(R.id.btnDone);
        SmileyRating smileyRating = dialog.findViewById(R.id.smile_rating);


        smileyRating.setSmileySelectedListener(new SmileyRating.OnSmileySelectedListener() {
            @Override
            public void onSmileySelected(SmileyRating.Type type) {
                SmileyRating.Type smiley = smileyRating.getSelectedSmiley();
                // You can compare it with rating Type
                if (SmileyRating.Type.TERRIBLE == type) {
                    Log.d(TAG, "GlobalUtils.onSmileySelected: Terrible rating");
                    /*Log.d("Aisah:", "Terrible rating ");*/
                }
                else if (SmileyRating.Type.BAD == type) {
                    Log.d(TAG, "GlobalUtils.onSmileySelected: Bad rating");
                    /*Log.d("Aisah:", "Bad rating ");*/
                }
                else if (SmileyRating.Type.OKAY == type) {
                    Log.d(TAG, "GlobalUtils.onSmileySelected: Okay rating");
                    /*Log.d("Aisah:", "Okay rating ");*/
                }

                else if (SmileyRating.Type.GOOD == type) {
                    Log.d(TAG, "GlobalUtils.onSmileySelected: Good rating");
                    /*Log.d("Aisah:", "Good rating ");*/
                }

                else if (SmileyRating.Type.GREAT == type) {
                    Log.d(TAG, "GlobalUtils.onSmileySelected: Wow, the user gave high rating");
                    /*Log.d("Aisah:", "Wow, the user gave high rating ");*/
                }


                // You can get the user rating too
                // rating will between 1 to 5
                rating = type.getRating();
                Log.d(TAG, "GlobalUtils.onSmileySelected: User rating " + rating);
                /*Log.d("Aisah:", "User rating " + rating);*/
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(TAG, "GlobalUtils.btnDone.setOnClickListener");
                if(dialogCallback != null) {
                    dialogCallback.callback(rating);
                    Log.d(TAG, "GlobalUtils.onSmileySelected: The rating" + rating);
                    /*Log.d("Aisah", "The rating" + rating);*/
                    dialog.dismiss();
                    Toast.makeText(context, "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
}
