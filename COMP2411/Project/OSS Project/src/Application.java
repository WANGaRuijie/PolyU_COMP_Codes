import model.OSS;

import java.sql.SQLException;

public class Application {

    public static void main(String[] args) throws SQLException {
        OSS oss = new OSS();
        oss.activate();
    }

}
