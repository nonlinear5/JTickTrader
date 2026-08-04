package com.jticktrader.platform.web;

import com.jticktrader.platform.model.Dispatcher;
import com.jticktrader.platform.preferences.PreferencesHolder;
import com.jticktrader.platform.report.EventReport;
import com.jticktrader.platform.startup.JTickTrader;
import com.jticktrader.platform.util.ui.MessageDialog;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import static com.jticktrader.platform.preferences.JBTPreferences.WebAccess;
import static com.jticktrader.platform.preferences.JBTPreferences.WebAccessPort;

/**
 * @author Eugene Kononov
 */
public class MonitoringServer {
    private static HttpServer server;

    public static void start() {
        if (server == null) {
            PreferencesHolder prefs = PreferencesHolder.getInstance();
            if (prefs.get(WebAccess).equalsIgnoreCase("enabled")) {
                EventReport eventReport = Dispatcher.getInstance().getEventReport();
                try {
                    int port = Integer.parseInt(prefs.get(WebAccessPort));
                    server = HttpServer.create(new InetSocketAddress(port), 0);
                    HttpContext context = server.createContext("/", new WebHandler());
                    context.setAuthenticator(new WebAuthenticator());
                    server.setExecutor(Executors.newSingleThreadExecutor());
                    server.start();
                    eventReport.report(JTickTrader.APP_NAME, "Monitoring server started");
                } catch (Exception e) {
                    eventReport.report(e);
                    MessageDialog.showError("Could not start monitoring server: " + e);
                }
            }
        }
    }

    public static void stop() {
        if (server != null) {
            server.stop(0);
        }
    }
}
