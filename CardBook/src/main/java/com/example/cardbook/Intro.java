package com.example.cardbook;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cardbook.MagicMessuper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Intro extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    private static Context context;
    private static ConnectivityManager connectivity;
    public static String URL ;
    public static Activity activity;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    public static String getURL(){
        return Intro.URL;
    }
    public static Activity gettheActivity(){
        return Intro.activity;
    }
    public static Context getAppContext() {
        return Intro.context;
    }
    public static  ConnectivityManager getSystemService() {
        return Intro.connectivity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.activity = this;
        this.URL = "http://ec2-54-221-23-251.compute-1.amazonaws.com/register/";
        super.onCreate(savedInstanceState);
        Intro.context = getApplicationContext();
        Intro.connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.intro);
        PagerTitleStrip titleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
        titleStrip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        titleStrip.setTextColor(Color.WHITE);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.intro, menu);
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new Fragment();
            switch (position) {
                case 0:
                    return fragment = new IntroFragment1();
                case 1:
                    return fragment = new IntroFragment2();
                case 2:
                    return fragment = new IntroFragment3();
                default:
                    break;
            }
            return fragment;
        }

            @Override
        public int getCount() {
            // Show 2 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public static class IntroFragment1 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        public IntroFragment1() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.intro_fragment_one, container, false);
            return rootView;
        }
    }
    public static class IntroFragment2 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        public IntroFragment2() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.intro_fragment_two, container, false);
            return rootView;
        }
    }
    public static class IntroFragment3 extends Fragment {

        public Map<String, String> theArray = new HashMap<String, String>();
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        public IntroFragment3() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.intro_fragment_three, container, false);
            //Form Validation
            MyLovelyOnClickListener CustomClickListener =  new MyLovelyOnClickListener(rootView);
            rootView.findViewById(R.id.register).setOnClickListener(CustomClickListener);
            return rootView;
        }

        public class MyLovelyOnClickListener implements View.OnClickListener
        {
            View myLovelyVariable;
            public MyLovelyOnClickListener(View myLovelyVariable) {
                this.myLovelyVariable = myLovelyVariable;
            }

            @Override
            public void onClick(View v)
            {
                if (FormValidation(this.myLovelyVariable)){
                    //Ok everything is fine, let's continue to storing private and public keys
                    String Thename = IntroFragment3.this.theArray.get("FullName");
                    String ThePhone = IntroFragment3.this.theArray.get("Phone");
                    String PrivateKey = md5(Thename + ThePhone).substring(0,16);
                    String PublicKey = new StringBuffer(PrivateKey).reverse().toString();
                    String Identifier = md5(ThePhone + Thename);
                    SavetheValues(PrivateKey, PublicKey, Identifier);
                    RegisterMe();
                }
            }

        };

        public void RegisterMe(){
            ConnectivityManager connMgr = Intro.connectivity;
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected()){
                Alert("Unable to connect to the internet, please check your internet connection");
            }else{
                //we have an internet connection let's go to registration
                Intro intr = new Intro();
                DownloadWebpage task = new DownloadWebpage();
                task.execute(intr.getURL());
            }
        }

        class DownloadWebpage extends AsyncTask<String, Void, Integer> {

            // arguments are given by execute() method call (defined in the parent): params[0] is the url.
            protected Integer doInBackground(String... urls) {
                Integer st = null;
                try {

                    Connection Connect = new Connection("POST",urls[0]);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    Map Treasures = ReadTheIdentifier();
                    params.add(new BasicNameValuePair("Identifier", Treasures.get("Identifier").toString()));
                    String PrivateKey = Treasures.get("PrivateKey").toString();
                    String PublicKey = Treasures.get("PublicKey").toString();
                    MagicMessuper Encoder = new MagicMessuper(PrivateKey, PublicKey);
                    for (Map.Entry<String, String> entry : IntroFragment3.this.theArray.entrySet()) {
                        String Value =  Encoder.bytesToHex(Encoder.encrypt(entry.getValue()));
                        params.add(new BasicNameValuePair(entry.getKey(),Value));
                    }
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
                if (result != null && result == 200){
                    Successmessage("Registration successfully completed");
                    SharedPreferences sharedPref = Intro.getAppContext().getSharedPreferences(
                            getString(R.string.PrivateKeys), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("Intro", false);
                    // record the fact that the app has been started at least once
                    editor.commit();
                    Intent intent = new Intent(Intro.gettheActivity(), main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Intro.gettheActivity().finish();
                }
                else{
                    Alert("Connection to the server has failed");
                }

            }

        }

        public static String md5(String s)
        {
            MessageDigest digest;
            try
            {
                digest = MessageDigest.getInstance("MD5");
                digest.update(s.getBytes(),0,s.length());
                String hash = new BigInteger(1, digest.digest()).toString(16);
                return hash;
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            return "";
        }

        public void SavetheValues(String PrivateKey, String PublicKey, String Identifier){
            SharedPreferences sharedPref = Intro.getAppContext().getSharedPreferences(
                    getString(R.string.PrivateKeys), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("PrivateKey", PrivateKey);
            editor.putString("PublicKey", PublicKey);
            editor.putString("Identifier", Identifier);
            editor.commit();
        }

        public Map  ReadTheIdentifier(){
            SharedPreferences sharedPref = Intro.getAppContext().getSharedPreferences(
                    getString(R.string.PrivateKeys), Context.MODE_PRIVATE);
            Map<String, String> theTreasures = new HashMap<String, String>();
            theTreasures.put("Identifier", sharedPref.getString("Identifier", null));
            theTreasures.put("PrivateKey", sharedPref.getString("PrivateKey", null));
            theTreasures.put("PublicKey", sharedPref.getString("PublicKey", null));
            return theTreasures;
        }

        public boolean FormValidation(View rootView){

            int minLength = 5;
            EditText name = (EditText )rootView.findViewById(R.id.name);
            this.theArray.put("FullName", name.getText().toString());
            EditText phone = (EditText )rootView.findViewById(R.id.phone);
            this.theArray.put("PhoneNumber", phone.getText().toString());
            EditText address = (EditText )rootView.findViewById(R.id.address);
            this.theArray.put("Address", address.getText().toString());
            EditText city = (EditText )rootView.findViewById(R.id.city);
            this.theArray.put("City", city.getText().toString());
            EditText country = (EditText )rootView.findViewById(R.id.country);
            this.theArray.put("Country", country.getText().toString());
            EditText company = (EditText )rootView.findViewById(R.id.company);
            this.theArray.put("Company",company.getText().toString());
            Boolean Flag = true;
            for (Map.Entry<String, String> entry : this.theArray.entrySet()) {
                if (entry.getValue() == null || entry.getValue().isEmpty() || entry.getValue().length() < minLength){
                    Alert("Please fill the " + entry.getKey() + " field correctly.");
                    Flag = false;
                    break;
                }
            }
            return Flag;
        }

        public void Alert(String text){
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(Intro.getAppContext(), text, duration);
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
            Toast toast = Toast.makeText(Intro.getAppContext(), text, duration);
            toast.setGravity(Gravity.TOP, 0, 10);
            toast.getView().setBackgroundColor(Color.GREEN);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextColor(Color.BLACK);
            toastTV.setTextSize(16);
            toast.show();
        }

    }




}