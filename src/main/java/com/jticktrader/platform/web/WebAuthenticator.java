package com.jticktrader.platform.web;

import com.jticktrader.platform.preferences.PreferencesHolder;
import com.jticktrader.platform.startup.JTickTrader;
import com.sun.net.httpserver.BasicAuthenticator;

import static com.jticktrader.platform.preferences.JBTPreferences.WebAccessPassword;
import static com.jticktrader.platform.preferences.JBTPreferences.WebAccessUser;

/**
 * @author Eugene Kononov
 */
public class WebAuthenticator extends BasicAuthenticator {
    private final String expectedUser;
    private final String expectedPassword;

    public WebAuthenticator() {
        super(JTickTrader.APP_NAME);
        PreferencesHolder prefs = PreferencesHolder.getInstance();
        expectedUser = prefs.get(WebAccessUser);
        expectedPassword = prefs.get(WebAccessPassword);
    }

    @Override
    public boolean checkCredentials(String userName, String password) {
        return expectedUser.equals(userName) && expectedPassword.equals(password);
    }
}