package hu.com.pillenzaehler15;


import org.threeten.bp.LocalDate;

import java.util.Arrays;

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

    String name = "";
    public int einsatz = EINNAHME_NACH_REZEPT;
    int inhalt = 0;
    float preis = 0f;
    float bestandIst = 0f;
    float bestandKontrollPunkt = 0f;
    int einkauf = 0;
    boolean neu = true;

    int kaufPackungen = 0; // manko planung
    float[] dosis = new float[]{0, 0, 0, 0, 0};

    public LocalDate datumGueltigAbCal = LocalDate.now ();
    public LocalDate datumGueltigBisCal = LocalDate.MAX;
    int seq = 0;
    int farbIdx = 0;
    int farbIdxTemp = -1;

    public Medi() {
    }

    @Override
    public String toString() {
        return "Medi{" +
                "name='" + name + '\'' +
                ", einsatz=" + einsatz +
                ", inhalt=" + inhalt +
                ", preis=" + preis +
                ", bestandIst=" + bestandIst +
                ", bestandKontrollPunkt=" + bestandKontrollPunkt +
                ", einkauf=" + einkauf +
                ", neu=" + neu +
                ", kaufPackungen=" + kaufPackungen +
                ", dosis=" + Arrays.toString (dosis) +
                ", datumGueltigAbCal=" + datumGueltigAbCal +
                ", datumGueltigBisCal=" + datumGueltigBisCal +
                ", seq=" + seq +
                ", farbIdx=" + farbIdx +
                ", farbIdxTemp=" + farbIdxTemp +
                '}';
    }
}
