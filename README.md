
# Multi-tenancy JPA and WildFLY
It is an example multi-tenancy with JPA running on Wildfly/JBoss EAP and connecting to a postgresql database. This example was tested in the Wildfly 18, but is suposed to work in the next versions. Some Hibenarte's features was used in this sample, then it is not vendor decouped as it is copled to Hibernate.

This example expose multi-tenancy by schema and database, and as the Wildfly works with datasources and already starts some connections to us, we'll apply the switch of connetion switching the datasource. Here I'll not explain the concepts and when use each of these multi-tenancy, but it will be directly example of implementation.

## Testing the Application

These are the steps to test this application:

 1. Create the databases, schemas and tables (a script is available here
    to help you in it).   
 2. Configure the  multi-tenancy inside the persistence.xml   
 3. Add the datasources used by application (MyDataSource,
    AmericaDB,EuropaDB and AsiaDB. Feel free to check it in
    `net.rhuanrocha.dao.multitenancy.DatabaseTenantResolver`)    
 4. Compile and deploy the application
 5. Test it via HTTP request using REST.

### Step 1

Execute the script in `$APP_HOME/sqlscripts/script.sql`. It is just a suggestion of scenario, but you can create your own scenarios, just make sure the application is able to execute the scenario.

### Step 2
Consigure the persistence.xml inside the `META-INF` with the multi-tenancy you want to. 

#### By schema

    <persistence>  
	     <persistence-unit name="javaee8">  
      
	     <jta-data-source>jdbc/MyDataSource</jta-data-source>  
	     <properties> 
		     <property name="javax.persistence.schema-generation.database.action" value="none" />  
		     <property name="hibernate.dialect" value="org.hibernate.dialect.PostgresPlusDialect"/>  
		     <property name="hibernate.multiTenancy" value="SCHEMA"/>  
		     <property name="hibernate.tenant_identifier_resolver" value="net.rhuanrocha.dao.multitenancy.SchemaTenantResolver"/>  
		     <property name="hibernate.multi_tenant_connection_provider" value="net.rhuanrocha.dao.multitenancy.SchemaMultiTenantProvider"/>  
	     </properties>  
     </persistence-unit></persistence>

#### By database
    <persistence>  
	     <persistence-unit name="javaee8">  
      
	     <jta-data-source>jdbc/MyDataSource</jta-data-source>  
	     <properties> 
		     <property name="javax.persistence.schema-generation.database.action" value="none" />  
		     <property name="hibernate.dialect" value="org.hibernate.dialect.PostgresPlusDialect"/>  
		     <property name="hibernate.multiTenancy" value="DATABASE"/>  
		     <property name="hibernate.tenant_identifier_resolver" value="net.rhuanrocha.dao.multitenancy.DatabaseTenantResolver"/>  
		     <property name="hibernate.multi_tenant_connection_provider" value="net.rhuanrocha.dao.multitenancy.DatabaseMultiTenantProvider"/>  
	     </properties>  
     </persistence-unit></persistence>

Note that the you should define the `hibernate.multiTenancy`, `hibernate.tenant_identifier_resolver` and `hibernate.multi_tenant_connection_provider` according to multi-tenancy you want. 

### Step 3
Connecting to Wildfly via CLI:

    $WILDFLY_HOME/bin/jboss-cli.sh --connect

Adding postgresql's driver:
    
    /subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql,driver-xa-datasource-class-name=org.postgresql.xa.PGXADataSource)

Adding the datasources:

    data-source add --name=MyDataSource --jndi-name=java:/jdbc/MyDataSource --driver-name=postgresql  --connection-url=jdbc:postgresql://localhost:5432/testdb --user-name=admin --password=admin --min-pool-size=10 --max-pool-size=20
    data-source add --name=AmericaDB --jndi-name=java:/jdbc/AmericaDB --driver-name=postgresql  --connection-url=jdbc:postgresql://localhost:5432/testdb2 --user-name=admin --password=admin --min-pool-size=10 --max-pool-size=20
    data-source add --name=EuropaDB --jndi-name=java:/jdbc/EuropaDB --driver-name=postgresql  --connection-url=jdbc:postgresql://localhost:5432/testdb3 --user-name=admin --password=admin --min-pool-size=10 --max-pool-size=20
    data-source add --name=AsiaDB --jndi-name=java:/jdbc/AsiaDB --driver-name=postgresql  --connection-url=jdbc:postgresql://localhost:5432/testdb4 --user-name=admin --password=admin --min-pool-size=10 --max-pool-size=20

### Step 4

Compile the application and deploy in the Wildfly:

```
mvn clean package
cp $APP_HOME/target/samplejpamultitenancy-1.0-SNAPSHOT.war .$HOME_WILDFLY/standalone/deployments/
```

### Step 5

Test the application sending a POST request to insert data and GET request to read data.

POST:
 curl -X POST "http://localhost:8080/samplejpamultitenancy-1.0-SNAPSHOT/resources/job?multitenancyId=america" -v -d "companyName=MyCompany"

GET:
 curl  "http://localhost:8080/samplejpamultitenancy-1.0-SNAPSHOT/resources/job?multitenancyId=america"

Note the multitenancyId parameter that is used to switch the database.

To multi-tenancy by datasource check `net.rhuanrocha.dao.multitenancy.DatabaseTenantResolver` and `net.rhuanrocha.dao.multitenancy.DatabaseMultiTenantProvider`.

To multi-tenancy by schema check `net.rhuanrocha.dao.multitenancy.SchemaTenantResolver` and `net.rhuanrocha.dao.multitenancy.SchemaMultiTenantProvider`.
