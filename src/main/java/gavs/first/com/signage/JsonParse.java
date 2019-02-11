package gavs.first.com.signage;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static gavs.first.com.signage.Utils.host_ip;

public class JsonParse {

    String TAG="";
    double current_latitude, current_longitude;

    public JsonParse() {
    }

    public JsonParse(double current_latitude, double current_longitude) {
        this.current_latitude = current_latitude;
        this.current_longitude = current_longitude;
    }

    public List<SuggestGetSet> getParseJsonWCF(String sName) {
        List<SuggestGetSet> ListData = new ArrayList<SuggestGetSet>();
        String searchString = "";
        searchString = sName;

        URL url;
        HttpURLConnection urlConnection = null;

        try {

//                 url = new URL("http://10.0.14.22:8080/gavs/api/print/card");


            url = new URL(host_ip+"/gavs/api/form/autofill");
            //url = new URL("http://192.168.1.81:8081/gavs/api/form/autofill");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());



            try {


                JSONObject obj = new JSONObject();

                obj.put("sub", searchString);
                wr.writeBytes(obj.toString());
                Log.e("JSON Input", obj.toString());
                wr.flush();
                wr.close();
                urlConnection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line = reader.readLine();

                Log.i(TAG, "getParseJsonWCF: "+line);

                JSONObject jsonResponse = new JSONObject(line);
                JSONArray jsonArray = jsonResponse.getJSONArray("employee");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject r = jsonArray.getJSONObject(i);

//                ListData.add(new SuggestGetSet(r.getString("id"),r.getString("name")));
                    ListData.add(new SuggestGetSet(r.getString("name")));





                }



   /*     try {
            String temp=sName.replace(" ", "%20");
            URL js = new URL("http://10.0.8.133:8081/gavs/api/form/autofill"+temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            JSONArray jsonArray = jsonResponse.getJSONArray("results");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject r = jsonArray.getJSONObject(i);

//                ListData.add(new SuggestGetSet(r.getString("id"),r.getString("name")));
                ListData.add(new SuggestGetSet(r.getString("name")));
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }*/
                return ListData;

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ListData;
    }
}
