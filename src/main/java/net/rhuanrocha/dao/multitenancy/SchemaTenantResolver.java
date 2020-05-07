package net.rhuanrocha.dao.multitenancy;

import java.util.HashMap;
import java.util.Map;

public class SchemaTenantResolver extends MuiltitenancyResolver {

    private Map<String, String> userDatasourceMap;

    public SchemaTenantResolver(){
        userDatasourceMap = new HashMap();
        userDatasourceMap.put("default", "public");
        userDatasourceMap.put("username1", "usernameone");
        userDatasourceMap.put("username2", "usernametwo");

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