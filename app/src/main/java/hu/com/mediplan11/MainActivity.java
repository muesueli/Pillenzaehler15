package hu.com.mediplan11;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/* generated application1
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
*/


// icons https://design.google.com/icons/   select icon, download
// device monitor: storage/sdcard/bookbuyMedi
// cd C:\Users\hu\AppData\Local\Android\sdk\platform-tools
// adb shell
//        root@generic:/ # cd storage
//        root@generic:/storage # cd sdcard
//        root@generic:/storage/sdcard # ls -R
//        root@generic:/storage/sdcard # rm -r medi

// samsung path: /data/data/hu.com.mediplan10/files/medi  --- internal storage
//               /mnt/shell/emulated/0/medi --- external mit adb
// C:\Users\hu\javalib\eclipse\android\MediPlan10\app\src\main\res\raw

// emulator path:  data/data ...
// sony path:
//      intern: /data/user/0/hu.com.mediplan10/files/medi/meditable
//      extern: /storage/emulated/0/medi
//      /storage/emulated/0/medi
// 192.168.1.112.55432

//neues Projekt
//libaries: include validator, guava, lucene
// ctrl/shift/Neu/S
// dup modules
// nach Neuen app namen suchen

// see C:\Users\hu\javalib\eclipse\android\RecipeComputer01\app\build.gradle
//dependencies {
//        compile fileTree(dir: 'libs', include: ['*.jar'])
//        compile 'com.android.support:appcompat-v7:22.0.0'
//        compile files('libs/commons-validator-1.4.0.jar')
//        compile files('libs/guava-18.0.jar')
//        compile files('libs/lucene-analyzers-common-5.0.0.jar')
//        }
//android {
//        packagingOptions {
//        exclude 'META-INF/LICENSE.txt'
//        exclude 'META-INF/NOTICE.txt'
//        }


// gson: copy jars to libs, add as library
// imitiere gradle build

//manifest: share send intent
//resources

//use adjustResize instead of adjustPan in the Manifest
//Surround your xml layout with a ScrollView
//Also specify a android:minHeight for your EditText

public class MainActivity extends AppCompatActivity {

    public static List<Medi> mediList = new ArrayList<>();
    static DateTimeFormatter dateTooltipFormat = DateTimeFormatter.ofPattern("dd.MM");
    static DateTimeFormatter dateMultilineFormat = DateTimeFormatter.ofPattern("dd.MM.yy");
    public Parms parms;
    public AlertDialog updateDialog;
    public AlertDialog planDatumDialog;
    public AlertDialog bestandDatumDialog;
    public InputMethodManager imm;
    public int popup_row_id;
    public int bookbuyRowIid;
    public TableRow bookbuyRow;
    public Medi bookbuyMedi;
    public Medi pendingMedi;
    public TextView medikamentTv;
    public WebView chartWebView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public DatePicker datePickerBisMedi;
    public DatePicker datePickerAbMedi;
    TableLayout medi_table;
    TextView bestandHeaderTv;
    TextView bestandDatumTv;
    TextView planHeaderTv;
    TextView planDatumTv;

    //String rawMediList = "meditableplay";
    //String rawParms = "mediparmsplay";
    String rawMediList = "meditable0205";
    String rawParms = "mediparms0205";
    // String rawMediList = "meditablez";
    // String rawParms = "mediparmsz";
    String filenameMediList = "meditable";
    String filenameParms = "mediparms";
    // KOLONNEN INDICES
    int SEQ = 0;
    int MEDIKAMENT = 1;
    int PACKUNG = 2;
    int KAUFPLUS = 3;
    int BESTANDIST = 4;
    int table_primary_light;
    // WebAppInterface Map<String, Medi> timelines = new HashMap<>();
    TextView[] headerTv = new TextView[11];
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy", Locale.GERMAN);

    public static String getLN() {
        String s = " " + Thread.currentThread().getStackTrace()[3].getLineNumber()
                + " " + Thread.currentThread().getStackTrace()[4].getLineNumber();
        return s;
    }

    protected void onPause() {
        super.onPause();
        System.out.println("******** onPause start ******** " + getLN());
        refreshAllx();
        System.out.println("******** onPause end ********" + getLN());
    }

    /*********************************************************/

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        System.out.println("###### onResume start ********" + getLN());

// external storage
//        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            Toast.makeText(this, "Cannot use storage.", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//        rootPath = new File(Environment.getExternalStorageDirectory(), dir);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

// internal storage ohne spezielles directory
        // cleanfiles(null);

        File fileParms = new File(getFilesDir(), filenameParms);
        try {
            System.out.println("###### onResume0 fileParms exists " + fileParms.exists() + ", "
                    + fileParms.getAbsolutePath() + " dir: " + getFilesDir() + " fn: " + filenameParms);
            if (!fileParms.exists()) {
                saveMediList(null);
                saveParms(null);
                System.out.println("###### onResume1 fileParms exists " + fileParms.exists() + ", "
                        + fileParms.getAbsolutePath() + " dir: " + getFilesDir() + " fn: " + filenameParms);
                return;
            }
        } catch (Exception e) {
            System.out.println("###### onResume exception2 ************* " + e);
            return;
        }
        refreshAllx();
        System.out.println("###### onResume3 fileParms exists " + fileParms.exists() + ", "
                + fileParms.getAbsolutePath() + " dir: " + getFilesDir() + " fn: " + filenameParms);
    }


 /* WebAppInterface --- laeuft in anderem thread
    public class WebAppInterface {

        Context context;

        WebAppInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void timelineClick(String rowNr) {

            pendingMedi = timelines.get(rowNr);

            PopupMenu popup = new PopupMenu(context, bestandHeaderTv);
            MenuInflater inflater = popup.getMenuInflater();

            inflater.inflate(R.menu.popup_mutation, popup.getMenu());
            popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                                           @Override
                                           public void onDismiss(PopupMenu popupMenu) {
                                               refreshAllx();
                                           }
                                       }
            );

            MenuItem kaufMi = popup.getMenu().findItem(R.id.kauf);
            MenuItem changeMi = popup.getMenu().findItem(R.id.change);
            MenuItem takebyneedMi = popup.getMenu().findItem(R.id.takebyneed);
            MenuItem discontinueMi = popup.getMenu().findItem(R.id.discontinue);
            MenuItem copyMi = popup.getMenu().findItem(R.id.copy);
            MenuItem removeMi = popup.getMenu().findItem(R.id.remove);
            if (pendingMedi.einsatz > 0) {
                kaufMi.setVisible(false);
                takebyneedMi.setVisible(false);
                discontinueMi.setVisible(false);
            }
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                public boolean onMenuItemClick(MenuItem item) {

                    if (item.getItemId() == R.id.change) {
                        isInsert = false;
                        proc_mediMutation();
                    }

                    if (item.getItemId() == R.id.discontinue) {
                        pendingMedi.datumGueltigAbCal = new GregorianCalendar();
                        float dosis = getDosisTag(pendingMedi);
                        long restDauer = 0;
                        if (dosis > 0f) {
                            restDauer = (long) (pendingMedi.bestandIst / dosis);
                        }
                        GregorianCalendar heute = new GregorianCalendar();
                        long milliTag = (1000 * 60 * 60 * 24);
                        long restDatumMilli = heute.getTimeInMillis() + restDauer * milliTag;
                        pendingMedi.datumGueltigBisCal.setTimeInMillis(restDatumMilli);
                        errorMsg("Vorrat " + pendingMedi.name, restDauer + " Tage, bis " + dateFormatter.format(pendingMedi.datumGueltigBisCal.getTime()));
                        pendingMedi.einsatz = Medi.ABGESETZT;
                        saveMediList(null);
                        refreshAllx();
                    }

                    if (item.getItemId() == R.id.takebyneed) {
                        pendingMedi.datumGueltigAbCal = new GregorianCalendar();
                        pendingMedi.datumGueltigBisCal = new GregorianCalendar(2099, 12, 31);
                        pendingMedi.einsatz = Medi.EINNAHME_BEI_BEDARF;
                        saveMediList(null);
                        refreshAllx();
                    }

                    if (item.getItemId() == R.id.copy) {
                        isInsert = true;
                        Medi newMedi = new Medi();
                        pendingMedi.copyTo(newMedi);
                        pendingMedi = newMedi;
                        proc_mediMutation();
                    }

                    if (item.getItemId() == R.id.remove) {
                        mediList.remove(pendingMedi);
                        saveMediList(null);
                        refreshAllx();
                    }

                    return false;
                }
            });
            popup.show();
            */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidThreeTen.init(this);

        System.out.println("******** oncreate start ********" + getLN());

        setContentView(R.layout.activity_main);

        /* war noch im 10
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {

                    @Override
                    public void uncaughtException(Thread thread, Throwable ex) {
                        String s = "Unhandled exception: " + ex.getMessage();
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        System.exit(1);
                    }
                });

        table_primary_light = ContextCompat.getColor(this, R.color.table_primary_light);

        imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);

        /*=== kopf ********************************************************====*/

        parms = new Parms();

        bestandHeaderTv = (TextView) findViewById(R.id.bestandHeaderTv);
        bestandDatumTv = (TextView) findViewById(R.id.bestandDatumTv);
        bestandDatumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proc_setupBestandDatum();
            }
        });

        planHeaderTv = (TextView) findViewById(R.id.planHeaderTv);
        planDatumTv = (TextView) findViewById(R.id.planDatumTv);
        planDatumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proc_setupPlanDatum();
            }
        });


        headerTv[0] = (TextView) findViewById(R.id.headerTv00);
        headerTv[1] = (TextView) findViewById(R.id.headerTv01);
        headerTv[2] = (TextView) findViewById(R.id.headerTv02);
        headerTv[3] = (TextView) findViewById(R.id.headerTv03);

        View view = this.getCurrentFocus(); // keyboard jua
        if (view != null) {
            this.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

        chartWebView = (WebView) findViewById(R.id.chartWv);
        chartWebView.setWebViewClient(new MyBrowser());
        chartWebView.getSettings().setJavaScriptEnabled(true);
        chartWebView.getSettings().setLoadWithOverviewMode(true);
        //chartWebView.getSettings().setLoadsImagesAutomatically(true);
        // WebAppInterface chartWebView.addJavascriptInterface(new WebAppInterface(this), "Huweb");
        //chartWebView.getSettings().setUseWideViewPort(true);
        //chartWebView.getSettings().setLoadWithOverviewMode(true);
        // chartWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        metrics();

//        chartWebView.loadData(s, "text/html; charset=UTF-8", null);

//        chartWebView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//
//            }
//        });

        medi_table = (TableLayout) findViewById(R.id.medi_table);

        System.out.println("******** onCreate end ********" + getLN());

    }

    private void metrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        System.out.println("metrics Display width/height " + metrics.widthPixels + "/" + metrics.heightPixels + ", density " + metrics.density);
        parms.chartWebViewWidth = chartWebView.getWidth();
        parms.chartWebViewHeight = chartWebView.getHeight();
        System.out.println("metrics WebView pixel width/height " + parms.chartWebViewWidth + "/" + parms.chartWebViewHeight);
        float heightPixels = ((float) (metrics.heightPixels) * 4f) / (metrics.density * 12); // weight WebView 5, total sum 12
        parms.chartWebViewHeightUnscaled = (int) heightPixels;
        System.out.println("metrics WebView pixels height unscaled " + parms.chartWebViewHeightUnscaled);
    }

    /*********************************************************
     * =======
     *//*
        }
    }
*/
    public String readRawHtml(String rawFileName) {
        byte[] bh = null;
        try {
            Resources res = getResources();
            int htmlId = this.getResources().getIdentifier(rawFileName, "raw", getPackageName());
            InputStream in_sp = res.openRawResource(htmlId);
            bh = new byte[in_sp.available()];
            in_sp.read(bh);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(bh);
    }

    /*********************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        System.out.println("******** onCreateOptionsMenu ======= " + getLN());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        View current = getCurrentFocus();
        if (current != null) current.clearFocus();

        switch (item.getItemId()) {
            case R.id.actions:
            case R.id.helpTop: {
                return true;
            }
        }

        switch (item.getItemId()) {

            case R.id.insert: {
                popup_row_id = -1;
                pendingMedi = new Medi();
                proc_mediMutation();
                break;
            }

            case R.id.kaufliste: {
                listeEinkauf();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, parms.einkaufliste);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            }

            case R.id.kontrolle: {
                proc_zukunft();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, parms.kontrollblatt);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            }

            case R.id.kosten: {
                listeKosten();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, parms.kostenliste);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            }
            case R.id.help: {
                displayHelpDialog();
                break;
            }
            case R.id.saveSample: {
                System.out.println("******** saveSample start ******** saves and reloads" + getLN());
                try {
                    Resources res = getResources();

                    int mediparmsId = this.getResources().getIdentifier(rawParms, "raw", getPackageName());
                    InputStream in_sp = res.openRawResource(mediparmsId); // R.raw.mediparms
                    byte[] bp = new byte[in_sp.available()];
                    in_sp.read(bp);
                    saveParms(new String(bp)); // saveSample
                    // parms.printParms("printParms saveSample");

                    int meditableId = this.getResources().getIdentifier(rawMediList, "raw", getPackageName());
                    InputStream in_sm = res.openRawResource(meditableId); // R.raw.meditable
                    byte[] bm = new byte[in_sm.available()];
                    in_sm.read(bm);
                    saveMediList(new String(bm));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                refreshAllx();

                System.out.println("******** saveSample end nach load ******** saved and reloaded" + getLN());
                break;
            }

/*

            case R.id.exportx: {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String txt = saveParms(null);
                txt += jsonTrenner;
                txt += saveMediList(null);
                sendIntent.putExtra(Intent.EXTRA_TEXT, txt);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            }
*/
        }

        return super.onOptionsItemSelected(item);
    }

    /*********************************************************/
    public void proc_zukunft() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null || netInfo.isConnectedOrConnecting()==false) {
            errorMsg("Internetverbindung anschalten","sie ist nicht verfügbar");
            return;
        }

        String s = readRawHtml("teil01");
        int lineCount = -1;

        String farbenReplacementString = "'#000000',";
        try {

// Balken Plan
            String[] planVon = editCal(parms.bestandCal);
            String[] planBis = editCal(parms.planBisCal);
            String tooltipBlack = "Dauer: " + (ChronoUnit.DAYS.between(parms.bestandCal, parms.planBisCal) + 1) + " Tage";
            s += String.format(
                    "    [ '--- ', '%s', '%s', new Date(%s), new Date(%s) ],\n",
                    "Plan " + planVon[1] + " - " + planBis[1], tooltipBlack, planVon[0], planBis[0]);

            parms.kontrollblatt =
                    "KONTROLLBLATT: " + planVon[1] + " - " + planBis[1] + "\n\n"
                            + "Lager/Plan        Medikament\n\n";

            for (int i = 0; i < mediList.size(); i++) {

                Medi medi = mediList.get(i);
                // System.out.println("zukunft =============================================================");
                // System.out.println("zukunft " + medi.toString());

                medi.kaufPackungen = 0;
                medi.nummerReihe = -1;

                if (medi.einsatz > 0) {
                    continue;
                }
                float dosisTag = getDosisTag(medi);
                if (dosisTag == 0f) {
                    continue;
                }

                if (medi.datumGueltigBisCal.isAfter(medi.datumGueltigBisCal)) {
                    errorMsg("Fehler gueltig: von > bis",
                            medi.datumGueltigAbCal.format(dateMultilineFormat) + " >" + medi.datumGueltigBisCal.format(dateMultilineFormat));
                    return;
                }

                if (medi.datumGueltigBisCal.isBefore(parms.bestandCal)) {
                    continue;
                }

                if (medi.datumGueltigAbCal.isAfter(parms.planBisCal)) {
                    continue;
                }

                LocalDate abCalGo;
                if (medi.datumGueltigAbCal.isBefore(parms.bestandCal)) {
                    abCalGo = parms.bestandCal;
                } else {
                    abCalGo = medi.datumGueltigAbCal;
                }

                LocalDate bisCalGo;
                if (medi.datumGueltigBisCal.isAfter(parms.planBisCal)) {
                    bisCalGo = parms.planBisCal;
                } else {
                    bisCalGo = medi.datumGueltigBisCal;
                }

                String vonBisString = "\n";
                if (!abCalGo.equals(parms.bestandCal) ||
                        !bisCalGo.equals(parms.planBisCal)) {
                    vonBisString = " (" + editCal(abCalGo)[1] + " - " + editCal(bisCalGo)[1] + ")\n";
                }

                lineCount++;
                medi.nummerReihe = lineCount;

                long bedarfTage = ChronoUnit.DAYS.between(abCalGo, bisCalGo) + 1;
                double bedarf = bedarfTage * dosisTag; // start,end inklusive
                String bedarfString = String.format("%d", (long) bedarf);

                long bestandIstTage = (long) Math.floor(medi.bestandIst / dosisTag);

                double rest = medi.bestandIst - bedarf;
                String restString = String.format("%d", (long) rest);
                long restTage = (long) (rest / dosisTag);

                double manko = bedarf - medi.bestandIst;
                String mankoString = String.format("%d", (long) manko);
                long mankoTage = (long) (manko / dosisTag);

                // shu stock high used
                // shr stock high rest
                // slu stock low used
                // slp stock low purchased


                if (medi.bestandIst >= bedarf) { // es langt

                    medi.farbIdxTemp = -1;
                    farbenReplacementString += parms.mediFarbeStringDefault + ", ";

                    String[] shu_VonString = editCal(abCalGo);
                    String[] shu_BisString = editCal(bisCalGo);
                    String[] shr_VonString = editCal(bisCalGo.plusDays(1));
                    String[] shr_BisString = editCal(bisCalGo.plusDays(1 + restTage - 1));
                    // WebAppInterface timelines.put("" + lineCount, medi);

                    if (medi.datumGueltigBisCal.isBefore(parms.planBisCal) || // beendet
                            medi.datumGueltigBisCal.equals(parms.planBisCal)) {

                        String tooltip = "Einnahme bis " + shu_BisString[1] + "<br/>Rest: " + restString + " &rarr;  " + restTage + " Tage ";
                        s += String.format(
                                "    [ '%d', '%s', '%s', new Date(%s), new Date(%s) ],\n",
                                lineCount, medi.name, tooltip, shu_VonString[0], shu_BisString[0]);
                    } else {

                        String tooltip = "Lager " + bedarfString + " &rarr; " + bedarfTage + " Tage " + shu_VonString[1] + " - " + shu_BisString[1];
                        if (restTage > 0) {
                            tooltip += "<br/>Rest   " + restString + " &rarr; " + restTage + " Tage  " + shr_VonString[1] + " - " + shr_BisString[1];
                        }
                        s += String.format(
                                "    [ '%d', '%s', '%s', new Date(%s), new Date(%s) ],\n",
                                lineCount, medi.name, tooltip, shu_VonString[0], shr_BisString[0]);
                    }
                    parms.kontrollblatt += ((int) medi.bestandIst) + "/" + ((int) (medi.bestandIst - bedarf)) + "   " + medi.name + vonBisString + "\n";

                } else {

                    medi.farbIdxTemp = medi.farbIdx;
                    farbenReplacementString += parms.mediFarbenString[medi.farbIdx] + ", ";

                    String packungen = " Packungen ";
                    medi.kaufPackungen = (int) Math.ceil(manko / medi.inhalt);
                    if (medi.kaufPackungen == 1) {
                        packungen = " Packung ";
                    }
                    long kaufTage = (long) ((medi.kaufPackungen * medi.inhalt) / dosisTag);

                    String[] slu_VonString = editCal(abCalGo);
                    String[] slu_BisString = editCal(abCalGo.plusDays(bestandIstTage - 1));
                    String[] slp_VonString = editCal(abCalGo.plusDays(bestandIstTage - 1 + 1));
                    String[] slp_BisString = editCal(abCalGo.plusDays(bestandIstTage + 1 + kaufTage - 1));

                    String tooltip = "";
                    if (bestandIstTage > 0f) {
                        tooltip = "Lager " + medi.bestandIst + " &rarr; " + bestandIstTage + " Tage " + slu_VonString[1] + " - " + slu_BisString[1] + "<br/>";
                    }
                    tooltip += "+ " + medi.kaufPackungen + packungen + " &rarr; " + kaufTage + " Tage " + slp_VonString[1] + " - "  + slp_BisString[1];
                    s += String.format(
                            "    [ '%d', '%s', '%s', new Date(%s), new Date(%s) ],\n",
                            lineCount, medi.name, tooltip, slu_VonString[0], slp_BisString[0]);

                    int bestandPlan = (int) (medi.bestandIst + (medi.kaufPackungen * medi.inhalt) - bedarf);
                    parms.kontrollblatt += ((int) medi.bestandIst) + "/" + bestandPlan + "  " + medi.name + vonBisString + "\n";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (lineCount == -1)

        { // nichts im diagramm
            return;
        }

        int s_Length = s.length();
        String s1 = s.substring(0, s_Length - 2);
        s1 += readRawHtml("teil02");

        String frs = farbenReplacementString.substring(0, farbenReplacementString.length() - 2);
        String s2 = s1.replace("farben", frs);

        metrics();

        String stilReplacement = "style=\"height: " + parms.chartWebViewHeightUnscaled + "px;\"";
        String s3 = s2.replace("stil", stilReplacement); // in div
        chartWebView.loadData(s3, "text/html; charset=UTF-8", null);
        System.out.println("zukunft " + s3);
    }

    public String[] editCal(LocalDate datum) {
        String[] temp = new String[2];
        temp[0] = "new Date(" + datum.getYear() + ", " + (datum.getMonthValue() - 1) + ", " + datum.getDayOfMonth() + ")";
        temp[1] = datum.format(dateTooltipFormat);
        return temp;
    }


    private void listeEinkauf() {

        String[] planVon = editCal(parms.bestandCal);
        String[] planBis = editCal(parms.planBisCal);
        parms.einkaufliste = "Kauf: Packungen für " + planVon[1] + " - " + planBis[1] + "\n\n";
        for (int i = 0; i < mediList.size(); i++) {
            Medi medi = mediList.get(i);
            if (medi.kaufPackungen > 0) {
                parms.einkaufliste += medi.kaufPackungen + " " + medi.name + " " + medi.specs + "\n\n";
            }
            if (medi.einsatz == Medi.EINNAHME_BEI_BEDARF) {
                parms.einkaufliste += "    " + medi.name + " " + medi.specs + "\n\n";
            }
        }
    }

    /*********************************************************/

    private void listeKosten() {
        parms.kostenliste = "KOSTEN \n\n";
        float totaltag = 0f;
        for (int i = 0; i < mediList.size(); i++) {
            Medi medi = mediList.get(i);
            if (medi.einsatz == 0) {
                float stueckkosten = medi.preis / medi.inhalt;
                float dosisTag = getDosisTag(medi);
                float tageskosten = getDosisTag(medi) * stueckkosten;
                totaltag += tageskosten;
                float jahreskosten = 365 * tageskosten;
                parms.kostenliste += medi.name + " " + medi.specs + "\n";
                parms.kostenliste += String.format("   Pkg. %d Preis %.2f pro Stück %.2f\n", medi.inhalt, medi.preis, stueckkosten);
                parms.kostenliste += String.format("   Tagesdosis %.2f", dosisTag);
                parms.kostenliste += String.format(" Kosten %.2f\n", tageskosten);
                parms.kostenliste += String.format("   pro Jahr %.2f\n\n", jahreskosten);
            }
        }
        float totaljahr = 365 * totaltag;
        parms.kostenliste += String.format("Total pro Tag %.2f, pro  Jahr %.2f", totaltag, totaljahr);
    }

    public float getDosisTag(Medi medi) {
        float dosisTag = 0f;
        for (int i = 0; i < 4; i++) {
            dosisTag += medi.dosis[i];
        }
        return dosisTag;
    }

    public void saveParms(String jsonParms) {

        if (jsonParms == null) {
            final Gson gson = new Gson();
            // converting an object to json object
            jsonParms = gson.toJson(parms);
        }
        try {
            File outputFileParms = new File(getFilesDir(), filenameParms);
            System.out.println("###### saveMediList0 outputFileTable exists " + outputFileParms.exists() + ", "
                    + outputFileParms.getAbsolutePath() + " dir: " + getFilesDir() + " fn: " + filenameMediList);
            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFileParms), "UTF-8"));
            System.out.println("###### saveMediList1 outputFileTable exists " + outputFileParms.exists() + ", "
                    + outputFileParms.getAbsolutePath() + " dir: " + getFilesDir() + " fn: " + filenameMediList);
            out.write(jsonParms);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("******** saveParms end ********" + getLN());
        return;
    }

    /*********************************************************/

    public void saveMediList(String jsonMediList) {

        if (jsonMediList == null) {
            if (mediList.size() > 0) {
                proc_zukunft();
            }
            final Gson gson = new Gson();
            // converting an object to json object
            jsonMediList = gson.toJson(mediList);
        }
        try {
            File outputFileTable = new File(getFilesDir(), filenameMediList);
            System.out.println("###### saveMediList0 outputFileTable exists " + outputFileTable.exists() + ", "
                    + outputFileTable.getAbsolutePath() + " dir: " + getFilesDir() + " fn: " + filenameMediList);
            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFileTable), "UTF-8"));
            out.write(jsonMediList);
            out.close();
            System.out.println("###### saveMediList1 outputFileTable exists " + outputFileTable.exists() + ", "
                    + outputFileTable.getAbsolutePath() + " dir: " + getFilesDir() + " fn: " + filenameMediList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    /*********************************************************/

    public void load() {

        System.out.println("******** load start ********" + getLN());
        final Gson gson = new Gson();

        try {
            File inputFileParms = new File(getFilesDir(), filenameParms);
            System.out.println("###### load inputFileParms0 exists " + inputFileParms.exists() + ", "
                    + inputFileParms.getAbsolutePath() + " dir: " + getFilesDir() + " fn: " + filenameParms);
            InputStream inputStream = new FileInputStream(inputFileParms);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                json.append(line);
            }
            inputStream.close();
            System.out.println("###### load inputFileParms1 exists " + inputFileParms.exists() + ", "
                    + inputFileParms.getAbsolutePath() + " dir: " + getFilesDir() + " fn: " + filenameParms);
            // converting json to object
            parms = gson.fromJson(json.toString(), Parms.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String datumBestand = parms.bestandCal.format(dateMultilineFormat);
        bestandDatumTv.setText(datumBestand);
        String datumPlan = parms.planBisCal.format(dateMultilineFormat);
        planDatumTv.setText(datumPlan);

        medi_table.removeViews(0, medi_table.getChildCount());
        mediList.clear();

        try {
            File inputFileTable = new File(getFilesDir(), filenameMediList);
            System.out.println("###### load inputFileTable0 exists " + inputFileTable.exists() + ", "
                    + inputFileTable.getAbsolutePath() + " dir: " + getFilesDir() + " fn: " + filenameParms);
            InputStream inputStream = new FileInputStream(inputFileTable);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonTable = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                jsonTable.append(line);
            }
            inputStream.close();
            System.out.println("###### load inputFileTable1 exists " + inputFileTable.exists() + ", "
                    + inputFileTable.getAbsolutePath() + " dir: " + getFilesDir() + " fn: " + filenameParms);

            Type collectionType2 = new TypeToken<List<Medi>>() {
            }.getType();
            mediList = gson.fromJson(jsonTable.toString(), collectionType2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        invalidateOptionsMenu();
        System.out.println("******** load end ********" + getLN());
    }

    /*********************************************************/

    public void displayHelpDialog() {
/*
        if (localeSprache.equals("en")) {
            helpHtml = Data.helpAnfang + Data.helpD;
            titel = "Help";
        }
        if (localeSprache.equals("fr")) {
            helpHtml = Data.helpAnfang + Data.helpD;
            titel = "Aide";
*/

        String titel = "Medikamente planen mit dem Pillen-Zähler";
        WebView myWebView = new WebView(MainActivity.this);
        myWebView.loadUrl("file:///android_asset/helpd.html");

        new AlertDialog.Builder(MainActivity.this).setView(myWebView).setTitle(titel).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).show();
    }

    public void errorMsg(String title, String msg) {

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.back),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;

    }

    public void toastIt(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 500, 500);
        toast.show();
    }

    /*********************************************************/

    public long millisFromLocalDate(LocalDate localDate) {
        String datumString = localDate.format(dateMultilineFormat);
        Date date = null;
        try {
            date = simpleDateFormat.parse(datumString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        long timeInMillis = calendar.getTimeInMillis();
        return timeInMillis;
    }

    public void proc_setupBestandDatum() {

        LayoutInflater inflater = getLayoutInflater();
        final View parmsDatumLayout = inflater.inflate(R.layout.medi_parmsdatum, null);

        final DatePicker parmsDatumDp = (DatePicker) parmsDatumLayout.findViewById(R.id.parmsDatumDp);
        parmsDatumDp.setMaxDate(millisFromLocalDate(parms.planBisCal));
        parmsDatumDp.updateDate(parms.bestandCal.getYear(), parms.bestandCal.getMonthValue() - 1, parms.bestandCal.getDayOfMonth());

        AlertDialog.Builder alertParmsDatum = new AlertDialog.Builder(this);
        alertParmsDatum.setTitle("Lager");
        alertParmsDatum.setView(parmsDatumLayout);
        alertParmsDatum.setCancelable(false); // click neben fenster scliesst dialog nicht
        alertParmsDatum.setPositiveButton(android.R.string.ok, null);
        alertParmsDatum.setNegativeButton(android.R.string.cancel, null);

        bestandDatumDialog = alertParmsDatum.create();
        bestandDatumDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button bp = bestandDatumDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                bp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        parms.bestandCal = LocalDate.of(parmsDatumDp.getYear(),
                                parmsDatumDp.getMonth() + 1,
                                parmsDatumDp.getDayOfMonth());

                        bestandDatumTv.setText(parms.bestandCal.format(dateMultilineFormat));
                        saveParms(null);
                        saveMediList(null);
                        refreshAllx();
                        bestandDatumDialog.dismiss();

                    }
                });
                Button bn = bestandDatumDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                bn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bestandDatumDialog.dismiss();
                    }
                });
            }
        });

        bestandDatumDialog.show();
    }

    /**********************************************************/

    public void proc_setupPlanDatum() { // field pendingMedi set

        LayoutInflater inflater = getLayoutInflater();
        final View parmsDatumLayout = inflater.inflate(R.layout.medi_parmsdatum, null);

        final DatePicker parmsDatumDp = (DatePicker) parmsDatumLayout.findViewById(R.id.parmsDatumDp);
        parmsDatumDp.setMinDate(millisFromLocalDate(parms.bestandCal));
        parmsDatumDp.updateDate(parms.planBisCal.getYear(), parms.planBisCal.getMonthValue() - 1, parms.planBisCal.getDayOfMonth());

        AlertDialog.Builder alertParmsDatum = new AlertDialog.Builder(this);
        alertParmsDatum.setTitle("Plandatum");
        alertParmsDatum.setView(parmsDatumLayout);
        alertParmsDatum.setCancelable(false); // click neben fenster scliesst dialog nicht
        alertParmsDatum.setPositiveButton(android.R.string.ok, null);
        alertParmsDatum.setNegativeButton(android.R.string.cancel, null);

        planDatumDialog = alertParmsDatum.create();
        planDatumDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button bp = planDatumDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                bp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        parms.planBisCal = LocalDate.of(parmsDatumDp.getYear(),
                                parmsDatumDp.getMonth() + 1,
                                parmsDatumDp.getDayOfMonth());
                        planDatumTv.setText(parms.planBisCal.format(dateMultilineFormat));
                        saveParms(null);
                        saveMediList(null);
                        refreshAllx();
                        planDatumDialog.dismiss();
                    }
                });
                Button bn = planDatumDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                bn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        planDatumDialog.dismiss();
                    }
                });
            }
        });
//        planDatumDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); jua
//        WindowManager.LayoutParams wmlp = planDatumDialog.getWindow().getAttributes();
//        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        // wmlp.x = 100;   //x position
        // wmlp.y = 100;   //y position

        planDatumDialog.show();
    }

    /**********************************************************/
    public void proc_mediMutation() { // field pendingMedi set

        LayoutInflater inflater = getLayoutInflater();
        final View updateLayout = inflater.inflate(R.layout.medi_mutation, null);

        EditText modNameEt = (EditText) updateLayout.findViewById(R.id.modNametEt);
        EditText modSpecsEt = (EditText) updateLayout.findViewById(R.id.modSpecsEt);
        TextView modEinsatzTv = (TextView) updateLayout.findViewById(R.id.modEinsatzTv);
        EditText modInhaltEt = (EditText) updateLayout.findViewById(R.id.modInhaltEt);
        EditText modPreisEt = (EditText) updateLayout.findViewById(R.id.modPreisEt);
        EditText modBestandIstEt = (EditText) updateLayout.findViewById(R.id.modBestandIstEt);
        EditText modDosisMoEt = (EditText) updateLayout.findViewById(R.id.modDosisMoEt);
        EditText modDosisMiEt = (EditText) updateLayout.findViewById(R.id.modDosisMiEt);
        EditText modDosisAbEt = (EditText) updateLayout.findViewById(R.id.modDosisAbEt);
        EditText modDosisNaEt = (EditText) updateLayout.findViewById(R.id.modDosisNaEt);
        EditText modDosisAndEt = (EditText) updateLayout.findViewById(R.id.modDosisAndEt);
        final TextView modDatumGueltigAbTv = (TextView) updateLayout.findViewById(R.id.modDatumGueltigAbTv);
        final TextView modDatumGueltigBisTv = (TextView) updateLayout.findViewById(R.id.modDatumGueltigBisTv);

        modNameEt.setText(pendingMedi.name);
        modSpecsEt.setText(pendingMedi.specs);
        modEinsatzTv.setText(Medi.einsatzString[pendingMedi.einsatz]);
        modInhaltEt.setText(Integer.toString(pendingMedi.inhalt));
        modPreisEt.setText(Float.toString(pendingMedi.preis));
        modBestandIstEt.setText(Float.toString(pendingMedi.bestandIst));
        modDosisMoEt.setText(Float.toString(pendingMedi.dosis[Medi.DOSIS_MO]));
        modDosisMiEt.setText(Float.toString(pendingMedi.dosis[Medi.DOSIS_MI]));
        modDosisAbEt.setText(Float.toString(pendingMedi.dosis[Medi.DOSIS_AB]));
        modDosisNaEt.setText(Float.toString(pendingMedi.dosis[Medi.DOSIS_NA]));
        modDosisAndEt.setText(Float.toString(pendingMedi.dosis[Medi.DOSIS_AND]));

        Medi.calendar2string(pendingMedi.datumGueltigAbCal, modDatumGueltigAbTv, LocalDate.MIN);
        Medi.calendar2string(pendingMedi.datumGueltigBisCal, modDatumGueltigBisTv, LocalDate.MAX);

        /*************************==ab********************************=====*/

        modDatumGueltigBisTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datumGueltigBisDpd = getBisDatePickerDialog(modDatumGueltigBisTv);
                datumGueltigBisDpd.show();
            }
        });

        /******************************************************/

        modDatumGueltigAbTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datumGueltigAbDpd = geAbtDatePickerDialog(modDatumGueltigAbTv);
                datumGueltigAbDpd.show();
            }
        });

        /*********************************************************=======*/

        AlertDialog.Builder alertUpdate = new AlertDialog.Builder(this);
        // alertUpdate.setTitle(getResources().getString(R.string.updateTitle));
        alertUpdate.setView(updateLayout);
        alertUpdate.setCancelable(false); // click neben fenster scliesst dialog nicht
        alertUpdate.setPositiveButton(android.R.string.ok, null);
        alertUpdate.setNegativeButton(android.R.string.cancel, null);

        updateDialog = alertUpdate.create();
        updateDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button bp = updateDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                bp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {

                            Medi tempMedi = new Medi();

                            tempMedi.name = ((EditText) updateLayout.findViewById(R.id.modNametEt)).getText().toString();
                            tempMedi.specs = ((EditText) updateLayout.findViewById(R.id.modSpecsEt)).getText().toString();

                            int testint = validInteger("Fehler Inhalt", (EditText) updateLayout.findViewById(R.id.modInhaltEt));
                            if (testint != Integer.MIN_VALUE) {
                                tempMedi.inhalt = testint;
                            } else {
                                return;
                            }

                            float testfloat = validFloat("Fehler Preis", (EditText) updateLayout.findViewById(R.id.modPreisEt));
                            if (testfloat != Float.MIN_VALUE) {
                                tempMedi.preis = testfloat;
                            } else {
                                return;
                            }

                            testfloat = validFloat("Fehler Lager", (EditText) updateLayout.findViewById(R.id.modBestandIstEt));
                            if (testfloat != Float.MIN_VALUE) {
                                tempMedi.bestandIst = testfloat;
                            } else {
                                return;
                            }

                            testfloat = validFloat("Fehler Dosis morgens", (EditText) updateLayout.findViewById(R.id.modDosisMoEt));
                            if (testfloat != Float.MIN_VALUE) {
                                tempMedi.dosis[Medi.DOSIS_MO] = testfloat;
                            } else {
                                return;
                            }
                            testfloat = validFloat("Fehler Dosis mittags", (EditText) updateLayout.findViewById(R.id.modDosisMiEt));
                            if (testfloat != Float.MIN_VALUE) {
                                tempMedi.dosis[Medi.DOSIS_MI] = testfloat;
                            } else {
                                return;
                            }
                            testfloat = validFloat("Fehler Dosis abends", (EditText) updateLayout.findViewById(R.id.modDosisAbEt));
                            if (testfloat != Float.MIN_VALUE) {
                                tempMedi.dosis[Medi.DOSIS_AB] = testfloat;
                            } else {
                                return;
                            }
                            testfloat = validFloat("Fehler Dosis nachts", (EditText) updateLayout.findViewById(R.id.modDosisNaEt));
                            if (testfloat != Float.MIN_VALUE) {
                                tempMedi.dosis[Medi.DOSIS_NA] = testfloat;
                            } else {
                                return;
                            }

                            testfloat = validFloat("Fehler Dosis andere", (EditText) updateLayout.findViewById(R.id.modDosisAndEt));
                            if (testfloat != Float.MIN_VALUE) {
                                tempMedi.dosis[Medi.DOSIS_AND] = testfloat;
                            } else {
                                return;
                            }

                            pendingMedi.name = tempMedi.name;
                            pendingMedi.specs = tempMedi.specs;
                            pendingMedi.inhalt = tempMedi.inhalt;
                            pendingMedi.preis = tempMedi.preis;
                            pendingMedi.bestandIst = tempMedi.bestandIst;

                            pendingMedi.dosis[Medi.DOSIS_MO] = tempMedi.dosis[Medi.DOSIS_MO];
                            pendingMedi.dosis[Medi.DOSIS_MI] = tempMedi.dosis[Medi.DOSIS_MI];
                            pendingMedi.dosis[Medi.DOSIS_AB] = tempMedi.dosis[Medi.DOSIS_AB];
                            pendingMedi.dosis[Medi.DOSIS_NA] = tempMedi.dosis[Medi.DOSIS_NA];
                            pendingMedi.dosis[Medi.DOSIS_AND] = tempMedi.dosis[Medi.DOSIS_AND];

                            if (popup_row_id == -1) {
                                pendingMedi.farbIdx = parms.farbenCount % parms.mediFarbenInt.length;
                                parms.farbenCount++;
                                saveParms(null);
                                mediList.add(pendingMedi);
                            }

                            saveMediList(null);
                            refreshAllx();
                            updateDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Button bn = updateDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                bn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        refreshAllx(); // loescht selektion
                        updateDialog.dismiss();
                    }
                });
            }
        });
        updateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = updateDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        // wmlp.x = 100;   //x position
        // wmlp.y = 100;   //y position

        updateDialog.show();
    }

    private DatePickerDialog getBisDatePickerDialog(final TextView modDatumGueltigBisTv) {
        LocalDate initBisLocalDate;
        if (pendingMedi.datumGueltigBisCal.equals(LocalDate.MAX)) {
            if (!pendingMedi.datumGueltigAbCal.equals(LocalDate.MIN)) {
                initBisLocalDate = pendingMedi.datumGueltigAbCal;
            } else {
                initBisLocalDate = LocalDate.now();
            }
        } else {
            initBisLocalDate = pendingMedi.datumGueltigBisCal;
        }

        final DatePickerDialog datumGueltigBisDpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                LocalDate temp = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                if (temp.isBefore(pendingMedi.datumGueltigAbCal)) {
                    errorMsg("Fehler 'gültig bis'", "gültig bis < gültig ab");
                    return;
                }
                pendingMedi.datumGueltigBisCal = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                Medi.calendar2string(pendingMedi.datumGueltigBisCal, modDatumGueltigBisTv, LocalDate.MIN);
            }
        }, initBisLocalDate.getYear(), initBisLocalDate.getMonthValue() - 1, initBisLocalDate.getDayOfMonth());

        datumGueltigBisDpd.setTitle("Datum 'gültig bis' setzen");
        datePickerBisMedi = datumGueltigBisDpd.getDatePicker();
        return datumGueltigBisDpd;
    }

    private DatePickerDialog geAbtDatePickerDialog(final TextView modDatumGueltigAbTv) {
        LocalDate initAbLocalDate;
        if (pendingMedi.datumGueltigAbCal.equals(LocalDate.MIN)) {
            initAbLocalDate = LocalDate.now();
        } else {
            initAbLocalDate = pendingMedi.datumGueltigBisCal;
        }

        final DatePickerDialog datumGueltigAbDpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                LocalDate temp = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                if (temp.isAfter(pendingMedi.datumGueltigBisCal)) {
                    errorMsg("Fehler 'gültig ab'", "gültig ab > gültig bis");
                    return;
                }
                pendingMedi.datumGueltigAbCal = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                Medi.calendar2string(pendingMedi.datumGueltigAbCal, modDatumGueltigAbTv, LocalDate.MIN);
            }
        }, initAbLocalDate.getYear(), initAbLocalDate.getMonthValue() - 1, initAbLocalDate.getDayOfMonth());

        datumGueltigAbDpd.setTitle("Datum 'gültig ab' setzen");
        datePickerAbMedi = datumGueltigAbDpd.getDatePicker();
        return datumGueltigAbDpd;
    }

    /*********************************************************/

    private int validInteger(String msg, EditText et) {
        String modString = et.getText().toString();
        int i;
        try {
            i = Integer.parseInt(modString);
        } catch (Exception e) {
            et.setError(msg);
            return Integer.MIN_VALUE;
        }
        if (msg.equals("Fehler Inhalt") && i < 1) {
            et.setError(msg);
            return Integer.MIN_VALUE;
        }

        return i;
    }

    /**
     * @param msg
     * @param et
     * @return
     */
    private float validFloat(String msg, EditText et) {
        String modString = et.getText().toString();
        float f;
        try {
            f = Float.parseFloat(modString);
        } catch (Exception e) {
            et.setError(msg);
            return Float.MIN_VALUE;
        }
        return f;
    }

    public void printStackTrace(String s) {
        StackTraceElement[] stes = Thread.currentThread().getStackTrace();
        for (int i = stes.length - 1; i >= 0; i--) {
            String klasse = stes[i].getClassName();
            //if (klasse.equals("hu.com.mediplan11.MainActivity")) {
            //Log.e(s, "****** printStackTrace ******: " + stes[i].toString());
            //}
        }
    }

    public void refreshAllx() {

        System.out.println("******** refreshAllx start ********" + getLN());

        load(); // loescht tabelle und liste
        Collections.sort(mediList, new MediVergleicher(0));
        proc_zukunft();

        for (Medi medi : mediList) {
            mediData2Table(medi);
        }

        System.out.println("******** refreshAllx end ********" + getLN());
    }

    /*********************************************************/

    public void mediData2Table(final Medi medi) { // clear: set value to MIN_VALUE

        final TableRow newTableRow = (TableRow) getLayoutInflater().inflate(R.layout.row_layout, null);
        TextView tv;
        /*********************************************************=======*/

        final EditText bestand_istEt = (EditText) newTableRow.findViewById(R.id.bestand_istEt);
        bestand_istEt.setText(Float.toString(medi.bestandIst));
//            EditText bestandIstEt = (EditText) row.getChildAt(BESTANDIST);
//            if (medi.bestandIst <= 0f) {
//                bestandIstEt.setBackgroundColor(alarmStrong);
//                bestandIstEt.setTextColor(0xff000000);
//            } else {
//                bestandIstEt.setBackgroundColor(colorBESTANDIST);
//                bestandIstEt.setTextColor(0xffffffff);
//            }

        bestand_istEt.setOnEditorActionListener(new EditorActionBestandIst(bestand_istEt, medi));

        tv = (TextView) newTableRow.findViewById(R.id.packungTv);
        tv.setText(Integer.toString(medi.inhalt));

        tv = (TextView) newTableRow.findViewById(R.id.seq);
        if (medi.nummerReihe == -1) {
            tv.setText("");
        } else {
            tv.setText(Integer.toString(medi.nummerReihe));
        }
        /*********************************************************/

        tv = (TextView) newTableRow.findViewById(R.id.medikamentTv);
        tv.setText(medi.name + " " + medi.specs + Medi.einsatzString[medi.einsatz]);

        tv.setText(medi.name + " " + medi.specs + Medi.einsatzString[medi.einsatz]);
        if (medi.nummerReihe == -1) {
            tv.setBackgroundColor(parms.alarmWeak);
        } else {
            if (medi.farbIdxTemp == -1) {
                tv.setBackgroundColor(parms.mediFarbeIntDefault);
                // System.out.println("farbe medidata " + medi.farbIdxTemp + " " + parms.mediFarbeIntDefault);
            } else {
                tv.setBackgroundColor(parms.mediFarbenInt[medi.farbIdxTemp]);
                // System.out.println("farbe medidata " + medi.farbIdxTemp + " " + parms.mediFarbenInt[medi.farbIdxTemp]);
            }
        }

        tv.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {

                                      popup_row_id = medi_table.indexOfChild(newTableRow);
                                      pendingMedi = mediList.get(popup_row_id);
                                      medikamentTv = (TextView) newTableRow.getChildAt(MEDIKAMENT);

                                      PopupMenu popup = new PopupMenu(getApplicationContext(), v);
                                      MenuInflater inflater = popup.getMenuInflater();

                                      inflater.inflate(R.menu.popup_mutation, popup.getMenu());
/*
                                      popup.setOnDismissListener(new PopupMenu.OnDismissListener() { // jua
                                                                     @Override
                                                                     public void onDismiss(PopupMenu popupMenu) {
                                                                         refreshAllx();
                                                                     }
                                                                 }
                                      );
*/
                                      MenuItem mediname = popup.getMenu().findItem(R.id.mediname);
                                      mediname.setTitle(medi.name);
                                      mediname.setEnabled(false);
                                      MenuItem changeMi = popup.getMenu().findItem(R.id.change);
                                      MenuItem takebyneedMi = popup.getMenu().findItem(R.id.takebyneed);
                                      MenuItem discontinueMi = popup.getMenu().findItem(R.id.discontinue);
                                      MenuItem copyMi = popup.getMenu().findItem(R.id.copy);
                                      MenuItem removeMi = popup.getMenu().findItem(R.id.remove);
                                      if (pendingMedi.einsatz > 0) {
                                          takebyneedMi.setVisible(false);
                                          discontinueMi.setVisible(false);
                                      }

                                      popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                                          public boolean onMenuItemClick(MenuItem item) {

                                              if (item.getItemId() == R.id.change) {
                                                  medikamentTv.setBackgroundColor(parms.selektion); // Selektion
                                                  proc_mediMutation();
                                              }

                                              if (item.getItemId() == R.id.discontinue) {
                                                  medikamentTv.setBackgroundColor(parms.selektion); // Selektion
                                                  pendingMedi.datumGueltigBisCal = LocalDate.now();
                                                  float dosis = getDosisTag(pendingMedi);
                                                  long restDauer = 0;
                                                  if (dosis > 0f) {
                                                      restDauer = (long) (pendingMedi.bestandIst / dosis);
                                                  }
                                                  LocalDate lagerBis = pendingMedi.datumGueltigBisCal.plusDays(restDauer - 1);
                                                  errorMsg("Lager " + pendingMedi.name, restDauer + " Tage, bis "
                                                          + lagerBis.format(dateMultilineFormat));
                                                  pendingMedi.einsatz = Medi.ABGESETZT;
                                                  saveMediList(null);
                                                  refreshAllx();
                                              }

                                              if (item.getItemId() == R.id.takebyneed) {
                                                  medikamentTv.setBackgroundColor(parms.selektion); // Selektion
                                                  pendingMedi.datumGueltigAbCal = LocalDate.now();
                                                  pendingMedi.datumGueltigBisCal = LocalDate.MAX;
                                                  pendingMedi.einsatz = Medi.EINNAHME_BEI_BEDARF;
                                                  saveMediList(null);
                                                  refreshAllx();
                                              }

                                              if (item.getItemId() == R.id.copy) {
                                                  medikamentTv.setBackgroundColor(parms.selektion); // Selektion
                                                  popup_row_id = -1; // initialisierung ueberschreiben
                                                  Medi newMedi = new Medi();
                                                  pendingMedi.copyTo(newMedi);
                                                  pendingMedi = newMedi;
                                                  proc_mediMutation();
                                              }

                                              if (item.getItemId() == R.id.remove) {
                                                  medikamentTv.setBackgroundColor(parms.selektion); // Selektion
                                                  mediList.remove(popup_row_id);
                                                  medi_table.removeViews(popup_row_id, 1); // table daten werden noch gebraucht
                                                  saveMediList(null);
                                                  refreshAllx();
                                              }

                                              return false;
                                          }
                                      });
                                      popup.show();
                                      /*********************************************************=======*/
                                  }
                              }

        );


        /*********************************************************/

        final Button plusButton = (Button) newTableRow.findViewById(R.id.plusButton);
        if (medi.kaufPackungen > 0) {
            if (medi.nummerReihe == -1) {
                plusButton.setBackgroundColor(table_primary_light);
            } else {
                plusButton.setBackgroundColor(parms.mediFarbenInt[medi.farbIdxTemp]);
            }
        }
        plusButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                bookbuyRowIid = medi_table.indexOfChild(newTableRow);
                                bookbuyRow = (TableRow) medi_table.getChildAt(bookbuyRowIid);
                                bookbuyMedi = mediList.get(bookbuyRowIid);

                                bookbuyMedi.bestandIst += bookbuyMedi.inhalt;
                                EditText bestandIstEt = (EditText) bookbuyRow.getChildAt(BESTANDIST);
                                bestandIstEt.setText(Float.toString(bookbuyMedi.bestandIst));

                                saveMediList(null);
                                refreshAllx();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                String s1 = "Haben Sie mindestens 1 Packung beschafft?";
                String s2 = getResources().getString(R.string.msgYes);
                String s3 = getResources().getString(R.string.msgNo);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(s1).setPositiveButton(s2, dialogClickListener).setNegativeButton(s3, dialogClickListener).show();
            }
        });

        medi_table.addView(newTableRow);
    }

    boolean isEnter(TextView view, int actionId, KeyEvent event) {
        if (event == null) {
            if (actionId == EditorInfo.IME_ACTION_DONE) ;
                // Capture soft enters in a singleLine EditText that is the last EditText.
            else if (actionId == EditorInfo.IME_ACTION_NEXT) ;
                // Capture soft enters in other singleLine EditTexts
            else return false;  // Let system handle all other null KeyEvents
        } else if (actionId == EditorInfo.IME_NULL) {
            // Capture most soft enters in multi-line EditTexts and all hard enters.
            // They supply a zero actionId and a valid KeyEvent rather than
            // a non-zero actionId and a null event like the previous cases.
            if (event.getAction() == KeyEvent.ACTION_DOWN) ;
                // We capture the event when key is first pressed.
            else return true;   // We consume the event when the key is released.
        } else return false;

        // We let the system handle it when the listener
        // is triggered by something that wasn't an enter.

        // Code from this point on will execute whenever the user
        // presses enter in an attached view, regardless of position,
        // keyboard, or singleLine status.
        return true;   // Consume the event
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void cleanfiles(View view) {  // delete app

        medi_table.removeViews(0, medi_table.getChildCount());
        mediList.clear();
        String msg = "";

        // internal storage
        File rootPathInt = new File(getFilesDir(), "medi");
        try {
            File[] files = rootPathInt.listFiles();
            if (files != null) {
                for (File file : files) {
                    boolean b = file.delete();
                    if (b) {
                        msg = "delete file " + file.getAbsolutePath() + "\n";
                    }
                }
            }
            boolean b = rootPathInt.delete();
            msg += " rootPathInt " + rootPathInt.getAbsolutePath() + " " + b + "\n";
        } catch (Exception e) {
            msg += " exception " + e + "\n";
        }

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            msg += "Cannot use storage \n";
            System.out.println(msg);
            return;
        }

        File rootPathExt = new File(Environment.getExternalStorageDirectory(), "medi");
        try {

            File[] files = rootPathExt.listFiles();
            if (files != null) {
                for (File file : files) {
                    boolean b = file.delete();
                    if (b) {
                        msg += "delete file " + file.getAbsolutePath() + "\n";
                    }
                }
            }
            boolean b = rootPathExt.delete();
            msg += " rootPathExt " + rootPathExt.getAbsolutePath() + " " + b + "\n";
        } catch (Exception e) {
            msg += " exception " + e + "\n";
        }
        System.out.println(msg);
    }

    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            //view.loadUrl(request.getUrl().toString());
            // return true;
            return super.shouldOverrideUrlLoading(view, request);
        }

    }

    class MediVergleicher implements Comparator {
        Locale[] locals = {Locale.FRENCH, Locale.GERMAN, Locale.US};
        int sprache = 0;

        public MediVergleicher(int sprache) {
            this.sprache = sprache;
        }

        @Override
        public int compare(Object o1, Object o2) {
            Collator collator = Collator.getInstance(locals[sprache]);
            collator.setStrength(Collator.PRIMARY);
            Medi medi1 = (Medi) o1;
            Medi medi2 = (Medi) o2;
            String s1 = "" + medi1.einsatz + medi1.name;
            String s2 = "" + medi2.einsatz + medi2.name;
            int comparison = collator.compare(s1, s2);
            return comparison;
        }

    }

    private class EditorActionKaufSoll implements TextView.OnEditorActionListener {

        private EditText et;
        private Medi medi;

        public EditorActionKaufSoll(EditText et, Medi medi) {
            this.et = et;
            this.medi = medi;
        }

        @Override
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            if (isEnter(view, actionId, event)) {
                String s = et.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    et.setError("kaufen mit Länge 0");
                    return false;
                }
                medi.kaufPackungen = Integer.parseInt(s);
                refreshAllx();
            }
            return false;
        }
    }

    private class EditorActionBestandIst implements TextView.OnEditorActionListener {

        private EditText et;
        private Medi medi;

        public EditorActionBestandIst(EditText et, Medi medi) {
            this.et = et;
            this.medi = medi;
        }

        @Override
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            if (isEnter(view, actionId, event)) {

                String s = et.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    et.setError("Lager mit Länge 0");
                    return false;
                }
                medi.bestandIst = Float.parseFloat(s);
                saveMediList(null);
                refreshAllx();
            }
            return false;
        }
    }

}


/*
class editTextFocusChangeListener implements View.OnFocusChangeListener {

    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            final EditText et = (EditText) v;
            et.post(new Runnable() {
                @Override
                public void run() {
                    et.setSelection(et.getText().length());
                }
            });
        }
    }
}

  table handling
    class layoutListener implements View.OnLayoutChangeListener {

        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

            List<Integer> colWidths = new LinkedList<Integer>();

            TableLayout header = (TableLayout) findViewById(R.id.header_table);
            TableLayout body = (TableLayout) findViewById(R.id.medi_table);

            for (int rownum = 0; rownum < body.getChildCount(); rownum++) {
                TableRow row = (TableRow) body.getChildAt(rownum);
                for (int cellnum = 0; cellnum < row.getChildCount(); cellnum++) {
                    View cell = row.getChildAt(cellnum);
                    Integer cellWidth = cell.getWidth();
                    if (colWidths.size() <= cellnum) {
                        colWidths.add(cellWidth);
                    } else {
                        Integer current = colWidths.get(cellnum);
                        if (cellWidth > current) {
                            colWidths.remove(cellnum);
                            colWidths.add(cellnum, cellWidth);
                        }
                    }
                }
            }

            TableRow row = (TableRow) header.getChildAt(0);

            for (int cellnum = 0; cellnum < row.getChildCount(); cellnum++) {
                View cell = row.getChildAt(cellnum);
                TableRow.LayoutParams params = (TableRow.LayoutParams) cell.getLayoutParams();
                System.out.println("vorher cellnum " + cellnum + ":" + cell.getWidth() + " " + params.column + ": " + params.width);
            }

            for (int cellnum = 0; cellnum < row.getChildCount(); cellnum++) {
                View cell = row.getChildAt(cellnum);
                TableRow.LayoutParams params = (TableRow.LayoutParams) cell.getLayoutParams();
                params.width = colWidths.get(cellnum);
            }

            for (int cellnum = 0; cellnum < row.getChildCount(); cellnum++) {
                View cell = row.getChildAt(cellnum);
                TableRow.LayoutParams params = (TableRow.LayoutParams) cell.getLayoutParams();
                System.out.println("nachher cellnum " + cellnum + ":" + cell.getWidth() + " " + params.column + ": " + params.width);
            }
        }
    }

    public class TextWatcherSetupBookbuy implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int lineCount, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int lineCount) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            proc_setupBookbuy();
        }
    }


    public void blink(TextView tv, int duration, int offset) {
        Animation anim = new AlphaAnimation(0.4f, 1.0f);
        anim.setDuration(duration); //You can manage the blinking time with this parameter
        anim.setStartOffset(offset);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        tv.startAnimation(anim);
    }

*/


/**
 *
 */
/*
    public void proc_konsumrechnen() {

        // jua printStackTrace("proc_konsumrechnen");
        long abStd = getTage(parms.bestandCal);
        long bisStd = getTage(parms.datumStichtagCal);

        // fix konsumTageTv.setText(Long.toString((bisStd - abStd)));
        System.out.println("777888 ===========================================================================");

        for (int i = 0; i < mediList.size(); i++) { // header rows
            Medi medi = mediList.get(i);

            medi.konsum = 0;

            if (medi.einsatz > 0) {
                continue;
            }

            long ab = getTage(medi.datumGueltigAbCal);
            long bis = getTage(medi.datumGueltigBisCal);

            if (bis < abStd) {
                continue;
            }

            if (ab > bisStd) {
                continue;
            }

            long abGo = Math.max(ab, abStd);
            long bisGo = Math.min(bis, bisStd);
            long konsumtage = bisGo - abGo + 1;
            medi.konsum = konsumtage * medi.dosis[4]; // andere

            System.out.println("777000 +++++++ " + Arrays.toString(medi.dosis) + " konsumtage start " + konsumtage + " --- " + medi.name);

 */
/*           if (ab < abStd && bis > bisStd) {
                medi.konsum = konsumtage * getDosisTag(medi);
                System.out.println("777900 konsumtage ohne Limite " + konsumtage + " konsum  " + medi.konsum);
                System.out.println("777777 ******* konsumtage " + konsumtage + " konsum  " + medi.konsum + " **********************************************");
                continue;
            }
*//*

            // ab/bis mindestens 1 Tag Differenz mit Erfassung

            if (abStd >= ab) { // medi hat kein bestandzeit
                konsumHits("777991 test limite 1. Tag ", medi, parms.bestandZeit + 1, 3);
                konsumtage--;
            }

            if (bisStd <= bis) {
                konsumHits("777991 test limite letzter Tag ", medi, 0, parms.stichtagZeit);
                konsumtage--;
            }

            medi.konsum += konsumtage * getDosisTag(medi);

            System.out.println("777777 ******* konsumtage " + konsumtage + " konsum  " + medi.konsum + " **********************************************\n");
        }
    }
*/
