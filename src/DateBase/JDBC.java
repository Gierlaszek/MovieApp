package DateBase;

import Window.Library_win;
import java.sql.*;
import Window.More_info_win;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
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
    //map will be used to holds parametrs of searched movie from site OMBDAPI.com
    private Map<String, String> map_JSON = new HashMap<>();
    //map will be used to holds parametrs of table from MySQL 
    private HashMap<String, ArrayList<String>> map_database = new HashMap<String, ArrayList<String>>();
    //List with all params Title, Year etc. 
    private List<String> key = new ArrayList<String>();

    //r_s_e_i - user determines what want to do r- read   s - save  e - export to XML   i - import XML 
    //parametric constructor with Map where be parameters of searched movie
    public JDBC(Map<String, String> m, String r_s_e_i) throws SQLException, inCorrect, ParserConfigurationException, TransformerException, SAXException, IOException
    {
        map_JSON.put("Title", m.get("Title"));
        map_JSON.put("Year", m.get("Year"));
        map_JSON.put("Genre", m.get("Genre"));
        map_JSON.put("RunTime", m.get("RunTime"));
        map_JSON.put("Director", m.get("Director"));
        map_JSON.put("Country", m.get("Country"));
        map_JSON.put("Rate", m.get("Rate"));
        
        connect(r_s_e_i);
        
    }
    public JDBC(String r_s_e_i) throws SQLException, inCorrect, ParserConfigurationException, TransformerException, SAXException, IOException
    {
        connect(r_s_e_i);
    }
    
    //method that will always be used
    private void connect(String r_s_e_i) throws SQLException, inCorrect, ParserConfigurationException, TransformerException, SAXException, IOException
    {
        try
        {
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "admin" , "admin");
            myStmt = myConn.createStatement(); 
            if(r_s_e_i.equals("save"))
            {
                save();
            }
            else if(r_s_e_i.equals("read"))
            {
                read();
            }
            else if(r_s_e_i.equals("export"))
            {
                //save document XML 
                Document doc = toDocument(myConn);
                DOMSource domSource = new DOMSource(doc);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                
                //in this place user have to give file path to folder MovieApp and name for this XML
                String xmlPath = Library_win.get_xmlFile();
                StreamResult sr = new StreamResult(new File("" + xmlPath));
                transformer.transform(domSource, sr);
            }
            else if(r_s_e_i.equals("import"))
            {
                String XMLpath = Library_win.get_xmlFile();
                XMLtoTable(XMLpath, myConn);
            }
        }      
        catch(SQLException | ParserConfigurationException | TransformerException exc)
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
    
    //method when user want to save record to MySQL
    private void save() throws SQLException, inCorrect
    {        
        myRs = myStmt.executeQuery("select Title from Movie_library order by Title");

        //check if the same record exist already
        while (myRs.next())
        {
            if(map_JSON.get("Title").equals(myRs.getString("Title")))
            {

                correct = false;
                throw new inCorrect("This movie already exists in database.");
            }
        }

        //save record to table in MySQL
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
    
    //method when user want to read table from MySQL
    //
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
    
    //method to export table to document XML
    public static Document toDocument(Connection con) throws ParserConfigurationException, SQLException
    {
        //new Document 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        
        //Descripiton table - table name, count of column
        Element results = doc.createElement("Table");
        doc.appendChild(results);

        //download data from MySQL
        PreparedStatement prestmt = con.prepareStatement("select * from Movie_library");
        ResultSet rs = prestmt.executeQuery();

        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();

        Element tableName = doc.createElement("TableName");
        tableName.appendChild(doc.createTextNode(rsmd.getTableName(1)));
        results.appendChild(tableName);

        Element structure = doc.createElement("TableStructure");
        results.appendChild(structure);
        
        //description column - type - int, char etc.
        Element col = null;
        for (int i = 1; i <= colCount; i++)
        {
            col = doc.createElement("Column" + i);
            results.appendChild(col);
            
            Element columnNode = doc.createElement("ColumnName");
            columnNode.appendChild(doc.createTextNode(rsmd.getColumnName(i)));
            col.appendChild(columnNode);

            Element typeNode = doc.createElement("ColumnType");
            typeNode.appendChild(doc.createTextNode(String.valueOf((rsmd.getColumnTypeName(i)))));
            col.appendChild(typeNode);

            Element lengthNode = doc.createElement("Length");
            lengthNode.appendChild(doc.createTextNode(String.valueOf((rsmd.getPrecision(i)))));
            col.appendChild(lengthNode);

            structure.appendChild(col);
        }

        //create place which contains movie data 
        Element productList = doc.createElement("TableData");
        results.appendChild(productList);
        
        int l = 0;

        while (rs.next())
        {
            Element row = doc.createElement("Product" + (++l));
            results.appendChild(row);
            for (int i = 1; i <= colCount; i++)
            {
                    String columnName = rsmd.getColumnName(i);
                    Object value = rs.getObject(i);
                    Element node = doc.createElement(columnName);
                    node.appendChild(doc.createTextNode((value != null) ? value.toString() : ""));
                    row.appendChild(node);
            }
            productList.appendChild(row);
        }
        return doc;
    }
    
    public static void XMLtoTable(String xmlPath, Connection con) throws SQLException, ParserConfigurationException, SAXException, IOException
    {
        //create Document from path to XML file
        DocumentBuilderFactory bdf = DocumentBuilderFactory.newInstance();
        DocumentBuilder bd = bdf.newDocumentBuilder();
        Document doc = bd.parse(new File(xmlPath));

        //create ddl and dml 
        //ddl used to create table 
        //dml used to insert, update table
        StringBuffer ddl = new StringBuffer("create table "+ doc.getElementsByTagName("TableName").item(0).getTextContent() + "1 (");
        StringBuffer dml = new StringBuffer("insert into  "+ doc.getElementsByTagName("TableName").item(0).getTextContent() + "1 (");
        
        //element from document XML
        NodeList tableStructure = doc.getElementsByTagName("TableStructure");
        int col_count= tableStructure.item(0).getChildNodes().getLength();

        //read from XML info about table
        for (int i = 0; i < col_count; i++)
        {
            ddl.append(doc.getElementsByTagName("ColumnName").item(i).getTextContent()+ " "+ 
                    doc.getElementsByTagName("ColumnType").item(i).getTextContent()+ "("
                    + doc.getElementsByTagName("Length").item(i).getTextContent() + "),");
            
            dml.append(doc.getElementsByTagName("ColumnName").item(i).getTextContent()+ ",");

        }

        ddl = ddl.replace(ddl.length() - 1, ddl.length(), ")");
        dml = dml.replace(dml.length() - 1, dml.length(), ") values(");

        for (int k = 0; k < col_count; k++)
        {
            dml.append("?,");
        }

        dml = dml.replace(dml.length() - 1, dml.length(), ")");

        //create table in MySQL 
        Statement stmt = null;
        try
        {
            stmt = con.createStatement();
            stmt.executeUpdate(ddl.toString());
        } 
        catch (Exception e)
        {
            System.out.println("Tables already created, skipping table creation process"+ e.toString());
        }

        NodeList tableData = doc.getElementsByTagName("TableData");
        int table_len = tableData.item(0).getChildNodes().getLength();

        PreparedStatement prepStmt = con.prepareStatement(dml.toString());

        String colName = "";
        for (int i = 0; i < table_len; i++)
        {
            for (int j = 0; j < tableStructure.item(0).getChildNodes().getLength(); j++)
            {
                colName = doc.getElementsByTagName("ColumnName").item(j).getTextContent();
                prepStmt.setString(j + 1, doc.getElementsByTagName(colName).item(i).getTextContent());

            }
            prepStmt.addBatch();
        }
    }
    
    //initialize getters
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
    

