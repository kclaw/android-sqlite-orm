package alt.sqlite2.Interface;

/**
 * Created by root on 15年3月27日.
 */
public interface ISQLiteCommand {
    void executeNonQuery();
    void setCommand(String command);
    String getCommand();
}
