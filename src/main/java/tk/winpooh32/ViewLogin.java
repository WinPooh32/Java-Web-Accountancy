package tk.winpooh32;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

public class ViewLogin extends VerticalLayout implements View {
    private User _user;

    public ViewLogin(User user){
        _user = user;

        setSizeFull();

        //Setup login form ---------------------------------------------------------------------------------------------
        FormLayout form = new FormLayout();
        String fieldWidth = "200px";

        TextField nameField = new TextField("Пользователь");
        nameField.setWidth(fieldWidth);

        PasswordField passwdField = new PasswordField("Пароль");
        passwdField.setWidth(fieldWidth);

        form.addComponents(nameField, passwdField);
        form.setWidth("100%");
        form.setStyleName("loginform");
        //--------------------------------------------------------------------------------------------------------------


        //Setup login button -------------------------------------------------------------------------------------------
        HorizontalLayout hzLayout  = new HorizontalLayout();
        hzLayout.setWidth(fieldWidth);

        Button btnLogin = new Button("Войти");
        btnLogin.addClickListener(click->{_user.logIn(nameField.getValue(), passwdField.getValue());});
        btnLogin.setStyleName("button");

        hzLayout.addComponent(btnLogin);
        hzLayout.setComponentAlignment(btnLogin, Alignment.MIDDLE_RIGHT);

        form.addComponent(hzLayout);
        //--------------------------------------------------------------------------------------------------------------


        VerticalLayout shadowedBox = new VerticalLayout();
        shadowedBox.setWidthUndefined();
        shadowedBox.setStyleName("loginbox");

        shadowedBox.addComponents(form);
        shadowedBox.setWidthUndefined();

        addComponent(shadowedBox);
        setComponentAlignment(shadowedBox, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if(_user.authenticateByCookie() && _user.isPrivileged()){
            _user.navigateTo(Navigation.PAGE_MAIN);
        }else{
            Page.getCurrent().setTitle(Navigation.TITLE_LOGIN);
        }
    }
}
