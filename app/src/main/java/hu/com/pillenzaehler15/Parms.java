package hu.com.pillenzaehler15;

import org.threeten.bp.LocalDate;

import java.util.Comparator;

/**
 * Created by hu on 04.02.2016.
 */
public class Parms {

    public LocalDate heuteCal = LocalDate.now();
    public LocalDate planBisCal = LocalDate.now().plusDays(35);
    public LocalDate kontrollPunktCal = LocalDate.MIN;

    public static int chartWebViewWidthUnscaled = 480;
    public static int chartWebViewHeightUnscaled = 160;
    public static int chartWebViewWidth = 480;
    public static int chartWebViewHeight = 160;

    int farbenCount = 0;
    // int selektion = 0xFF03A9F4;
    // static int selektion = 0xFFFFFFFF;
    static int alarmWeak = 0xFFFFF48E;
    static int alarmStrong = 0xFFFFEB3B;

    static int[] mediFarbenInt = new int[]{
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

    static String[] mediFarbenString = new String[]
            {"'#dc3912'", "'#ffb74d'", "'#4db6ac'", "'#ba68c8'", "'#7986cb'", "'#ff8a65'", "'#ffd54f'", "'#aed581'", "'#a1887f'", "'#3366cc'"};

    // static int mediFarbeIntDefault = 0xff64b5f6;
    static int mediFarbeIntDefault = 0xffcfd8dc;

    //static String mediFarbeStringDefault = "'#64b5f6'";
    static String mediFarbeStringDefault = "'#cfd8dc'";

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

    @Override
    public String toString() {
        return "Parms{" +
                "heuteCal=" + heuteCal +
                ", planBisCal=" + planBisCal +
                ", kontrollPunktCal=" + kontrollPunktCal +
                '}';
    }

    public Parms() {
    }


    class DatumVergleicher implements Comparator {
        public DatumVergleicher() {
        }

        @Override
        public int compare(Object o1, Object o2) {
            LocalDate datum1 = (LocalDate) o1;
            LocalDate datum2 = (LocalDate) o2;
            if (datum2.isEqual(datum1)) {
                return 0;
            }
            if (datum2.isAfter(datum1)) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
