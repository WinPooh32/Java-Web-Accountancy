package tk.winpooh32;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.navigator.View;

public class BarTools extends HorizontalLayout{
    enum Page {
        Nomenclature, ItemsArrival, Registers
    }

    private Page _pageType;
    private View _parentView;

    public BarTools(View parentView, Page pageType){
        _pageType = pageType;
        _parentView = parentView;

        switch (_pageType){
            case Nomenclature:
                initForNomenclature();
                break;

            case ItemsArrival:
                initItemsArrival();
                break;

            case Registers:
                initRegisters();
                break;
        }
    }

    public Page getPageType(){
        return _pageType;
    }

    private void initForNomenclature(){
        Button btnAdd = new Button("Добавить");
        Button btnDelete = new Button("Удалить");

        btnAdd.addClickListener(click ->{
            SubWinAddItem sub = new SubWinAddItem();
            UI.getCurrent().addWindow(sub);

            sub.addCloseListener(closeEvent -> {
                ((ViewNomenclature)_parentView).initTable();
            });
        });

        addComponents(btnAdd, btnDelete);
    }

    private void initItemsArrival(){
        Button btnWrite = new Button("Записать");
        Button btnWriteWithSuccess = new Button("Провести");
        Button btnClear = new Button("Очистить");

        ViewArrival parent = (ViewArrival) _parentView;
        btnWrite.addClickListener(click -> parent.writeDocument(false));
        btnWriteWithSuccess.addClickListener(click -> parent.writeDocument(true));
        btnClear.addClickListener(click -> {parent.clearAll();});

        addComponents(btnWrite, btnWriteWithSuccess, btnClear);
    }

    private void initRegisters(){
//        Button btnWrite = new Button("Записать");
//        Button btnWriteWithSuccess = new Button("Провести");
//        Button btnClear = new Button("Очистить");
//
//        ViewArrival parent = (ViewArrival) _parentView;
//        btnWrite.addClickListener(click -> parent.writeDocument(false));
//        btnWriteWithSuccess.addClickListener(click -> parent.writeDocument(true));
//        btnClear.addClickListener(click -> {parent.clearAll();});

        addComponents();
    }
}
