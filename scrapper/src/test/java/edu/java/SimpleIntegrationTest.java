package edu.java;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SimpleIntegrationTest extends IntegrationTest {
    private static DataSource dataSource;

    private final static String INSERT_SQL_QUERY = "INSERT INTO tg_chats(id) VALUES(12345),(34567),(56789);";

    private final static String SELECT_SQL_QUERY = "SELECT * FROM tg_chats;";

    private final static String CREATE_SQL_QUERY = """
        CREATE TABLE IF NOT EXISTS users(
            id BIGINT PRIMARY KEY
        );
        """;
    private final static String DELETE_SQL_QUERY = "DELETE FROM tg_chats";
    private final static String DROP_SQL_QUERY = "DROP TABLE users";

    @BeforeAll
    public static void getDataSource() {
        dataSource = DataSourceBuilder.create()
            .url(POSTGRES.getJdbcUrl())
            .username(POSTGRES.getUsername())
            .password(POSTGRES.getPassword())
            .build();
    }

    @Test
    @SneakyThrows
    public void databaseShouldWorkProperlyWithBasicDatabaseOperations() {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate(INSERT_SQL_QUERY);
        ResultSet resultSet = statement.executeQuery(SELECT_SQL_QUERY);

        String expected = "12345 34567 56789";
        StringBuilder result = new StringBuilder();
        while (resultSet.next()) {
            result.append(resultSet.getString("id") + (resultSet.isLast() ? "" : " "));
        }

        Assertions.assertThat(result.toString()).isEqualTo(expected);

        statement.executeUpdate(DELETE_SQL_QUERY);
    }

    @Test
    @SneakyThrows
    public void databaseShouldWorkProperlyWithSchemaChanges() {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate(CREATE_SQL_QUERY);

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tablesResultSet = metaData.getTables(null, null, "users",
            new String[] {"TABLE"}
        );

        assertThat(tablesResultSet.next()).isTrue();

        statement.executeUpdate(DROP_SQL_QUERY);
    }
}
