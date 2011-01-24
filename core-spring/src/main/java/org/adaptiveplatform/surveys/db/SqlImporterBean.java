package org.adaptiveplatform.surveys.db;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

public class SqlImporterBean implements InitializingBean {

	private List<String> sqlScripts;
	private String tableBeingImported;

	private SqlScriptImporter importer;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (tableBeingImported==null || !importer.tableExists(tableBeingImported)) {
			for (String sqlScript : sqlScripts) {
				importer.executeScript(sqlScript);
			}
		}
	}

	public void setSqlScripts(List<String> sqlScripts) {
		this.sqlScripts = sqlScripts;
	}

	public void setTableBeingImported(String tableToCheck) {
		this.tableBeingImported = tableToCheck;
	}

	public void setImporter(SqlScriptImporter importer) {
		this.importer = importer;
	}
}
