package it.unitn.hci.feed.server;

import it.unitn.hci.feed.PollingEngine;
import java.net.InetAddress;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;

public class ServerLauncher
{

    public static void main(String[] args) throws Exception
    {
        final String port;
        if (args.length == 1) port = args[0];
        else port = "6767";

        final String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port + "/";

        HttpServerFactory.create(url).start();
        System.out.println("Server started on " + url + "\n [kill the process to exit]");
        
        Logger.getRootLogger().setLevel(Level.OFF);

        PollingEngine engine = new PollingEngine();
        engine.start();
        System.out.println("PollingEngine started");
    }
}
