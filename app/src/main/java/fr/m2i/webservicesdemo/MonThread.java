package fr.m2i.webservicesdemo;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrateur on 17/01/2018.
 */

public class MonThread extends Thread {

    private String adr;
    private String response="";

    public String getAdr() {
        return adr;
    }

    public String getRes() {
        return response;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    @Override
    public void run() {
        // traitement à réaliser
        URL url = null;
        HttpURLConnection cnt = null;

        try {
            url = new URL(getAdr());
            System.out.println("URL "+url);
            cnt = (HttpURLConnection) url.openConnection();
            InputStream stream = cnt.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line = "";
            System.out.println("avant while");
            while ((line = reader.readLine()) != null) {
                response+= line;
                System.out.println("LA reponse "+response);

            }
            stream.close();
            reader.close();
        }
        catch (Exception ex) {
            response += "\nErreur : " + ex.getMessage();
        }
        finally {
            cnt.disconnect();
        }


    }

}
