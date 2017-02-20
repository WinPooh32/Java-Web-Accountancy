package tk.winpooh32;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.Map;

public class ViewRegisterTables extends VerticalLayout implements View {
    User _user;
    BarHeader _header;
    BarTools _tools;
    private Table _table;

    public ViewRegisterTables(User user){
        _user = user;
        _header = new BarHeader(_user, "Таблицы-регистры");
        _tools = new BarTools(this, BarTools.Page.Registers);
        _table = new Table();

        _table.addContainerProperty("Дата", String.class, null);
        _table.addContainerProperty("Товар", String.class, null);
        _table.addContainerProperty("Количество", String.class, null);
        _table.addContainerProperty("Цена", String.class, null);
        _table.addContainerProperty("Номер", String.class, null);

        _table.setSizeFull();


        addComponents(_header, _tools, _table);
    }

    public void fillTable() {
        _table.removeAllItems();

        Map<Integer, String> items = DBConnection.getItemsMap();
        ArrayList<String[]> registers = DBConnection.getRegisters();

        int i = 0;
        for (String[] row : registers) {
            String[] real_row = new String[5];
            real_row[0] = row[4];
            real_row[1] = items.get(Integer.parseInt(row[0]));
            real_row[2] = row[1];
            real_row[3] = row[6];
            real_row[4] = row[3];

            _table.addItem(real_row, i);
            ++i;
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if(!_user.checkAuthentication()){
            return;
        }else{
            Page.getCurrent().setTitle(Navigation.PAGE_REGISTERS);
            fillTable();
        }
    }
}
