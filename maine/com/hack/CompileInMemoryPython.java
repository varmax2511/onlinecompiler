package com.hack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class CompileInMemoryPython {
	 public static String exec(Map<String, String> map) throws IOException {
		 	String str=map.get("source");
		    BufferedWriter writer = new BufferedWriter(new FileWriter("test.py"));
		    writer.write(str);
		    writer.close();
		    
		    Process p = Runtime.getRuntime().exec("python  test.py");
		    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    String ret = in.readLine();
		    System.out.println(ret);
		return ret; 
	 }
}
