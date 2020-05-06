package net.rhuanrocha.business;

import net.rhuanrocha.dao.JobDao;
import net.rhuanrocha.entity.Job;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class JobBusiness {

    @Inject
    private JobDao jobDao;

    public Optional<Job> findById( Job job, String multitenancyIdentifier){

        return jobDao.findById(job, multitenancyIdentifier);
    }

    public Optional<Job> findById( Long id, String multitenancyIdentifier){

        return jobDao.findById(Job.build(id),multitenancyIdentifier);
    }

    public List<Job> findAll(String multitenancyIdentifier){
        return jobDao.findAll(multitenancyIdentifier);
    }

    public Job save (Job job, String multitenancyIdentifier){

        return jobDao.save(job,multitenancyIdentifier);
    }

}
