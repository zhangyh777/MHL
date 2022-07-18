import dao.BasicDAO;
import utils.JDBCUtilsByDruid;
import utils.Utility;

import java.sql.Connection;
import java.sql.SQLException;

public class UtilityTest {
    public static void main(String[] args) {
        try {
            Connection connection = JDBCUtilsByDruid.getConnect();
            System.out.println(connection.getClass());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
