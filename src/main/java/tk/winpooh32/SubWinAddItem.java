package tk.winpooh32;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.UserError;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class SubWinAddItem  extends Window {

    private TextField shortName;
    private TextField fullName;
    private ComboBox categoryCombo;
    private CheckBox isService;
    private ComboBox unitsCombo;
    private ComboBox vatCombo;
    private TextField manufacturer;
    private TextField code;

    public SubWinAddItem(){
        super("Добавление нового товара"); // Set window caption
        center();
        setModal(true);

        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        setContent(content);

        GridLayout grid = new GridLayout(2, 8);
        grid.addStyleName("my-form-grid");
        grid.setSpacing(true);

        //Краткое наименование------------------------------------------------------------------------------------------
        shortName = new TextField();
        grid.addComponent(new Label("Краткое наименование"));
        grid.addComponent(shortName);


        // Define validation as usual
        shortName.addValidator(new StringLengthValidator(
                "Краткое название должно содержать 3-45 символов",
                3, 45, true));
        hideValidatorBeforeFocus(shortName);

        shortName.focus();
        //--------------------------------------------------------------------------------------------------------------


        //Полное наименование-------------------------------------------------------------------------------------------
        fullName = new TextField();
        grid.addComponent(new Label("Полное наименование"));
        grid.addComponent(fullName);

        fullName.addValidator(new StringLengthValidator(
                "Полное название должно содержать 3-200 символов",
                3, 200, true));
        hideValidatorBeforeFocus(fullName);
        //--------------------------------------------------------------------------------------------------------------


        //Категория-----------------------------------------------------------------------------------------------------
        categoryCombo = new ComboBox();
        grid.addComponent(new Label("Категория"));
        grid.addComponent(categoryCombo);

        categoryCombo.setNullSelectionAllowed(false);
        categoryCombo.addItems(generateCategoriesList());
        //--------------------------------------------------------------------------------------------------------------


        //Услуга--------------------------------------------------------------------------------------------------------
        isService = new CheckBox("Услуга");
        grid.addComponent(new Label(""));
        grid.addComponent(isService);
        //--------------------------------------------------------------------------------------------------------------


        //Единица-------------------------------------------------------------------------------------------------------
        unitsCombo = new ComboBox();
        grid.addComponent(new Label("Единица"));
        grid.addComponent(unitsCombo);

        unitsCombo.setNullSelectionAllowed(false);
        unitsCombo.addItems(generateUnitsList());
        unitsCombo.select("Шт");
        //--------------------------------------------------------------------------------------------------------------


        //НДС-----------------------------------------------------------------------------------------------------------
        vatCombo = new ComboBox();
        grid.addComponent(new Label("НДС"));
        grid.addComponent(vatCombo);

        vatCombo.setNullSelectionAllowed(false);
        vatCombo.addItems(generateVATlist(100));
        vatCombo.select("18%");
        //--------------------------------------------------------------------------------------------------------------


        //Производитель-------------------------------------------------------------------------------------------------
        manufacturer = new TextField();
        grid.addComponent(new Label("Производитель"));
        grid.addComponent(manufacturer);

        manufacturer.addValidator(new StringLengthValidator(
                "Поле должно содержать 3-200 символов",
                3, 200, true));
        hideValidatorBeforeFocus(manufacturer);
        //--------------------------------------------------------------------------------------------------------------


        //Код-----------------------------------------------------------------------------------------------------------
        code = new TextField();
        grid.addComponent(new Label("Код"));
        grid.addComponent(code);

        code.addValidator(new StringLengthValidator(
                "Поле должно содержать 1-45 символов",
                3, 45, true));
        hideValidatorBeforeFocus(code);
        //--------------------------------------------------------------------------------------------------------------



        HorizontalLayout btnsLayout = new HorizontalLayout();
        btnsLayout.setSizeFull();

        Label expanderHorizontal = new Label("");

        Button btnAddClose = new Button("Добавить и закрыть");
        btnAddClose.addClickListener(clickEvent -> {
            try{
                validateAndPush();
                close();
            }catch(Validator.InvalidValueException badVal){
                btnAddClose.setComponentError(new UserError(badVal.getMessage()));
            }
        });

        Button btnAdd = new Button("Добавить");
        btnAdd.addClickListener(clickEvent -> {
            try{
                validateAndPush();
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

    private ArrayList<String> generateUnitsList(){
        ArrayList<String> arr = new ArrayList();
        Map<String, Integer> unitsMap = DBConnection.getUnits();
        Set<Map.Entry<String, Integer>> set = unitsMap.entrySet();

        for (Map.Entry<String, Integer> element : set) {
            arr.add(element.getKey());
        }

        return arr;
    }

    private ArrayList<String> generateCategoriesList(){
        ArrayList<String> arr = new ArrayList();
        Map<String, Integer> categoryMap = DBConnection.getCategories();
        Set<Map.Entry<String, Integer>> set = categoryMap.entrySet();

        arr.add("");

        for (Map.Entry<String, Integer> element : set) {
            if(!element.getKey().equals("none")){
                arr.add(element.getKey());
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

    private void validateAndPush() throws Validator.InvalidValueException{
        shortName.validate();
        fullName.validate();
        manufacturer.validate();
        code.validate();

        Map<String, Integer> unitsMap = DBConnection.getUnits();
        Map<String, Integer> categoryMap = DBConnection.getCategories();

        DBConnection.AddItem(new String[]{
                fullName.getValue(),
                shortName.getValue(),
                code.getValue(),
                (isService.getValue()) ? "1" : "0",
                vatCombo.getValue().toString().replace("%", ""),
                ((categoryCombo.getValue() == null || categoryCombo.getValue().equals("")) ? Integer.toString(categoryMap.get("none")) : Integer.toString(categoryMap.get(categoryCombo.getValue()))),
                manufacturer.getValue(),
                "0",
                Integer.toString(unitsMap.get(unitsCombo.getValue()))
        });

        Notification.show("Товар добавлен");
    }
}
