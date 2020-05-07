package net.rhuanrocha.dao.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public abstract class MuiltitenancyResolver implements CurrentTenantIdentifierResolver {

    protected String tenantIdentifier;


    public void setTenantIdentifier(String tenantIdentifier) {
        this.tenantIdentifier = tenantIdentifier;
    }
}
