package br.com.devdojo.javaclient;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaClientTest {


    public static void main(String args[]) {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;

        try {
            String user = "toyo";
            String password = "devdojo";
            URL url = new URL("http://localhost:8080/v1/protected/students/2");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.addRequestProperty("Authorization", "Basic " + encodeUsernamePassword(user, password));

            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonSB = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                jsonSB.append(line);
            }

            System.out.println(jsonSB.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            IOUtils.closeQuietly(bufferedReader);
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String encodeUsernamePassword(String user, String password) {
        String userPassword = user + ":" + password;
        return new String(Base64.encodeBase64(userPassword.getBytes()));
    }

}
