package tk.winpooh32;


import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class ViewArrival extends VerticalLayout implements View {

    private User _user;
    private BarHeader _header;
    private BarTools _tools;
    private Table _table;

    private ArrayList<String[]> _Rows;

    private Label expander;

    private TextField number;
    private DateField date;
    private ComboBox  counterpartyCombo;

    public ViewArrival(User user){
        _user = user;
        _header = new BarHeader(_user, "Поступления");
        _tools = new BarTools(this, BarTools.Page.ItemsArrival);
        _Rows = new ArrayList<>();

        expander = new Label("");


        _table = new Table("Товары/Услуги");
        // Define two columns for the built-in container

        _table.addContainerProperty("N", String.class, null);
        _table.addContainerProperty("Номенклатура", String.class, null);
        _table.addContainerProperty("Количество", String.class, null);
        _table.addContainerProperty("Цена", String.class, null);
        _table.addContainerProperty("Сумма", String.class, null);
        _table.addContainerProperty("НДС%", String.class, null);
        _table.addContainerProperty("НДС", String.class, null);
        _table.addContainerProperty("Всего", String.class, null);

        //DecimalFormat df = new DecimalFormat("###.##");

        fillTable();

        // Show exactly the currently contained rows (items)
        _table.setPageLength(_table.size());
        _table.setSizeFull();


        setSizeFull();

        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);

        GridLayout grid = new GridLayout(2, 3);
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


        HorizontalLayout itemActions = new HorizontalLayout();
        content.setMargin(true);
        Button btnAddItem = new Button("Добавить");
        Button btnDelItem = new Button("Удалить");
        itemActions.addComponents(btnAddItem, btnDelItem);

        btnAddItem.addClickListener(click ->{
            SubWinAddArrivalItem sub = new SubWinAddArrivalItem(this);
            UI.getCurrent().addWindow(sub);

            sub.addCloseListener(closeEvent -> {
                fillTable();
            });
        });

        btnDelItem.addClickListener(click ->{

        });

        content.addComponent(grid);
        addComponents(_header, _tools, content, itemActions, _table);

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

    public void addRow(String[] row){
        _Rows.add(row);
    }
    public int getItemsCount(){return _Rows.size();}

    public void fillTable(){
        _table.removeAllItems();

        int i = 0;
        for (String[] row : _Rows) {
            _table.addItem(row,i);
            ++i;
        }
    }

    public void writeDocument(boolean success){
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date.getValue());
        cal.set(java.util.Calendar.MILLISECOND, 0);
        String str_date = (new java.sql.Date(cal.getTimeInMillis())).toString();

        String party_id = Integer.toString(DBConnection.getCounterparties().get(counterpartyCombo.getValue().toString()));
        String successed = success ? "1" : "0";

        int docId = DBConnection.AddDocument(new String[]{
                str_date,
                number.getValue().toString(),
                party_id,
                "1",
                successed
        });

        pushRows(docId);
    }

    public void pushRows(int docId){
        Map<String, Integer> ItemsIds = DBConnection.getItemsIds();

       for(String[] row: _Rows){
           String[] result_row = new String[8];

           String name = row[1];
           System.out.println(name);

           result_row[0] = Integer.toString(ItemsIds.get(row[1]));
           result_row[1] = row[2];
           result_row[2] = row[3];
           result_row[3] = row[4];
           result_row[4] = row[5];
           result_row[5] = row[6];
           result_row[6] = row[7];
           result_row[7] = Integer.toString(docId);

           DBConnection.AddDocItem(result_row);
       }
    }

    public void clearAll(){
        _table.removeAllItems();

        _Rows.clear();

        number.clear();
        date.clear();
        counterpartyCombo.clear();
    }

    private void hideValidatorBeforeFocus(TextField field){
        field.setValidationVisible(false);
        field.addBlurListener(fucus -> {
            field.setValidationVisible(true);
        });
    }


    private ArrayList<String> generateCounterpartiesList(){
        ArrayList<String> arr = new ArrayList();
        Map<String, Integer> categoryMap = DBConnection.getCounterparties();
        Set<Map.Entry<String, Integer>> set = categoryMap.entrySet();

        for (Map.Entry<String, Integer> element : set) {
            arr.add(element.getKey());
        }
        return arr;
    }
}
