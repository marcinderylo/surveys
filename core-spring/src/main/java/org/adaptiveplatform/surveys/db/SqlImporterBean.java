package org.adaptiveplatform.surveys.db;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

public class SqlImporterBean implements InitializingBean {

	private List<String> sqlScripts;

	private SqlScriptImporter importer;

	@Override
	public void afterPropertiesSet() throws Exception {
		for (String sqlScript : sqlScripts) {
			importer.executeScript(sqlScript);
		}
	}

	public List<String> getSqlScripts() {
		return sqlScripts;
	}

	public void setSqlScripts(List<String> sqlScripts) {
		this.sqlScripts = sqlScripts;
	}

	public SqlScriptImporter getImporter() {
		return importer;
	}

	public void setImporter(SqlScriptImporter importer) {
		this.importer = importer;
	}

}
