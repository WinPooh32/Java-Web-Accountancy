package tk.winpooh32;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Notification;

import javax.servlet.http.Cookie;
import java.util.UUID;

public class User {
    private static final String COOKIE_UUID = "UUID";

    private boolean _allowed;
    private Navigator _navigator;
    private String _username;
    private String _loginFrom;

    public User(Navigator navigator){
        _navigator = navigator;
    }

    public boolean isPrivileged(){
        return _allowed;
    }
    public String getUsername(){
        return _username;
    }
    public String getLoginFrom(){
        return _loginFrom;
    }

    public void navigateTo(String dest){
        _navigator.navigateTo(dest);
    }

    public void logIn(String name, String passwd){
        try {
            authenticate(name, passwd);

            String prevState = null;

            if(_loginFrom != null && _loginFrom.length() >= 2){
                prevState = getLoginFrom().substring(1);//skip !
            }

            if(prevState == null || prevState.equals("") || prevState.equals(Navigation.PAGE_LOGIN)){
                navigateTo(Navigation.PAGE_MAIN);
            }else{
                navigateTo(prevState);
            }
        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
            Notification.show(e.getMessage());
        }
    }

    public void logOut(){
        deleteCookieUUID();

        _allowed = false;
        _navigator.navigateTo(Navigation.PAGE_LOGIN);
    }

    public Cookie getCookieByName(String name) {
        // Fetch all cookies from the request
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

        // Iterate to find cookie by its name
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }

        return null;
    }

    public void authenticate(String login, String password) throws Exception{
        if("user".equals(login) && "qwerty".equals(password)){
            _username = login;
            _allowed = true;

            addUUIDCookie();
        }else{
            throw new Exception ("Login failed !");
        }
    }

    public boolean authenticateByCookie(){
        Cookie cookie = getCookieByName(COOKIE_UUID);
        String token;

        if(cookie != null){
            token = cookie.getValue();

            if(token != null && token.length() >= 36){
                //check UID validity here
                return true;
            }
        }

        //deleteCookieUUID();
        return false;
    }

    public boolean checkAuthentication(){
        if(authenticateByCookie() || _allowed){
            return true;
        }else{
            _loginFrom = Page.getCurrent().getUriFragment();

            if(_loginFrom == null || !_loginFrom.equals("!" + Navigation.PAGE_LOGIN)){
                _navigator.navigateTo(Navigation.PAGE_LOGIN);
            }

            return false;
        }
    }

    private void deleteCookieUUID(){
//        System.out.println("Delete cookie");
//
//        Cookie myCookie = new Cookie(COOKIE_UUID, "");
//
//        // By setting the cookie maxAge to 0 it will deleted immediately
//        //myCookie.setMaxAge(0);
//        //myCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
//        myCookie.setPath("/");
//        VaadinService.getCurrentResponse().addCookie(myCookie);
        Page.getCurrent().getJavaScript().execute(String.format("document.cookie = '%s=%s;';", COOKIE_UUID, ""));
    }

    private void addUUIDCookie(){
//        // Create a new cookie
//        Cookie myCookie = new Cookie(COOKIE_UUID, UUID.randomUUID().toString());
//
//        // Make cookie expire in 2 minutes
//        //myCookie.setMaxAge(1000000);
//
//        // Set the cookie path.
//        //myCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
//        myCookie.setPath("/");
//
//        System.out.println("Set cookie !");
//
//        // Save cookie
//
//        VaadinService.getCurrentResponse().addCookie(myCookie);
        Page.getCurrent().getJavaScript().execute(String.format("document.cookie = '%s=%s;';", COOKIE_UUID, UUID.randomUUID().toString()));
    }
}
