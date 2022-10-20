package db.migrations.base;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V2__InsertStudents extends BaseJavaMigration {

    private static final String INSERT_STUDENT_ONE="insert into students (id,uuid, lastName, firstName, address, city) " +
            "values (1, 1,'Brown', 'Bobby', 'Lake City', 'Boston')";

    private static final String INSERT_STUDENT_TWO="insert into students (id,uuid, lastName, firstName, address, city) " +
            "values (2, 2,'Brown', 'Jake', 'Lake City', 'Boston')";

    private static final String INSERT_STUDENT_THREE="insert into students (id,uuid, lastName, firstName, address, city) " +
            "values (3, 3,'Brown', 'Maria', 'Down Town', 'New York')";

    @Override
    public void migrate(Context context) throws Exception {

        try(Statement insert = context.getConnection().createStatement()) {

            insert.addBatch(INSERT_STUDENT_ONE);
            insert.addBatch(INSERT_STUDENT_TWO);
            insert.addBatch(INSERT_STUDENT_THREE);
            insert.executeBatch();
        }

    }
}
