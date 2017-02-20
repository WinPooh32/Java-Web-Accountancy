package tk.winpooh32;

import com.vaadin.data.Validator;
import com.vaadin.server.UserError;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class SubWinAddArrivalItem extends Window {

    private ComboBox nomenclatureCombo;
    private ComboBox countCombo;
    private ComboBox vatCombo;
    private TextField cost;

    private ViewArrival _parent;

    public SubWinAddArrivalItem(ViewArrival parent){
        super("Добавление нового товара"); // Set window caption
        center();
        setModal(true);

        _parent = parent;

        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        setContent(content);

        GridLayout grid = new GridLayout(2, 8);
        grid.addStyleName("my-form-grid");
        grid.setSpacing(true);

        //Номенклатура-----------------------------------------------------------------------------------------------------
        nomenclatureCombo = new ComboBox();
        grid.addComponent(new Label("Номенклатура"));
        grid.addComponent(nomenclatureCombo);

        nomenclatureCombo.setNullSelectionAllowed(false);
        nomenclatureCombo.addItems(generateItemsList());
        //--------------------------------------------------------------------------------------------------------------

        //Количество-------------------------------------------------------------------------------------------------------
        countCombo = new ComboBox();
        grid.addComponent(new Label("Количество"));
        grid.addComponent(countCombo);

        countCombo.setNullSelectionAllowed(false);
        countCombo.addItems(generateCountList(100));
        countCombo.select("1");
        countCombo.setNewItemsAllowed(true);
        //--------------------------------------------------------------------------------------------------------------

        //Цена-------------------------------------------------------------------------------------------------
        cost = new TextField();
        grid.addComponent(new Label("Цена"));
        grid.addComponent(cost);

        //cost.addValidator(new FloatRangeValidator(
        //        "Поле должно содержать вещественное целое число", 0.0f, null));
        //hideValidatorBeforeFocus(cost);
        //--------------------------------------------------------------------------------------------------------------

        //НДС-----------------------------------------------------------------------------------------------------------
        vatCombo = new ComboBox();
        grid.addComponent(new Label("НДС"));
        grid.addComponent(vatCombo);

        vatCombo.setNullSelectionAllowed(false);
        vatCombo.addItems(generateVATlist(100));
        vatCombo.select("18%");
        //--------------------------------------------------------------------------------------------------------------


        HorizontalLayout btnsLayout = new HorizontalLayout();
        btnsLayout.setSizeFull();

        Label expanderHorizontal = new Label("");

        Button btnAddClose = new Button("Добавить и закрыть");
        btnAddClose.addClickListener(clickEvent -> {
            try{
                parent.addRow(validateAndCreateRow());
                close();
            }catch(Validator.InvalidValueException badVal){
                btnAddClose.setComponentError(new UserError(badVal.getMessage()));
            }
        });

        Button btnAdd = new Button("Добавить");
        btnAdd.addClickListener(clickEvent -> {
            try{
                parent.addRow(validateAndCreateRow());
            }catch(Validator.InvalidValueException badVal){
                btnAdd.setComponentError(new UserError(badVal.getMessage()));
            }
        });
        btnsLayout.addComponents(expanderHorizontal, btnAddClose, btnAdd);
        btnsLayout.setExpandRatio(expanderHorizontal, 1.0f);
        btnsLayout.setSizeFull();
        btnsLayout.setSpacing(true);

        Label expanderVertical = new Label("");
        expanderVertical.setWidth(null);
        expanderVertical.setHeight("100%");

        content.addComponents(grid, expanderVertical, btnsLayout);
    }

    private ArrayList<String> generateVATlist(int size){
        ArrayList<String> arr = new ArrayList();

        for(int i = 0; i <= size; i++){
            arr.add(Integer.toString(i) + "%");
        }

        return arr;
    }

    private ArrayList<String> generateCountList(int size){
        ArrayList<String> arr = new ArrayList();

        for(int i = 0; i <= size; i++){
            arr.add(Integer.toString(i));
        }

        return arr;
    }

    private ArrayList<String> generateItemsList(){
        ArrayList<String> arr = new ArrayList();

        Map<Integer, Vector<String[]>> Table = DBConnection.getItemsData();
        Set<Map.Entry<Integer, Vector<String[]>>> set = Table.entrySet();

        for (Map.Entry<Integer, Vector<String[]>> element : set) {
            Vector<String[]> vec = element.getValue();

            for(String[] row: vec) {
                arr.add(row[0]);
            }
        }

        return arr;
    }

    private void hideValidatorBeforeFocus(TextField field){
        field.setValidationVisible(false);
        field.addBlurListener(fucus -> {
            field.setValidationVisible(true);
        });
    }

    private String[] validateAndCreateRow() throws Validator.InvalidValueException{
        //cost.validate();

        String[] row = new String[8];


        row[0] = Integer.toString(_parent.getItemsCount() + 1);
        row[1] = nomenclatureCombo.getValue().toString();
        row[2] = countCombo.getValue().toString();
        row[3] = cost.getValue();

        //Sum
        row[4] = Float.toString(Float.parseFloat(row[3]) * Integer.parseInt(row[2]));
        //VAT
        row[5] = vatCombo.getValue().toString().replace("%", "");
        //VAT value
        row[6] = Float.toString((Float.parseFloat(row[5]) / 100.0f) * Float.parseFloat(row[4]));
        //Result sum
        row[7] = Float.toString(Float.parseFloat(row[4]) + Float.parseFloat(row[6]));

        Notification.show("Товар добавлен");
        return row;
    }
}
