
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Storage {
    private Connection conn;

    public Storage(){
        try{
            String url = "jdbc:sqlite:db.sqlite";
            conn = DriverManager.getConnection(url);

            PreparedStatement prepared = conn.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS log (\
                    "date" TEXT NOT NULL,
                    "from" TEXT NOT NULL,
                    "to" TEXT NOT NULL,
                    "amount" NUMERIC NOT NULL DEFAULT 0,
                    "converted" NUMERIC NOT NULL DEFAULT 0
                    )""");
            prepared.execute();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertLog(String from, String to, double amount, double converted){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            PreparedStatement prepared = conn.prepareStatement("INSERT INTO log VALUES (?,?,?,?,?)");
            prepared.setString(1, dateFormat.format(new Date()));
            prepared.setString(2, from);
            prepared.setString(3, to);
            prepared.setDouble(4, amount);
            prepared.setDouble(5, converted);
            prepared.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertFromConvertOptions(ConvertOptions opt){
        insertLog(opt.getFrom(), opt.getTo(), opt.getAmount(), opt.getConverted());
    }

    public ResultSet getLog(){
        try {
            String query = "SELECT * FROM log ORDER BY date";
            PreparedStatement prepared = conn.prepareStatement(query);
            return prepared.executeQuery();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

}
