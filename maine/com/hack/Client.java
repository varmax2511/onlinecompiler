package com.hack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Client {
  public static void main(String args[]) {

    String clientId = "780321fa5fa2ee8fb9e69a46cfdbb0d"; // Replace with your client ID
    String clientSecret = "7099dbb88678fdabdeb5fc5b64095d66f9cd18c401f4fd64622c9b457969d6ff"; // Replace with your client Secret
    String script = "public class Test { public static void main(String[] args) {"
        + "System.out.println();}}";
    String language = "java";
    String versionIndex = "0";

    try {
      URL url = new URL("https://api.jdoodle.com/v1/execute");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/json");

      String input = "{\"clientId\": \"" + clientId + "\",\"clientSecret\":\""
          + clientSecret + "\",\"script\":\"" + script + "\",\"language\":\""
          + language + "\",\"versionIndex\":\"" + versionIndex + "\"} ";

      System.out.println(input);

      OutputStream outputStream = connection.getOutputStream();
      outputStream.write(input.getBytes());
      outputStream.flush();

      if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
        throw new RuntimeException(
            "Please check your inputs : HTTP error code : "
                + connection.getResponseCode());
      }

      BufferedReader bufferedReader;
      bufferedReader = new BufferedReader(
          new InputStreamReader((connection.getInputStream())));

      String output;
      System.out.println("Output from JDoodle .... \n");
      while ((output = bufferedReader.readLine()) != null) {
        System.out.println(output);
      }

      connection.disconnect();

    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
