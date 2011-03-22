package org.adaptiveplatform.surveys;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@ContextConfiguration(locations = "classpath:/testConfigurationContext.xml")
public class JdbcTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private DataSource dataSource;
	private Connection connection;

	@Test
	public void testIfTableExists() throws Exception {
		String tableName = "testTable";
		Assert.assertFalse(checkIfTableExists(tableName));
		createTable(tableName);
		Assert.assertTrue(checkIfTableExists(tableName));
	}

	private void createTable(String tableName) throws Exception {
		Statement statement = connection.createStatement();
		statement.execute("create table " + tableName + " ( N integer );");
	}

	private boolean checkIfTableExists(String tableName) throws Exception {
		Statement statement = connection.createStatement();
		try {
			statement.executeQuery("select count(*) from " + tableName);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	@BeforeMethod
	public void beforeMethod() throws Exception {
		connection = dataSource.getConnection();
	}

	@AfterMethod
	public void afterMethod() throws Exception {
		connection.close();
	}
}
