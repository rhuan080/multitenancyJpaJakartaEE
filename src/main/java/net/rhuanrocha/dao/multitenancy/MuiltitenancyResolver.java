package net.rhuanrocha.dao.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class MuiltitenancyResolver implements CurrentTenantIdentifierResolver {

    private String tenantIdentifier = "public";

    @Override
    public String resolveCurrentTenantIdentifier() {
        return tenantIdentifier;
    }
    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
    public void setTenantIdentifier(String tenantIdentifier) {
        this.tenantIdentifier = tenantIdentifier;
    }
}
