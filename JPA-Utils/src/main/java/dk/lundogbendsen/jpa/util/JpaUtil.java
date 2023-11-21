package dk.lundogbendsen.jpa.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import jakarta.persistence.EntityManager;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

//Hibernate 3.5
//import org.hibernate.impl.SessionImpl;

// Hibernate 4
import org.hibernate.internal.SessionImpl;

import dk.lundogbendsen.string.util.StringUtil;

public final class JpaUtil {

	// Final class and private constructor prevents instantiation
	private JpaUtil() {
	}

	/**
	 * Configures Hibernate logging (merging/overriding log4j.properties)
	 * 
	 * @param config
	 *            specifies the configuration we want to use.
	 */
	public static void configureHibernateLogging(HibernateLoggingConfig config) {

		final Level INHERITED = null;

		Logger.getLogger("org.hibernate").setLevel(config.hibernateRootLogLevel);

		if (config.logSqlEnabled) {
			Logger.getLogger("org.hibernate.SQL").setLevel(Level.DEBUG);
		} else {
			Logger.getLogger("org.hibernate.SQL").setLevel(INHERITED);
		}

		if (config.logParameterBindingEnabled) {
			Logger.getLogger("org.hibernate.type").setLevel(Level.TRACE);
		} else {
			Logger.getLogger("org.hibernate.type").setLevel(INHERITED);
		}

		if (config.logDdlEnabled) {
			Logger.getLogger("org.hibernate.tool.hbm2ddl").setLevel(Level.DEBUG);
		} else {
			Logger.getLogger("org.hibernate.tool.hbm2ddl").setLevel(INHERITED);
		}
	}
	
	public static int getEntityCount(EntityManager entityManager) {
		SessionImpl hibernateSession = (SessionImpl) entityManager.getDelegate();
		return hibernateSession.getStatistics().getEntityCount();
	}

	public static void prettyPrintQueryResult(String comment, String jpaql, Object result) {
		prettyPrintQueryResult(comment, jpaql, result, false);
	}

	public static void prettyPrintQueryResult(String comment, String jpaql, Object result, boolean printArrayAsList) {
		StringUtil.prettyPrintHeadline(comment);
		prettyPrintQueryResult(jpaql, result, printArrayAsList);
	}

	public static void prettyPrintQueryResult(String jpaql, Object result) {
		prettyPrintQueryResult(jpaql, result, false);
	}

	public static void prettyPrintQueryResult(String jpaql, Object result, boolean printArrayAsList) {
		String msg = "[" + jpaql + "] -> ";
		msg += toPrettyString(0, result, printArrayAsList);
		System.out.println(msg);
	}

	@SuppressWarnings("unchecked")
	public static String toPrettyString(int tabCount, Object object, boolean printArrayAsList) {
		String s = "";
		if (object == null) {
			s += "null";
		} else if (object instanceof Collection) {
			Collection collection = (Collection) object;
			s += toPrettyString(tabCount, collection, printArrayAsList);
		} else if (object instanceof Object[]) {
			Object[] objectArray = (Object[]) object;
			if (printArrayAsList) {
				s += toPrettyString(tabCount, Arrays.asList(objectArray), printArrayAsList);
			} else {
				s += toPrettyString(tabCount, objectArray, printArrayAsList);
			}
		} else {
			return object.toString();
		}
		return s;
	}

	private static String newLine(int tabCount) {
		return '\n' + tabs(tabCount);
	}

	private static String toPrettyString(int tabCount, Object[] objects, boolean printArrayAsList) {
		String s = "";
		for (Object item : objects) {
			s += "|";
			s += toPrettyString(tabCount, item, printArrayAsList);
		}
		s += "|";
		return s;
	}

	@SuppressWarnings("unchecked")
	private static String toPrettyString(int tabCount, Collection collection, boolean printArrayAsList) {
		String s = "[";
		tabCount++;
		for (Object item : collection) {
			s += newLine(tabCount);
			s += toPrettyString(tabCount, item, printArrayAsList);
		}
		tabCount--;
		s += newLine(tabCount) + "]";
		return s;
	}

	private static String tabs(int count) {
		return repeat('\t', count);
	}

	private static String repeat(char c, int count) {
		return repeat("" + c, count);
	}

	private static String repeat(String s, int count) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < count; n++) {
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * Used to specify the configuration relating to the EntityManagers
	 * constructed by method in the class JpaUtils.
	 */
	public static class HibernateLoggingConfig {
		private boolean logDdlEnabled = false;
		private boolean logSqlEnabled = false;
		private boolean logParameterBindingEnabled = false;
		private Level hibernateRootLogLevel = Level.ERROR;

		/**
		 * Whether Hibernate Should log the DDL statements it sends to the
		 * database.
		 * 
		 * @param logDdlEnabled
		 *            should DDL logging be enabled
		 */
		public void setLogDdlEnabled(boolean logDdlEnabled) {
			this.logDdlEnabled = logDdlEnabled;
		}

		/**
		 * Whether Hibernate Should log the SQL statements it sends to the
		 * database.
		 * 
		 * @param logSqlEnabled
		 *            should SQL logging be enabled
		 */
		public void setLogSqlEnabled(boolean logSqlEnabled) {
			this.logSqlEnabled = logSqlEnabled;
		}

		/**
		 * Whether Hibernate Should log the values it binds to prepared
		 * statement parameters.
		 * 
		 * @param logParameterBindingEnabled
		 *            should logging of parameter binding be enabled
		 */
		public void setLogParameterBindingEnabled(boolean logParameterBindingEnabled) {
			this.logParameterBindingEnabled = logParameterBindingEnabled;
		}

		/**
		 * What log4j log level we want to use as threshold for the Hibernate
		 * root logger. That is the threshold level for the logger named
		 * "org.hibernate".
		 * 
		 * @param logSqlEnabled
		 *            should SQL logging be enabled
		 */
		public void setHibernateRootLogLevel(Level hibernateRootLogLevel) {
			this.hibernateRootLogLevel = hibernateRootLogLevel;
		}
	}

	/**
	 * Used to create a map of properties to override properties set in a JPA
	 * persistence.xml file.<p/>
	 * 
	 * Example use:<p/>
	 * 
	 * <code>
	 * PersistenceConfig config = new PersistenceConfig();<br/>
	 * config.setDatabaseUrl("jdbc:derby://localhost:1527/JpaTest;create=true");<br/>
	 * config.setConnectionUsername("JpaUser");<br/>
	 * config.setConnectionPassword("JpaPassword");<p/>
	 * 
	 * Map<String, String> props = config.getConfigProperties();<br/>
	 * String unitName = "StandaloneJpaTestPersistenceUnit";<p/>
	 * 
	 * EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(unitName, props);<br/>
	 * EntityManager entityManager = entityManagerFactory.createEntityManager();<br/>
	 * </code>
	 */
	public static class PersistenceConfig {
		private SortedMap<String, String> configProperties = new TreeMap<String, String>();

		public void addConfigProperty(String key, String value) {
			this.configProperties.put(key, value);
		}

		public SortedMap<String, String> getConfigProperties() {
			return Collections.unmodifiableSortedMap(this.configProperties);
		}

		public void setJdbcDriver(String driverClassName) {
			configProperties.put("hibernate.connection.driver_class", driverClassName);
		}

		public void useDerbyJdbcClientDriver() {
			setJdbcDriver("org.apache.derby.jdbc.ClientDriver");
		}

		public void useMySqlJdbcDriver() {
			setJdbcDriver("com.mysql.jdbc.Driver");
		}

		public void setDatabaseUrl(String url) {
			configProperties.put("hibernate.connection.url", url);
		}

		public void createAndSetDerbyUrl(String host, int port, String database, boolean createDatabase) {
			String url = "jdbc:derby://";
			url += host;
			url += ":" + port;
			url += "/" + database;
			url += createDatabase ? ";create=true" : "";
			setDatabaseUrl(url);
		}

		public void createAndSetMySqlUrl(String host, String database) {
			String url = "jdbc:mysql://";
			url += host;
			url += "/" + database;
			setDatabaseUrl(url);
		}

		public void setConnectionUsername(String username) {
			configProperties.put("hibernate.connection.username", username);
		}

		public void setConnectionPassword(String password) {
			configProperties.put("hibernate.connection.password", password);
		}

		public void setHibernateDialect(String dialect) {
			configProperties.put("hibernate.dialect", dialect);
		}

		public void useDerbyDialect() {
			setHibernateDialect("org.hibernate.dialect.DerbyDialect");
		}

		public void useMySqlDialect() {
			setHibernateDialect("org.hibernate.dialect.MySQLInnoDBDialect");
		}

		public void useAutomaticDdlCreateAndDrop() {
			configProperties.put("hibernate.hbm2ddl.auto", "create-drop");
		}
	}
}