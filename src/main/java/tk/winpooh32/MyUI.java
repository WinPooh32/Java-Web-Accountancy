package tk.winpooh32;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
    static final String COOKIE_UUID = "UUID";

    private final String appName = "Система торгового учета";

    private String currentUser = null;
    private String loginFrom = "!"+VIEW_MAIN;
    private boolean allowed = false;

//    private MyMenu_old menu = new MyMenu_old();

//    public class MyMenu_old extends MenuBar implements View{
//        public MyMenu_old(){
//            //setSizeFull();
//            setWidth("100%");
//// Define a common menu command for all the menu items.
//            MenuBar.Command mycommand = new MenuBar.Command() {
//                public void menuSelected(MenuItem selectedItem) {
//
//                }
//            };
//
//            MenuBar.Command cmdLogout = (Command) selectedItem -> logOut();
//
//
//            // A top-level menu item that opens a submenu
//            MenuItem drinks = addItem("Beverages", null, null);
//
//// Submenu item with a sub-submenu
//            MenuItem hots = drinks.addItem("Hot", null, null);
//            hots.addItem("Tea",
//                    new ThemeResource("icons/tea-16px.png"),    mycommand);
//            hots.addItem("Coffee",
//                    new ThemeResource("icons/coffee-16px.png"), mycommand);
//
//// Another submenu item with a sub-submenu
//            MenuItem colds = drinks.addItem("Cold", null, null);
//            colds.addItem("Milk",      null, mycommand);
//            colds.addItem("Weissbier", null, mycommand);
//
//// Another top-level item
//            MenuItem snacks = this.addItem("Snacks", null, null);
//            snacks.addItem("Weisswurst", null, mycommand);
//            snacks.addItem("Bratwurst",  null, mycommand);
//            snacks.addItem("Currywurst", null, mycommand);
//
//// Yet another top-level item
//            MenuItem servs = this.addItem("Services", null, null);
//            servs.addItem("Car Service", null, mycommand);
//
//            MenuItem btnItem = this.addItem("Button", null, null);
//            MenuItem outItem = this.addItem("LogOut", null, cmdLogout);
//        }
//
//        @Override
//        public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
//
//        }
//    }
//
//    public class ColorCellStyleGenerator implements Table.CellStyleGenerator {
//        @Override
//        public String getStyle(Table table, Object itemId, Object propertyId) {
//            return "cellcolored";
//        }
//    }
//
//
//
//    public class MainView extends VerticalLayout implements View {
//        public MainView(){
//            setSizeFull();
//            addComponent(menu);
//
//            System.out.println("We are in PAGE_MAIN page");
//            addComponent(new Label("Yo"));
//
//            TreeTable ttable = new TreeTable("My TreeTable");
//
//            ttable.setSelectable(true);
//            ttable.setImmediate(true);
//            ttable.setNullSelectionAllowed(false);
//
//            ttable.setReadOnly(true);
//
//            ttable.setCellStyleGenerator(new ColorCellStyleGenerator());
//            ttable.setSizeFull();
//            ttable.addContainerProperty("Краткое наименование", String.class, null);
//            ttable.addContainerProperty("Код", Integer.class, null);
//
//            ttable.setColumnExpandRatio("Код", 1);
//            ttable.setColumnExpandRatio("Краткое наименование", 2);
//
//            ttable.addColumnResizeListener(columnResizeEvent -> {
//                ttable.setColumnExpandRatio("Код", 1);
//                ttable.setColumnExpandRatio("Краткое наименование", 2);
//            });
//
//// Create the tree nodes and set the hierarchy
//            ttable.addItem(new Object[]{"Товары", null}, 0);
//            ttable.addItem(new Object[]{"Beverages", null}, 1);
//            ttable.setParent(1, 0);
//            ttable.addItem(new Object[]{"Foods", null}, 2);
//            ttable.setParent(2, 0);
//            ttable.addItem(new Object[]{"Coffee", 23}, 3);
//            ttable.addItem(new Object[]{"Tea", 42}, 4);
//            ttable.setParent(3, 1);
//            ttable.setParent(4, 1);
//            ttable.addItem(new Object[]{"Bread", 13}, 5);
//            ttable.addItem(new Object[]{"Cake", 11}, 6);
//            ttable.setParent(5, 2);
//            ttable.setParent(6, 2);
//
//            // Expand the tree
//            for (Object itemId: ttable.getContainerDataSource()
//                    .getItemIds()) {
//                ttable.setCollapsed(itemId, false);
//
//                // As we're at it, also disallow children from
//                // the current leaves
//                if (! ttable.hasChildren(itemId))
//                    ttable.setChildrenAllowed(itemId, false);
//            }
//
//
//            ttable.addColumnResizeListener(new TreeTable.ColumnResizeListener(){
//                public void columnResize(TreeTable.ColumnResizeEvent event) {
//                    // Get the new width of the resized column
//                    int width = event.getCurrentWidth();
//
//                    // Get the property ID of the resized column
//                    String column = (String) event.getPropertyId();
//
//                    // Do something with the information
//                    ttable.setColumnFooter(column, String.valueOf(width) + "px");
//                }
//            });
//
//            // Must be immediate to send the resize events immediately
//            ttable.setImmediate(true);
//
////
////            int shownRowsCount = ttable.getVisibleItemIds().size();
////            ttable.setPageLength(shownRowsCount + 10);
//
//            addComponent(ttable);
//
//            setExpandRatio(ttable, 1);
//        }
//
//        @Override
//        public void enter(ViewChangeListener.ViewChangeEvent event) {
//            System.out.println("Entering to Main");
//            if(!checkAuthentication()){return;}
//
//            getPage().setTitle("Главная - " + appName);
//            Notification.show("Heyo! This is main page!");
//        }
//    }

    Navigator navigator;
    protected static final String VIEW_LOGIN= "login";
    protected static final String VIEW_MAIN = "main";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getPage().setTitle("Вход - " + appName);

        // Create a navigator to control the views
        navigator = new Navigator(this, this);

        User user = new User(navigator);

        //Connect to DB
        DBConnection.connect();

        // Create and register the views
        navigator.addView(VIEW_LOGIN, new ViewLogin(user));
        navigator.addView(VIEW_MAIN, new ViewNomenclature(user));

        //System.out.print(UUID.randomUUID().toString());

        if (Page.getCurrent().getUriFragment() == null || Page.getCurrent().getUriFragment().equals("")) {
            navigator.navigateTo(VIEW_MAIN);
        }


        //navigator.navigateTo(VIEW_LOGIN);
        //Page.getCurrent().setUriFragment("!login");
        //setContent(new LoginView());


//        final VerticalLayout layout = new VerticalLayout();
//
//        final TextField name = new TextField();
//
//        MenuBar barmenu = new MenuBar();
//        addMenuItems(barmenu);
//        layout.addComponent(barmenu);
//
//        name.setCaption("Type your name here:");
//
//        Button button = new Button("Click Me");
//        button.addClickListener( e -> {
//            layout.addComponent(new Label("Thanks " + name.getValue()
//                    + ", it works!"));
//        });
//
//        layout.addComponents(name, button);
//        layout.setMargin(true);
//        layout.setSpacing(true);
//
//
//        JDBCConnectionPool pool = null;
//        try {
////            String dbUrl = "jdbc:mysql://localhost:3306/Accountancy";
////            Connection con;
////
////            con = DriverManager.getConnection(dbUrl, "accountancy", "159357zxc");
////            PreparedStatement ps;
////            Statement cs;
////            ResultSet rs;
////
////            cs = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
////            rs = cs.executeQuery("select * from tablename");
//
//            pool = new SimpleJDBCConnectionPool(
//                    "com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/Accountancy",
//                    "accountancy", "159357zxc", 2, 2);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        //TableQuery tq = new TableQuery("new_table", pool);
//        //tq.setVersionColumn("OPTLOCK");
//
//        FreeformQuery query = null;
//        try {
//            query = new FreeformQuery( "SELECT * FROM new_table", pool, "ID");
//            SQLContainer container = new SQLContainer(query);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        SQLContainer sql_container = null;
//        try {
//            sql_container = new SQLContainer(query);
//            sql_container.setAutoCommit(true);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//
//        //container.enableCacheFlushNotifications();
//
//
//        //sql_container.addContainerFilter();
//        GeneratedPropertyContainer container = new GeneratedPropertyContainer(sql_container);
//
//        //container.addItem(new Object[]{new Integer("3"), new Integer("123"), "heyyo228"});
////
////        Table table = new Table("The Brightest Stars");
////        table.setContainerDataSource(container);
////        table.setEditable(true);
////
////
////
////        //table.setNewItemsAllowed(true);
////        table.setVisibleColumns(new Object[]{"super_row", "row_2"});
////        table.refreshRowCache();
////
////        layout.addComponent(table);
//
//
//        Grid grid = new Grid();
//        grid.setContainerDataSource(sql_container);
//        grid.setEditorEnabled(true);
//        grid.setReadOnly(false);
//
//
//        layout.addComponent(grid);
//
//
//
//        //table.setNewItemsAllowed(true);
//
//        Button btn_save = new Button("Save table");
//        btn_save.addClickListener( e -> {
//            //table.commit();
//
//            System.out.print("Hey, btn save pressed");
//        });
//
//        layout.addComponent(btn_save);
//
//
//        Button btn_addrow = new Button("Add new row");
//        //SQLContainer finalContainer = container;
//        SQLContainer finalSql_container = sql_container;
//        btn_addrow.addClickListener(e -> {
//            //table.addItem(new Object[]{"3", "999", "Last Modified"});
//            //table.addItem(new Object[] {new Integer(123), new String("Hey string")});
//            //grid.addRow(new Integer(123), "heyyo228");
//            //finalContainer.addItem(new Object[]{"3", "999", "Last Modified"});
//
//            //grid.addRow(5, 234324, "HeyString");
//
//            Object id = finalSql_container.addItem();
//            //finalSql_container.getContainerProperty(id, "id").setValue("7");
//            finalSql_container.getContainerProperty(id, "super_row").setValue("234");
//            finalSql_container.getContainerProperty(id, "row_2").setValue("A LOT SHIT");
//
//            try {
//                finalSql_container.commit();
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        });
//
//        layout.addComponent(btn_save);
//        layout.addComponent(btn_addrow);
//
//
//
//        // Create a PersonForm and bind form fields to a person bean.
//        PersonForm form = new PersonForm();
//
//        layout.addComponent(form);
//
//
//        // Some UI logic to open the sub-window
//        final Button open = new Button("Open Sub-Window");
//        open.addClickListener(new Button.ClickListener() {
//            public void buttonClick(Button.ClickEvent event) {
//                MySub sub = new MySub();
//
//                // Add it to the root component
//                UI.getCurrent().addWindow(sub);
//            }
//        });
//
//        layout.addComponent(open);
//
//
//
//        Page.getCurrent().setUriFragment("mars");
//
//        setContent(layout);
    }

    public void close(){
        super.close();
        DBConnection.disconnect();
    }

    public String getCurrentUser(){
        return currentUser;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinServlet {
    }
}
