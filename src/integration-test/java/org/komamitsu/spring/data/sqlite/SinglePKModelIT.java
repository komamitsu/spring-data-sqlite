package org.komamitsu.spring.data.sqlite;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.komamitsu.spring.data.sqlite.domain.model.SinglePKModel;
import org.komamitsu.spring.data.sqlite.domain.repository.SinglePKModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SqliteJdbcConfiguration.class)
@EnableAutoConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SinglePKModelIT {

  private static final String TABLE = "single_pk_model";
  private static final String ACCOUNT_ID = "account_id";
  private static final String ACCOUNT_TYPE = "account_type";
  private static final String BALANCE = "balance";
  private static final String NAME = "name";
  private static final int INITIAL_BALANCE = 1000;
  private static final int NUM_ACCOUNTS = 12;

  private static final String JDBC_URL = "jdbc:sqlite:integration-test.db";

  @Autowired SinglePKModelRepository repository;

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
              "CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INT, %s INT, %s TEXT)",
              TABLE, ACCOUNT_ID, ACCOUNT_TYPE, BALANCE, NAME));
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

  private String getAccountName(int accountId) {
    return String.format("account_%d", accountId);
  }

  @Test
  public void findById_ForExistingRecord_ShouldReturnIt() throws SQLException {
    populateRecords();

    Optional<SinglePKModel> testModel = repository.findById(0);

    assertThat(testModel).isPresent();
    testModel.ifPresent(
        found -> {
          assertThat(found.accountId).isEqualTo(0);
          assertThat(found.accountType).isEqualTo(0);
          assertThat(found.balance).isEqualTo(INITIAL_BALANCE);
          assertThat(found.name).isEqualTo(getAccountName(0));
        });
  }

  // This is internally same as findById_ForExistingRecord_ShouldReturnIt()
  @Test
  public void findByPrimaryKey_ForExistingRecord_ShouldReturnIt() throws SQLException {
    populateRecords();

    Optional<SinglePKModel> testModel = repository.findFirstByAccountId(0);

    assertThat(testModel).isPresent();
    testModel.ifPresent(
        found -> {
          assertThat(found.accountId).isEqualTo(0);
          assertThat(found.accountType).isEqualTo(0);
          assertThat(found.balance).isEqualTo(INITIAL_BALANCE);
          assertThat(found.name).isEqualTo(getAccountName(0));
        });
  }

  @Test
  public void findByIndex_ForExistingRecords_ShouldReturnThem() throws SQLException {
    populateRecords();

    List<SinglePKModel> records = repository.findByAccountType(2);

    assertThat(records).size().isEqualTo(3);
    {
      SinglePKModel testModel = records.get(0);
      assertThat(testModel.accountId).isEqualTo(2);
      assertThat(testModel.accountType).isEqualTo(2);
      assertThat(testModel.balance).isEqualTo(INITIAL_BALANCE);
      assertThat(testModel.name).isEqualTo(getAccountName(2));
    }
    {
      SinglePKModel testModel = records.get(1);
      assertThat(testModel.accountId).isEqualTo(6);
      assertThat(testModel.accountType).isEqualTo(2);
      assertThat(testModel.balance).isEqualTo(INITIAL_BALANCE);
      assertThat(testModel.name).isEqualTo(getAccountName(6));
    }
    {
      SinglePKModel testModel = records.get(2);
      assertThat(testModel.accountId).isEqualTo(10);
      assertThat(testModel.accountType).isEqualTo(2);
      assertThat(testModel.balance).isEqualTo(INITIAL_BALANCE);
      assertThat(testModel.name).isEqualTo(getAccountName(10));
    }
  }

  @Test
  public void findByIndexWithLimit_ForExistingRecords_ShouldReturnThem() throws SQLException {
    populateRecords();

    List<SinglePKModel> records = repository.findTop2ByAccountType(3);

    assertThat(records).size().isEqualTo(2);
    {
      SinglePKModel testModel = records.get(0);
      assertThat(testModel.accountId).isEqualTo(3);
      assertThat(testModel.accountType).isEqualTo(3);
      assertThat(testModel.balance).isEqualTo(INITIAL_BALANCE);
      assertThat(testModel.name).isEqualTo(getAccountName(3));
    }
    {
      SinglePKModel testModel = records.get(1);
      assertThat(testModel.accountId).isEqualTo(7);
      assertThat(testModel.accountType).isEqualTo(3);
      assertThat(testModel.balance).isEqualTo(INITIAL_BALANCE);
      assertThat(testModel.name).isEqualTo(getAccountName(7));
    }
  }

  @Test
  public void findByPrimaryKey_ForNonExistingRecord_ShouldReturnEmpty() throws SQLException {
    populateRecords();

    Optional<SinglePKModel> record = repository.findFirstByAccountId(42);

    assertThat(record).isEmpty();
  }

  @Test
  public void findByIndex_ForNonExistingRecord_ShouldReturnEmpty() throws SQLException {
    populateRecords();

    List<SinglePKModel> records = repository.findByAccountType(10);

    assertThat(records).size().isEqualTo(0);
  }

  @Test
  public void findAll_ForExistingRecords_ShouldReturnThem() throws SQLException {
    populateRecords();

    List<SinglePKModel> records = repository.findAllByOrderByAccountId();

    int index = 0;
    for (int i = 0; i < NUM_ACCOUNTS; i++) {
      SinglePKModel testModel = records.get(index);
      assertThat(testModel.accountId).isEqualTo(i);
      assertThat(testModel.accountType).isEqualTo(i % 4);
      assertThat(testModel.balance).isEqualTo(INITIAL_BALANCE);
      assertThat(testModel.name).isEqualTo(getAccountName(i));
      index++;
    }
    assertThat(index).isEqualTo(NUM_ACCOUNTS);
  }

  @Test
  public void count_ForExistingRecord_ShouldReturnTheCount() throws SQLException {
    populateRecords();

    long result = repository.count();

    assertThat(result).isEqualTo(NUM_ACCOUNTS);
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

  private ResultSet getAccount(Connection connection, int accountId) throws SQLException {
    PreparedStatement ps =
        connection.prepareStatement(
            String.format("SELECT * FROM %s WHERE %s = ?", TABLE, ACCOUNT_ID));
    ps.setInt(1, accountId);
    return ps.executeQuery();
  }

  @Test
  public void insert_ForNonExistingRecord_ShouldCreateIt() throws SQLException {
    int expected = INITIAL_BALANCE;

    SinglePKModel saved = repository.insert(new SinglePKModel(0, 0, expected, getAccountName(0)));
    assertThat(saved.accountId).isEqualTo(0);
    assertThat(saved.accountType).isEqualTo(0);
    assertThat(saved.balance).isEqualTo(expected);
    assertThat(saved.name).isEqualTo(getAccountName(0));

    try (Connection connection = getConnection()) {
      ResultSet resultSet = getAccount(connection, 0);

      assertThat(resultSet.next()).isTrue();
      assertThat(resultSet.getInt(ACCOUNT_ID)).isEqualTo(0);
      assertThat(resultSet.getInt(ACCOUNT_TYPE)).isEqualTo(0);
      assertThat(resultSet.getInt(BALANCE)).isEqualTo(expected);
      assertThat(resultSet.getString(NAME)).isEqualTo(getAccountName(0));
      assertThat(resultSet.next()).isFalse();
    }
  }

  @Test
  public void updateAfterRead_ForExistingRecord_ShouldUpdateIt() throws SQLException {
    populateRecords();

    int amount = 100;
    repository.incrementBalance(0, amount);

    try (Connection connection = getConnection()) {
      ResultSet resultSet = getAccount(connection, 0);

      assertThat(resultSet.next()).isTrue();
      assertThat(resultSet.getInt(ACCOUNT_ID)).isEqualTo(0);
      assertThat(resultSet.getInt(ACCOUNT_TYPE)).isEqualTo(0);
      assertThat(resultSet.getInt(BALANCE)).isEqualTo(INITIAL_BALANCE + amount);
      assertThat(resultSet.getString(NAME)).isEqualTo(getAccountName(0));
      assertThat(resultSet.next()).isFalse();
    }
  }

  @Test
  public void save_ForNonExistingRecord_ShouldCreateIt() throws SQLException {
    int expected = INITIAL_BALANCE;

    SinglePKModel saved = repository.save(new SinglePKModel(null, 0, expected, "unknown"));
    assertThat(saved.accountType).isEqualTo(0);
    assertThat(saved.balance).isEqualTo(expected);
    assertThat(saved.name).isEqualTo("unknown");

    try (Connection connection = getConnection()) {
      ResultSet resultSet = getAccount(connection, saved.accountId);

      assertThat(resultSet.next()).isTrue();
      assertThat(resultSet.getInt(ACCOUNT_ID)).isEqualTo(saved.accountId);
      assertThat(resultSet.getInt(ACCOUNT_TYPE)).isEqualTo(0);
      assertThat(resultSet.getInt(BALANCE)).isEqualTo(expected);
      assertThat(resultSet.getString(NAME)).isEqualTo("unknown");
      assertThat(resultSet.next()).isFalse();
    }
  }

  @Test
  public void save_ForExistingRecord_ShouldUpdateIt() throws SQLException {
    populateRecords();

    int amount = 100;
    SinglePKModel saved =
        repository.save(new SinglePKModel(0, 42, INITIAL_BALANCE + amount, getAccountName(0)));
    assertThat(saved.accountId).isEqualTo(0);
    assertThat(saved.accountType).isEqualTo(42);
    assertThat(saved.balance).isEqualTo(INITIAL_BALANCE + amount);
    assertThat(saved.name).isEqualTo(getAccountName(0));

    try (Connection connection = getConnection()) {
      ResultSet resultSet = getAccount(connection, 0);

      assertThat(resultSet.next()).isTrue();
      assertThat(resultSet.getInt(ACCOUNT_ID)).isEqualTo(0);
      assertThat(resultSet.getInt(ACCOUNT_TYPE)).isEqualTo(42);
      assertThat(resultSet.getInt(BALANCE)).isEqualTo(INITIAL_BALANCE + amount);
      assertThat(resultSet.getString(NAME)).isEqualTo(getAccountName(0));
      assertThat(resultSet.next()).isFalse();
    }
  }

  @Test
  public void insertWithNullValue_ForNonExistingRecord_ShouldCreateIt() throws SQLException {
    SinglePKModel saved = repository.insert(new SinglePKModel(0, 0, null, null));
    assertThat(saved.accountId).isEqualTo(0);
    assertThat(saved.accountType).isEqualTo(0);
    assertThat(saved.balance).isNull();
    assertThat(saved.name).isNull();

    try (Connection connection = getConnection()) {
      ResultSet resultSet = getAccount(connection, 0);

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
  public void updatesAfterFindById_ForExistingRecords_ShouldUpdateThem() throws SQLException {
    populateRecords();
    int amount = 100;
    int fromId = 0;
    int toId = 1;

    repository.transfer(fromId, toId, amount);

    try (Connection connection = getConnection()) {
      connection.setAutoCommit(false);

      {
        ResultSet resultSet = getAccount(connection, fromId);
        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getInt(ACCOUNT_ID)).isEqualTo(fromId);
        assertThat(resultSet.getInt(BALANCE)).isEqualTo(INITIAL_BALANCE - amount);
        assertThat(resultSet.getString(NAME)).isEqualTo(getAccountName(fromId));
        assertThat(resultSet.next()).isFalse();
      }

      {
        ResultSet resultSet = getAccount(connection, toId);
        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getInt(ACCOUNT_ID)).isEqualTo(toId);
        assertThat(resultSet.getInt(BALANCE)).isEqualTo(INITIAL_BALANCE + amount);
        assertThat(resultSet.getString(NAME)).isEqualTo(getAccountName(toId));
      }
    }
  }

  @Test
  public void delete_ForExistingRecord_ShouldDeleteIt() throws SQLException {
    populateRecords();

    repository.deleteById(0);

    try (Connection connection = getConnection()) {
      ResultSet resultSet = getAccount(connection, 0);

      assertThat(resultSet.next()).isFalse();
    }
  }

  private void populateRecords() throws SQLException {
    try (Connection connection = getConnection()) {
      connection.setAutoCommit(false);

      for (int i = 0; i < NUM_ACCOUNTS; i++) {
        PreparedStatement ps =
            connection.prepareStatement(String.format("INSERT INTO %s VALUES(?, ?, ?, ?)", TABLE));
        ps.setInt(1, i);
        ps.setInt(2, i % 4);
        ps.setInt(3, INITIAL_BALANCE);
        ps.setString(4, getAccountName(i));
        ps.executeUpdate();
      }
      connection.commit();
    }
  }
}
