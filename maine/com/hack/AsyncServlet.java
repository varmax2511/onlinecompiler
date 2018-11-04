package com.hack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.AsyncContext;
import javax.servlet.RequestDispatcher;
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

  /*
   * protected void doGet(HttpServletRequest request, HttpServletResponse
   * response) throws ServletException, IOException {
   *
   * response.setContentType("text/html;charset=UTF-8"); final AsyncContext
   * acontext = request.startAsync(); acontext.start(new Runnable() { public
   * void run() { Map<String, String> map = new HashMap<>(); map.put("source",
   * "package test; public class Test { static {\r\n" +
   * "    System.out.println(\"hello\"); } public static void main(String[] args) {\r\n"
   * + "    System.out.println(\"world\"); } }"); map.put("lang", "java"); try {
   * response.getWriter().write(CompileSourceInMemory.exec(map));
   * response.setStatus(200); } catch (ClassNotFoundException |
   * InstantiationException | IllegalAccessException | NoSuchMethodException |
   * SecurityException | IllegalArgumentException | InvocationTargetException |
   * IOException e) { // TODO Auto-generated catch block try {
   * response.sendError(400, e.getMessage()); } catch (IOException e1) { // TODO
   * Auto-generated catch block e1.printStackTrace(); } } // catch
   *
   * acontext.complete(); } }); }
   */ /*
       * String source = request.getParameter("source"); String lang =
       * request.getParameter("lang");
       *
       * AsyncContext async = request.startAsync(); ServletOutputStream out =
       * response.getOutputStream(); out.setWriteListener(new WriteListener() {
       *
       * @Override public void onWritePossible() throws IOException { while
       * (out.isReady()) {
       *
       * if (!content.hasRemaining()) { response.setStatus(200);
       * async.complete(); return; } out.write(content.get()); } }
       *
       * @Override public void onError(Throwable t) {
       * getServletContext().log("Async Error", t); async.complete(); } }); }
       */

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    URL obj = new URL("http://10.84.101.155:8090/compile");
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    // optional default is GET
    con.setRequestMethod("GET");

    int responseCode = con.getResponseCode();
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

    int oCount = Integer.parseInt(myResponse.get("count").toString());

    if (oCount < count) {
      RequestDispatcher dispatcher = getServletContext()
          .getRequestDispatcher("http://10.84.101.155:8090/compile");
      dispatcher.forward(request, response);
      return;
    }

    response.setContentType("text/html;charset=UTF-8");

    final String test = request.getReader().lines()
        .collect(Collectors.joining(System.lineSeparator()));

    final JSONObject json = new JSONObject(test);
    final Map<String, String> map = new HashMap<>();
    map.put("source", json.getString("source"));
    map.put("lang", json.getString("lang"));

    final AsyncContext acontext = request.startAsync();
    acontext.start(new Runnable() {
      @Override
      public void run() {

        count++;

        try {
          response.getWriter().write(CompileSourceInMemory.exec(map));
          response.setStatus(200);
        } catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | NoSuchMethodException | SecurityException
            | IllegalArgumentException | InvocationTargetException
            | IOException e) {
          // TODO Auto-generated catch block
          try {
            response.sendError(400, e.getMessage());
          } catch (final IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        } // catch

        count--;
        acontext.complete();
      }
    });
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.getWriter().write("{\"count\":" + count + "}");
    response.setStatus(200);
  }

}
