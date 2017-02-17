package tk.winpooh32;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.Table;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class ViewNomenclature extends VerticalLayout implements View {
    User _user;
    BarHeader _header;
    BarTools _tools;
    TreeTable _ttable;

    public ViewNomenclature(User user){
        _user = user;
        _header = new BarHeader(_user, "Номенклатура");
        _tools = new BarTools(this, BarTools.Page.Nomenclature);

        setSizeFull();

        addComponents(_header, _tools);

        setSpacing(true);
    }

    public void initTable(){
        TreeTable table = new TreeTable();

        if(_ttable != null){
            replaceComponent(_ttable, table);
        }
        _ttable = table;

        _ttable.setSizeFull();
        _ttable.setSelectable(true);
        _ttable.setImmediate(true);
        _ttable.setNullSelectionAllowed(false);

        _ttable.setReadOnly(true);

        _ttable.setCellStyleGenerator(new ColorCellStyleGenerator());
        _ttable.setSizeFull();
        _ttable.addContainerProperty("Краткое наименование", String.class, null);
        _ttable.addContainerProperty("Код", String.class, null);

        _ttable.setColumnExpandRatio("Код", 1);
        _ttable.setColumnExpandRatio("Краткое наименование", 2);

        _ttable.addColumnResizeListener(columnResizeEvent -> {
            _ttable.setColumnExpandRatio("Код", 1);
            _ttable.setColumnExpandRatio("Краткое наименование", 2);
        });

        // Create the tree nodes and set the hierarchy
        _ttable.addItem(new Object[]{"Товары", null}, 0);

        Map<Integer, Vector<String[]>> Table = DBConnection.getItemsData();
        Set<Map.Entry<Integer, Vector<String[]>>> set = Table.entrySet();

        int i = 1;
        for (Map.Entry<Integer, Vector<String[]>> me : set) {
            Vector<String[]> vec = me.getValue();

            int parent;
            if(me.getKey() != 4){//"none" category aka root
                _ttable.addItem(new Object[]{vec.get(0)[2], null}, i);
                _ttable.setParent(i, 0);
                parent = i;
            }else{
                parent = 0;
            }

            int j = 1;
            for(String[] row: vec) {
                _ttable.addItem(new Object[]{row[0], row[1]}, i + j);
                _ttable.setParent(i + j, parent);
                j++;
            }

            i += j + 1;
        }

        // Expand the tree
        for (Object itemId: _ttable.getContainerDataSource().getItemIds()) {
            _ttable.setCollapsed(itemId, false);

            // As we're at it, also disallow children from
            // the current leaves
            if (!_ttable.hasChildren(itemId)){
                _ttable.setChildrenAllowed(itemId, false);
            }else{
                _ttable.setItemIcon(itemId, VaadinIcons.FOLDER);
            }
        }

        _ttable.addColumnResizeListener(new TreeTable.ColumnResizeListener(){
            public void columnResize(TreeTable.ColumnResizeEvent event) {
                // Get the new width of the resized column
                int width = event.getCurrentWidth();

                // Get the property ID of the resized column
                String column = (String) event.getPropertyId();

                // Do something with the information
                _ttable.setColumnFooter(column, String.valueOf(width) + "px");
            }
        });

        // Must be immediate to send the resize events immediately
        _ttable.setImmediate(true);

        addComponent(_ttable);

        //Expand table
        setExpandRatio(_ttable, 1);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if(!_user.checkAuthentication()){
            return;
        }else{
            Page.getCurrent().setTitle(Navigation.TITLE_MAIN);

            initTable();
        }
    }


    public class ColorCellStyleGenerator implements Table.CellStyleGenerator {
        @Override
        public String getStyle(Table table, Object itemId, Object propertyId) {
            return "cellcolored";
        }
    }
}
