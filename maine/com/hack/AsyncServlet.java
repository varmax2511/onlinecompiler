package com.hack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class AsyncServlet extends HttpServlet {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private static int count = 0;

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    int oCount = getOtherCount();

    if (oCount < count) {
      response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
      response.setHeader("Location", "http://10.84.101.155:8090/compile");

      return;
    }

    final AsyncContext acontext = request.startAsync();
    acontext.start(new Runnable() {
      @Override
      public void run() {

        count++;

        response.setContentType("text/html;charset=UTF-8");

        String test = "";
        try {
          test = request.getReader().lines()
              .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        final JSONObject json = new JSONObject(test);
        final Map<String, String> map = new HashMap<>();
        map.put("source", json.getString("source"));
        map.put("lang", json.getString("lang"));

        if (map.get("source") == null || map.get("source").equals("")) {
          try {
            response.sendError(200, "Invalid Input");
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          return;
        }

        if (json.has("params")) {
          map.put("lang", json.getString("params"));
        }

        try {
          if (map.get("lang").equalsIgnoreCase("java")) {
            response.getWriter().write(CompileSourceInMemory.exec(map));
            response.setStatus(200);
          } else if (map.get("lang").equalsIgnoreCase("python")) {
            response.getWriter().write(CompileInMemoryPython.exec(map));
            response.setStatus(200);
          }
        } catch (Throwable t) {
          // TODO Auto-generated catch block
          try {
            response.sendError(200, t.getMessage());
            t.printStackTrace();
          } catch (final IOException e1) {

            e1.printStackTrace();
          }
        } // catch

        count--;
        acontext.complete();
      }
    });
  }

  private int getOtherCount()
      throws MalformedURLException, IOException, ProtocolException {
    URL obj = new URL("http://10.84.101.155:8090/compile");
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    // optional default is GET
    con.setRequestMethod("GET");
    con.setConnectTimeout(100);

    int oCount = Integer.MAX_VALUE;
    try {
      int responseCode = con.getResponseCode();
      if (responseCode != 200) {
        return Integer.MAX_VALUE;
      }

      System.out.println("Response Code : " + responseCode);
      BufferedReader in = new BufferedReader(
          new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer out = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        out.append(inputLine);
      }
      in.close();
      // print in String
      System.out.println(out.toString());
      // Read JSON response and print
      JSONObject myResponse = new JSONObject(out.toString());

      oCount = Integer.parseInt(myResponse.get("count").toString());
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return oCount;
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.getWriter().write("{\"count\":" + count + "}");
    response.setStatus(200);
  }

  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    setAccessControlHeaders(resp);
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  private void setAccessControlHeaders(HttpServletResponse resp) {
    resp.setHeader("Access-Control-Allow-Origin", "http://localhost:8090");
    resp.setHeader("Access-Control-Allow-Methods", "GET, POST");
  }
}
