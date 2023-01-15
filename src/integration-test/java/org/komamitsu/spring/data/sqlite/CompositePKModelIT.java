package org.komamitsu.spring.data.sqlite;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.komamitsu.spring.data.sqlite.domain.model.CompositePKModel;
import org.komamitsu.spring.data.sqlite.domain.repository.CompositePKModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SqliteJdbcConfiguration.class)
@EnableAutoConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompositePKModelIT {

  private static final String TABLE = "composite_pk_model";
  private static final String ACCOUNT_ID = "account_id";
  private static final String ACCOUNT_TYPE = "account_type";
  private static final String BALANCE = "balance";
  private static final String NAME = "name";
  private static final int INITIAL_BALANCE = 1000;
  private static final int NUM_ACCOUNTS = 4;
  private static final int NUM_TYPES = 4;

  private static final String JDBC_URL = "jdbc:sqlite:integration-test.db";

  @Autowired CompositePKModelRepository repository;

  @BeforeAll
  public void beforeAll() throws Exception {
    createTables();
  }

  private Connection getConnection() throws SQLException {
    return DriverManager.getConnection(JDBC_URL);
  }

  private void createTables() throws SQLException {
    try (Connection connection = getConnection();
        Statement statement = connection.createStatement()) {
      statement.execute(
          String.format(
              "CREATE TABLE IF NOT EXISTS %s (%s INT, %s INT, %s INT, %s TEXT, PRIMARY KEY (%s, %s))",
              TABLE, ACCOUNT_ID, ACCOUNT_TYPE, BALANCE, NAME, ACCOUNT_ID, ACCOUNT_TYPE));
      statement.execute(
          String.format(
              "CREATE INDEX IF NOT EXISTS index_%s_on_%s ON %s (%s)",
              TABLE.toLowerCase(), NAME.toLowerCase(), TABLE, NAME));
    }
  }

  @BeforeEach
  public void setUp() throws SQLException {
    truncateTables();
  }

  private void truncateTables() throws SQLException {
    try (Connection connection = getConnection();
        Statement statement = connection.createStatement()) {
      statement.execute(String.format("DELETE FROM %s", TABLE));
    }
  }

  @AfterAll
  public void afterAll() throws SQLException {
    dropTables();
  }

  private void dropTables() throws SQLException {
    try (Connection connection = getConnection();
        Statement statement = connection.createStatement()) {
      statement.execute(String.format("DROP TABLE %s", TABLE));
    }
  }

  private String getAccountName(int accountId, int accountType) {
    return String.format("account_%d_%d", accountId, accountType);
  }

  @Test
  public void findByPrimaryKey_ForExistingRecord_ShouldReturnIt() throws SQLException {
    populateRecords();

    Optional<CompositePKModel> testModel = repository.findFirstByAccountIdAndAccountType(0, 0);

    assertThat(testModel).isPresent();
    testModel.ifPresent(
        found -> {
          assertThat(found.accountId).isEqualTo(0);
          assertThat(found.accountType).isEqualTo(0);
          assertThat(found.balance).isEqualTo(INITIAL_BALANCE);
          assertThat(found.name).isEqualTo(getAccountName(0, 0));
        });
  }

  @Test
  public void findByPrimaryKeyAndRange_ForExistingRecord_ShouldReturnThem() throws SQLException {
    populateRecords();

    List<CompositePKModel> records = repository.findByAccountIdAndAccountTypeBetween(1, 0, 2);

    assertThat(records).size().isEqualTo(3);
    {
      CompositePKModel testModel = records.get(0);
      assertThat(testModel.accountId).isEqualTo(1);
      assertThat(testModel.accountType).isEqualTo(0);
      assertThat(testModel.balance).isEqualTo(INITIAL_BALANCE);
      assertThat(testModel.name).isEqualTo(getAccountName(1, 0));
    }
    {
      CompositePKModel testModel = records.get(1);
      assertThat(testModel.accountId).isEqualTo(1);
      assertThat(testModel.accountType).isEqualTo(1);
      assertThat(testModel.balance).isEqualTo(INITIAL_BALANCE);
      assertThat(testModel.name).isEqualTo(getAccountName(1, 1));
    }
    {
      CompositePKModel testModel = records.get(2);
      assertThat(testModel.accountId).isEqualTo(1);
      assertThat(testModel.accountType).isEqualTo(2);
      assertThat(testModel.balance).isEqualTo(INITIAL_BALANCE);
      assertThat(testModel.name).isEqualTo(getAccountName(1, 2));
    }
  }

  @Test
  public void findByPrimaryKeyAndRangeWithOrder_ForExistingRecords_ShouldReturnThem()
      throws SQLException {
    populateRecords();

    List<CompositePKModel> records =
        repository.findByAccountIdAndAccountTypeBetweenOrderByAccountTypeDesc(1, 0, 2);

    assertThat(records).size().isEqualTo(3);
    {
      CompositePKModel testModel = records.get(0);
      assertThat(testModel.accountId).isEqualTo(1);
      assertThat(testModel.accountType).isEqualTo(2);
      assertThat(testModel.balance).isEqualTo(INITIAL_BALANCE);
      assertThat(testModel.name).isEqualTo(getAccountName(1, 2));
    }
    {
      CompositePKModel testModel = records.get(1);
      assertThat(testModel.accountId).isEqualTo(1);
      assertThat(testModel.accountType).isEqualTo(1);
      assertThat(testModel.balance).isEqualTo(INITIAL_BALANCE);
      assertThat(testModel.name).isEqualTo(getAccountName(1, 1));
    }
    {
      CompositePKModel testModel = records.get(2);
      assertThat(testModel.accountId).isEqualTo(1);
      assertThat(testModel.accountType).isEqualTo(0);
      assertThat(testModel.balance).isEqualTo(INITIAL_BALANCE);
      assertThat(testModel.name).isEqualTo(getAccountName(1, 0));
    }
  }

  @Test
  public void findByPrimaryKeyAndRange_ForExistingRecords_ShouldReturnThem() throws SQLException {
    populateRecords();

    List<CompositePKModel> records = repository.findTop2ByAccountIdAndAccountTypeBetween(1, 0, 2);

    assertThat(records).size().isEqualTo(2);
    {
      CompositePKModel testModel = records.get(0);
      assertThat(testModel.accountId).isEqualTo(1);
      assertThat(testModel.accountType).isEqualTo(0);
      assertThat(testModel.balance).isEqualTo(INITIAL_BALANCE);
      assertThat(testModel.name).isEqualTo(getAccountName(1, 0));
    }
    {
      CompositePKModel testModel = records.get(1);
      assertThat(testModel.accountId).isEqualTo(1);
      assertThat(testModel.accountType).isEqualTo(1);
      assertThat(testModel.balance).isEqualTo(INITIAL_BALANCE);
      assertThat(testModel.name).isEqualTo(getAccountName(1, 1));
    }
  }

  @Test
  public void findByPrimaryKey_ForNonExistingRecord_ShouldReturnEmpty() throws SQLException {
    populateRecords();

    Optional<CompositePKModel> record = repository.findFirstByAccountIdAndAccountType(0, 4);

    assertThat(record).isEmpty();
  }

  @Test
  public void findByPrimaryKeyAndRange_ForNonExistingRecord_ShouldReturnEmpty()
      throws SQLException {
    populateRecords();

    List<CompositePKModel> records = repository.findByAccountIdAndAccountTypeBetween(0, 4, 6);

    assertThat(records).size().isEqualTo(0);
  }

  @Test
  public void findByIndex_ForExistingRecords_ShouldReturnThem() throws SQLException {
    populateRecords();

    List<CompositePKModel> records = repository.findByName(getAccountName(2, 1));

    assertThat(records).size().isEqualTo(1);

    CompositePKModel testModel = records.get(0);
    assertThat(testModel.accountId).isEqualTo(2);
    assertThat(testModel.accountType).isEqualTo(1);
    assertThat(testModel.balance).isEqualTo(INITIAL_BALANCE);
    assertThat(testModel.name).isEqualTo(getAccountName(2, 1));
  }

  @Test
  public void findAll_ForExistingRecords_ShouldReturnThem() throws SQLException {
    populateRecords();

    List<CompositePKModel> records = repository.findAllByOrderByAccountIdAscAccountTypeAsc();

    int index = 0;
    for (int i = 0; i < NUM_ACCOUNTS; i++) {
      for (int j = 0; j < NUM_TYPES; j++) {
        {
          CompositePKModel testModel = records.get(index);
          assertThat(testModel.accountId).isEqualTo(i);
          assertThat(testModel.accountType).isEqualTo(j);
          assertThat(testModel.balance).isEqualTo(INITIAL_BALANCE);
          assertThat(testModel.name).isEqualTo(getAccountName(i, j));
          index++;
        }
      }
    }
    assertThat(index).isEqualTo(NUM_ACCOUNTS * NUM_TYPES);
  }

  @Test
  public void count_ForExistingRecord_ShouldReturnTheCount() throws SQLException {
    populateRecords();

    long result = repository.count();

    assertThat(result).isEqualTo(NUM_ACCOUNTS * NUM_TYPES);
  }

  @Test
  public void existsById_ForExistingRecord_ShouldReturnTrue() throws SQLException {
    populateRecords();

    boolean result = repository.existsById(0);

    assertThat(result).isTrue();
  }

  @Test
  public void existsById_ForNonExistingRecord_ShouldReturnFalse() throws SQLException {
    populateRecords();

    boolean result = repository.existsById(NUM_ACCOUNTS);

    assertThat(result).isFalse();
  }

  private ResultSet getAccount(Connection conn, int accountId, int accountType)
      throws SQLException {
    PreparedStatement ps =
        conn.prepareStatement(
            String.format(
                "SELECT * FROM %s WHERE %s = ? AND %s = ?", TABLE, ACCOUNT_ID, ACCOUNT_TYPE));
    ps.setInt(1, accountId);
    ps.setInt(2, accountType);
    return ps.executeQuery();
  }

  @Test
  public void insert_ForNonExistingRecord_ShouldCreateIt() throws SQLException {
    int expected = INITIAL_BALANCE;

    CompositePKModel saved = repository.insert(new CompositePKModel(0, 0, expected, "account_0_0"));
    assertThat(saved.accountId).isEqualTo(0);
    assertThat(saved.accountType).isEqualTo(0);
    assertThat(saved.balance).isEqualTo(expected);
    assertThat(saved.name).isEqualTo(getAccountName(0, 0));

    try (Connection connection = getConnection()) {
      ResultSet resultSet = getAccount(connection, 0, 0);

      assertThat(resultSet.next()).isTrue();
      assertThat(resultSet.getInt(ACCOUNT_ID)).isEqualTo(0);
      assertThat(resultSet.getInt(ACCOUNT_TYPE)).isEqualTo(0);
      assertThat(resultSet.getInt(BALANCE)).isEqualTo(expected);
      assertThat(resultSet.getString(NAME)).isEqualTo(getAccountName(0, 0));
      assertThat(resultSet.next()).isFalse();
    }
  }

  @Test
  public void updateAfterRead_ForExistingRecord_ShouldUpdateThem() throws SQLException {
    populateRecords();

    int amount = 100;
    repository.incrementBalance(0, 0, amount);

    try (Connection connection = getConnection()) {
      ResultSet resultSet = getAccount(connection, 0, 0);

      assertThat(resultSet.next()).isTrue();
      assertThat(resultSet.getInt(ACCOUNT_ID)).isEqualTo(0);
      assertThat(resultSet.getInt(ACCOUNT_TYPE)).isEqualTo(0);
      assertThat(resultSet.getInt(BALANCE)).isEqualTo(INITIAL_BALANCE + amount);
      assertThat(resultSet.getString(NAME)).isEqualTo(getAccountName(0, 0));
      assertThat(resultSet.next()).isFalse();
    }
  }

  @Test
  public void insertWithNullValue_ForNonExistingRecord_ShouldCreateIt() throws SQLException {
    CompositePKModel saved = repository.insert(new CompositePKModel(0, 0, null, null));
    assertThat(saved.accountId).isEqualTo(0);
    assertThat(saved.accountType).isEqualTo(0);
    assertThat(saved.balance).isNull();
    assertThat(saved.name).isNull();

    try (Connection connection = getConnection()) {
      ResultSet resultSet = getAccount(connection, 0, 0);

      assertThat(resultSet.next()).isTrue();
      assertThat(resultSet.getInt(ACCOUNT_ID)).isEqualTo(0);
      assertThat(resultSet.getInt(ACCOUNT_TYPE)).isEqualTo(0);
      assertThat(resultSet.getInt(BALANCE)).isEqualTo(0);
      assertThat(resultSet.wasNull()).isTrue();
      assertThat(resultSet.getString(NAME)).isNull();
      assertThat(resultSet.next()).isFalse();
    }
  }

  @Test
  public void updatesAfterFindByPrimaryKey_ForExistingRecords_ShouldUpdateThem()
      throws SQLException {
    populateRecords();

    int amount = 100;
    int fromId = 0;
    int toId = 1;
    int accountType = 0;

    repository.transfer(fromId, accountType, toId, accountType, amount);

    try (Connection connection = getConnection()) {
      connection.setAutoCommit(false);

      {
        ResultSet resultSet = getAccount(connection, fromId, 0);
        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getInt(ACCOUNT_ID)).isEqualTo(fromId);
        assertThat(resultSet.getInt(ACCOUNT_TYPE)).isEqualTo(accountType);
        assertThat(resultSet.getInt(BALANCE)).isEqualTo(INITIAL_BALANCE - amount);
        assertThat(resultSet.getString(NAME)).isEqualTo(getAccountName(fromId, accountType));
        assertThat(resultSet.next()).isFalse();
      }

      {
        ResultSet resultSet = getAccount(connection, toId, 0);
        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getInt(ACCOUNT_ID)).isEqualTo(toId);
        assertThat(resultSet.getInt(ACCOUNT_TYPE)).isEqualTo(accountType);
        assertThat(resultSet.getInt(BALANCE)).isEqualTo(INITIAL_BALANCE + amount);
        assertThat(resultSet.getString(NAME)).isEqualTo(getAccountName(toId, accountType));
        assertThat(resultSet.next()).isFalse();
      }
    }
  }

  @Test
  public void deleteAfterRead_ForExistingRecord_ShouldDeleteIt() throws SQLException {
    populateRecords();

    repository.deleteByAccountIdAndAccountTypeAfterSelect(0, 0);

    try (Connection connection = getConnection()) {
      ResultSet resultSet = getAccount(connection, 0, 0);

      assertThat(resultSet.next()).isFalse();
    }
  }

  @Test
  public void deleteAll_ForExistingRecord_ShouldDeleteThem() throws SQLException {
    populateRecords();

    repository.deleteAll();

    assertThat(repository.count()).isEqualTo(0);
  }

  private void populateRecords() throws SQLException {
    try (Connection connection = getConnection()) {
      connection.setAutoCommit(false);

      for (int i = 0; i < NUM_ACCOUNTS; i++) {
        for (int j = 0; j < NUM_TYPES; j++) {
          PreparedStatement ps =
              connection.prepareStatement(
                  String.format("INSERT INTO %s VALUES(?, ?, ?, ?)", TABLE));
          ps.setInt(1, i);
          ps.setInt(2, j);
          ps.setInt(3, INITIAL_BALANCE);
          ps.setString(4, getAccountName(i, j));
          ps.executeUpdate();
        }
      }
      connection.commit();
    }
  }
}
