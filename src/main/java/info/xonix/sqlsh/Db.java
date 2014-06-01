package info.xonix.sqlsh;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: xonix
 * Date: 5/25/14
 * Time: 6:24 PM
 */
public class Db {
    public static boolean exists(Connection connection, String query, Object... params) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            for (int i = 1; i <= params.length; i++) {
                Object param = params[i - 1];
                statement.setObject(i, param);
            }
            ResultSet resultSet = statement.executeQuery();
            return resultSet.first();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> listStrings(Connection connection, String query, Object... params) {
        return (List<String>) (List) list1(connection, query, params);
    }

    public static List<Object> list1(Connection connection, String query, Object... params) {
        List<List<Object>> listRes = list(connection, query, params);
        List<Object> res = new ArrayList<>();
        for (List<Object> row : listRes) {
            res.add(row.get(0));
        }
        return res;
    }

    public static List<List<Object>> list(Connection connection, String query, Object... params) {
        List<List<Object>> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            for (int i = 1; i <= params.length; i++) {
                Object param = params[i - 1];
                statement.setObject(i, param);
            }

            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                ArrayList<Object> row = new ArrayList<>();

                for (int j = 1; j <= columnCount; j++) {
                    row.add(resultSet.getObject(j));
                }

                result.add(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static Object single(Connection connection, String query, Object... params) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 1; i <= params.length; i++) {
                Object param = params[i - 1];
                statement.setObject(i, param);
            }

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getObject(1);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
