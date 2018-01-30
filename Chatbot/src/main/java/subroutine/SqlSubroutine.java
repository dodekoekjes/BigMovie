//--------------------------------------------------------------------------------------//
// Deze pagina is gemaakt door Benjamin                                                 //
//--------------------------------------------------------------------------------------//
package subroutine;

import com.rivescript.macro.Subroutine;
import com.rivescript.util.StringUtils;

import java.sql.*;

public class SqlSubroutine implements Subroutine {
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    public SqlSubroutine(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    @Override
    public String call(com.rivescript.RiveScript rs, String[] args) {
        String result = "";
        String sql = StringUtils.join(args, " ");

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection= DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + Integer.toString(port) + "/" + database + "?autoReconnect=true&useSSL=false",
                    username, password);
            statement= connection.createStatement();
            System.out.println(sql);
            resultSet=statement.executeQuery(sql);
            while(resultSet.next()) {
                int i = resultSet.getMetaData().getColumnCount();
                for (int j = 1; j <= i; j++) {
                    if (result.equals("")) {
                        result = resultSet.getString(j);
                    } else {
                        result += "" + resultSet.getString(j);
                    }
                }
                if (!result.equals(""))
                    result += "";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally{
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(result);
        return result;
    }
}
