package acra;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class Main {

	public static void main(String[] args) {
		try {
            URL url = new URL ("http://192.168.100.198:8883/mytodolists?filter={\"$and\":[{}]}");
            String authData = "apadmin:dktnfk!!";
            byte[] aitjDataByteArr = authData.getBytes();
            
            String encoding = Base64.getEncoder().encodeToString(aitjDataByteArr);
            System.out.println(encoding);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty  ("Authorization", "Basic " + encoding);
            
            InputStream content = (InputStream)connection.getInputStream();
            BufferedReader in   = 
                new BufferedReader (new InputStreamReader (content));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

	}

}
