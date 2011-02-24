package org.adaptiveplatform.surveys.test;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * DataSource that supports saving a snapshot of the whole database state. This
 * state can be later restored any number of times.
 * 
 * @author Rafał Jamróz
 */
public class HsqlSavableDataSource extends DelegatingDataSource implements DisposableBean {

	private static final String USER = "sa";
	private static final String PASSWORD = "";

	private String originalDatabase;
	private String clonedDatabase;
	private boolean snapshotCreated;

	public HsqlSavableDataSource(String originalDatabase, String clonedDatabase) throws Exception {
		Class.forName("org.hsqldb.jdbcDriver");
		this.originalDatabase = originalDatabase;
		this.clonedDatabase = clonedDatabase;
		cleanupFiles();
		createDatasource();
	}

	public void saveSnapshot() throws SQLException, IOException {
		shutdownDatabase();
		cloneFile(".script");
		cloneFile(".properties");
		createDatasource();
		snapshotCreated = true;
	}

	public void restoreFromSnapshot() throws SQLException, IOException {
		assertTrue(snapshotCreated, "no db snapshot created");
		shutdownDatabase();
		restoreFile(".script");
		restoreFile(".properties");
		createDatasource();
	}

	private void restoreFile(String string) throws IOException {
		FileUtils.copyFile(new File(clonedDatabase + string), new File(originalDatabase + string));
	}

	private void cloneFile(String string) throws IOException {
		FileUtils.copyFile(new File(originalDatabase + string), new File(clonedDatabase + string));
	}

	private void shutdownDatabase() throws SQLException {
		Connection connection = getConnection();
		connection.createStatement().execute("SHUTDOWN");
		connection.close();
	}

	private void createDatasource() {
		DataSource dataSource = new DriverManagerDataSource("jdbc:hsqldb:file:" + originalDatabase, USER, PASSWORD);
		setTargetDataSource(dataSource);
	}

	@Override
	public void destroy() throws Exception {
		shutdownDatabase();
		cleanupFiles();
	}

	private void cleanupFiles() {
		String[] extensionsToDelete = new String[] { ".script", ".properties", ".log", ".tmp" };
		for (String extension : extensionsToDelete) {
			FileUtils.deleteQuietly(new File(originalDatabase + extension));
			FileUtils.deleteQuietly(new File(clonedDatabase + extension));
		}

	}
}
