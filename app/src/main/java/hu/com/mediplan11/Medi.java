package hu.com.mediplan11;

import android.widget.TextView;

import org.threeten.bp.LocalDate;

import static hu.com.mediplan11.MainActivity.dateMultilineFormat;

/**
 * Created by hu on 07.01.2016.
 */
public class Medi {

    static int DOSIS_MO = 0;
    static int DOSIS_MI = 1;
    static int DOSIS_AB = 2;
    static int DOSIS_NA = 3;
    static int DOSIS_AND = 4;
    static int EINNAHME_NACH_REZEPT = 0;
    static int EINNAHME_BEI_BEDARF = 1;
    static int ABGESETZT = 2;

    static String[] einsatzString = new String[]{"", " (bei Bedarf)", " (abgesetzt)", " (aufbrauchen)"};

    String name = "";
    String specs = "";
    public int einsatz = EINNAHME_NACH_REZEPT;
    int inhalt = 0;
    float preis = 0f;
    int kaufPackungen = 0;
    float bestandIst = 0f;
    float[] dosis = new float[]{0, 0, 0, 0, 0};
    public LocalDate datumGueltigAbCal = LocalDate.MIN;
    public LocalDate datumGueltigBisCal = LocalDate.MAX;
    int nummerReihe = -1;
    int farbIdx = 0;
    int farbIdxTemp = -1;

    public static void calendar2string(LocalDate calendar, TextView tv, LocalDate undef) {
        if (calendar.equals(undef)) {
            tv.setText("undef");
        } else {
            tv.setText(calendar.format(MainActivity.dateMultilineFormat));
        }
    }

    @Override
    public String toString() {
        String abString = datumGueltigAbCal.format(dateMultilineFormat);
        String bisString = datumGueltigBisCal.format(dateMultilineFormat);

        return "Medi{" +
                abString +
                " --- " + bisString +
                " ::: " + name;
    }

    public void copyTo(Medi targetMedi) {
        targetMedi.name = name;
        targetMedi.specs = specs;
        // System.arraycopy(dosis, 0, targetMedi.dosis, 0, 5);
    }

}
