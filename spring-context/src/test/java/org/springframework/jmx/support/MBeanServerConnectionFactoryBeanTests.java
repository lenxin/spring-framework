

package org.springframework.jmx.support;

import java.net.MalformedURLException;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.junit.Test;

import org.springframework.aop.support.AopUtils;
import org.springframework.jmx.AbstractMBeanServerTests;
import org.springframework.util.SocketUtils;

import static org.junit.Assert.*;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
public class MBeanServerConnectionFactoryBeanTests extends AbstractMBeanServerTests {

	private final String serviceUrl = "service:jmx:jmxmp://localhost:" + SocketUtils.findAvailableTcpPort(9800, 9900);


	private JMXServiceURL getJMXServiceUrl() throws MalformedURLException {
		return new JMXServiceURL(serviceUrl);
	}

	private JMXConnectorServer getConnectorServer() throws Exception {
		return JMXConnectorServerFactory.newJMXConnectorServer(getJMXServiceUrl(), null, getServer());
	}

	@Test
	public void testTestValidConnection() throws Exception {
		JMXConnectorServer connectorServer = getConnectorServer();
		connectorServer.start();

		try {
			MBeanServerConnectionFactoryBean bean = new MBeanServerConnectionFactoryBean();
			bean.setServiceUrl(serviceUrl);
			bean.afterPropertiesSet();

			try {
				MBeanServerConnection connection = bean.getObject();
				assertNotNull("Connection should not be null", connection);

				// perform simple MBean count test
				assertEquals("MBean count should be the same", getServer().getMBeanCount(), connection.getMBeanCount());
			}
			finally {
				bean.destroy();
			}
		}
		finally {
			connectorServer.stop();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithNoServiceUrl() throws Exception {
		MBeanServerConnectionFactoryBean bean = new MBeanServerConnectionFactoryBean();
		bean.afterPropertiesSet();
	}

	@Test
	public void testTestWithLazyConnection() throws Exception {
		MBeanServerConnectionFactoryBean bean = new MBeanServerConnectionFactoryBean();
		bean.setServiceUrl(serviceUrl);
		bean.setConnectOnStartup(false);
		bean.afterPropertiesSet();

		MBeanServerConnection connection = bean.getObject();
		assertTrue(AopUtils.isAopProxy(connection));

		JMXConnectorServer connector = null;
		try {
			connector = getConnectorServer();
			connector.start();
			assertEquals("Incorrect MBean count", getServer().getMBeanCount(), connection.getMBeanCount());
		}
		finally {
			bean.destroy();
			if (connector != null) {
				connector.stop();
			}
		}
	}

	@Test
	public void testWithLazyConnectionAndNoAccess() throws Exception {
		MBeanServerConnectionFactoryBean bean = new MBeanServerConnectionFactoryBean();
		bean.setServiceUrl(serviceUrl);
		bean.setConnectOnStartup(false);
		bean.afterPropertiesSet();

		MBeanServerConnection connection = bean.getObject();
		assertTrue(AopUtils.isAopProxy(connection));
		bean.destroy();
	}

}
