package db.migrations.update;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import static utils.UuidConverter.convertUuid;
import static utils.UuidConverter.createUuid;

public class V3__UpdateStudentUuid extends BaseJavaMigration {

    private static final String UPDATE_STUDENT_UUID = "update students set uuid=? where id=?";

    @Override
    public void migrate(Context context) throws Exception {
        try (Statement select = context.getConnection().createStatement()) {
            try (ResultSet rows = select.executeQuery("SELECT id, firstName, LastName, city FROM students ORDER BY id")) {
                while (rows.next()) {


                    String firstName = rows.getString(2);
                    String lastName = rows.getString(3);
                    String city = rows.getString(4);

                    int id = rows.getInt(1);
                    byte[] bytes = new byte[16];
                    UUID uuid = createUuid(String.format("%d%s%s%s",id , firstName, lastName, city));
                    convertUuid(uuid, bytes);

                    try (PreparedStatement update = context.getConnection().prepareStatement(UPDATE_STUDENT_UUID)) {

                        update.setBytes(1, bytes);
                        update.setInt(2, id);
                        update.executeUpdate();
                    }
                }
            }
        }
    }
}
