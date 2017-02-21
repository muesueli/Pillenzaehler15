package hu.com.mediplan11;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by hu on 30.07.2016.
 */
public class Tram01 {


    public static void main(String[] args) {
        File fnIn = new File("C:/Users/hu/Downloads", "Fahrzeiten_SOLL_IST_20160508_20160514.csv");
        File fnOut = new File("C:/Users/hu/Downloads", "tram01.csv");
        BufferedReader is;
        BufferedWriter os;

        try {
            is = new BufferedReader(new InputStreamReader(new FileInputStream(
                    fnIn)));
            os = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fnOut)));

            String zeile = is.readLine();
            os.write(zeile + "\n");
            while ((zeile = is.readLine()) != null) {
                if (zeile.length() == 0) {
                    continue;
                }
                int offset = zeile.indexOf("OPER");
                if (offset > 0) {
                    os.write(zeile + "\n");
                }

            }
            os.flush();
            os.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
