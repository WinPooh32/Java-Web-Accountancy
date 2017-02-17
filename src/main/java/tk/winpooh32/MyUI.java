package tk.winpooh32;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
    static final String COOKIE_UUID = "UUID";

    private final String appName = "Система торгового учета";
    private String currentUser = null;

    Navigator navigator;
    protected static final String VIEW_LOGIN= "login";
    protected static final String VIEW_MAIN = "main";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getPage().setTitle("Вход - " + appName);

        // Create a navigator to control the views
        navigator = new Navigator(this, this);

        User user = new User(navigator);

        //Connect to DB
        DBConnection.connect();

        // Create and register the views
        navigator.addView(VIEW_LOGIN, new ViewLogin(user));
        navigator.addView(VIEW_MAIN, new ViewNomenclature(user));

        //System.out.print(UUID.randomUUID().toString());

        if (Page.getCurrent().getUriFragment() == null || Page.getCurrent().getUriFragment().equals("")) {
            navigator.navigateTo(VIEW_MAIN);
        }
    }

    public void close(){
        super.close();
        DBConnection.disconnect();
    }

    public String getCurrentUser(){
        return currentUser;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinServlet {
    }
}
