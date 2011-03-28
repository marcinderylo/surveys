package org.adaptiveplatform.surveys.application;

import org.adaptiveplatform.codegenerator.api.RemoteService;

@RemoteService
public interface SystemInformationDao {
	String getSystemVersion();
}
