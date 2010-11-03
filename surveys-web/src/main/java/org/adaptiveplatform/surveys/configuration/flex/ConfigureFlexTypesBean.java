package org.adaptiveplatform.surveys.configuration.flex;

import org.springframework.beans.factory.InitializingBean;

import flex.messaging.io.PropertyProxyRegistry;

/**
 * Registers custom java types to be externalizable to flex.
 */
public class ConfigureFlexTypesBean implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		PropertyProxyRegistry.getRegistry().register(Enum.class,
				new EnumPropertyProxy());
	}

}
