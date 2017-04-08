import java.util.ArrayList;


public class TableSqlResult implements SqlResult {


    private int columnsCount;
    private int rowsCount;
    private ArrayList<String> columnNames;
    private ArrayList<ArrayList<String>> tableContent;

    public TableSqlResult(int columnsCount, int rowsCount, ArrayList<String> columnNames,
                          ArrayList<ArrayList<String>> tableContent) {
        this.columnsCount = columnsCount;
        this.rowsCount = rowsCount;
        this.columnNames = columnNames;
        this.tableContent = tableContent;
    }

    public int getColumnsCount() {
        return columnsCount;
    }


    public int getRowsCount() {
        return rowsCount;
    }


    public ArrayList<String> getColumnNames() {
        return columnNames;
    }


    public ArrayList<ArrayList<String>> getContent() {
        return tableContent;
    }
}
