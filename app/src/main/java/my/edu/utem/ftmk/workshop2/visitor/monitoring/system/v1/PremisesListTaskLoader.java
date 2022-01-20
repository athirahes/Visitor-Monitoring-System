package my.edu.utem.ftmk.workshop2.visitor.monitoring.system.v1;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

public class PremisesListTaskLoader extends AsyncTaskLoader<Vector<Premise>> {

    private static final String TAG = "PQ";

    public PremisesListTaskLoader(@NonNull Context context) {
        super(context);
        Log.d(TAG, "PremisesListTaskLoader.PremisesListTaskLoader.context: " + context);
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "PremisesListTaskLoader.onStartLoading");
        forceLoad();
    }

    @Nullable
    @Override
    public Vector<Premise> loadInBackground() {
        Log.d(TAG, "PremisesListTaskLoader.InBackground");
        Vector<Premise> premises = null;


        String bearerToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYXNjb3ZlQGdtYWlsLmNvbSIsInNjb3BlcyI6WyJURU5BTlRfQURNSU4iXSwidXNlcklkIjoiZjIzN2QxZTAtMjAwMi0xMWViLWI0YzItOTEyMGQ5ZjFlNTA4IiwiZW5hYmxlZCI6dHJ1ZSwiaXNQdWJsaWMiOmZhbHNlLCJ0ZW5hbnRJZCI6ImRhMWM0MDAwLTIwMDItMTFlYi1iNGMyLTkxMjBkOWYxZTUwOCIsImN1c3RvbWVySWQiOiIxMzgxNDAwMC0xZGQyLTExYjItODA4MC04MDgwODA4MDgwODAiLCJpc3MiOiJpaW90c21lLmNvbSIsImlhdCI6MTYxMTcxNzczNiwiZXhwIjoxNjExOTgwNTM2fQ.xWm9MNr5yREIZgsIwJv39-yKQQjx1hysGvvnH2tfAHj6AwXfWw8go6Pc7xdsVHIadKkb3Tr7FOWIA_StCIP9vg";
        try {
            String deviceTenants = "https://iiotsme.com/api/tenant/devices?limit=1000";
            HttpsURLConnection connection1 = (HttpsURLConnection) new URL(deviceTenants).openConnection();
            Log.d(TAG, "PremisesListTaskLoader.InBackground.connection1: " + connection1);

            connection1.setDoInput(true);
            connection1.setRequestMethod("GET");
            connection1.setRequestProperty("Accept", "application/json");
            connection1.setRequestProperty("Content-Type", "application/json");
            connection1.setRequestProperty("X-Authorization", bearerToken);

            int code1 = connection1.getResponseCode();
            Log.d(TAG, "PremisesListTaskLoader.InBackground.code: " + code1);

            if (code1 / 100 == 2) {
                Log.d(TAG, "PremisesListTaskLoader.InBackground.if (code / 100 == 2): " + code1);
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
                StringBuilder sb1 = new StringBuilder();
                String input1;
                while ((input1 = reader1.readLine()) != null) {
                    Log.d(TAG, "PremisesListTaskLoader.InBackground.input1: " + input1);
                    sb1.append(input1);
                }
                reader1.close();

                JSONObject response1 = new JSONObject(sb1.toString());
                Log.d(TAG, "PremisesListTaskLoader.InBackground.response: " + response1.toString());
                JSONArray data1 = response1.getJSONArray("data");
                int length1 = data1.length();
                premises = new Vector<>();
                Log.d(TAG, "PremisesListTaskLoader.InBackground.length1: " + length1 + " data1: " + data1);

                for (int i = 0; i < length1; i++) {
                    JSONObject object1 = data1.getJSONObject(i);
                    Premise premise = new Premise();

                    premise.setPremiseName(object1.getString("name"));
                    premise.setPremiseType(object1.getString("type"));
                    premise.setPremiseDescription(object1.getJSONObject("additionalInfo").getString("description"));
                    premise.setPremiseLocation(object1.getString("label"));
                    premise.setTrackVisitorCount(0);


                    String deviceType1 = object1.getJSONObject("id").getString("entityType");
                    String deviceId1 = object1.getJSONObject("id").getString("id");
                    String deviceAttributes1 = "https://iiotsme.com/api/plugins/telemetry/" + deviceType1 + "/" + deviceId1 + "/values/attributes?keys=capacity,free,customerrating";

                    Log.d(TAG, "PremisesListTaskLoader.InBackground.for(" + i + "): " + premise.getPremiseName() + " deviceType: " + deviceType1 + " deviceId: " + deviceId1 + " deviceAttributes: " + deviceAttributes1);

                    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
                    Log.d(TAG, "---------------------------------------------------------------------START-------------------------------------------------------------------------------------------------------- ");
                    try {
                        HttpsURLConnection connection2 = (HttpsURLConnection) new URL(deviceAttributes1).openConnection();
                        Log.d(TAG, "PremisesListTaskLoader.InBackground.connection2: " + connection2);

                        connection2.setDoInput(true);
                        connection2.setRequestMethod("GET");
                        connection2.setRequestProperty("Accept", "application/json");
                        connection2.setRequestProperty("Content-Type", "application/json");
                        connection2.setRequestProperty("X-Authorization", bearerToken);

                        int code2 = connection2.getResponseCode();
                        Log.d(TAG, "PremisesListTaskLoader.InBackground.code2: " + code2);

                        if (code2 / 100 == 2) {
                            BufferedReader reader2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
                            Log.d(TAG, "PremisesListTaskLoader.InBackground.reader2: " + reader2.toString());
                            StringBuilder sb2 = new StringBuilder();
                            String input2;
                            while ((input2 = reader2.readLine()) != null) {
                                Log.d(TAG, "PremisesListTaskLoader.InBackground.input2: " + input2);
                                sb2.append(input2);
                            }
                            reader2.close();

                            JSONArray response2 = new JSONArray(sb2.toString());
                            Log.d(TAG, "PremisesListTaskLoader.InBackground.response2: " + response2.toString());
                            int length2 = response2.length();
                            Log.d(TAG, "PremisesListTaskLoader.InBackground.length2: " + length2 + " response2: " + response2);

                            for (int j = 0; j < length2; j++) {
                                JSONObject object2 = response2.getJSONObject(j);
                                Log.d(TAG, "PremisesListTaskLoader.InBackground.object2 = response2.getJSONObject(" + j + "): " + object2.toString());
                                String deviceType = object2.getString("key");
                                Log.d(TAG, "loadInBackground: deviceType: " + deviceType);

                                switch (deviceType) {
                                    case "capacity":
                                        String deviceCapacity_s = object2.getString("value");
                                        int deviceCapacity = Integer.parseInt(deviceCapacity_s);
                                        premise.setVisitorAllowed(deviceCapacity);
                                        Log.d(TAG, "PremisesListTaskLoader.InBackground.for(" + j + "): " + premise.getPremiseName() + " deviceType: " + deviceType + " deviceCapacity_s: " + deviceCapacity_s);
                                        break;
                                    case "free":
                                        String deviceFree_s = object2.getString("value");
                                        int deviceFree = Integer.parseInt(deviceFree_s);
                                        premise.setCurrentCount(deviceFree);
                                        Log.d(TAG, "PremisesListTaskLoader.InBackground.for(" + j + "): " + premise.getPremiseName() + " deviceType: " + deviceType + " deviceFree_s: " + deviceFree_s);
                                        break;
                                    case "customerrating":
                                        double deviceRating = object2.getDouble("value");
                                        premise.setRating(deviceRating);
                                        Log.d(TAG, "PremisesListTaskLoader.InBackground.for(" + j + "): " + premise.getPremiseName() + " deviceType: " + deviceType + " deviceRating: " + deviceRating);
                                        break;
                                }
                            }
                        }
                        connection2.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("Athirah2", "PremisesListTaskLoader.InBackground.catch (Exception e): " + e.toString());
                    }
                    Log.d(TAG, "---------------------------------------------------------------------- END -------------------------------------------------------------------------------------------------------- ");
                    /*----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
                    premises.add(premise);
                    Log.d(TAG, "PremisesListTaskLoader.InBackground.for(" + i + "): " + premise.getPremiseName() + " Type: " + premise.getPremiseType() + " [Current/Max]: [" + premise.getCurrentCount() + "/" + premise.getVisitorAllowed() + "]" + " [Tracking/Max]: [" + premise.getTrackVisitorCount() + "/" + premise.getVisitorAllowed() + "]" + "[The rating]: [ " + premise.getRating() + " ]");
                    Log.d(TAG, "PremisesListTaskLoader.InBackground.for(" + i + "): " + "[Customer Rating]: " + premise.getRating());
                }
                Log.d(TAG, "PremisesListTaskLoader.InBackground.premises.size(): " + premises.size());
            }
            connection1.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "PremisesListTaskLoader.InBackground.catch (Exception e): " + e.toString());
        }
        Log.d(TAG, "---------------------------------------------------------------------- END -------------------------------------------------------------------------------------------------------- ");
        Log.d(TAG, "---------------------------------------------------------------------- END -------------------------------------------------------------------------------------------------------- ");
        Log.d(TAG, "---------------------------------------------------------------------- END -------------------------------------------------------------------------------------------------------- ");
        return premises;
    }
}