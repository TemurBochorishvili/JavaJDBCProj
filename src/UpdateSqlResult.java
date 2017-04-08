
public class UpdateSqlResult implements SqlResult {

    private int rowsAffected;

    public UpdateSqlResult(int rowsAffected) {
        this.rowsAffected = rowsAffected;
    }

    public int getRowsAffected(){
        return rowsAffected;
    }
}
