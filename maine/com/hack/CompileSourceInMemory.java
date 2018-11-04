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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import sun.nio.ch.IOUtil;

public class CompileSourceInMemory {

  public static String exec(Map<String, String> map)
      throws IOException, ClassNotFoundException, InstantiationException,
      IllegalAccessException, NoSuchMethodException, SecurityException,
      IllegalArgumentException, InvocationTargetException {

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

    String[] params = map.containsKey("params")
        ? map.get("params").split(",")
        : null; // init params accordingly
    meth.invoke(null, (Object) params);

    
    if(sourceFile.exists()) {
      FileUtils.forceDelete(sourceFile);
    }
    
    StringBuilder sb = new StringBuilder();
    sb.append(stream.toString("utf-8"));
    return sb.toString();

  }
}
