package db.migrations.base;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V1__CreateStudents extends BaseJavaMigration {

    private static final String CREATE_STUDENTS_QUERY=
            "CREATE TABLE Students (\n" +
                    "id int not null, \n" +
                    "    uuid binary(16),\n" +
                    "    lastName varchar(255),\n" +
                    "    firstName varchar(255),\n" +
                    "    address varchar(255),\n" +
                    "    city varchar(255), \n" +
                    " primary key(id));";

    @Override
    public void migrate(Context context) throws Exception {
        try (Statement createStudent = context.getConnection().createStatement()) {

            createStudent.executeUpdate(CREATE_STUDENTS_QUERY);
        }
    }
}
