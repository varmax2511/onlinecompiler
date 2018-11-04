package com.hack;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class JettyServer {

  private Server server;

  public void start() throws Exception {
    int maxThreads = 100;
    int minThreads = 10;
    int idleTimeout = 120;

    QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads,
        idleTimeout);

    server = new Server(threadPool);
    ServerConnector connector = new ServerConnector(server);
    connector.setPort(8090);
    server.setConnectors(new Connector[]{connector});

    ServletContextHandler context = new ServletContextHandler();
    context.setContextPath("/");
    ServletHolder asyncHolder = context.addServlet(AsyncServlet.class,"/compile");
    asyncHolder.setAsyncSupported(true);
    server.setHandler(context);
    
    /*ServletHandler servletHandler = new ServletHandler();
    server.setHandler(servletHandler);

    servletHandler.addServletWithMapping(CompileServlet.class, "/status");
    servletHandler.addServletWithMapping(AsyncServlet.class, "/compile");
*/
    server.start();
  }
  void stop() throws Exception {
    server.stop();
  }

}
