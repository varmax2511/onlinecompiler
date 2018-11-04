package com.hack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class CompileInMemoryPython {
  public static String exec(Map<String, String> map) throws IOException {
    String str = map.get("source");

    File file = new File("test.py");
    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    writer.write(str);
    writer.close();

    Process p = Runtime.getRuntime()
        .exec("C:\\Users\\rathj\\Anaconda3\\python test.py");
    BufferedReader in = new BufferedReader(
        new InputStreamReader(p.getInputStream()));
    BufferedReader err = new BufferedReader(
        new InputStreamReader(p.getErrorStream()));

    StringBuilder sb = new StringBuilder();
    String ret = "";
    while ((ret = in.readLine()) != null) {
      sb.append(ret + "\n");
    }
    
    while ((ret = err.readLine()) != null) {
      sb.append(ret + "\n");
    }
    
    return sb.toString();
  }

}
