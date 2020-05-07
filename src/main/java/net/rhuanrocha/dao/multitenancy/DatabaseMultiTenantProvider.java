package net.rhuanrocha.dao.multitenancy;

import org.hibernate.HibernateException;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseMultiTenantProvider implements MultiTenantConnectionProvider, ServiceRegistryAwareService{
    private static final long serialVersionUID = 1L;
    private static final String TENANT_SUPPORTED = "DATABASE";
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
        return connection;

    }
    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {

        final Context init;
        //Just use the multitenancy if the hibernate.multiTenancy == DATABASE
        if(TENANT_SUPPORTED.equals(typeTenancy)) {
            try {
                init = new InitialContext();
                dataSource = (DataSource) init.lookup("java:/jdbc/" + tenantIdentifier);
            } catch (NamingException e) {
                throw new HibernateException("Error trying to get datasource ['java:/jdbc/" + tenantIdentifier + "']", e);
            }
        }

        return dataSource.getConnection();

    }


    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }


    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        releaseAnyConnection(connection);
    }

}
