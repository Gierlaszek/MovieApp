package DateBase;

import java.sql.*;
import Window.More_info_win;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
/**
 *
 * @author kamil
 */
public class JDBC
{
    private boolean correct = true;
    private Connection myConn = null;
    private Statement myStmt = null;
    private ResultSet myRs = null;

    private int amount = 0;
    private Map<String, String> map_JSON = new HashMap<>();
    private HashMap<String, ArrayList<String>> map_database = new HashMap<String, ArrayList<String>>();
    private List<String> key = new ArrayList<String>();

    
    public JDBC(Map<String, String> m, String ReadOrSave) throws SQLException, inCorrect
    {
        map_JSON.put("Title", m.get("Title"));
        map_JSON.put("Year", m.get("Year"));
        map_JSON.put("Genre", m.get("Genre"));
        map_JSON.put("RunTime", m.get("RunTime"));
        map_JSON.put("Director", m.get("Director"));
        map_JSON.put("Country", m.get("Country"));
        map_JSON.put("Rate", m.get("Rate"));
        
        connect(ReadOrSave);
        
    }
    public JDBC(String ReadOrSave) throws SQLException, inCorrect
    {
        connect(ReadOrSave);
    }
    
    private void connect(String ReadOrSave) throws SQLException, inCorrect
    {
        try
        {
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "admin" , "admin");
            myStmt = myConn.createStatement(); 
            if(ReadOrSave.equals("save"))
            {
                save();
            }
            else if(ReadOrSave.equals("read"))
            {
                read();
            }
        }      
        catch(SQLException exc)
        {
            exc.printStackTrace();
        }
        finally
        {
            if (myRs != null) {
                myRs.close();
            }

            if (myStmt != null) {
                myStmt.close();
            }

            if (myConn != null) {
                myConn.close();
            }
        }
        
    }
    
    private void save() throws SQLException, inCorrect
    {        
        myRs = myStmt.executeQuery("select Title from Movie_library order by Title");

            
        while (myRs.next())
        {
            if(map_JSON.get("Title").equals(myRs.getString("Title")))
            {

                correct = false;
                throw new inCorrect("This movie already exists in database.");
            }
        }

        if(correct == true)
        {
            int rowsAffectes = myStmt.executeUpdate(
                    "insert into Movie_library" +
                    "(Title, Year, Genre, RunTime, Director, Country, Rate)" +
                    "values" +
                    "('" + map_JSON.get("Title") + "','"  + map_JSON.get("Year") + "','" +
                    map_JSON.get("Genre") + "','" + map_JSON.get("RunTime") + "','" +
                    map_JSON.get("Director") + "','" + map_JSON.get("Country") + "','" +
                    map_JSON.get("Rate") + "')"
            );
        }
 
    }
    
    private void read() throws SQLException
    {
        myRs = myStmt.executeQuery("select * from Movie_library");
        key.add("Title");
        key.add("Year");
        key.add("Genre");
        key.add("RunTime");
        key.add("Director");
        key.add("Country");
        key.add("Rate");

        for(int i = 0; i < key.size(); i++)
        {
            map_database.put(key.get(i), new ArrayList<String>());
            
            while(myRs.next())
            {
                map_database.get(key.get(i)).add(myRs.getString(key.get(i)));
                amount++;
            }
            myRs.beforeFirst();
        }
        
    }  
    public int amount_rows()
    {
        return amount/key.size();
    }
    
    public List getKey()
    {
        return key;
    }
    
    
    public Map getMap()
    {
        return map_database;
    }
}
    

