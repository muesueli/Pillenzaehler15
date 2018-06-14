package hu.com.pillenzaehler15;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static java.lang.String.format;

/* storage samsung
    USB verwenden für Bilder übertragen
 */

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
// cd C:\Users\hu\AppData\Local\Android\sdk\platform-tools
// adb shell
//        root@generic:/ # cd storage
//        root@generic:/storage # cd sdcard
//        root@generic:/storage/sdcard # ls -R
//        root@generic:/storage/sdcard # rm -r medi

// samsung path: /data/data/hu.com.pillenzaehler15/files/medi  --- internal storage
//               /mnt/shell/emulated/0/medi --- external mit adb
// C:\Users\hu\javalib\eclipse\android\MediPlan10\app\src\main\res\raw

// emulator path:  data/data ...
// sony path:
//      intern: /data/user/0/hu.com.pillenzaehler15/files/medi/meditable
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

    public List<Medi> mediList = new ArrayList<> ();
    public android.support.v7.widget.RecyclerView mRecyclerView;
    public android.support.v7.widget.RecyclerView.Adapter mAdapter;
    public android.support.v7.widget.RecyclerView.LayoutManager mLayoutManager;
    public Parms parms;
    public AlertDialog mutationDialog;
    public AlertDialog checkDialog;
    public AlertDialog planDatumDialog;
    public AlertDialog bestandDatumDialog;
    public InputMethodManager imm;
    public Medi pendingMedi;
    public WebView chartWebView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public String[] einsatzarrayName;
    DateTimeFormatter dateTooltipFormatD = DateTimeFormatter.ofPattern ("dd.MM.");
    DateTimeFormatter dateTooltipFormatE = DateTimeFormatter.ofPattern ("MM/dd");
    DateTimeFormatter dateDDMMYYFormatD = DateTimeFormatter.ofPattern ("dd.MM.yy");
    DateTimeFormatter dateDDMMYYFormatE = DateTimeFormatter.ofPattern ("MM/dd/yy");
    DateTimeFormatter dateDDMMYYFormat;
    DateTimeFormatter dateTooltipFormat;
    TextView checkKpBestandDatumTv;
    TextView checkSummeTv;
    TextView checkSollVerbrauchTv;
    TextView checkVerbrauchTageTv;
    TextView checkSollbestandTv;
    TextView checkBestandIstDatumTv;
    TextView checkDifferenzTv;
    TextView checkAndereTv;

    EditText checkKpBestandEt;
    EditText checkEinkaufEt;
    EditText checkBestandIstEt;
    TextView checkHeaderTv;
    TextView checkDatumTv;
    TextView startHeaderTv;
    TextView startDatumTv;
    TextView endHeaderTv;
    TextView endDatumTv;
    long milliTag = (1000 * 60 * 60 * 24);
    //String rawMediList = "meditable";
    //String rawParms = "mediparms";
    String rawMediList = "meditable";
    String rawParms = "mediparms";
    String filenameMediList = "meditable.json";
    String filenameParms = "mediparms.json";
    Locale locale;
    String sprache;
    public String liste;

    // WebAppInterface Map<String, Medi> timelines = new HashMap<>();

    public static String getLN() {
        String s = " " + Thread.currentThread ().getStackTrace ()[3].getLineNumber ()
                + " " + Thread.currentThread ().getStackTrace ()[4].getLineNumber ();
        return s;
    }

    protected void onPause() {
        super.onPause ();
        System.out.println ("????????? onPause start ******** " + getLN ());
        saveParms ();
        saveMediList (null);
        System.out.println ("????????? onPause end ********" + getLN ());
    }

    /*********************************************************/

    @Override
    public void onResume() {
        super.onResume ();  // Always call the superclass method first
        System.out.println ("????????? onResume start ********" + getLN ());

        getWindow ().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        /* test */
//        File fileParms = new File(getExternalFilesDir(null), filenameParms);
//        fileParms.delete();
//        File fileMedi = new File(getExternalFilesDir(null), filenameMediList);
//        fileParms.delete();

        File fileParms = new File (getExternalFilesDir (null), filenameParms);
        System.out.println ("????????? onResume0 fileParms exists " + fileParms.exists () + ", "
                + fileParms.getAbsolutePath () + " dir: " + getExternalFilesDir (null) + " fn: " + filenameParms);

        if (!fileParms.exists ()) {
            AlertDialog alertDialog = new AlertDialog.Builder (MainActivity.this).create ();
            alertDialog.setTitle (getResources ().getString (R.string.sampletitle));
            alertDialog.setMessage (getResources ().getString (R.string.samplemsg));
            alertDialog.setCancelable (false); // click neben fenster scliesst dialog nicht
            alertDialog.setButton (AlertDialog.BUTTON_POSITIVE, getResources ().getString (R.string.msgYes),
                    new DialogInterface.OnClickListener () {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                int meditableId = getResources ().getIdentifier (rawMediList, "raw", getPackageName ());
                                InputStream in_sm = getResources ().openRawResource (meditableId); // R.raw.meditable
                                byte[] bm = new byte[in_sm.available ()];
                                in_sm.read (bm);
                                saveMediList (new String (bm));

                                parms.heuteCal = LocalDate.now ();
                                parms.kontrollPunktCal = parms.heuteCal.minusDays (19);
                                parms.planBisCal = parms.heuteCal.plusDays (35);
                                parms.farbenCount = 7;
                                saveParms ();
                            } catch (IOException e) {
                                e.printStackTrace ();
                            }
                            refreshAllx (false);
                            dialog.dismiss ();
                        }
                    });
            alertDialog.setButton (AlertDialog.BUTTON_NEGATIVE, getResources ().getString (R.string.msgNo),
                    new DialogInterface.OnClickListener () {
                        public void onClick(DialogInterface dialog, int which) {
                            parms.kontrollPunktCal = LocalDate.now ();
                            parms.planBisCal = parms.heuteCal.plusDays (28);
                            refreshAllx (false);
                            dialog.dismiss ();
                        }
                    });
            alertDialog.show ();
        } else {
            refreshAllx (false);
        }
    }

    private void printFarbIdx() {
        for (Medi medi : mediList) {
            if (medi.farbIdx != -1) {
                System.out.println ("??? " +
                        parms.mediFarbenInt.length + " " + parms.farbenCount + " "
                        + medi.name + " " + medi.farbIdx + " " +
                        parms.mediFarbenString[medi.farbIdx]);
            }
        }
    }

    /* WebAppInterface --- laeuft in anderem thread - popup abgeschnitte
       public class WebAppInterface {

           Context context;

           WebAppInterface(Context context) {
               this.context = context;
           }

           @JavascriptInterface
           public void timelineClick(String rowNr) {

               pendingMedi = timelines.get(rowNr);

               PopupMenu popup = new PopupMenu(context, startHeaderTv);
               MenuInflater inflater = popup.getMenuInflater();

               inflater.inflate(R.menu.popup_mutation, popup.getMenu());
               popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                                              @Override
                                              public void onDismiss(PopupMenu popupMenu) {
                                                  refreshAllx();
                                              }
                                          }
               );
*/

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        AndroidThreeTen.init (this);

        System.out.println ("????????? oncreate start ********" + getLN ());

        setContentView (R.layout.activity_main);

        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mRecyclerView = findViewById (R.id.listUI);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager (this);
        mRecyclerView.setLayoutManager (mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MediAdapter (this);
        mRecyclerView.setAdapter (mAdapter);

        Thread.setDefaultUncaughtExceptionHandler (
                new Thread.UncaughtExceptionHandler () {

                    @Override
                    public void uncaughtException(Thread thread, Throwable ex) {
                        String s = "Unhandled exception: " + ex.getMessage ();
                        Toast.makeText (getApplicationContext (), s, Toast.LENGTH_LONG).show ();
                        System.exit (1);
                    }
                });

        imm = (InputMethodManager) getSystemService (
                Context.INPUT_METHOD_SERVICE);

        locale = Locale.getDefault ();
        sprache = locale.getLanguage ();
        if (sprache.equals ("de")) {
            locale = new Locale ("de", "CH");
            dateTooltipFormat = dateTooltipFormatD;
            dateDDMMYYFormat = dateDDMMYYFormatD;
        } else {
            locale = new Locale ("en", "US");
            dateTooltipFormat = dateTooltipFormatE;
            dateDDMMYYFormat = dateDDMMYYFormatE;
        }

        einsatzarrayName = getResources ().getStringArray (R.array.einsatzArrayName);

        /*=== kopf ********************************************************====*/

        parms = new Parms ();

        checkHeaderTv = findViewById (R.id.checkHeaderTv);
        checkDatumTv = findViewById (R.id.checkDatumTv);

        startHeaderTv = findViewById (R.id.startHeaderTv);
        startDatumTv = findViewById (R.id.startDatumTv);
/* jua
        startDatumTv.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                proc_setupHeuteDatum (); // jua
            }
        });
*/

        endHeaderTv = findViewById (R.id.endHeaderTv);
        endDatumTv = findViewById (R.id.endDatumTv);
        endDatumTv.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                proc_setupPlanDatum ();
            }
        });

        View view = this.getCurrentFocus (); // keyboard jua
        if (view != null) {
            this.getWindow ().setSoftInputMode (
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

        chartWebView = findViewById (R.id.chartWv);
        chartWebView.setWebViewClient (new WebViewClient () {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText (getApplicationContext (), "Oh no! " + description, Toast.LENGTH_SHORT).show ();
            }
        });
        chartWebView.getSettings ().setJavaScriptEnabled (true);
        chartWebView.getSettings ().setLoadWithOverviewMode (true);

        metrics ();

        System.out.println ("????????? onCreate end ********" + getLN ());

    }

    private void notificationScheduling() {

        Intent notificationIntent = new Intent (this, NotificationPublisher.class);
        notificationIntent.putExtra (NotificationPublisher.NOTIFICATION_ID, 0);
        notificationIntent.putExtra (NotificationPublisher.NOTIFICATION, notificationCatch ());
        PendingIntent pendingIntent = PendingIntent.getBroadcast (this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        LocalDate alarmDate = parms.planBisCal.minusDays (7);
        AlarmManager alarmManager = (AlarmManager) getSystemService (Context.ALARM_SERVICE);
        alarmManager.set (AlarmManager.RTC, millisFromLocalDate (alarmDate), pendingIntent);
    }

    private Notification notificationCatch() {
        Notification.Builder builder = new Notification.Builder (this);
        builder.setContentTitle (getString (R.string.app_name));
        builder.setContentText (getString (R.string.lager_kontrollieren));
        builder.setSmallIcon (R.drawable.ic_stat_onesignal_default);
        return builder.build ();
    }

    private void metrics() {

        DisplayMetrics metrics = new DisplayMetrics ();
        getWindowManager ().getDefaultDisplay ().getMetrics (metrics);
        // System.out.println("metrics Display width/height " + metrics.widthPixels + "/" + metrics.heightPixels + ", density " + metrics.density);
        Parms.chartWebViewWidth = chartWebView.getWidth ();
        Parms.chartWebViewHeight = chartWebView.getHeight ();
        // System.out.println("metrics WebView pixel width/height " + parms.chartWebViewWidth + "/" + parms.chartWebViewHeight);
        float heightPixels = ((float) (metrics.heightPixels) * 4f) / (metrics.density * 12); // weight WebView 5, total sum 12
        Parms.chartWebViewHeightUnscaled = (int) heightPixels;
        // System.out.println("metrics WebView pixels height unscaled " + parms.chartWebViewHeightUnscaled);
    }

    /*********************************************************
     * =======
     */

    public String readRawHtml(String rawFileName) {
        byte[] bh = null;
        try {
            Resources res = getResources ();
            int htmlId = this.getResources ().getIdentifier (rawFileName, "raw", getPackageName ());
            InputStream in_sp = res.openRawResource (htmlId);
            bh = new byte[in_sp.available ()];
            in_sp.read (bh);
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return new String (bh);
    }

    /*********************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        menu.clear ();
        getMenuInflater ().inflate (R.menu.menu_main, menu);
        System.out.println ("????????? onCreateOptionsMenu ======= " + getLN ());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        View current = getCurrentFocus ();
        if (current != null) current.clearFocus ();

        switch (item.getItemId ()) {
            case R.id.actions: {
                return true;
            }
        }

        switch (item.getItemId ()) {

            case R.id.insert: {
                pendingMedi = new Medi ();
                proc_mediMutation ();
                break;
            }

            case R.id.kaufliste: {
                listeEinkauf ();
                proc_liste_display ();
                break;
            }

            case R.id.kontrolle: {
                proc_kontrolle_batch ();
                break;
            }
            case R.id.kosten: {
                if (sprache.equals ("de")) {
                    listeKostenD ();
                } else {
                    listeKostenE ();
                }
                proc_liste_display ();
                break;
            }
            case R.id.help: {
                displayHelpDialog ();
                break;
            }
       }
        return super.onOptionsItemSelected (item);
    }

     public void test01 () {

//*** test ******************************************************

         long[][] von_bis = new long[][]{

                 {-35l, -25},
                 {-35l, 0l},
                 {-35l, 5l},
                 {-35l, 19},
                 {-35l, 25},

                 {-0l, 0l},
                 {-0l, 5l},
                 {-0l, 19},
                 {-0l, 25},

                 {+5l, 5l},
                 {+5l, 19},
                 {+5l, 25},

                 {+19l, 19},
                 {+19l, 25},

                 {+25l, +35l}
         };

         float[] bi = new float[]{10f, 50f};
         for (int i0 = 0; i0 < 2; i0++) {
             for (int i = 0; i < von_bis.length; i++) {
                 Medi testmedi = new Medi ();
                 testmedi.name = "test " + i + ":  " + von_bis[i][0] + "/" + von_bis[i][1];
                 testmedi.dosis[2] = 1f;
                 testmedi.inhalt = 30;
                 testmedi.bestandKontrollPunkt = 30;
                 testmedi.bestandIst = bi[i0];
                 testmedi.datumGueltigAbCal = parms.kontrollPunktCal.plusDays (von_bis[i][0]);
                 testmedi.datumGueltigBisCal = parms.kontrollPunktCal.plusDays (von_bis[i][1]);
                 testmedi.name += "  " + testmedi.datumGueltigAbCal.format (dateDDMMYYFormat)
                         + " - " + testmedi.datumGueltigBisCal.format (dateDDMMYYFormat);
                 mediList.add (testmedi);
                 System.out.println ("************************* " + testmedi.name);
             }
         }
     }


    public void proc_kontrolle_rechnen_screen() {

// jua test
//        if (medi.name.contains ("spezielle Dauer")) {
//            medi.datumGueltigAbCal = LocalDate.now ().minusDays (20);
//            medi.datumGueltigBisCal = LocalDate.now ().minusDays (11);
//        }


        Periode periode = checkPeriod (pendingMedi, parms.kontrollPunktCal, parms.heuteCal);
        long verbrauchTage = 0l;
        if (periode.in == false) {
            proc_kontrolle_farbeTV (checkKpBestandDatumTv, true);
            proc_kontrolle_farbeTV (checkBestandIstDatumTv, true);
            checkKpBestandDatumTv.setText (pendingMedi.datumGueltigAbCal.format (dateDDMMYYFormat));
            checkBestandIstDatumTv.setText (pendingMedi.datumGueltigBisCal.format (dateDDMMYYFormat));
        } else {
            proc_kontrolle_farbeTV (checkKpBestandDatumTv, false);
            proc_kontrolle_farbeTV (checkBestandIstDatumTv, false);
            checkKpBestandDatumTv.setText (periode.von_bisCal[0].format (dateDDMMYYFormat));
            checkBestandIstDatumTv.setText (periode.von_bisCal[1].format (dateDDMMYYFormat));
            verbrauchTage = ChronoUnit.DAYS.between (periode.von_bisCal[0], periode.von_bisCal[1]) + 1;
        }

        String kpBestand = checkKpBestandEt.getText ().toString ();
        String s2 = emptyString (kpBestand);
        float bestandKontrollPunkt = Float.parseFloat (s2);
        checkKpBestandEt.setText (format (locale, "%.2f", bestandKontrollPunkt));

        String einkaufString = checkEinkaufEt.getText ().toString ();
        s2 = emptyString (einkaufString);
        int einkauf = Integer.parseInt (s2);
        checkEinkaufEt.setText (format (locale, "%d", einkauf));

        String bestandIstString = checkBestandIstEt.getText ().toString ();
        s2 = emptyString (bestandIstString);
        float bestandIst = Float.parseFloat (s2);
        checkBestandIstEt.setText (format (locale, "%.2f", bestandIst));

        float dosisTag = getDosisTag (pendingMedi);
        if (dosisTag == 0f) {
            proc_kontrolle_farbeTV (checkVerbrauchTageTv, true);
        } else {
            proc_kontrolle_farbeTV (checkVerbrauchTageTv, false);
        }

        float summe = bestandKontrollPunkt + einkauf;
        checkSummeTv.setText (format (locale, "%.2f", summe));

        String verbrauchTageString = format (locale, "%d", verbrauchTage) + " " + getString (R.string.tage);
        checkVerbrauchTageTv.setText (verbrauchTageString + " "
                + " * " + dosisTag);

        float sollVerbrauch = verbrauchTage * dosisTag; // start, end exklusive
        checkSollVerbrauchTv.setText (format (locale, "%8.2f", sollVerbrauch));

        float bestandSoll = summe - sollVerbrauch;
        checkSollbestandTv.setText (format (locale, "%8.2f", bestandSoll));
        if (bestandSoll < 0f) {
            proc_kontrolle_farbeTV (checkSollbestandTv, true);
        } else {
            proc_kontrolle_farbeTV (checkSollbestandTv, false);
        }

        float differenz = bestandIst - bestandSoll;
        checkDifferenzTv.setText (format (locale, "%8.2f", differenz));
        if (Math.abs (differenz) > 20) {
            proc_kontrolle_farbeTV (checkDifferenzTv, true);
        } else {
            proc_kontrolle_farbeTV (checkDifferenzTv, false);
        }

        if (pendingMedi.dosis[4] != 0) {
            checkAndereTv.setText (getString (R.string.andereTxt) + " "
                    + Float.toString (pendingMedi.dosis[4]));
        }
    }

    public void proc_kontrolle_farbeTV(TextView rechenFaktorTv, boolean fehler) {
        if (fehler) {
            rechenFaktorTv.setTextColor (getResources ().getColor (R.color.colorFehler));
        } else {
            rechenFaktorTv.setTextColor (getResources ().getColor (R.color.secondary_text));
        }
    }

    private void proc_kontrolle_batch() {

        String von = parms.kontrollPunktCal.format (dateDDMMYYFormat);
        String bis = parms.heuteCal.format (dateDDMMYYFormat);

        long verbrauchTage = ChronoUnit.DAYS.between (parms.kontrollPunktCal, parms.heuteCal) + 1;
        String verbrauchTageString = format (locale, "%d", verbrauchTage);
        liste = getResources ().getString (R.string.kontrolle) + " "
                + verbrauchTageString + " " + getString (R.string.tage) + "\n"
                + "  " + getString (R.string.von) + " " + von + " - " + bis + "\n\n";

        for (int i = 0; i < mediList.size (); i++) {

            final Medi medi = mediList.get (i);
            if (medi.einsatz > 0) {
                continue;
            }
            liste += medi.name + "\n";

            float dosisTag = getDosisTag (medi);
            if (dosisTag == 0f) {
                liste += "         "+getString (R.string.dosis) + " = 0\n\n";
                continue;
            }

            Periode periode = checkPeriod (medi, parms.kontrollPunktCal, parms.heuteCal);
            if (periode.in == false) {
                liste += "         "+ getString (R.string.datum_ausser_bereich) + ": "
                        + medi.datumGueltigAbCal.format (dateDDMMYYFormat) + " - "
                        + medi.datumGueltigBisCal.format (dateDDMMYYFormat) + "\n\n";
                continue;
            }

            liste += "\n";

            liste += format (locale, "%8.2f", medi.bestandKontrollPunkt) + " "
                    + getResources ().getString (R.string.bestand_start) + "\n";

            liste += format (locale, "%5d", medi.einkauf) + ".00 "
                    + "+ " + getResources ().getString (R.string.einkauf) + "\n";

            float summe = medi.bestandKontrollPunkt + medi.einkauf;
            liste += format (locale, "%8.2f", summe)
                    + " = " + getResources ().getString (R.string.summe) + "\n";

            long verbrauchTageEff;
            String temp_von_bis = null;
            String indent = "           ";
            if (periode.von_bisCal[0].isEqual (parms.kontrollPunktCal) && periode.von_bisCal[1].
                    isEqual (parms.heuteCal)) {
                verbrauchTageEff = verbrauchTage;
            } else {
                verbrauchTageEff = ChronoUnit.DAYS.between (periode.von_bisCal[0], periode.von_bisCal[1]) + 1;
                temp_von_bis = indent + periode.von_bisCal[0].format (dateDDMMYYFormat) + " - "
                        + periode.von_bisCal[1].format (dateDDMMYYFormat) + "\n";
            }
            float sollVerbrauch = verbrauchTageEff * dosisTag; // start, end exklusive
            String sollVerbrauchString = format (locale, "%8.2f", sollVerbrauch);
            liste += sollVerbrauchString + " "
                    + "- " + getResources ().getString (R.string.sollverbrauch) + "\n";
            if (temp_von_bis != null) {
                liste += temp_von_bis;
            }
            String s = format (locale, "%d", verbrauchTageEff);
            liste += indent + s + " " + getString (R.string.tage) + " * "
                    + format (locale, "%.2f", dosisTag) + "\n";

            float sollBestand = summe - sollVerbrauch;
            String sollBestandString = format (locale, "%8.2f", sollBestand);
            liste += sollBestandString + " "
                    + "= " + getResources ().getString (R.string.sollbestand);
            if (sollBestand < 0f) {
                liste += " ???";
            }
            liste += "\n";

            liste += String.format (locale, "%8.2f", medi.bestandIst) + " "
                    + "- " + getResources ().getString (R.string.bestand_end) + "\n";

            float differenz = medi.bestandIst - sollBestand;
            String differenzString = format (locale, "%8.2f", differenz);
            liste += differenzString + " "
                    + "= " + getResources ().getString (R.string.differenz);
            if (Math.abs (differenz) > 20) {
                liste += " ???";
            }
            liste += "\n";

            if (medi.dosis[4] != 0) {
                liste += "\n         " + getString (R.string.andereTxt) + " "
                        + Float.toString (medi.dosis[4]) + "\n";
            }

            liste += "\n";
        }

        proc_liste_display ();
    }


    /*** kontrolle alt ******************************************************/
    private void proc_liste_display() {

        LayoutInflater inflater = getLayoutInflater ();
        View kontrollView = inflater.inflate (R.layout.kontrolle, null);

        TextView kontrollTextEt = kontrollView.findViewById (R.id.kontrollTv);
        kontrollTextEt.setText (liste);

        AlertDialog kontrollDialog = new AlertDialog.Builder (MainActivity.this).create ();
        kontrollDialog.setView (kontrollView);

        kontrollDialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        kontrollDialog.setCanceledOnTouchOutside (false);

        kontrollDialog.setButton (AlertDialog.BUTTON_NEGATIVE, getResources ().
                        getString (R.string.back),
                new DialogInterface.OnClickListener () {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss ();
                    }
                });
        kontrollDialog.setButton (AlertDialog.BUTTON_POSITIVE, getResources ().
                        getString (R.string.share),
                new DialogInterface.OnClickListener () {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent sendIntent = new Intent ();
                        sendIntent.setAction (Intent.ACTION_SEND);
                        sendIntent.putExtra (Intent.EXTRA_TEXT, liste);
                        sendIntent.setType ("text/plain");
                        // Create intent to show chooser
                        Intent chooser = Intent.createChooser (sendIntent, getResources ().getString (R.string.sharechecklist));
                        // Verify the intent will resolve to at least one activity
                        if (sendIntent.resolveActivity (getPackageManager ()) != null) {
                            startActivity (chooser);
                        }
                    }
                });
        WindowManager.LayoutParams wmlp = kontrollDialog.getWindow ().getAttributes ();
        wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        kontrollDialog.show ();
    }

    /*********************************************************/
    String sb(String s, boolean links, boolean rechts) {
        String s0 = "";
        if (links) {
            s0 += " ";
        }
        s0 += s;
        if (rechts) {
            s0 += " ";
        }
        return s0;
    }

    public void proc_zukunft() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo ();
        if (netInfo == null || !netInfo.isConnectedOrConnecting ()) {
            errorMsg (getString (R.string.internet), getString (R.string.intern_not_avalable));
            return;
        }

        String s;
        String s01 = readRawHtml ("teil01");
        if (sprache.equals ("de")) {
            s = s01.replace ("land", "'language': 'de'");
        } else {
            s = s01.replace ("land", "'language': 'en'");
        }

        String farbenReplacementString = "'#000000',";

// Balken Plan
        String[] planVon = editCal (parms.heuteCal);
        String[] planBis = editCal (parms.planBisCal);
        String tooltipBlack = sb (getString (R.string.dauer), false, true)
                + (ChronoUnit.DAYS.between (parms.heuteCal, parms.planBisCal) + 1) + sb (getString (R.string.tage), true, false);

        s += format (locale,
                "    [ '---> ', '%s', '%s', new Date(%s), new Date(%s) ]",
                sb (getString (R.string.plan), false, true) + planVon[1] + " - " + planBis[1], tooltipBlack, planVon[0], planBis[0]);

        String s0 = "";
        for (int i = 0; i < mediList.size (); i++) {

            Medi medi = mediList.get (i);
            //System.out.println("zukunft " + medi.toString());

            medi.kaufPackungen = 0;

            if (medi.seq < 0) {
                continue;
            }
            float dosisTag = getDosisTag (medi);
            if (dosisTag == 0f) {
                continue;
            }

            Periode periode = checkPeriod (medi, parms.heuteCal, parms.planBisCal);
            if (periode.in == false) {
                continue;
            }

            LocalDate abCalGo = periode.von_bisCal[0];
            LocalDate bisCalGo = periode.von_bisCal[1];

            long bedarfTage = ChronoUnit.DAYS.between (abCalGo, bisCalGo) + 1;
            double bedarf = bedarfTage * dosisTag; // start,end inklusive
            String bedarfString = format (locale, "%d", (long) bedarf);

            long bestandIstTage = (long) Math.floor (medi.bestandIst / dosisTag);

            double rest = medi.bestandIst - bedarf;
            String restString = format (locale, "%d", (long) rest);
            long restTage = (long) (rest / dosisTag);

            double manko = bedarf - medi.bestandIst;
            String mankoString = format (locale, "%d", (long) manko);
            long mankoTage = (long) (manko / dosisTag);

            // shu stock high used
            // shr stock high rest
            // slu stock low used
            // slp stock low purchased

            String nameFixed = medi.name.replaceAll ("'", "\"");

            medi.farbIdxTemp = medi.farbIdx;
            farbenReplacementString += Parms.mediFarbenString[medi.farbIdx] + ", ";

            if (medi.bestandIst >= bedarf) { // es langt

                String[] shu_VonString = editCal (abCalGo);
                String[] shu_BisString = editCal (bisCalGo);
                String[] shr_VonString = editCal (bisCalGo.plusDays (1));
                String[] shr_BisString = editCal (bisCalGo.plusDays (1 + restTage - 1));
                // WebAppInterface timelines.put("" + lineCount, medi);

                if (medi.datumGueltigBisCal.isBefore (parms.planBisCal) || // beendet
                        medi.datumGueltigBisCal.equals (parms.planBisCal)) {

                    String tooltip = sb (getString (R.string.einnahme_bis), false, true) + shu_BisString[1] + "<br/>" +
                            sb (getString (R.string.rest), false, true) + restString + " &rarr;  " + restTage + sb (getString (R.string.tage), true, false);
                    s0 += format (locale,
                            "    [ '%d', '%s', '%s', new Date(%s), new Date(%s) ],\n",
                            medi.seq, nameFixed, tooltip, shu_VonString[0], shu_BisString[0]);
                } else {

                    String tooltip = sb (getString (R.string.lager), false, true) + bedarfString + " &rarr; " + bedarfTage + sb (getString (R.string.tage), true, true) + shu_VonString[1] + " - " + shu_BisString[1];
                    if (restTage > 0) {
                        tooltip += "<br/>" + sb (getString (R.string.rest), false, true) + restString + " &rarr; " + restTage + sb (getString (R.string.tage), true, true) + shr_VonString[1] + " - " + shr_BisString[1];
                    }
                    s0 += format (locale,
                            "    [ '%d', '%s', '%s', new Date(%s), new Date(%s) ],\n",
                            medi.seq, nameFixed, tooltip, shu_VonString[0], shr_BisString[0]);
                }

            } else {

                String packungen = sb (getString (R.string.packungen), true, true);
                medi.kaufPackungen = (int) Math.ceil (manko / medi.inhalt);
                if (medi.kaufPackungen == 1) {
                    packungen = sb (getString (R.string.packung1), true, true);
                }
                long kaufTage = (long) ((medi.kaufPackungen * medi.inhalt) / dosisTag);

                String tooltip = "";
                if (bestandIstTage > 0f) {
                    String[] slu_VonString = editCal (abCalGo);
                    String[] slu_BisString = editCal (abCalGo.plusDays (bestandIstTage - 1));
                    String[] slp_VonString = editCal (abCalGo.plusDays (bestandIstTage - 1 + 1));
                    String[] slp_BisString = editCal (abCalGo.plusDays (bestandIstTage - 1 + 1 + kaufTage - 1));
                    tooltip = sb (getString (R.string.lager), false, true) + medi.bestandIst + " &rarr; " + bestandIstTage + sb (getString (R.string.tage), true, true) + slu_VonString[1] + " - " + slu_BisString[1] + "<br/>";
                    tooltip += "+ " + medi.kaufPackungen + packungen + " &rarr; " + kaufTage + sb (getString (R.string.tage), true, true) + slp_VonString[1] + " - " + slp_BisString[1];
                    s0 += format (locale,
                            "    [ '---> %d', '%s', '%s', new Date(%s), new Date(%s) ],\n",
                            medi.seq, nameFixed, tooltip, slu_VonString[0], slp_BisString[0]);
                } else {
                    String[] slu_VonString = editCal (abCalGo);
                    String[] slp_BisString = editCal (abCalGo.plusDays (kaufTage - 1));
                    tooltip += "+ " + medi.kaufPackungen + packungen + " &rarr; " + kaufTage + sb (getString (R.string.tage), true, true) + slu_VonString[1] + " - " + slp_BisString[1];
                    s0 += format (locale,
                            "    [ '---> %d', '%s', '%s', new Date(%s), new Date(%s) ],\n",
                            medi.seq, nameFixed, tooltip, slu_VonString[0], slp_BisString[0]);
                }
            }
        }

        s += ",\n"+s0;

        s += readRawHtml ("teil02");

        String frs = farbenReplacementString.substring (0, farbenReplacementString.length () - 2);
        String s2 = s.replace ("farben", frs);

        metrics ();

        String stilReplacement = "style=\"height: " + Parms.chartWebViewHeightUnscaled + "px;\"";
        String s3 = s2.replace ("stil", stilReplacement); // in div

        try {
            String fnHTML = "chart.html";
            File outputFileHTML = new File (getExternalFilesDir (null), fnHTML);
            Writer out = new BufferedWriter (new OutputStreamWriter (
                    new FileOutputStream (outputFileHTML), "UTF-8"));
            out.write (s3);
            out.close ();
            System.out.println ("????????? file name HTML: "
                    + outputFileHTML.getAbsolutePath () + " dir: " + getExternalFilesDir (null) + " fn: " + fnHTML);
            chartWebView.loadUrl ("file:///" + outputFileHTML.getAbsolutePath ());
        } catch (Exception e) {
            e.printStackTrace ();
        }

    }

    public class Periode {
        boolean in = true;
        LocalDate[] von_bisCal = new LocalDate[2];
    }

    private Periode checkPeriod(Medi medi, LocalDate abCal, LocalDate bisCal) {

        Periode periode = new Periode ();
        periode.von_bisCal[0] = abCal;
        periode.von_bisCal[1] = bisCal;

        if (medi.datumGueltigBisCal.isBefore (abCal)) {
            periode.in = false;
            return periode;
        }

        if (medi.datumGueltigAbCal.isAfter (bisCal)) {
            periode.in = false;
            return periode;
        }

        if (medi.datumGueltigAbCal.isAfter (abCal)) {
            periode.von_bisCal[0] = medi.datumGueltigAbCal;
        }

        if (medi.datumGueltigBisCal.isBefore (bisCal)) {
            periode.von_bisCal[1] = medi.datumGueltigBisCal;
        }

        return periode;
    }

    public String[] editCal(LocalDate datum) {
        String[] temp = new String[2];
        temp[0] = "new Date(" + datum.getYear () + ", " + (datum.getMonthValue () - 1) + ", " + datum.getDayOfMonth () + ")";
        temp[1] = datum.format (dateTooltipFormat);
        return temp;
    }

    private void listeEinkauf() {


        String[] planVon = editCal (parms.heuteCal);
        String[] planBis = editCal (parms.planBisCal);
        liste = getString (R.string.kauf_packungen_fuer) + planVon[1] + " - " + planBis[1] + "\n\n";
        for (int i = 0; i < mediList.size (); i++) {
            Medi medi = mediList.get (i);
            if (medi.kaufPackungen > 0) {
                liste += medi.kaufPackungen + " " + medi.name + "\n\n";
            }
            if (medi.einsatz == Medi.EINNAHME_BEI_BEDARF) {
                liste += "    " + medi.name + "\n\n";
            }
        }
    }

    /*********************************************************/

    private void listeKostenD() {
        liste = getString (R.string.costs) + "\n\n";
        float totaltag = 0f;
        for (int i = 0; i < mediList.size (); i++) {
            Medi medi = mediList.get (i);
            if (medi.einsatz == 0) {
                float stueckkosten = medi.preis / medi.inhalt;
                float dosisTag = getDosisTag (medi);
                float tageskosten = getDosisTag (medi) * stueckkosten;
                totaltag += tageskosten;
                float jahreskosten = 365 * tageskosten;
                liste += medi.name + "\n";
                liste += format (locale, "   Pkg. %d Preis %.2f\n   pro Stück %.2f\n", medi.inhalt, medi.preis, stueckkosten);
                liste += format (locale, "   Tagesdosis %.2f", dosisTag);
                liste += format (locale, " Kosten %.2f\n", tageskosten);
                liste += format (locale, "   pro Jahr %.2f\n\n", jahreskosten);
            }
        }
        float totaljahr = 365 * totaltag;
        liste += format (locale, "Total\npro Tag %.2f\npro Jahr %.2f\n", totaltag, totaljahr);
    }

    private void listeKostenE() {
        liste = getString (R.string.costs) + "\n\n";
        float totaltag = 0f;
        for (int i = 0; i < mediList.size (); i++) {
            Medi medi = mediList.get (i);
            if (medi.einsatz == 0) {
                float stueckkosten = medi.preis / medi.inhalt;
                float dosisTag = getDosisTag (medi);
                float tageskosten = getDosisTag (medi) * stueckkosten;
                totaltag += tageskosten;
                float jahreskosten = 365 * tageskosten;
                liste += medi.name + "\n";
                liste += format (locale, "   pkg. %d price %.2f\n   per piece %.2f\n", medi.inhalt, medi.preis, stueckkosten);
                liste += format (locale, "   daily dose %.2f", dosisTag);
                liste += format (locale, " cost %.2f\n", tageskosten);
                liste += format (locale, "   per year %.2f\n\n", jahreskosten);
            }
        }
        float totaljahr = 365 * totaltag;
        liste += format (locale, "Total\nper day %.2f\nper year %.2f \n", totaltag, totaljahr);
    }

    public float getDosisTag(Medi medi) {
        float dosisTag = 0f;
        for (int i = 0; i < 4; i++) {
            dosisTag += medi.dosis[i];
        }
        return dosisTag;
    }

    public void saveParms() {

        final Gson gson = new Gson ();
        // converting an object to json object
        String jsonParms = gson.toJson (parms);
        try {
            File outputFileParms = new File (getExternalFilesDir (null), filenameParms);
            System.out.println ("????????? saveParms outputFileTable exists " + outputFileParms.exists () + ", "
                    + outputFileParms.getAbsolutePath () + " dir: " + getExternalFilesDir (null) + " fn: " + filenameMediList);
            Writer out = new BufferedWriter (new OutputStreamWriter (
                    new FileOutputStream (outputFileParms), "UTF-8"));
            out.write (jsonParms);
            out.close ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
        System.out.println ("????????? saveParms end ********" + getLN ());
    }

    /*********************************************************/

    public void saveMediList(String jsonMediList) {

        if (jsonMediList == null) {
            final Gson gson = new Gson ();
            // converting an object to json object
            jsonMediList = gson.toJson (mediList);
        }
        try {
            File outputFileTable = new File (getExternalFilesDir (null), filenameMediList);
            System.out.println ("????????? saveMediList outputFileTable exists " + outputFileTable.exists () + ", "
                    + outputFileTable.getAbsolutePath () + " dir: " + getExternalFilesDir (null) + " fn: " + filenameMediList);
            // System.out.println ("????????? out: " + jsonMediList);

            Writer out = new BufferedWriter (new OutputStreamWriter (
                    new FileOutputStream (outputFileTable), "UTF-8"));
            out.write (jsonMediList);
            out.close ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
        System.out.println ("????????? saveMediList end ********" + getLN ());
    }

    /*********************************************************/

    public void load() {

        System.out.println ("????????? load start ********" + getLN ());
        final Gson gson = new Gson ();

        try {
            File inputFileParms = new File (getExternalFilesDir (null), filenameParms);
            System.out.println ("????????? load inputFileParms exists " + inputFileParms.exists () + ", "
                    + inputFileParms.getAbsolutePath () + " dir: " + getExternalFilesDir (null) + " fn: " + filenameParms);
            InputStream inputStream = new FileInputStream (inputFileParms);
            BufferedReader r = new BufferedReader (new InputStreamReader (inputStream));
            StringBuilder json = new StringBuilder ();
            String line;
            while ((line = r.readLine ()) != null) {
                json.append (line);
            }
            inputStream.close ();
            // converting json to object
            parms = gson.fromJson (json.toString (), Parms.class);
        } catch (Exception e) {
            e.printStackTrace ();
        }

        parms.heuteCal = LocalDate.now (); // jua

        if (parms.planBisCal.isBefore (parms.heuteCal)) {
            parms.planBisCal = LocalDate.now ().plusDays (28);
            saveParms ();
        }

        try {
            File inputFileTable = new File (getExternalFilesDir (null), filenameMediList);
            System.out.println ("????????? load inputFileTable exists " + inputFileTable.exists () + ", "
                    + inputFileTable.getAbsolutePath () + " dir: " + getExternalFilesDir (null) + " fn: " + filenameParms);
            InputStream inputStream = new FileInputStream (inputFileTable);
            BufferedReader r = new BufferedReader (new InputStreamReader (inputStream));
            StringBuilder jsonTable = new StringBuilder ();
            String line;
            while ((line = r.readLine ()) != null) {
                jsonTable.append (line);
            }
            inputStream.close ();
            // System.out.println ("????????? " + jsonTable);

            Type collectionType2 = new TypeToken<List<Medi>> () {
            }.getType ();
            mediList.clear ();
            mediList = gson.fromJson (jsonTable.toString (), collectionType2);
            // jua test01 ();

        } catch (Exception e) {
            e.printStackTrace ();
        }

        invalidateOptionsMenu ();
        System.out.println ("????????? load end ********" + getLN ());
    }

    /*********************************************************/

    public void displayHelpDialog() {

        WebView myWebView = new WebView (MainActivity.this);

        String titel;
        if (sprache.equals ("de")) {
            titel = "Medikamente planen mit dem Pillenzähler";
            myWebView.loadUrl ("file:///android_asset/helpd.html");
        } else {
            // if (sprache.equals("en")) {
            titel = "Plan Drugs with the Pill Counter";
            myWebView.loadUrl ("file:///android_asset/helpe.html");
        }
        //if (sprache.equals("fr")) {
        //    titel = "Aide";
        //}

        new AlertDialog.Builder (MainActivity.this).setView (myWebView).setTitle (titel).setPositiveButton ("OK", new DialogInterface.OnClickListener () {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel ();
            }
        }).show ();
    }

    public void errorMsg(String title, String msg) {

        AlertDialog alertDialog = new AlertDialog.Builder (MainActivity.this).create ();
        alertDialog.setTitle (title);
        alertDialog.setMessage (msg);
        alertDialog.setButton (AlertDialog.BUTTON_NEUTRAL, getResources ().getString (R.string.back),
                new DialogInterface.OnClickListener () {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss ();
                    }
                });
        alertDialog.show ();
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap (view.getWidth (),
                view.getHeight (), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas (bitmap);
        view.draw (canvas);
        return bitmap;

    }

    public void toastIt(String msg) {
        Toast toast = Toast.makeText (getApplicationContext (), msg, Toast.LENGTH_LONG);
        toast.setGravity (Gravity.TOP | Gravity.RIGHT, 500, 500);
        toast.show ();
    }

    /*********************************************************/

    public long millisFromLocalDate(LocalDate localDate) {
        LocalDateTime date = LocalDateTime.of (localDate.getYear (), localDate.getMonth (), localDate.getDayOfMonth (), 0, 0);
        long milli = date.toInstant (ZoneOffset.ofTotalSeconds (0)).toEpochMilli ();
        return milli;
    }

    public void proc_setupHeuteDatum() {   // jua für test

        LayoutInflater inflater = getLayoutInflater ();
        final View parmsDatumLayout = inflater.inflate (R.layout.medi_parmsdatum, null);

        final DatePicker parmsDatumDp = parmsDatumLayout.findViewById (R.id.parmsDatumDp);
        parmsDatumDp.setMaxDate (millisFromLocalDate (parms.planBisCal) - milliTag);
        parmsDatumDp.updateDate (parms.heuteCal.getYear (), parms.heuteCal.getMonthValue () - 1, parms.heuteCal.getDayOfMonth ());

        AlertDialog.Builder alertParmsDatum = new AlertDialog.Builder (this);
        alertParmsDatum.setTitle (getString (R.string.lager));
        alertParmsDatum.setView (parmsDatumLayout);
        alertParmsDatum.setCancelable (false); // click neben fenster scliesst dialog nicht
        alertParmsDatum.setPositiveButton (android.R.string.ok, null);
        alertParmsDatum.setNegativeButton (android.R.string.cancel, null);

        bestandDatumDialog = alertParmsDatum.create ();
        bestandDatumDialog.setOnShowListener (new DialogInterface.OnShowListener () {
            @Override
            public void onShow(DialogInterface dialog) {
                Button bp = bestandDatumDialog.getButton (AlertDialog.BUTTON_POSITIVE);
                bp.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        parms.heuteCal = LocalDate.of (parmsDatumDp.getYear (),
                                parmsDatumDp.getMonth () + 1,
                                parmsDatumDp.getDayOfMonth ());

                        String s = parms.heuteCal.format (dateDDMMYYFormat);
                        startDatumTv.setText (s);
                        saveParms ();
                        refreshAllx (true);

                        bestandDatumDialog.dismiss ();

                    }
                });
                Button bn = bestandDatumDialog.getButton (AlertDialog.BUTTON_NEGATIVE);
                bn.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        bestandDatumDialog.dismiss ();
                    }
                });
            }
        });

        bestandDatumDialog.show ();
    }

    /**********************************************************/

    public void proc_setupPlanDatum() { // field pendingMedi set

        LayoutInflater inflater = getLayoutInflater ();
        final View parmsDatumLayout = inflater.inflate (R.layout.medi_parmsdatum, null);

        final CheckBox reminderCb = parmsDatumLayout.findViewById (R.id.reminderCb);

        final DatePicker parmsDatumDp = parmsDatumLayout.findViewById (R.id.parmsDatumDp);
        parmsDatumDp.setMinDate (millisFromLocalDate (parms.heuteCal) + milliTag);
        parmsDatumDp.updateDate (parms.planBisCal.getYear (), parms.planBisCal.getMonthValue () - 1, parms.planBisCal.getDayOfMonth ());

        AlertDialog.Builder alertParmsDatum = new AlertDialog.Builder (this);
        alertParmsDatum.setTitle (R.string.plandatum);
        alertParmsDatum.setView (parmsDatumLayout);
        alertParmsDatum.setCancelable (false); // click neben fenster schliesst dialog nicht
        alertParmsDatum.setPositiveButton (android.R.string.ok, null);
        alertParmsDatum.setNegativeButton (android.R.string.cancel, null);

        planDatumDialog = alertParmsDatum.create ();
        planDatumDialog.setOnShowListener (new DialogInterface.OnShowListener () {
            @Override
            public void onShow(DialogInterface dialog) {
                Button bp = planDatumDialog.getButton (AlertDialog.BUTTON_POSITIVE);
                bp.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        parms.planBisCal = LocalDate.of (parmsDatumDp.getYear (),
                                parmsDatumDp.getMonth () + 1,
                                parmsDatumDp.getDayOfMonth ());
                        endDatumTv.setText (parms.planBisCal.format (dateDDMMYYFormat));
                        if (reminderCb.isChecked ()) {
                            notificationScheduling ();
                        }
                        saveParms ();
                        refreshAllx (true);

                        planDatumDialog.dismiss ();
                    }
                });
                Button bn = planDatumDialog.getButton (AlertDialog.BUTTON_NEGATIVE);
                bn.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        planDatumDialog.dismiss ();
                    }
                });
            }
        });

        planDatumDialog.show ();
    }

    /**********************************************************/
    public void proc_mediMutation() {

        LayoutInflater inflater = getLayoutInflater ();
        final View mutationLayout = inflater.inflate (R.layout.medi_mutation, null);

        EditText modNameEt = mutationLayout.findViewById (R.id.modNametEt);
        modNameEt.setInputType (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        Spinner modEinsatzSp = mutationLayout.findViewById (R.id.modEinsatzSp);
        EditText modInhaltEt = mutationLayout.findViewById (R.id.modInhaltEt);
        EditText modPreisEt = mutationLayout.findViewById (R.id.modPreisEt);

        EditText modDosisMoEt = mutationLayout.findViewById (R.id.modDosisMoEt);
        EditText modDosisMiEt = mutationLayout.findViewById (R.id.modDosisMiEt);
        EditText modDosisAbEt = mutationLayout.findViewById (R.id.modDosisAbEt);
        EditText modDosisNaEt = mutationLayout.findViewById (R.id.modDosisNaEt);
        EditText modDosisAndEt = mutationLayout.findViewById (R.id.modDosisAndEt);

        final TextView modDatumGueltigAbTv = mutationLayout.findViewById (R.id.modDatumGueltigAbTv);
        final TextView modDatumGueltigBisTv = mutationLayout.findViewById (R.id.modDatumGueltigBisTv);

        modNameEt.setText (pendingMedi.name);

        modEinsatzSp.setSelection (pendingMedi.einsatz);

        modInhaltEt.setText (format (locale, "%d", pendingMedi.inhalt));
        modPreisEt.setText (format (locale, "%.2f", pendingMedi.preis));

        modDosisMoEt.setText (format (locale, "%.1f", pendingMedi.dosis[Medi.DOSIS_MO]));
        modDosisMiEt.setText (format (locale, "%.1f", pendingMedi.dosis[Medi.DOSIS_MI]));
        modDosisAbEt.setText (format (locale, "%.1f", pendingMedi.dosis[Medi.DOSIS_AB]));
        modDosisNaEt.setText (format (locale, "%.1f", pendingMedi.dosis[Medi.DOSIS_NA]));
        modDosisAndEt.setText (format (locale, "%.1f", pendingMedi.dosis[Medi.DOSIS_AND]));

        modDatumGueltigAbTv.setText (pendingMedi.datumGueltigAbCal.format (dateDDMMYYFormat));
        modDatumGueltigBisTv.setText (pendingMedi.datumGueltigBisCal.format (dateDDMMYYFormat));


        /*************************==ab********************************=====*/

        modDatumGueltigBisTv.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                getBisDatePickerDialog (modDatumGueltigBisTv);
            }
        });

        /******************************************************/

        modDatumGueltigAbTv.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                getAbDatePickerDialog (modDatumGueltigAbTv);
            }
        });

        /*** mutation *******************************************************/

        AlertDialog.Builder alertUpdate = new AlertDialog.Builder (this);
        // alertUpdate.setTitle(getResources().getString(R.string.updateTitle));
        alertUpdate.setView (mutationLayout);
        alertUpdate.setCancelable (false); // click neben fenster scliesst dialog nicht
        alertUpdate.setNeutralButton (R.string.remove, null);
        alertUpdate.setNegativeButton (android.R.string.cancel, null);
        alertUpdate.setPositiveButton (android.R.string.ok, null);

        mutationDialog = alertUpdate.create ();
        //mutationDialog.setCanceledOnTouchOutside(false);
        mutationDialog.setOnShowListener (new DialogInterface.OnShowListener () {
            @Override
            public void onShow(DialogInterface dialog) {

                Button bp = mutationDialog.getButton (AlertDialog.BUTTON_POSITIVE);
                bp.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        pendingMedi.name = ((EditText) mutationLayout.findViewById (R.id.modNametEt)).getText ().toString ();

                        Spinner einsatzSp = mutationLayout.findViewById (R.id.modEinsatzSp);
                        pendingMedi.einsatz = einsatzSp.getSelectedItemPosition ();

                        String s = ((EditText) mutationLayout.findViewById (R.id.modInhaltEt)).getText ().toString ();
                        pendingMedi.inhalt = Integer.parseInt (s);
                        if (pendingMedi.inhalt <= 0) {
                            errorMsg (pendingMedi.name, getString (R.string.fehler_packung_0));
                            return;

                        }
                        s = ((EditText) mutationLayout.findViewById (R.id.modPreisEt)).getText ().toString ();
                        pendingMedi.preis = Float.parseFloat (s);

                        s = ((EditText) mutationLayout.findViewById (R.id.modDosisMoEt)).getText ().toString ();
                        pendingMedi.dosis[Medi.DOSIS_MO] = Float.parseFloat (s);

                        s = ((EditText) mutationLayout.findViewById (R.id.modDosisMiEt)).getText ().toString ();
                        pendingMedi.dosis[Medi.DOSIS_MI] = Float.parseFloat (s);

                        s = ((EditText) mutationLayout.findViewById (R.id.modDosisAbEt)).getText ().toString ();
                        pendingMedi.dosis[Medi.DOSIS_AB] = Float.parseFloat (s);

                        s = ((EditText) mutationLayout.findViewById (R.id.modDosisNaEt)).getText ().toString ();
                        pendingMedi.dosis[Medi.DOSIS_NA] = Float.parseFloat (s);

                        s = ((EditText) mutationLayout.findViewById (R.id.modDosisAndEt)).getText ().toString ();
                        pendingMedi.dosis[Medi.DOSIS_AND] = Float.parseFloat (s);

                        if (pendingMedi.neu) {
                            if (parms.kontrollPunktCal.equals (LocalDate.MIN)) { //init
                                parms.kontrollPunktCal = LocalDate.now ();
                            }
                            pendingMedi.farbIdx = parms.farbenCount % Parms.mediFarbenInt.length;
                            parms.farbenCount++;
                            saveParms ();
                            pendingMedi.neu = false;
                            mediList.add (pendingMedi);
                        }

                        refreshAllx (true);
                        mutationDialog.dismiss ();
                    }
                });
                Button bn = mutationDialog.getButton (AlertDialog.BUTTON_NEGATIVE);
                bn.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        mutationDialog.dismiss ();
                    }
                });
                Button bneutral = mutationDialog.getButton (AlertDialog.BUTTON_NEUTRAL);
                bneutral.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        mediList.remove (pendingMedi);
                        refreshAllx (true);
                        mutationDialog.dismiss ();
                    }
                });
            }
        });
        mutationDialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = mutationDialog.getWindow ().getAttributes ();
        wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        // wmlp.x = 100;   //x position
        // wmlp.y = 100;   //y position

        mutationDialog.show ();
        modInhaltEt.setSelectAllOnFocus(true);
        modPreisEt.setSelectAllOnFocus(true);
        modDosisMoEt.setSelectAllOnFocus(true);
        modDosisMiEt.setSelectAllOnFocus(true);
        modDosisAbEt.setSelectAllOnFocus(true);
        modDosisNaEt.setSelectAllOnFocus(true);
        modDosisAndEt.setSelectAllOnFocus(true);
    }

    private void getBisDatePickerDialog(final TextView modDatumGueltigBisTv) {

        LocalDate pickerCal = LocalDate.now ().plusDays (28);

        final DatePickerDialog datumGueltigBisDpd = new DatePickerDialog (this, new DatePickerDialog.OnDateSetListener () {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                LocalDate temp = LocalDate.of (year, monthOfYear + 1, dayOfMonth);
                if (temp.isBefore (pendingMedi.datumGueltigAbCal)) {
                    errorMsg (getString (R.string.fehler_gueltig_bis), getString (R.string.fehler_gueltig_bis_kleiner));
                    return;
                }
                pendingMedi.datumGueltigBisCal = LocalDate.of (year, monthOfYear + 1, dayOfMonth);
                modDatumGueltigBisTv.setText (pendingMedi.datumGueltigBisCal.format (dateDDMMYYFormat));
            }
        }, pickerCal.getYear (), pickerCal.getMonthValue () - 1, pickerCal.getDayOfMonth ());

        datumGueltigBisDpd.setTitle (getString (R.string.datum_gueltig_bis_setzen));
        datumGueltigBisDpd.show ();
    }

    private void getAbDatePickerDialog(final TextView modDatumGueltigAbTv) {

        LocalDate pickerCal = LocalDate.now ();

        final DatePickerDialog datumGueltigAbDpd = new DatePickerDialog (this, new DatePickerDialog.OnDateSetListener () {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                LocalDate temp = LocalDate.of (year, monthOfYear + 1, dayOfMonth);
                if (temp.isAfter (pendingMedi.datumGueltigBisCal)) {
                    errorMsg (getString (R.string.fehler_gueltig_ab), getString (R.string.fehler_gueltig_ab_groesser));
                    return;
                }
                pendingMedi.datumGueltigAbCal = LocalDate.of (year, monthOfYear + 1, dayOfMonth);
                modDatumGueltigAbTv.setText (pendingMedi.datumGueltigAbCal.format (dateDDMMYYFormat));
            }
        }, pickerCal.getYear (), pickerCal.getMonthValue () - 1, pickerCal.getDayOfMonth ());

        datumGueltigAbDpd.show ();
    }

    /*********************************************************/


    public void printStackTrace(String s) {
        StackTraceElement[] stes = Thread.currentThread ().getStackTrace ();
        for (int i = stes.length - 1; i >= 0; i--) {
            String klasse = stes[i].getClassName ();
            //if (klasse.equals("MainActivity")) {
            //Log.e(s, "****** printStackTrace ******: " + stes[i].toString());
            //}
        }
    }

    public void refreshAllx(boolean doSave) {

        System.out.println ("????????? refreshAllx start ********" + getLN ());

        if (doSave) {
            saveMediList (null); // save updated for restart
        }
        load (); // loescht tabelle und liste

        String datumKontrollPunkt = parms.kontrollPunktCal.format (dateDDMMYYFormat);
        checkDatumTv.setText (datumKontrollPunkt);
        String datumHeute = parms.heuteCal.format (dateDDMMYYFormat);
        startDatumTv.setText (datumHeute);
        String datumPlan = parms.planBisCal.format (dateDDMMYYFormat);
        endDatumTv.setText (datumPlan);

        Collections.sort (mediList, new MediVergleicher (0));
        int seq = 0;
        for (Medi medi : mediList) {
            if (medi.einsatz > 0) {
                medi.seq = -1;
            } else {
                seq++;
                medi.seq = seq;
            }
        }

        proc_zukunft ();
        mAdapter.notifyDataSetChanged ();

        System.out.println ("????????? refreshAllx end ********" + getLN ());
    }

    /*** check **************************************************/

    public String emptyString(String s0) {
        if (s0.equals ("")) {
            return "0";
        } else {
            return s0;
        }
    }

    public void proc_kontrolle_launch() {

        LayoutInflater inflater = getLayoutInflater ();
        final View checkLayout = inflater.inflate (R.layout.check, null);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        String s0 = ((EditText) checkLayout.findViewById (R.id.checkKpBestandEt)).getText ().toString ();
                        String s1 = emptyString (s0);
                        pendingMedi.bestandKontrollPunkt = Float.parseFloat (s1);

                        s0 = ((EditText) checkLayout.findViewById (R.id.checkEinkaufEt)).getText ().toString ();
                        s1 = emptyString (s0);
                        pendingMedi.einkauf = Integer.parseInt (s1);

                        s0 = ((EditText) checkLayout.findViewById (R.id.checkBestandIstEt)).getText ().toString ();
                        s1 = emptyString (s0);
                        pendingMedi.bestandIst = Float.parseFloat (s1);

                        refreshAllx (true);
                        checkDialog.dismiss ();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        checkDialog.dismiss ();
                        break;
                }
            }
        };

        checkKpBestandEt = checkLayout.findViewById (R.id.checkKpBestandEt);
        checkEinkaufEt = checkLayout.findViewById (R.id.checkEinkaufEt);
        checkBestandIstEt = checkLayout.findViewById (R.id.checkBestandIstEt);

        checkKpBestandDatumTv = checkLayout.findViewById (R.id.checkKpBestandDatumTv);
        checkSummeTv = checkLayout.findViewById (R.id.checkSummeTv);
        checkVerbrauchTageTv = checkLayout.findViewById (R.id.checkVerbrauchTageTv);
        checkSollVerbrauchTv = checkLayout.findViewById (R.id.checkSollVerbrauchTv);
        checkSollbestandTv = checkLayout.findViewById (R.id.checkSollbestandTv);
        checkBestandIstDatumTv = checkLayout.findViewById (R.id.checkBestandIstDatumTv);
        checkDifferenzTv = checkLayout.findViewById (R.id.checkDifferenzTv);
        checkAndereTv = checkLayout.findViewById (R.id.checkAndereTv);

        Button packungPlusBt = checkLayout.findViewById (R.id.packungPlusBt);
        Button rechnenBt = checkLayout.findViewById (R.id.rechnenBt);

        checkKpBestandEt.setText (format (locale, "%.2f", pendingMedi.bestandKontrollPunkt));

        checkEinkaufEt.setText (format (locale, "%d", pendingMedi.einkauf));
        packungPlusBt.setText ("plus " + pendingMedi.inhalt);

        checkBestandIstEt.setText (format (locale, "%.2f", pendingMedi.bestandIst));
        Periode periode = checkPeriod (pendingMedi, parms.kontrollPunktCal, parms.heuteCal);

        checkKpBestandDatumTv.setText (periode.von_bisCal[0].format (dateDDMMYYFormat));
        checkBestandIstDatumTv.setText (periode.von_bisCal[1].format (dateDDMMYYFormat));

        proc_kontrolle_rechnen_screen ();

        packungPlusBt.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String s = checkEinkaufEt.getText ().toString ();
                if (s.equals ("")) {
                    s = "0";
                }
                int einkauf = Integer.parseInt (s);
                checkEinkaufEt.setText (format (locale, "%d", (einkauf + pendingMedi.inhalt)));
                String bestandIstString = checkBestandIstEt.getText ().toString ();
                float bestandIst = Float.parseFloat (bestandIstString);
                float bestandNeu = bestandIst + pendingMedi.inhalt;
                checkBestandIstEt.setText (format (locale, "%.2f", bestandNeu));

                proc_kontrolle_rechnen_screen ();
            }
        });

        rechnenBt.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                proc_kontrolle_rechnen_screen ();
            }
        });

        AlertDialog.Builder alertCheck = new AlertDialog.Builder (MainActivity.this);
        // alertUpdate.setTitle(getResources().getString(R.string.updateTitle));
        alertCheck.setView (checkLayout);
        alertCheck.setTitle (pendingMedi.name);
        alertCheck.setCancelable (false); // click neben fenster scliesst dialog nicht
        alertCheck.setPositiveButton (android.R.string.ok, dialogClickListener);
        alertCheck.setNegativeButton (android.R.string.cancel, dialogClickListener);

        checkDialog = alertCheck.create ();
        WindowManager.LayoutParams wmlp = checkDialog.getWindow ().getAttributes ();
        wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        checkDialog.show ();
        checkBestandIstEt.requestFocus ();
    }


    public void setCheckpoint(View v) {
        for (Medi medi : mediList) {
            medi.bestandKontrollPunkt = medi.bestandIst;
            medi.einkauf = 0;
        }
        parms.kontrollPunktCal = parms.heuteCal;
        saveParms ();
        String kontrollPunkDatum = parms.kontrollPunktCal.format (dateDDMMYYFormat);
        checkDatumTv.setText (kontrollPunkDatum);
    }

    @Override
    public void onStart() {
        super.onStart ();
    }

    @Override
    public void onStop() {
        super.onStop ();

    }

/*
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
*/

    class MediVergleicher implements Comparator {
        Locale[] locals = {Locale.FRENCH, Locale.GERMAN, Locale.US};
        int sprache = 0;

        public MediVergleicher(int sprache) {
            this.sprache = sprache;
        }

        @Override
        public int compare(Object o1, Object o2) {
            Collator collator = Collator.getInstance (locals[sprache]);
            collator.setStrength (Collator.PRIMARY);
            Medi medi1 = (Medi) o1;
            Medi medi2 = (Medi) o2;
            String s1 = "" + medi1.einsatz + medi1.name;
            String s2 = "" + medi2.einsatz + medi2.name;
            return collator.compare (s1, s2);
        }
    }
}


/* xxx
    private class EditorActionDone implements TextView.OnEditorActionListener {

        private EditText et;

        public EditorActionDone(EditText et, Medi medi) {
            this.et = et;
        }

        @Override
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {

            String s = "0";
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (TextUtils.isEmpty (s)) {
                    et.setText ("0");
                }
            }
            proc_kontrolle_rechnen_screen ();
            return true;
        }
    }
*/


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

/*

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
            if (event.getAction () == KeyEvent.ACTION_DOWN) ;
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


    private class EditorActionDone implements TextView.OnEditorActionListener {

        private EditText et;
        private Medi medi;

        public EditorActionDone(EditText et, Medi medi) {
            this.et = et;
            this.medi = medi;
        }

        @Override
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {

            String s = "0";
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (TextUtils.isEmpty (s)) {
                    et.setError (getString (R.string.lager_mit_laenge_0));
                    return false;
                }
            }
            proc_kontrolle_rechnen_screen (medi);
            return true;
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

            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event != null &&
                            event.getAction () == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode () == KeyEvent.KEYCODE_ENTER) {
                if (event == null || !event.isShiftPressed ()) {
                    String s = et.getText ().toString ();
                    if (TextUtils.isEmpty (s)) {
                        et.setError (getString (R.string.lager_mit_laenge_0));
                        return false;
                    }
                    medi.bestandIst = Float.parseFloat (s);
                    saveMediList (null);
                    ddd ("onEditorAction_save");
                    refreshAllx ();
                    ddd ("n");
                    return true; // consume.
                }
            }
            return false; // pass on to other listeners.
        }
    }
 */


