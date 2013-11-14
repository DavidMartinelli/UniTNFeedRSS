package it.unitn.hci.feed.server;

import java.net.InetAddress;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;

public class ServerLauncher
{

    public static void main(String[] args) throws Exception
    {
        final String port;
        if (args.length == 1) port = args[0];
        else port = "8080";

        final String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port + "/";

        HttpServerFactory.create(url).start();
        System.out.println("server starts on " + url + "\n [kill the process to exit]");
    }
}
