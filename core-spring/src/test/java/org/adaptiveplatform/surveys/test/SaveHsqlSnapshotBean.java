package org.adaptiveplatform.surveys.test;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Runs {@link #afterPropertiesSet()} method after {@link HsqlSavableDataSource}
 * and {@link SessionFactory} are fully initialized to save the snapshot of the
 * database. This Snapshot contains full schema that hibernate created but no
 * data and can be restored before every test to ensure clean start.
 * 
 * @author Rafał Jamróz
 */
public class SaveHsqlSnapshotBean implements InitializingBean {

	@Resource
	private HsqlSavableDataSource dataSource;

	@Resource
	private SessionFactory sessionFactory;

	@Override
	public void afterPropertiesSet() throws Exception {
		dataSource.saveSnapshot();
	}
}
