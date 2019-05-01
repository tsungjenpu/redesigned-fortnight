/**
 * Copyright @ 2000 Peter Rossbach (pr@webapp.de) und Lars Roewekamp (Lars@openKnowlege.de)
 *
 * Source is only for non commercial and coaching usage.
 *
 * Not Warranty to use it.
 */
package de.ix.jspTutorial.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Liste der Pizza fuer den Pizza Services Die Pizza werden aus einer Properties
 * Datei einmalig in den ServletContext geladen.
 *
 * @author Peter Rossbach (<a href="mailto://pr@webapp.de">pr@webapp.de</a>),
 * Lars Roewekamp (
 * <a href="mailto://lars.roewekamp@openKnowledge.de">lars.roewekamp@openKnowledge.de</a>)
 * @version $Id:$
 */
public class PizzaList
{
    public static final String HOST = "localhost";
    public static final int PORT = 5432;
    public static final String DATABASENAME = "postgres";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "kuraki06";
    public static final String TABLENAME = "pizzen";
    
    private final TreeMap<Long, Pizza> pizzas;

    /**
     * Leere Liste von Pizzen
     *
     *
     * @see
     */
    public PizzaList()
    {
        this.pizzas = new TreeMap<>();
    }

    /**
     * Initialisiern mit Testpizzen
     *
     *
     * @param pizzas Testpizzen
     *
     * @see
     */
    public PizzaList(TreeMap<Long, Pizza> pizzas)
    {
        this.pizzas = pizzas;
    }

    /**
     * Hole bestimmte Pizza
     *
     *
     * @param key Bestellnummer der Pizza
     *
     * @return Schluessel der Pizza
     *
     * @see
     */
    public Pizza getPizza(Long key)
    {
        return (Pizza) pizzas.get(key);
    }

    /**
     * Hole alle Pizzen
     *
     *
     * @return
     *
     * @see
     */
    public TreeMap getPizzas()
    {
        return pizzas;
    }

    /**
     * This function needs to be edited readListFromDB().
     */
    public void readListFromDB()
    {
        try
        {
            // Load postgreSQL driver
            Class.forName("org.postgresql.Driver");

            String connectionUrl = "jdbc:postgresql://" + PizzaList.HOST + ":" + PizzaList.PORT + "/" + PizzaList.DATABASENAME;

            // 1. Establish connection
            Connection connection = DriverManager.getConnection(connectionUrl, PizzaList.USERNAME, PizzaList.PASSWORD);

            // 2. Create statement
            Statement statement = connection.createStatement();

            // 3. Execute statement
            ResultSet result = statement.executeQuery("SELECT * FROM " + PizzaList.TABLENAME);

            // Read and print results
            while (result.next())
            {
//                for (int i = 1; i <= result.getMetaData().getColumnCount(); i++)
                pizzas.put(result.getLong("pizzaId"),
                        new Pizza(result.getLong("pizzaId"), result.getString("pizzaName"), result.getString("pizzaSize"), result.getDouble("basePrice")));
//                    System.out.println(result.getMetaData().getColumnLabel(i) + ": " + result.getString(i) + ";");
//                System.out.println();
            }

            // 4. Free ressources 
            result.close();
            statement.close();
            connection.close();
        }
        catch (ClassNotFoundException e)
        {
            // Handle exception for not found database driver
            System.out.println("JDBC-Driver postgres driver not found (Please add it to libraries)");
        }
        catch (SQLException e)
        {
            // Catch SQL Errors
            System.out.println("Error while talking to database");
            System.out.println(e.getMessage());
        }
    }

    /**
     * zeige die Liste der Pizzen
     *
     *
     * @return Liste der Pizzen
     *
     * @see
     */
    @Override
    public String toString()
    {
        StringBuilder outStr = new StringBuilder();
        Set entrySet = pizzas.entrySet();

        outStr.append("[");
        for (Iterator iter = entrySet.iterator(); iter.hasNext();)
        {
            Map.Entry entry = (Map.Entry) iter.next();

            outStr.append(((Pizza) entry.getValue()).toString());
            if (iter.hasNext())
                outStr.append(",\n");
        }
        outStr.append("]");
        return outStr.toString();
    }

}
