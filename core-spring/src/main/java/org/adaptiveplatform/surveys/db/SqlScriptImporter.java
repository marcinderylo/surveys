package org.adaptiveplatform.surveys.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;

@Service("sqlScriptImporter")
public class SqlScriptImporter {

	@Resource
	private DataSource dataSource;

	@Resource
	private ResourceLoader resourceLoader;

	public void executeScript(String resourcePath) throws IOException, SQLException {
		org.springframework.core.io.Resource resource = resourceLoader.getResource(ResourceLoader.CLASSPATH_URL_PREFIX + resourcePath);
		executeScript(resource.getInputStream());
	}

	public void executeScript(InputStream sqlScript) throws IOException, SQLException {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			String scriptText = slurp(sqlScript);
			connection.createStatement().execute(scriptText);
		} finally {
			JdbcUtils.closeConnection(connection);
		}
	}

	public boolean tableExists(String tableName) throws SQLException {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			return checkIfTableExists(connection, tableName);
		} finally {
			JdbcUtils.closeConnection(connection);
		}
	}

	private boolean checkIfTableExists(Connection connection, String tableName) throws SQLException {
		Statement statement = connection.createStatement();
		try {
			statement.executeQuery("select count(*) from " + tableName);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	private String slurp(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

}
