package net.rhuanrocha.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Objects;

@javax.persistence.Entity
@Table(name="job")
public class Job extends net.rhuanrocha.entity.Entity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "company_name")
    private String companyName;



    @Override
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }



    public static Job build (Long id){

        Job job = new Job();
        job.setId( id );

        return job;
    }

    public static Job build(String companyName){

        Job job = new Job();
        job.setCompanyName( companyName );

        return job;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        net.rhuanrocha.entity.Job job = (net.rhuanrocha.entity.Job) o;
        return Objects.equals(id, job.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
