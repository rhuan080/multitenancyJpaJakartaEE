package net.rhuanrocha.dao.multitenancy;

import org.hibernate.HibernateException;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.service.spi.Stoppable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MultiTenantProvider implements MultiTenantConnectionProvider, ServiceRegistryAwareService, Stoppable {
    private static final long serialVersionUID = 1L;
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
        if(dataSource != null) {
            final Connection connection = dataSource.getConnection();
            resetConnection(connection);// To make sure the connection start using schema/database default.
            return connection;
        }
        return null;

    }
    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {

        try {
            if("DATABASE".equals(typeTenancy)){
                return getConnectionMultitenancyDataBase(tenantIdentifier);
            }
            else{
              return getConnectionMultitenancySchema(tenantIdentifier);
            }

        } catch (final SQLException e) {
            if("DATABASE".equals(typeTenancy)){
                throw new HibernateException("Error trying to alter database [" + tenantIdentifier + "]", e);
            }
            else {
                throw new HibernateException("Error trying to alter schema [" + tenantIdentifier + "]", e);
            }
        }

    }

    private Connection getConnectionMultitenancySchema(String tenantIdentifier) throws SQLException {
        final Connection connection = getAnyConnection();
        connection.createStatement().execute("SET SCHEMA '" + tenantIdentifier + "'");
        return connection;
    }

    private Connection getConnectionMultitenancyDataBase(String tenantIdentifier) throws SQLException {
        final Context init;
        try {
            init = new InitialContext();
            dataSource = (DataSource) init.lookup("java:/jdbc/"+tenantIdentifier);
        } catch (NamingException e) {
            throw new HibernateException("Error trying to get datasource ['java:/jdbc/"+tenantIdentifier+"']", e);
        }

        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        //As the Wildfly/JBoss use the Container-Managed Container the schema can not released here (SET SCHEMA 'public')
        connection.close();
    }

    private void resetConnection(Connection connection){
        try {
            if("DATABASE".equals(typeTenancy)){
                //connection.createStatement().execute("USE 'testdb'");
            }
            else {
                connection.createStatement().execute("SET SCHEMA 'public'");
            }
        } catch (final SQLException e) {
            if("DATABASE".equals(typeTenancy)){
                throw new HibernateException("Error trying to alter database ['public']", e);
            }
            else {
                throw new HibernateException("Error trying to alter schema ['public']", e);
            }
        }
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        releaseAnyConnection(connection);
    }

    @Override
    public void stop() {


    }
}
