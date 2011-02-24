package org.adaptiveplatform.surveys.db;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

public class SqlImporterBean implements InitializingBean {

	private List<Resource> sqlScripts;
	private String tableBeingImported;

	@Autowired
	private SqlScriptImporter importer;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (tableBeingImported == null || !importer.tableExists(tableBeingImported)) {
			for (Resource resource : sqlScripts) {
				InputStream inputStream = resource.getInputStream();
				importer.executeScript(inputStream);
			}
		}
	}

	public void setSqlScripts(List<Resource> sqlScripts) {
		this.sqlScripts = sqlScripts;
	}

	public void setTableBeingImported(String tableToCheck) {
		this.tableBeingImported = tableToCheck;
	}

	public void setImporter(SqlScriptImporter importer) {
		this.importer = importer;
	}
}
