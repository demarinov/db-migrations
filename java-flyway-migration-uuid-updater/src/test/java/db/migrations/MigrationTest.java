package db.migrations;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MigrationTest {

    private static final String URL = "jdbc:h2:file:./target/mydb";
    private static final String USER = "myUsr";
    private static final String PASSWORD = "mySecretPwd";
    private static final String LOCATIONS_BASE = "db/migrations/base";
    private static final String LOCATIONS_ALL = "db/migrations";
    private static final String SELECT_QUERY = "select id, uuid, firstName, lastName from students";

    private static Flyway flyway;

    @BeforeEach
    void init() {
        flyway = Flyway.configure()
                .dataSource(URL, USER, PASSWORD)
                .cleanDisabled(false)
                .locations(LOCATIONS_BASE)
                .load();
    }

    @Test
    void testMigrationsBase() throws SQLException {

        flyway.clean();

        MigrationInfoService migrationInfoService = flyway.info();
        assertEquals("Pending", migrationInfoService.getInfoResult().migrations.get(0).state);

        flyway.migrate();

        migrationInfoService = flyway.info();
        assertEquals("Success", migrationInfoService.getInfoResult().migrations.get(0).state);

        // verify data in db
        Connection conn = flyway
                .getConfiguration().getDataSource().getConnection();

        Statement statement = conn.createStatement();

        ResultSet rows = statement.executeQuery(SELECT_QUERY);

        // check first record
        rows.next();
        assertEquals("Bobby", rows.getString("firstName"));
        assertEquals(1, rows.getInt("id"));
        byte[] expectedBytes = new byte[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        byte[] actualBytes = rows.getBytes("uuid");
        assertTrue(equalByteArrays(expectedBytes, actualBytes));

        // check 2nd record
        rows.next();
        assertEquals("Jake", rows.getString("firstName"));
        assertEquals(2, rows.getInt("id"));
        expectedBytes = new byte[]{0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        actualBytes = rows.getBytes("uuid");
        assertTrue(equalByteArrays(expectedBytes, actualBytes));

    }

    @Test
    void testMigrationUpdate() throws SQLException {

        flyway = Flyway.configure()
                .dataSource(URL, USER, PASSWORD)
                .cleanDisabled(false)
                .locations(LOCATIONS_ALL)
                .load();

        flyway.clean();

        MigrationInfoService migrationInfoService = flyway.info();
        assertEquals("Pending", migrationInfoService.getInfoResult().migrations.get(2).state);

        flyway.migrate();

        migrationInfoService = flyway.info();
        assertEquals("Success", migrationInfoService.getInfoResult().migrations.get(2).state);

        // verify data in db
        Connection conn = flyway
                .getConfiguration().getDataSource().getConnection();

        Statement statement = conn.createStatement();

        ResultSet rows = statement.executeQuery(SELECT_QUERY);

        // verify 1st row uuid change
        rows.next();
        assertEquals("Bobby", rows.getString("firstName"));
        assertEquals(1, rows.getInt("id"));
        byte[] expectedBytes = new byte[]{-87, -85, -84, 83, 15, -58, 61, -52, -88, -17, 98, -124, -26, 5, 27, 83};
        byte[] actualBytes = rows.getBytes("uuid");
        assertTrue(equalByteArrays(expectedBytes, actualBytes));

        // verify 2nd row uuid change
        rows.next();
        expectedBytes = new byte[]{-115, -126, -80, -17, -21, -117, 54, -84, -86, 29, -15, -75, -110, -117, 115, -118};
        actualBytes = rows.getBytes("uuid");
        assertTrue(equalByteArrays(expectedBytes, actualBytes));
    }

    private boolean equalByteArrays(byte[] arrayOne, byte[] arrayTwo) {

        if (arrayOne.length != arrayTwo.length) {
            return false;
        }

        for (int i = 0; i < arrayOne.length; i++) {
            if (arrayOne[i] != arrayTwo[i]) {
                return false;
            }
        }

        return true;
    }
}
