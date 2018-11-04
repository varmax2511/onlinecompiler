package com.hack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class PythonCompiler2 {

  public static boolean isAlive(Process p) {
    try {
      p.exitValue();
      return false;
    }
    catch (IllegalThreadStateException e) {
      return true;
    }
  }
  
  public static String exec(Map<String, String> map) throws IOException {
    
    String str = map.get("source");

    File file = new File("test.py");
    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    writer.write(str);
    writer.close();


    
    Process process = Runtime.getRuntime()
        .exec("C:\\Users\\rathj\\Anaconda3\\python test.py");
    InputStream out = process.getInputStream();
    OutputStream in = process.getOutputStream();
    InputStream stream = new ByteArrayInputStream(map.get("params").getBytes());
    
    byte[] buffer = new byte[4000];
    while (isAlive(process)) {
      int no = out.available();
      if (no > 0) {
        int n = out.read(map.get("params").getBytes(), 0, Math.min(no, buffer.length));
        System.out.println("wfwef" + new String(buffer, 0, n));
      }

      int ni = out.available();
      if (ni > 0) {
        int n = stream.read(buffer, 0, Math.min(ni, buffer.length));
        in.write(buffer, 0, n);
        in.flush();
      }

      try {
        Thread.sleep(10);
      }
      catch (InterruptedException e) {
      }
    }

    System.out.println(process.exitValue());
    return "";
  
  }

  
  public static void main(String[] args) throws IOException {
    Map<String, String> map = new HashMap<>();
    map.put("source", "print('input()')");
    map.put("lang", "");
    map.put("params", "varun2");
    
    System.out.println(exec(map));
  }
}
