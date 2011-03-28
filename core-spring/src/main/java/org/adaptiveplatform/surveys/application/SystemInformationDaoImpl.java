package org.adaptiveplatform.surveys.application;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;

@RemotingDestination
@Service("systemInformationDao")
public class SystemInformationDaoImpl implements SystemInformationDao {

	public String getSystemVersion() {
		return "0.9.1.SNAPSHOT";
	}
}
