package net.rhuanrocha.dao;

import net.rhuanrocha.entity.Job;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class JobDao extends Dao<Job> {

    public List<Job> findAll(String multitenancyIdentifier){

        return getEntityManager(multitenancyIdentifier).createQuery("select j from Job j", Job.class)
                .getResultList();

    }

}
