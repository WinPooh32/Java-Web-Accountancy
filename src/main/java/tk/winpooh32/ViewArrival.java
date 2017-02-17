package tk.winpooh32;


import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ViewArrival extends VerticalLayout implements View {

    private User _user;
    private BarHeader _header;
    private BarTools _tools;
    private Table _table;

    private Label expander;

    private TextField shortName;
    private TextField number;
    private DateField date;
    private ComboBox  counterpartyCombo;

    public ViewArrival(User user){
        _user = user;
        _header = new BarHeader(_user, "Поступления");
        _tools = new BarTools(this, BarTools.Page.ItemsArrival);

        expander = new Label("");


        _table = new Table("Товары/Услуги");
        // Define two columns for the built-in container

        _table.addContainerProperty("N", Integer.class, null);
        _table.addContainerProperty("Номенклатура", String.class, null);
        _table.addContainerProperty("Количество", Integer.class, null);
        _table.addContainerProperty("Цена", DecimalFormat.class, null);
        _table.addContainerProperty("Сумма", DecimalFormat.class, null);
        _table.addContainerProperty("НДС%", Integer.class, null);
        _table.addContainerProperty("НДС", DecimalFormat.class, null);
        _table.addContainerProperty("Всего", DecimalFormat.class, null);

        _table.addItem(new Object[]{"Alpha Centauri", -0.01f}, 4);

        // Show exactly the currently contained rows (items)
        _table.setPageLength(_table.size());
        _table.setSizeFull();


        setSizeFull();

        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);

        GridLayout grid = new GridLayout(2, 8);
        grid.addStyleName("my-form-grid");
        grid.setSpacing(true);

        //Код-----------------------------------------------------------------------------------------------------------
        number = new TextField();
        grid.addComponent(new Label("Номер"));
        grid.addComponent(number);

        number.addValidator(new StringLengthValidator(
                "Поле должно содержать 1-45 символов",
                3, 45, true));
        hideValidatorBeforeFocus(number);
        //--------------------------------------------------------------------------------------------------------------

        //Дата-----------------------------------------------------------------------------------------------------------
        date = new DateField();
        grid.addComponent(new Label("Дата"));
        grid.addComponent(date);
        //--------------------------------------------------------------------------------------------------------------


        //Контрагент-----------------------------------------------------------------------------------------------------
        counterpartyCombo = new ComboBox();
        grid.addComponent(new Label("Контрагент"));
        grid.addComponent(counterpartyCombo);

        counterpartyCombo.setNullSelectionAllowed(false);
        counterpartyCombo.addItems(generateCounterpartiesList());
        //--------------------------------------------------------------------------------------------------------------

        content.addComponent(grid);
        addComponents(_header, _tools, content, _table);

        setSpacing(true);

        expander.setSizeFull();
        setExpandRatio(_table, 1);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if(!_user.checkAuthentication()){
            return;
        }else{
            Page.getCurrent().setTitle(Navigation.TITLE_MAIN);
        }
    }

    private void hideValidatorBeforeFocus(TextField field){
        field.setValidationVisible(false);
        field.addBlurListener(fucus -> {
            field.setValidationVisible(true);
        });
    }


    private ArrayList<String> generateCounterpartiesList(){
        ArrayList<String> arr = new ArrayList();
        arr.add("Ололо");
        arr.add("Проба пера");
//        Map<String, Integer> categoryMap = DBConnection.getCategories();
//        Set<Map.Entry<String, Integer>> set = categoryMap.entrySet();
//
//        arr.add("");
//
//        for (Map.Entry<String, Integer> element : set) {
//            if(!element.getKey().equals("none")){
//                arr.add(element.getKey());
//            }
//        }
        return arr;
    }
}
