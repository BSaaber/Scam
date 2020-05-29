package e.max_1l.tabbedphmath;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import ru.noties.jlatexmath.JLatexMathDrawable;

public class MainActivity extends AppCompatActivity implements KeyBoard1Fragment.OnKeyBoard1FragmentListener, KeyBoard2Fragment.OnKeyBoard2FragmentListener, KeyBoard3Fragment.OnKeyBoard3FragmentListener {
    public String eq = "";
    public String x = "";
    private boolean first = true;
    public ArrayList<String> answerList = new ArrayList<>();
    Handler h;
    public TabLayout tabLayout;
    public static boolean needsToChange = false;
    public static String changingEq = "";
    public static String changingX = "";
    public static ArrayList<String> tempHistoryList;
    FloatingActionButton fab;
    InputFragment inputFragment;
    InfoFragment infoFragment;
    SolutionFragment solutionFragment;


    public static SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String HISTORY = "history";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //меняем тему, с загрузочной (SplashScreen) на обычную
        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.mSettings
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this); //adapter
        ////InputFragment tf = (InputFragment) mSectionsPagerAdapter.getItem(0);
        ////eq = tf.eq;
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter); //viewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (i == 0){
                    fab.hide();
                }
                else if (i == 1)
                    fab.show();
                else if (i == 2){
                    if (compute()) {
                        if (tempHistoryList.size() > 0) {
                            if (!tempHistoryList.get(tempHistoryList.size() - 1).equals(eq)) {
                                historyUpdate();
                            }
                        }
                        else {
                            historyUpdate();
                        }
                    }
                    fab.hide();
                }
            }

            private void historyUpdate(){
                addToHistory(eq);
                tempHistoryList.add(eq);
                solutionFragment.recyclerView.getAdapter().notifyDataSetChanged();
            }


            @Override
            public void onPageSelected(int i) {

            }


            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });




        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                TabLayout.Tab tab = tabLayout.getTabAt(2);
                tab.select();
                Log.d("eq1", "Changed");
            }
        });


        //считываем фрагменты
        inputFragment =(InputFragment) mSectionsPagerAdapter.fragments.get(0);
        solutionFragment = (SolutionFragment) mSectionsPagerAdapter.fragments.get(1);
        infoFragment = (InfoFragment) mSectionsPagerAdapter.fragments.get(2);

        h = new Handler() {   // хэндлер для установки сообщений в полях вывода
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                compute();
            }
        };

        class InputThread extends Thread {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        Thread.sleep(500);
                        h.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        }


        InputThread inputThread = new InputThread();
        inputThread.start();  //запускаем поток  вывода

        tempHistoryList = getLinesList();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        super.onStop();
        makeSharedPreferences(tempHistoryList);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean compute(){
        try {
            if (first){
                first = false;
                inputFragment.latexView.setLatex("memento\\:mori");
            }
            if (needsToChange){
                inputFragment.editEq.setText(changingEq);
                inputFragment.editX.setText(changingX);
                eq = changingEq;
                x = changingX;
                needsToChange = false;
            }
            else {
                eq = inputFragment.eq;
                x = inputFragment.x;
            }

            EquationBuilder equationBuilder = new EquationBuilder(eq);
            equationBuilder.setVariable(x);
            answerList = equationBuilder.solve();
            infoFragment.lines = answerList;
            inputFragment.latexView.setLatex(answerList.get(answerList.size() - 1));
            Log.d("eq1", "Success");
            return true;
        }
        catch (Exception e){
            Log.d("eq1", "Error");
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnDoKeyBoard1FragmentListener(String msg, int type) {
        int t = inputFragment.editEq.getSelectionStart();
        if (type == 0 || type == 4 || type == 5){
            String text = inputFragment.editEq.getText().toString();
            if (true) {
                inputFragment.editEq.setText(text.substring(0, t) + msg + text.substring(t));
                if (type != 5) {
                    inputFragment.editEq.setSelection(t + 1);
                }
                else {
                    inputFragment.editEq.setSelection(t + 2);
                }
            }
        }
        else if (type == 1){
            if (t != 0){
                inputFragment.editEq.setSelection(t - 1);
            }
        }
        else if (type == 2){
            if (t != inputFragment.editEq.getText().length()){
                inputFragment.editEq.setSelection(t + 1);
            }
        }
        else if (type == 3){
            String text = inputFragment.editEq.getText().toString();
            if (text.length() != 0 && t != 0) {
                inputFragment.editEq.setText(text.substring(0, t - 1) + text.substring(t));
                inputFragment.editEq.setSelection(t - 1);
            }
        }
        else if (type == 6){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.keyboard_container, new KeyBoard2Fragment());
            ft.commit();
        }

    }

    private void OnKB23Computing(String msg, int type, int fragment){
        int t = inputFragment.editEq.getSelectionStart();
        if (fragment == 3){
            msg = msg.toUpperCase();
        }

        if (type == 0 || type == 5){
            String text = inputFragment.editEq.getText().toString();
            if (true) {
                inputFragment.editEq.setText(text.substring(0, t) + msg + text.substring(t));
                if (type != 5) {
                    inputFragment.editEq.setSelection(t + 1);
                }
                else {
                    inputFragment.editEq.setSelection(t + 2);
                }
            }
        }
        else if (type == 1){
            if (t != 0){
                inputFragment.editEq.setSelection(t - 1);
            }
        }
        else if (type == 2){
            if (t != inputFragment.editEq.getText().length()){
                inputFragment.editEq.setSelection(t + 1);
            }
        }
        else if (type == 3){
            String text = inputFragment.editEq.getText().toString();
            if (text.length() != 0 && t != 0) {
                inputFragment.editEq.setText(text.substring(0, t - 1) + text.substring(t));
                inputFragment.editEq.setSelection(t - 1);
            }
        }
        else if (type == 6){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.keyboard_container, new KeyBoard1Fragment());
            ft.commit();
        }
        else if (type == 7){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (fragment == 2){
                ft.replace(R.id.keyboard_container, new KeyBoard3Fragment());
            }
            else if (fragment == 3){
                ft.replace(R.id.keyboard_container, new KeyBoard2Fragment());
            }

            ft.commit();
        }
    }

    @Override
    public void OnDoKeyBoard2FragmentListener(String msg, int type) {
        OnKB23Computing(msg, type, 2);
    }

    @Override
    public void OnDoKeyBoard3FragmentListener(String msg, int type) {
        OnKB23Computing(msg, type, 3);
    }

    /**SectionsPagerAdapter
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public ArrayList<Fragment> fragments = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, MainActivity ma) {
            super(fm);
            fragments.add(new InputFragment());
            SolutionFragment sf = new SolutionFragment();
            sf.setMainActivity(ma);
            sf.setTab(tabLayout);
            fragments.add(sf);
            fragments.add(new InfoFragment());
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);

            return fragments.get(position);
        }



        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


    }


    public static ArrayList<String> getLinesList(){
        String s = mSettings.getString(MainActivity.HISTORY, "");
        if (s != null) {
            if (!s.equals("")) {

                ArrayList<String> r = new ArrayList<>(Arrays.asList(s.split("@")));
                if (r.size() > 10){
                    r.remove(0);
                }
                return r;
            }

        }
        return new ArrayList<>();
    }

    public static boolean addToHistory(String s){
        if (!s.equals("")) {
            String history = MainActivity.mSettings.getString(MainActivity.HISTORY, "");
            if (!historyContains(s)) {
                SharedPreferences.Editor editor = MainActivity.mSettings.edit();
                editor.putString(HISTORY, history + "@" + s); //не возникает ли пустого пункта?
                editor.apply();
                return true;
            }
        }
        return false;
    }

    public static boolean historyContains(String s){
        return getLinesList().contains(s);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void makeSharedPreferences(ArrayList<String> list){
        String r = String.join("@", list);



        SharedPreferences.Editor editor = MainActivity.mSettings.edit();
        editor.putString(HISTORY, r); //не возникает ли пустого пункта?
        editor.apply();
    }


}
