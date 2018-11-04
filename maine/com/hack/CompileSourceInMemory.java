package com.hack;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class CompileSourceInMemory {

  public static String exec(Map<String, String> map)
      throws IOException, ClassNotFoundException, InstantiationException,
      IllegalAccessException, NoSuchMethodException, SecurityException,
      IllegalArgumentException, InvocationTargetException {

    // Prepare source somehow.
    // final String source = "package test; public class Test { static {
    // System.out.println(\"hello\"); } public static void main(String[] args) {
    // System.out.println(\"world\"); } }";
    final String source = map.get("source");

    // Save source in .java file.
    final File root = new File("/java"); // On Windows running on C:\, this is
    // C:\java.
    final File sourceFile = new File(root, "test/Test.java");
    sourceFile.getParentFile().mkdirs();
    Files.write(sourceFile.toPath(), source.getBytes(StandardCharsets.UTF_8));

    // Compile source file.
    final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    compiler.run(null, null, null, sourceFile.getPath());

    ByteArrayOutputStream stream = new ByteArrayOutputStream(4096);
    System.setOut(new PrintStream(stream));
    // Load and instantiate compiled class.
    final URLClassLoader classLoader = URLClassLoader
        .newInstance(new URL[]{root.toURI().toURL()});
    final Class<?> cls = Class.forName("test.Test", true, classLoader); // Should
    // print
    // "hello".
    
    
    
    final Object instance = cls.newInstance(); // Should print "world".
    Method meth = cls.getMethod("main", String[].class);
    String[] params = null; // init params accordingly
    meth.invoke(null, (Object) params);
    
    String output = stream.toString("utf-8");
    //System.out.println(instance); // Should print "test.Test@hashcode".
    return output;

  }
}
