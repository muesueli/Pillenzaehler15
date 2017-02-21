package hu.com.mediplan11;

import org.threeten.bp.LocalDate;

/**
 * Created by hu on 04.02.2016.
 */
public class Parms {

    public LocalDate bestandCal = LocalDate.now();
    public LocalDate planBisCal = LocalDate.now();

    public int chartWebViewWidthUnscaled = 480;
    public int chartWebViewHeightUnscaled = 160;
    public int chartWebViewWidth = 480;
    public int chartWebViewHeight = 160;

    int farbenCount = 0;
    // int selektion = 0xFF03A9F4;
    int selektion = 0xFFFFFFFF;
    int alarmWeak = 0xFFFFF48E;
    int alarmStrong = 0xFFFFEB3B;

    int[] mediFarbenInt = new int[]{
            0xffdc3912,
            0xffffb74d,
            0xff4db6ac,
            0xffba68c8,
            0xff7986cb,

            0xffff8a65,
            0xffffd54f,
            0xffaed581,
            0xffa1887f,
            0xff3366cc
    };

    String[] mediFarbenString = new String[]
            {"'#dc3912'", "'#ffb74d'", "'#4db6ac'", "'#ba68c8'", "'#7986cb'", "'#ff8a65'", "'#ffd54f'", "'#64b5f6'", "'#a1887f'", "'#3366cc'"};


    int mediFarbeIntDefault = 0xff64b5f6;

    String mediFarbeStringDefault = "'#64b5f6'";

/*
            0xffe57373,
            0xfff06292,
            0xff9575cd,
            0xff64b5f6,
            0xff4fc3f7,
            0xff4dd0e1,
            0xff81c784,
            0xffdce775,
            0xfffff176,
            0xffe0e0e0,
            0xff90a4ae,
            0xff7986cb,
            0xff4fc3f7
*/

    String einkaufliste = "";
    String kostenliste = "";
    String kontrollblatt = "";

    public Parms() {
    }

    @Override
    public String toString() {
        return "Parms{" +
                "bestandCal=" + bestandCal +
                ", planBisCal=" + planBisCal +
                ", einkaufliste='" + einkaufliste + '\'' +
                ", kostenliste='" + kostenliste + '\'' +
                '}';
    }
}
