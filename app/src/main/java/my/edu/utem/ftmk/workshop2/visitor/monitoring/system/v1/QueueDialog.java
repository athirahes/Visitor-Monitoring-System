package my.edu.utem.ftmk.workshop2.visitor.monitoring.system.v1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class QueueDialog extends AppCompatDialogFragment {

    private static final String TAG = "PQ";

    EditText eQueueInput;
    TextView txtQueueNote;
    QueueDialogInterFace queueDialogInterFace;
    DialogInterface.OnClickListener dialogClickListener;
    String pMax_s;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "QueueDialog.onCreateDialog");

        Bundle args = getArguments();
        assert args != null;
        pMax_s = args.getString("maxVisitor_s");
        int position = args.getInt("position");

        int pMax = Integer.parseInt(pMax_s);
        Log.d(TAG, "QueueDialog.onCreateDialog.pMax_s:" + pMax_s + " pMax:" + pMax + " position:" + position);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.queue_dialog, null);
        txtQueueNote = view.findViewById(R.id.queue_note);

        String note = "Max. visitor(s) allowed: " + pMax_s;
        txtQueueNote.setText(note);
        eQueueInput = view.findViewById(R.id.queue_input);

        builder.setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "QueueDialog.onCreateDialog.onClick.setPositiveButton");
                        String qInput_s = eQueueInput.getText().toString();
                        if (qInput_s.equals("")) {
                            Log.d(TAG, "QueueDialog.onCreateDialog.onClick.setPositiveButton.if (qInput_s.isEmpty()");
                            //alertdialog warning input null
                            LaunchNullDialog();
                        } else {
                            int qInput = Integer.parseInt(qInput_s);
                            /*queueDialogInterFace.applyTexts(qInput_s);*/
                            Log.d(TAG, "QueueDialog.onCreateDialog.onClick.setPositiveButton.String qInput_s: " + qInput_s + " qInput: " + qInput + " pMax: " + pMax);

                            if (qInput_s.equals("0")) {
                                Log.d(TAG, "QueueDialog.onCreateDialog.onClick.setPositiveButton.if (qInput_s.equals(0)");
                                //alertdialog warning input null
                                LaunchNullDialog();
                            } else if (qInput > pMax) {
                                Log.d(TAG, "QueueDialog.onCreateDialog.onClick.setPositiveButton.else if (qInput > pMax)");
                                //alertdialog warning input must be less than Max. guest(s)
                                LaunchMaxDialog();
                            } else {
                                Log.d(TAG, "QueueDialog.onCreateDialog.onClick.setPositiveButton.else");
                                queueDialogInterFace.applyTexts(qInput_s);
                            }
                        }
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "QueueDialog.onCreateDialog.onClick.setNegativeButton");
                        //DO SOMETHING
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        queueDialogInterFace = (QueueDialogInterFace) context;
        Log.d(TAG, "QueueDialog.onAttach");
    }

    public void LaunchNullDialog() {
        Log.d(TAG, "QueueDialog.LaunchAlertDialog");

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Invalid Input!");
        builder.setMessage("The entered value is invalid!");

        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    Log.d(TAG, "QueueDialog.LaunchAlertDialog.onClick.DialogInterface.BUTTON_POSITIVE");
                }
            }
        };
        builder.setPositiveButton("OK", dialogClickListener);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void LaunchMaxDialog() {
        Log.d(TAG, "QueueDialog.LaunchAlertDialog");

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Please Re-Enter Input");
        builder.setMessage("Max. Visitor for this premise is " + pMax_s);

        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // if click btn yes
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    Log.d(TAG, "QueueDialog.LaunchMaxDialog..onClick.DialogInterface.BUTTON_POSITIVE");
                }
            }
        };
        builder.setPositiveButton("OK", dialogClickListener);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface QueueDialogInterFace {
        void applyTexts(String text_eQueueInput);
    }
}