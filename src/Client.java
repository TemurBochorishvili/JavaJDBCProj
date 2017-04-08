import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", 5050)) {

            try(Scanner query = new Scanner(System.in);
                ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true)
                ){

                while (true) {
                    String command = query.nextLine();
                    printWriter.println(command);


                    SqlResult sqlResult = (SqlResult)objectInputStream.readObject();

                    if(sqlResult instanceof TableSqlResult){

                        TableSqlResult tableSqlResult = (TableSqlResult) sqlResult;

                        System.out.printf("Total rows: %d%n", tableSqlResult.getRowsCount());

                        System.out.println("[");

                        ArrayList<ArrayList<String>> content = tableSqlResult.getContent();

                        for (int i = 0; i < tableSqlResult.getRowsCount(); i++) {
                            ArrayList<String> rows = content.get(i);

                            System.out.println("\t{");

                            ArrayList<String> columnNames = tableSqlResult.getColumnNames();

                            for (int j = 0; j < tableSqlResult.getColumnsCount(); j++) {
                                System.out.printf("\t\t\"%s\" : \"%s\"%s%n",
                                        columnNames.get(j),
                                        rows.get(j),
                                        j == tableSqlResult.getColumnsCount() - 1 ? "" : ",");
                            }

                            System.out.printf("\t}%s%n", i == tableSqlResult.getRowsCount() - 1 ? "" : ",");
                        }

                        System.out.println("]");

                    }else if(sqlResult instanceof UpdateSqlResult){

                        System.out.println(((UpdateSqlResult) sqlResult).getRowsAffected());

                    }else if(sqlResult instanceof ExecuteSqlResult){

                        System.out.println(((ExecuteSqlResult) sqlResult).getResult());
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
