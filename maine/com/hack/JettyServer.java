package com.hack;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
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

    ResourceHandler resource_handler = new ResourceHandler();
    resource_handler.setDirectoriesListed(true);
    resource_handler.setWelcomeFiles(new String[]{ "test.html" });

    resource_handler.setResourceBase(".");
    
    ServletContextHandler context = new ServletContextHandler();
    context.setContextPath("/");
    ServletHolder asyncHolder = context.addServlet(AsyncServlet.class,"/compile");
    asyncHolder.setAsyncSupported(true);
    
    HandlerList handlers = new HandlerList();
    handlers.setHandlers(new Handler[] { resource_handler, context });
    
    server.setHandler(handlers);
    
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
