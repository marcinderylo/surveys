package org.adaptiveplatform.surveys;

import static org.testng.Assert.assertNotNull;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = "classpath:/testConfigurationContext.xml")
public class ConfigurationTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private BeanFactory beanFactory;

	@Test
	public void shouldInjectSessionFactory() throws Exception {
		SessionFactory sessionFactory = beanFactory.getBean("sessionFactory", SessionFactory.class);
		assertNotNull(sessionFactory);
	}
}
