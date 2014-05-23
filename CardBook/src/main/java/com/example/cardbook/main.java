package com.example.cardbook;

import com.example.cardbook.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class main extends Activity {

    ActionBar.Tab tab1, tab2, tab3, tab4;
    public static String URL;
    Fragment fragmentTab1 = new FragmentTab1();
    Fragment fragmentTab2 = new FragmentTab2();
    Fragment fragmentTab3 = new FragmentTab3();
    Fragment fragmentTab4 = new FragmentTab4();
    private static Context context;
    private static ConnectivityManager connectivity;
    public static Activity activity;

    public static Context getAppContext() {
        return main.context;
    }
    public static  ConnectivityManager getSystemService() {
        return main.connectivity;
    }
    public static String getURL(){
        return main.URL;
    }
    public static Activity gettheActivity(){
        return main.activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        main.context = getApplicationContext();
        main.connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        main.URL = "http://ec2-54-205-57-222.compute-1.amazonaws.com/";
        setContentView(R.layout.main);
        //Checking whether the application is running the first time
        findViewById(R.id.dummy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean Flag = FirstTimeIntro();
                if (Flag){
                    Intent intent = new Intent(main.this, Intro.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                }
                //otherwise just continue
            }
        });
        findViewById(R.id.dummy_button).performClick();
        findViewById(R.id.dummy_button).setVisibility(View.INVISIBLE);
        //Setting up the tabs
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        tab1 = actionBar.newTab().setText("Home");
        tab2 = actionBar.newTab().setText("Contacts");
        tab3 = actionBar.newTab().setText("Visit Card");
        tab4 = actionBar.newTab().setText("About");

        tab1.setTabListener(new MyTabListener(fragmentTab1));
        tab2.setTabListener(new MyTabListener(fragmentTab2));
        tab3.setTabListener(new MyTabListener(fragmentTab3));
        tab4.setTabListener(new MyTabListener(fragmentTab4));

        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);
        actionBar.addTab(tab4);
        // remove title
    }

    //Our custom function for checking whether the application is running for the first time
    // and performing the appropriate action
    public boolean FirstTimeIntro(){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.PrivateKeys), Context.MODE_PRIVATE);
        boolean Flag = sharedPref.getBoolean("Intro", true);
        Log.i("+++++++++++++++++++++++++++++++++++++++++++", Boolean.toString(Flag));
        if (Flag) {
            //the app is being launched for first time, do something
            return true;
        }else{
            return false;
        }
    }


    public class FragmentTab1 extends Fragment {
        public String Latitude = null;
        public String Longitude = null;
        public ImageButton SoftBump;
        public ProgressBar Progressbar;
        public String FullName = null;
        public String VENUE = null;
        public String PhoneNumber = null;
        public String City = null;
        public String Country = null;
        public String Company = null;
        public String Address = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.home, container, false);
            SoftBump = (ImageButton) view.findViewById(R.id.softbump);
            SoftBump.setBackgroundResource(R.drawable.custom_button2);
            Progressbar = (ProgressBar) view.findViewById(R.id.progressBar);
            LocationManager locationManager = (LocationManager) getSystemService(android.content.Context.LOCATION_SERVICE);
            // Define a listener that responds to location updates

            MyLocationListener myloc1 = new MyLocationListener();

            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myloc1);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myloc1);
            SoftBump.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Latitude != null && Longitude != null) {
                        DownloadWebpage task = new DownloadWebpage();
                        task.execute(main.getURL());
                    }
                }
            });

            return view;
        }

        class DownloadWebpage extends AsyncTask<String, Void, String> {
            Map<String, String> theTreasures = ReadTheIdentifier();
            // arguments are given by execute() method call (defined in the parent): params[0] is the url.
            protected void onPreExecute(){
                SoftBump.setVisibility(View.INVISIBLE);
                Progressbar.setVisibility(View.VISIBLE);

            }
            protected String doInBackground(String... urls) {
                String st = null;

                try {
                    Connection Connect = new Connection("POST",urls[0] + "bump/" + theTreasures.get("Identifier").toString());
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("Privatekey", theTreasures.get("PrivateKey").toString()));
                    params.add(new BasicNameValuePair("Publickey", theTreasures.get("PublicKey").toString()));
                    params.add(new BasicNameValuePair("Latitude", Latitude));
                    params.add(new BasicNameValuePair("Longitude", Longitude));
                    params.add(new BasicNameValuePair("handshake", "DOIT"));
                    st = Connect.getbump(params);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return st;
            }

            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {
                Progressbar.setVisibility(View.INVISIBLE);
                SoftBump.setVisibility(View.VISIBLE);
                if (result != null){
                    JSONObject jObject = null;
                    try {
                        jObject = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (jObject != null){
                        try {
                            MagicMessuper Encoder = new MagicMessuper(theTreasures.get("PrivateKey").toString(), theTreasures.get("PublicKey").toString());
                            FullName = jObject.getString("FullName").trim();
                            PhoneNumber = jObject.getString("PhoneNumber").trim();
                            City = jObject.getString("City").trim();
                            Country = jObject.getString("Country").trim();
                            Address = jObject.getString("Address").trim();
                            Company = jObject.getString("Company").trim();
                            VENUE = jObject.getString("Latitude").trim() + "," + jObject.getString("Longitude").trim();
                            params.add(new BasicNameValuePair("FullName", Encoder.bytesToHex(Encoder.encrypt(FullName))));
                            params.add(new BasicNameValuePair("PhoneNumber", Encoder.bytesToHex(Encoder.encrypt(PhoneNumber))));
                            params.add(new BasicNameValuePair("City", Encoder.bytesToHex(Encoder.encrypt(City))));
                            params.add(new BasicNameValuePair("Country", Encoder.bytesToHex(Encoder.encrypt(Country))));
                            params.add(new BasicNameValuePair("Address", Encoder.bytesToHex(Encoder.encrypt(Address))));
                            params.add(new BasicNameValuePair("Company", Encoder.bytesToHex(Encoder.encrypt(Company))));
                            params.add(new BasicNameValuePair("VENUE", Encoder.bytesToHex(Encoder.encrypt(VENUE))));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (FullName != null){
                            //Alert Dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(main.gettheActivity());
                        builder.setMessage("Would You like to exchange your contact details with "+ FullName)
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Ok Let's exchange details
                                        MakeTransaction task = new MakeTransaction();
                                        task.execute(main.getURL());
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        Log.i("+++++++++++++++++++++++++++++++++++++++++++++++++++++++", result);
                        }
                    }
                }
                else{
                    Info("Sorry but either there is no one to soft bump with, either connection to server has failed, please try again later");
                }

            }

        }

        class MakeTransaction extends AsyncTask<String, Void, Integer> {
            Map<String, String> theTreasures = ReadTheIdentifier();
            // arguments are given by execute() method call (defined in the parent): params[0] is the url.
            protected void onPreExecute(){
                SoftBump.setVisibility(View.INVISIBLE);
                Progressbar.setVisibility(View.VISIBLE);
            }
            protected Integer doInBackground(String... urls) {
                Integer st = null;
                try {
                    Connection Connect = new Connection("POST",urls[0] + "transaction/" + theTreasures.get("Identifier").toString());
                    st = Connect.downloadUrl(params);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return st;
            }

            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(Integer result) {
                Progressbar.setVisibility(View.INVISIBLE);
                SoftBump.setVisibility(View.VISIBLE);
                if (result == 200){
                    Successmessage(" You have successfully added " + FullName + " to Your CardBook");
                    main.gettheActivity().getActionBar().setSelectedNavigationItem(1);
                }
                else{
                    Alert("Whooops probably connection to the server has failed or this person is already in Your CardBook");
                }

            }

        }

        public class MyLocationListener implements LocationListener {

            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                if(location.getProvider().equals(LocationManager.NETWORK_PROVIDER)){
                    Log.i("+++++++++++++++++++++++++++++++++++++++++++++++++++++++", String.valueOf(location.getLatitude()));
                    Latitude = String.valueOf(location.getLatitude());
                    Longitude = String.valueOf(location.getLongitude());
                }
                else if(location.getProvider().equals(LocationManager.GPS_PROVIDER)){
                    Log.i("+++++++++++++++++++++++++++++++++++++++++++++++++++++++", String.valueOf(location.getLatitude()));
                    Latitude = String.valueOf(location.getLatitude());
                    Longitude = String.valueOf(location.getLongitude());
                }else{
                    Alert("Unable to retrieve your location");
                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        }
    }

    public class FragmentTab2 extends Fragment {
        public ListView listview;
        public ProgressBar Progressbar;

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.contacts, container, false);
            listview = (ListView) view.findViewById(R.id.listView);
            Progressbar = (ProgressBar) view.findViewById(R.id.progressBar);
            MakeTransaction task = new MakeTransaction();
            task.execute(main.getURL());
            return view;
        }

        class MakeTransaction extends AsyncTask<String, Void, String> {
            Map<String, String> theTreasures = ReadTheIdentifier();
            // arguments are given by execute() method call (defined in the parent): params[0] is the url.
            protected void onPreExecute(){
                Progressbar.setVisibility(View.VISIBLE);
            }
            protected String doInBackground(String... urls) {
                String st = null;
                try {
                    Connection Connect = new Connection("POST",urls[0] + "contacts/" + theTreasures.get("Identifier").toString());
                    st = Connect.getVisitcarddetails();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return st;
            }

            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {
                Progressbar.setVisibility(View.INVISIBLE);
                ArrayList<BriefLocation> locs = new ArrayList<BriefLocation>();
                if (result != null){
                    Log.i("+++++++++++++++++++++++++++++++++++++++++++++++++++++++", result);
                    //Data is loaded successfully
                    JSONArray data = null;
                    try {
                        data = new JSONArray(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (data != null){
                        Integer i = data.length();
                        Integer k = 0;
                        try {
                            MagicMessuper Encoder = new MagicMessuper(theTreasures.get("PrivateKey").toString(), theTreasures.get("PublicKey").toString());

                            for (k=0;k<i;k++){
                                JSONObject thename = data.getJSONObject(k);
                                String myname = thename.getString("FullName");
                                String myphone = thename.getString("PhoneNumber");
                                myname = new String(Encoder.decrypt(myname)).trim();
                                myphone = new String(Encoder.decrypt(myphone)).trim();
                                BriefLocation loc = new BriefLocation();
                                loc.name = myname;
                                loc.phone = myphone;
                                locs.add(loc);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Alert("Connection to the server has failed");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        createListing(locs);
                }
                else{
                    Alert("Connection to the server has failed or you don't have any contacts yet");
                }

            }

        }
        }

        public class BriefLocation {
            public String name;
            public String phone;

            public BriefLocation() {
                name = new String();
                phone = new String();
            }
        }

        public void createListing(final ArrayList<BriefLocation> locs) {

            // create a list containing a map of two strings, first corresponds to
            // location names, second to (x,y) coordinates
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            for (int i = 0; i < locs.size(); i++) {
                Map<String, String> datum = new HashMap<String, String>(2);
                datum.put("Loc", locs.get(i).name);
                datum.put("coord", locs.get(i).phone);
                data.add(datum);
            }

            // simple adapter takes our data, info about the ListView we are using
            // which uses text1, text2 internally
            SimpleAdapter adapter = new SimpleAdapter(main.gettheActivity(), data, android.R.layout.simple_list_item_2,
                    new String[] { "Loc", "coord" }, new int[] { android.R.id.text1, android.R.id.text2 });

            // link the list with the adapter
            listview.setAdapter(adapter);

            // whenever item is clicked, call a function showDescActivity which
            // actually opens com.example.chuckin.DescActivity
            listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //String item = locs.get(position).name;
                    //showDescActivity(item);
                }
            });

        }

    }

    public class FragmentTab3 extends Fragment {
        EditText name;
        EditText phone;
        EditText city;
        EditText country;
        EditText address;
        EditText company;

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.visitcard, container, false);
            name = (EditText) view.findViewById(R.id.name);
            phone = (EditText) view.findViewById(R.id.phone);
            city = (EditText) view.findViewById(R.id.city);
            country = (EditText) view.findViewById(R.id.country);
            address = (EditText) view.findViewById(R.id.address);
            company = (EditText) view.findViewById(R.id.company);
            ConnectivityManager connMgr = main.getSystemService();
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected()){
                Alert("Unable to connect to the internet, please check your internet connection");
            }else{
                //we have an internet connection let's go to registration
                DownloadWebpage task = new DownloadWebpage();
                task.execute(main.getURL());
            }
            return view;
        }

        class DownloadWebpage extends AsyncTask<String, Void, String> {
            Map<String, String> theTreasures = ReadTheIdentifier();
            // arguments are given by execute() method call (defined in the parent): params[0] is the url.
            protected String doInBackground(String... urls) {
                String st = null;

                try {
                    Connection Connect = new Connection("POST",urls[0] + "user/" + theTreasures.get("Identifier").toString());
                    st = Connect.getVisitcarddetails();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return st;
            }

            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {
                if (result != null){
                    Log.i("+++++++++++++++++++++++++++++++++++++++++++++++++++++++", result);
                    //Data is loaded successfully
                    JSONObject jObject = null;
                    try {
                        jObject = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (jObject != null){
                        try {
                            MagicMessuper Encoder = new MagicMessuper(theTreasures.get("PrivateKey").toString(), theTreasures.get("PublicKey").toString());

                            String thename = jObject.getString("name");
                            name.setText(new String(Encoder.decrypt(thename)).trim());
                            String thephone = jObject.getString("phone");
                            phone.setText(new String(Encoder.decrypt(thephone)).trim());
                            String thecity = jObject.getString("city");
                            city.setText(new String(Encoder.decrypt(thecity)).trim());
                            String thecountry = jObject.getString("country");
                            country.setText(new String(Encoder.decrypt(thecountry)).trim());
                            String theaddress = jObject.getString("address");
                            address.setText(new String(Encoder.decrypt(theaddress)).trim());
                            String thecompany = jObject.getString("company");
                            company.setText(new String(Encoder.decrypt(thecompany)).trim());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Alert("Connection to the server has failed");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else{
                        Alert("Connection to the server has failed");
                    }


                }
                else{
                    Alert("Connection to the server has failed");
                }

            }

        }
    }



    public class FragmentTab4 extends Fragment {
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.about, container, false);
            TextView Version = (TextView) view.findViewById(R.id.version);

            String temp = "";
            temp += "<p><strong>Version 0.1</strong></p>";
            temp += "<p><strong>Cardbook Application</strong></p>";
            temp += "<p><em>http://ec2-54-221-23-251.compute-1.amazonaws.com/</em></p>";
            temp += "<hr />";
            temp += "<p>Masdar Institute Of Science and Technology<br />";
            temp += "Masdar City, Khalifa City A<br />";
            temp += "Abu Dhabi, UAE</p>";
            temp += "<p><br />";
            temp += "<strong>+971-502332573<br />";
            temp += "+971-569536068</strong><br />";
            temp += "<em>arnukk@gmail.com<br />";
            temp += "waheebyaqub@gmail.com</em></p>";
            Version.setText(Html.fromHtml(temp));
            return view;
        }
    }

    public class MyTabListener implements ActionBar.TabListener {
        Fragment fragment;

        public MyTabListener(Fragment fragment) {
            this.fragment = fragment;
        }

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.replace(R.id.fragment_container, fragment);
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.remove(fragment);
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // nothing done here
        }
    }

    public void Alert(String text){
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(main.getAppContext(), text, duration);
        toast.setGravity(Gravity.TOP, 0, 10);
        toast.getView().setBackgroundColor(Color.RED);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextColor(Color.WHITE);
        toastTV.setTextSize(16);
        toast.show();
    }

    public void Successmessage(String text){
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(main.getAppContext(), text, duration);
        toast.setGravity(Gravity.TOP, 0, 10);
        toast.getView().setBackgroundColor(Color.GREEN);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextColor(Color.BLACK);
        toastTV.setTextSize(16);
        toast.show();
    }

    public void Info(String text){
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(main.getAppContext(), text, duration);
        toast.setGravity(Gravity.TOP, 0, 15);
        toast.getView().setBackgroundColor(Color.YELLOW);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextColor(Color.BLACK);
        toastTV.setTextSize(16);
        toast.show();
    }

    public Map ReadTheIdentifier(){
        SharedPreferences sharedPref = main.getAppContext().getSharedPreferences(
                getString(R.string.PrivateKeys), Context.MODE_PRIVATE);
        Map<String, String> theTreasures = new HashMap<String, String>();
        theTreasures.put("Identifier", sharedPref.getString("Identifier", null));
        theTreasures.put("PrivateKey", sharedPref.getString("PrivateKey", null));
        theTreasures.put("PublicKey", sharedPref.getString("PublicKey", null));
        return theTreasures;
    }

}