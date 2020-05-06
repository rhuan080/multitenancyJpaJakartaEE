package net.rhuanrocha.dao;

import net.rhuanrocha.dao.multitenancy.MuiltitenancyResolver;
import net.rhuanrocha.entity.Entity;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.jpa.HibernateEntityManagerFactory;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.Optional;

public abstract class Dao <T extends Entity>{


    @PersistenceUnit
    protected EntityManagerFactory emf;


    protected EntityManager getEntityManager(String multitenancyIdentifier){

        final MuiltitenancyResolver schemaResolver = (MuiltitenancyResolver) ((SessionFactoryImplementor) emf).getCurrentTenantIdentifierResolver();
        schemaResolver.setTenantIdentifier(multitenancyIdentifier);

        return emf.createEntityManager();
    }

    public Optional<T> findById(T entity, String multitenancyIdentifier){
        //emf.get

        return (Optional<T>) Optional
                .ofNullable( getEntityManager(multitenancyIdentifier).find( entity.getClass(), entity.getId() ) );

    }

    public T save (T entity, String multitenancyIdentifier){

        getEntityManager(multitenancyIdentifier).persist( entity );
        return entity;

    }

    public T update (T entity, String multitenancyIdentifier){

        getEntityManager(multitenancyIdentifier).merge( entity );
        return entity;

    }

    public void remove (T entity, String multitenancyIdentifier){

        getEntityManager(multitenancyIdentifier).remove( entity );

    }
}
