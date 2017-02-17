package tk.winpooh32;

import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public final class DBConnection {
    private static JDBCConnectionPool connectionPool;
    private static boolean _online = false;

    private DBConnection(){}

    public static void connect(){
        if(_online){
            return;
        }

        connectionPool = null;

        try {
            connectionPool = new SimpleJDBCConnectionPool(
                    "com.mysql.jdbc.Driver",
                    "jdbc:mysql://localhost:3306/Accountancy?useUnicode=true&characterEncoding=UTF8&characterSetResults=UTF8",
                    "accountancy",
                    "159357zxc", 2, 2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect(){
        if(_online){
            System.out.println("Disconnecting DB");

            connectionPool.destroy();
            connectionPool = null;
            _online = false;
        }
    }

    public static boolean checkAuth(String name, String passwrd){
        return true;
    }

    public static void AddItem(String[] row){
        //FreeformQuery query = new FreeformQuery( "SELECT * FROM new_table", connectionPool, "ID");
        try {
            Connection conn = connectionPool.reserveConnection();
            Statement statement = conn.createStatement();

            String query_str = "INSERT INTO "
                    + "Items(Name, ShortName, Code, Service, VAT, Category, Manufacturer, Count, UnitsType) "
                    + String.format("VALUES('%s', '%s','%s','%s','%s','%s','%s','%s','%s')",
                    row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7], row[8]);

            //String query_str = "INSERT INTO Items(Name, ShortName, Code, Service, VAT, Category, Manufacturer, Count, UnitsType) VALUES('Пшеница', 'Пшеница','5','0','16','3','Россия','1023','5')";

            System.out.println(query_str);

            statement.execute(query_str);

            conn.commit();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Vector<String[]>> getItemsData(){
        Map<Integer, Vector<String[]>> Data = new HashMap<>();

        SQLContainer items = getContainer("Items");
        SQLContainer categories = getContainer("Category");

        if(items == null || categories == null){
            return null;
        }

        Map<Integer, String> CategoriesMap = new HashMap<>();

        Collection<?> categoryIDS = categories.getItemIds();
        for (Object categoryID : categoryIDS){
            Property property = categories.getContainerProperty(categoryID, "Name");
            String name = property.getValue().toString();

            property = categories.getContainerProperty(categoryID, "id");
            int id = Integer.parseInt(property.getValue().toString());

            CategoriesMap.put(id, name);
        }


        Collection<?> itemIDS = items.getItemIds();
        for (Object itemID : itemIDS){
            Property property = items.getContainerProperty(itemID, "Name");
            String name = property.getValue().toString();

            property = items.getContainerProperty(itemID, "Code");
            String code = property.getValue().toString();

            property = items.getContainerProperty(itemID, "Category");
            int category = Integer.parseInt(property.getValue().toString());


            Vector<String[]> subItems = Data.get(category);

            if(subItems == null){
                subItems = new Vector<>();
                Data.put(category, subItems);
            }

            subItems.add(new String[]{name, code, CategoriesMap.get(category)});

            //Data.add(new String[]{name, code, CategoriesMap.get(category)});
        }

        return Data;
    }

    public static Map<String, Integer> getUnits(){
        Map<String,Integer> Units = new HashMap<>();
        SQLContainer unitsTable = getContainer("Units");

        if(unitsTable == null){
            return null;
        }

        Collection<?> unitsIDS = unitsTable.getItemIds();
        for (Object unitID : unitsIDS) {
            Property property = unitsTable.getContainerProperty(unitID, "id");
            int id = Integer.parseInt(property.getValue().toString());

            property = unitsTable.getContainerProperty(unitID, "Name");
            String name = property.getValue().toString();

            Units.put(name, id);
        }

        return Units;
    }

    public static Map<String, Integer> getCategories(){
        Map<String,Integer> CategoriesMap = new HashMap<>();
        SQLContainer categories = getContainer("Category");

        Collection<?> categoryIDS = categories.getItemIds();
        for (Object categoryID : categoryIDS){
            Property property = categories.getContainerProperty(categoryID, "Name");
            String name = property.getValue().toString();

            property = categories.getContainerProperty(categoryID, "id");
            int id = Integer.parseInt(property.getValue().toString());

            CategoriesMap.put(name, id);
        }

        return CategoriesMap;
    }

    private static SQLContainer getContainer(String table){
        TableQuery query = null;
        SQLContainer sql_container = null;

        try {
            query = new TableQuery(table, connectionPool);
            sql_container = new SQLContainer(query);

            return  sql_container;
        }
        catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }
}
