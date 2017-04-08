import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(5050)) {

            while (true) {
                final Socket accept = serverSocket.accept();

                System.out.println(accept.getInetAddress());
                Thread thread = new Thread(() -> {
                    String url = "jdbc:oracle:thin:@localhost:1521:orcl";

                    try (Connection connection = DriverManager.getConnection(
                            url, "scott", "tiger")) {

                        try (Statement statement = connection.createStatement()) {

                            try (Scanner input = new Scanner(accept.getInputStream());
                                 ObjectOutputStream objectOutputStream =
                                         new ObjectOutputStream(new BufferedOutputStream(accept.getOutputStream()))) {

                                objectOutputStream.flush();


                                while (input.hasNextLine()) {

                                    String query = input.nextLine();
                                    String[] selector = query.split("\\s");
                                    if (selector[0].equalsIgnoreCase("select")) {

                                        int columnsCount;
                                        int rowsCount;
                                        ArrayList<String> columnNames = new ArrayList<String>();
                                        ArrayList<ArrayList<String>> tableContent = new ArrayList<ArrayList<String>>();

                                        try (ResultSet resultSet = statement.executeQuery(query)) {

                                            ResultSetMetaData rsmd = statement.getResultSet().getMetaData();
                                            columnsCount = rsmd.getColumnCount();

                                            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                                                columnNames.add(rsmd.getColumnName(i));
                                            }

                                            while (resultSet.next()) {
                                                ArrayList<String> listOfList = new ArrayList<String>();
                                                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                                                    listOfList.add(resultSet.getString(i));
                                                }
                                                tableContent.add(listOfList);
                                            }

                                            rowsCount = tableContent.size();

                                        }


                                        objectOutputStream.writeObject(new TableSqlResult(columnsCount, rowsCount, columnNames, tableContent));
                                        objectOutputStream.flush();

                                    } else {
                                        if (selector[0].equalsIgnoreCase("insert") ||
                                                selector[0].equalsIgnoreCase("update") ||
                                                selector[0].equalsIgnoreCase("delete")) {

                                            statement.executeUpdate(query);
                                            int rowsAffected;
                                            rowsAffected = statement.getUpdateCount();

                                            objectOutputStream.writeObject(new UpdateSqlResult(rowsAffected));
                                            objectOutputStream.flush();
                                        } else {

                                            boolean result;

                                            try {
                                                statement.executeUpdate(query);
                                                result = true;
                                            } catch (SQLException e) {
                                                result = false;
                                            }

                                            objectOutputStream.writeObject(new ExecuteSqlResult(result));
                                            objectOutputStream.flush();
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                });
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    //DML method

    public static void setRowsAffected(Statement statement, String query) {
        try {
            statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //DDL method

    public static void setResult(Statement statement, String query) {

    }

}