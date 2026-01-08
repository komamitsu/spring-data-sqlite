package org.komamitsu.spring.data.sqlite;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;

/**
 * Annotation to enable SQLite repositories. Will scan the package of the annotated configuration
 * class for SQLite repositories by default. This code is the same as {@link
 * EnableJdbcRepositories}.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import(SqliteRepositoryRegistrar.class)
public @interface EnableSqliteRepositories {
  /**
   * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation
   * declarations e.g.: {@code @EnableSqliteRepositories("org.my.pkg")} instead of
   * {@code @EnableSqliteRepositories(basePackages="org.my.pkg")}.
   *
   * @return value
   */
  String[] value() default {};

  /**
   * Base packages to scan for annotated components. {@link #value()} is an alias for (and mutually
   * exclusive with) this attribute. Use {@link #basePackageClasses()} for a type-safe alternative
   * to String-based package names.
   *
   * @return base packages
   */
  String[] basePackages() default {};

  /**
   * Type-safe alternative to {@link #basePackages()} for specifying the packages to scan for
   * annotated components. The package of each class specified will be scanned. Consider creating a
   * special no-op marker class or interface in each package that serves no purpose other than being
   * referenced by this attribute.
   *
   * @return base package classes
   */
  Class<?>[] basePackageClasses() default {};

  /**
   * Specifies which types are eligible for component scanning. Further narrows the set of candidate
   * components from everything in {@link #basePackages()} to everything in the base packages that
   * matches the given filter or filters.
   *
   * @return include filters
   */
  Filter[] includeFilters() default {};

  /**
   * Specifies which types are not eligible for component scanning.
   *
   * @return exclude filters
   */
  Filter[] excludeFilters() default {};

  /**
   * Returns the postfix to be used when looking up custom repository implementations. Defaults to
   * {@literal Impl}. So for a repository named {@code PersonRepository} the corresponding
   * implementation class will be looked up scanning for {@code PersonRepositoryImpl}.
   *
   * @return repository implementation postfix
   */
  String repositoryImplementationPostfix() default "Impl";

  /**
   * Configures the location of where to find the Spring Data named queries properties file. Will
   * default to {@code META-INF/jdbc-named-queries.properties}.
   *
   * @return named queries location
   */
  String namedQueriesLocation() default "";

  /**
   * Returns the {@link FactoryBean} class to be used for each repository instance. Defaults to
   * {@link SqliteJdbcRepositoryFactoryBean}.
   *
   * @return repository factory bean class
   */
  Class<?> repositoryFactoryBeanClass() default SqliteJdbcRepositoryFactoryBean.class;

  /**
   * Configure the repository base class to be used to create repository proxies for this particular
   * configuration.
   *
   * @return repository base class
   */
  Class<?> repositoryBaseClass() default SqliteRepositoryImpl.class;

  /**
   * Configures whether nested repository-interfaces (e.g. defined as inner classes) should be
   * discovered by the repositories' infrastructure.
   *
   * @return consider nested repositories
   */
  boolean considerNestedRepositories() default false;

  /**
   * Configures the name of the {@link
   * org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations} bean definition to be
   * used to create repositories discovered through this annotation. Defaults to {@code
   * namedParameterJdbcTemplate}.
   *
   * @return jdbc operations ref
   * @deprecated since 4.0, use {@link #jdbcAggregateOperationsRef()} instead.
   */
  @Deprecated
  String jdbcOperationsRef() default "";

  /**
   * Configures the name of the {@link org.springframework.data.jdbc.core.JdbcAggregateOperations}
   * bean definition to be used to create repositories discovered through this annotation.
   *
   * @return jdbc aggregate operations ref
   */
  String jdbcAggregateOperationsRef() default "";

  /**
   * Configures the name of the {@link
   * org.springframework.data.jdbc.core.convert.DataAccessStrategy} bean definition to be used to
   * create repositories discovered through this annotation. Defaults to {@code
   * defaultDataAccessStrategy}.
   *
   * @return data access strategy ref
   * @deprecated since 4.0, use {@link #jdbcAggregateOperationsRef()} instead.
   */
  @Deprecated
  String dataAccessStrategyRef() default "";

  /**
   * Configures the name of the DataSourceTransactionManager bean definition to be used to create
   * repositories discovered through this annotation. Defaults to {@code transactionManager}.
   *
   * @return transaction manager ref
   */
  String transactionManagerRef() default "transactionManager";

  /**
   * Returns the key of the {@link QueryLookupStrategy} to be used for lookup queries for query
   * methods. Defaults to {@link QueryLookupStrategy.Key#CREATE_IF_NOT_FOUND}.
   *
   * @return query lookup strategy
   */
  QueryLookupStrategy.Key queryLookupStrategy() default QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND;

  /**
   * Configures whether to enable default transactions for Spring Data JDBC repositories. If
   * disabled, repositories must be used behind a facade that's configuring transactions (e.g. using
   * Spring's annotation driven transaction facilities) or repository methods have to be used to
   * demarcate transactions.
   *
   * @return whether to enable default transactions
   */
  boolean enableDefaultTransactions() default true;
}
