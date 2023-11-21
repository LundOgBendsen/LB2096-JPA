package dk.lundogbendsen.ejb.util;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JndiEjbUtil {

	public static final String DEFAULT_IMPLEMENTATION_CLASS_SUFFIX = "Impl";
	public static final String DEFAULT_REMOTE_INTERFACE_SUFFIX = "/remote";
	
	private String implementationClassSuffix;
	private String remoteInterfaceSuffix;
	private boolean writeJndiNameToSystemOut = false;
	private boolean propriotaryJndiNames = false;

	/**
	 * Use this to override the defaults used to calculate the bean jndi name.
	 * 
	 * @param implementationClassSuffix
	 *            Is suffixed to the name of the interface to calculate the bean
	 *            jndi name.
	 * @param remoteInterfaceSuffix
	 *            Is suffixed to the bean jndi name to get the jndi name of its
	 *            remote interface.
	 * @param writeJndiNameToSystemOut
	 *            Should the calculated jndi name be written to system out?
	 */
	public JndiEjbUtil(String implementationClassSuffix,
			String remoteInterfaceSuffix, boolean writeJndiNameToSystemOut,
			boolean propriotaryJndiNames) {
		this.implementationClassSuffix = implementationClassSuffix;
		this.remoteInterfaceSuffix = remoteInterfaceSuffix;
		this.writeJndiNameToSystemOut = writeJndiNameToSystemOut;
		this.propriotaryJndiNames = propriotaryJndiNames;
	}

	/**
	 * Use this to override the defaults used to calculate the bean jndi name.
	 * The default string "/remote" will be suffixed to the bean jndi name to
	 * get the jndi name of its remote interface.
	 * 
	 * @param implementationClassSuffix
	 *            Is suffixed to the name of the interface to calculate the bean
	 *            jndi name.
	 * @param writeJndiNameToSystemOut
	 *            Should the calculated jndi name be written to system out?
	 */
	public JndiEjbUtil(String implementationClassSuffix,
			boolean writeJndiNameToSystemOut) {
		this(implementationClassSuffix, DEFAULT_REMOTE_INTERFACE_SUFFIX,
				writeJndiNameToSystemOut, false);
	}

	/**
	 * Use this if you want to use the defaults to calculate the bean jndi name.
	 * The default string "Impl" will be suffixed to the name of the remote
	 * interface type name to calculate the bean jndi name. The default string
	 * "/remote" will be suffixed to the bean jndi name to get the jndi name of
	 * its remote interface.
	 * 
	 * @param writeJndiNameToSystemOut
	 *            Should the calculated jndi name be written to system out?
	 */
	public JndiEjbUtil(boolean writeJndiNameToSystemOut) {
		this(writeJndiNameToSystemOut, false);
	}

	/**
	 * Use this if you want to use the defaults to calculate the bean jndi name.
	 * The default string "Impl" will be suffixed to the name of the remote
	 * interface type name to calculate the bean jndi name. The default string
	 * "/remote" will be suffixed to the bean jndi name to get the jndi name of
	 * its remote interface.
	 * 
	 * @param writeJndiNameToSystemOut
	 *            Should the calculated jndi name be written to system out?
	 * @param propriotaryJndiNames
	 *            use propriotaryJndiNames (JBoss 4.x - 5.x)
	 */
	public JndiEjbUtil(boolean writeJndiNameToSystemOut,
			boolean propriotaryJndiNames) {
		this(DEFAULT_IMPLEMENTATION_CLASS_SUFFIX,
				DEFAULT_REMOTE_INTERFACE_SUFFIX, writeJndiNameToSystemOut,
				propriotaryJndiNames);
	}

	/**
	 * Use this if you want to use the defaults to calculate the bean jndi name.
	 * The default string "Impl" will be suffixed to the name of the remote
	 * interface type name to calculate the bean jndi name. The default string
	 * "/remote" will be suffixed to the bean jndi name to get the jndi name of
	 * its remote interface. The calculated jndi name will not be written to
	 * System.out.
	 */
	public JndiEjbUtil() {
		this(false);
	}

	/**
	 * Set whether the the calculated jndi name be written to system out
	 * (default is false).
	 */
	public void setWriteJndiNameToSystemOut(boolean writeJndiNameToSystemOut) {
		this.writeJndiNameToSystemOut = writeJndiNameToSystemOut;
	}

	/**
	 * The use of this method requires that jndi.properties is on the classpath
	 * in the project.
	 * 
	 * @param <T>
	 *            The remote interface type of the bean to lookup.
	 * @param remoteInterfaceType
	 *            The Class-object representing the remote interface type of the
	 *            bean to lookup.
	 * @return A remote reference to the bean.
	 * @throws RuntimeException
	 *             If the JNDI lookup results in a NamingExceptions, the
	 *             exception will be wrapped in a RuntimeException.
	 * @throws ClassCastException
	 *             If the remote reference does not implement the expected
	 *             interface.
	 */
	public <T> T lookupRemoteEjbInterface(Class<T> remoteInterfaceType)
			throws RuntimeException, ClassCastException {

		String jndiName = getJBoss7JndiName(remoteInterfaceType);
		if (propriotaryJndiNames) {
			jndiName = getJBossOldJndiName(remoteInterfaceType);
		}

		debug(remoteInterfaceType, jndiName);

		return lookupRemoteEJB(jndiName);
	}

	private void debug(final Class<?> remoteInterfaceType, String jndiName) {
		if (this.writeJndiNameToSystemOut) {
			String msg = "The interface ["
					+ remoteInterfaceType.getSimpleName() + "]";
			msg += " is being looked up using  [" + jndiName + "].";
			System.out.println(msg);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> T lookupRemoteEJB(final String jndiName) {
		
		final Properties jndiProperties = new Properties();
		if(propriotaryJndiNames){
			// jboss 4.x - 5.x
			jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
			jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
			jndiProperties.put(Context.PROVIDER_URL, "localhost:1099");
		}else{
			// jboss 7.x
			jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		}
		
		
		InitialContext context = null;
		Object remoteInterfaceAsObject;
		try {
			context = new InitialContext(jndiProperties);
			remoteInterfaceAsObject = context.lookup(jndiName);
		} catch (NamingException e) {
			String msg = "Failed to lookup remote interface of bean with JNDI name ["
					+ jndiName + "]";
			throw new RuntimeException(msg, e);
		} finally {
			if (context != null) {
				try {
					context.close();
				} catch (NamingException e) {
					// Ignore
				}
			}
		}
		// May throw ClassCastException
		T remoteInterface = (T) remoteInterfaceAsObject;
		return remoteInterface;

	}

	private String getJBossOldJndiName(Class<?> remoteInterfaceType) {
		String interfaceName = remoteInterfaceType.getSimpleName();
		String implementationClassSuffix = this.implementationClassSuffix;
		String remoteIntefaceSuffix = this.remoteInterfaceSuffix;
		return interfaceName + implementationClassSuffix + remoteIntefaceSuffix;

	}

	private String getJBoss7JndiName(Class<?> remoteInterfaceType) {
		// The app name is the application name of the deployed EJBs. This is
		// typically the ear name without the .ear suffix. However, the
		// application name could be overridden in the application.xml of the
		// EJB deployment on the server.
		// Since we haven't deployed the application as a .ear, the app name for
		// us will be an empty string
		final String appName = "";
		// This is the module name of the deployed EJBs on the server. This is
		// typically the jar name of the
		// EJB deployment, without the .jar suffix, but can be overridden via
		// the ejb-jar.xml
		// In this course we use the ejb module JpaTestEjbJar
		final String moduleName = "JpaTestEjbJar";
		// AS7 allows each deployment to have an (optional) distinct name. We
		// haven't specified a distinct name for
		// our EJB deployment, so this is an empty string
		final String distinctName = "";
		// The EJB name which by default is the simple class name of the bean
		// implementation class
		final String beanName = remoteInterfaceType.getSimpleName()
				+ DEFAULT_IMPLEMENTATION_CLASS_SUFFIX;
		// the remote view fully qualified class name
		final String viewClassName = remoteInterfaceType.getName();
		// let's do the lookup
		return "ejb:" + appName + "/" + moduleName + "/" + distinctName + "/"
				+ beanName + "!" + viewClassName;
	}
}
