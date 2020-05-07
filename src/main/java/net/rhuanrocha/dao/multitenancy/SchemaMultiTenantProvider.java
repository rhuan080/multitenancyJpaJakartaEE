package net.rhuanrocha.dao.multitenancy;

import org.hibernate.HibernateException;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SchemaMultiTenantProvider implements MultiTenantConnectionProvider, ServiceRegistryAwareService {

    private static final long serialVersionUID = 1L;
    private static final String TENANT_SUPPORTED = "SCHEMA";
    private DataSource dataSource;
    private String typeTenancy ;

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }
    @Override
    public void injectServices(ServiceRegistryImplementor serviceRegistry) {

        typeTenancy = (String) ((ConfigurationService)serviceRegistry
                .getService(ConfigurationService.class))
                .getSettings().get("hibernate.multiTenancy");

        dataSource = (DataSource) ((ConfigurationService)serviceRegistry
                .getService(ConfigurationService.class))
                .getSettings().get("hibernate.connection.datasource");


    }
    @SuppressWarnings("rawtypes")
    @Override
    public boolean isUnwrappableAs(Class clazz) {
        return false;
    }
    @Override
    public <T> T unwrap(Class<T> clazz) {
        return null;
    }
    @Override
    public Connection getAnyConnection() throws SQLException {
        final Connection connection = dataSource.getConnection();
        resetConnection(connection);// To make sure the connection start using schema/database default.
        return connection;
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {

        //Just use the multitenancy if the hibernate.multiTenancy == SCHEMA
        if(TENANT_SUPPORTED.equals(typeTenancy)) {
            try {
                final Connection connection = getAnyConnection();
                connection.createStatement().execute("SET SCHEMA '" + tenantIdentifier + "'");
                return connection;
            } catch (final SQLException e) {
                 throw new HibernateException("Error trying to alter schema [" + tenantIdentifier + "]", e);
            }
        }

        return getAnyConnection();

    }



    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        //As the Wildfly/JBoss has the Container-Managed Container change the SCHEMA in the end can be dangerous (SET SCHEMA 'public').
        //Thus it just close the connetion.
        connection.close();
    }

    private void resetConnection(Connection connection){
        if(TENANT_SUPPORTED.equals(typeTenancy)) {
            try {
                connection.createStatement().execute("SET SCHEMA 'public'");
            } catch (final SQLException e) {
                throw new HibernateException("Error trying to alter schema [public]", e);
            }
        }
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        releaseAnyConnection(connection);
    }

}
