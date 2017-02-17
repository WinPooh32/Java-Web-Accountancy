package tk.winpooh32;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class BarHeader extends HorizontalLayout {
    private User _user;

    private final Button btn_main = new Button("Номенклатура");
    private final Button btn_arrivals = new Button("Поступления");
    private final Button btn_registers = new Button("Таблицы-регистры");
    private final Button btn_logout = new Button("Выход");

    public BarHeader(User user, String pageTitle){
        _user = user;

        Label title = new Label(pageTitle);

        btn_main.setIcon(VaadinIcons.LIST);
        btn_arrivals.setIcon(VaadinIcons.CART_O);
        btn_logout.setIcon(VaadinIcons.EXIT_O);
        btn_registers.setIcon(VaadinIcons.CLIPBOARD_PULSE);

        btn_main.setEnabled(false);

        btn_main.addClickListener(click -> _user.navigateTo(Navigation.PAGE_MAIN));
        btn_logout.addClickListener(click -> _user.logOut());


        Label expander = new Label("");
        expander.setSizeFull();

        setWidth("100%");

        addComponents(btn_main, btn_arrivals, btn_registers, expander, btn_logout);
        setExpandRatio(expander, 1.f);
    }
}