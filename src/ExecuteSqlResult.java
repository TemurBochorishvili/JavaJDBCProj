
public class ExecuteSqlResult implements SqlResult {

    private boolean result;

    public ExecuteSqlResult(boolean result) {
        this.result = result;
    }

    public boolean getResult() {
        return result;
    }
}
