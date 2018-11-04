package com.hack;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AsyncServlet extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("text/html;charset=UTF-8");
    final AsyncContext acontext = request.startAsync();
    acontext.start(new Runnable() {
      public void run() {
        Map<String, String> map = new HashMap<>();
        map.put("source", "package test; public class Test { static {\r\n"
            + "    System.out.println(\"hello\"); } public static void main(String[] args) {\r\n"
            + "    System.out.println(\"world\"); } }");
        map.put("lang", "java");
        try {
          CompileSourceInMemory.exec(map);
        } catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | NoSuchMethodException | SecurityException
            | IllegalArgumentException | InvocationTargetException
            | IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        acontext.complete();
      }
    });
  }
  /*
   * String source = request.getParameter("source"); String lang =
   * request.getParameter("lang");
   * 
   * AsyncContext async = request.startAsync(); ServletOutputStream out =
   * response.getOutputStream(); out.setWriteListener(new WriteListener() {
   * 
   * @Override public void onWritePossible() throws IOException { while
   * (out.isReady()) {
   * 
   * if (!content.hasRemaining()) { response.setStatus(200); async.complete();
   * return; } out.write(content.get()); } }
   * 
   * @Override public void onError(Throwable t) {
   * getServletContext().log("Async Error", t); async.complete(); } }); }
   */

}
