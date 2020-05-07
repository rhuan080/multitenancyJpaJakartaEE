package net.rhuanrocha.dao.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DatabaseTenantResolver extends MuiltitenancyResolver {

    private Map<String, String> userDatasourceMap;

    public DatabaseTenantResolver(){
        userDatasourceMap = new HashMap();
        userDatasourceMap.put("default", "MyDataSource");
        userDatasourceMap.put("america", "AmericaDB");
        userDatasourceMap.put("europa", "EuropaDB");
        userDatasourceMap.put("asia", "AsiaDB");
    }

    @Override
    public String resolveCurrentTenantIdentifier() {


        if(this.tenantIdentifier != null
                && userDatasourceMap.containsKey(this.tenantIdentifier)){
            return userDatasourceMap.get(this.tenantIdentifier);
        }

        return userDatasourceMap.get("default");

    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }

}
