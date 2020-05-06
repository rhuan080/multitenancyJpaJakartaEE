package net.rhuanrocha.resource;


import net.rhuanrocha.business.JobBusiness;
import net.rhuanrocha.entity.Job;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.net.URI;


@Path("job")
public class JobResource {

    @Inject
    private JobBusiness jobBusiness;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("multitenancyId") String multitenancyIdentifier){

        return Response
                .ok(jobBusiness.findAll(multitenancyIdentifier))
                .build();
    }


    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id,@QueryParam("multitenancyId") String multitenancyIdentifier){

        return Response
                .ok(jobBusiness.findById(id, multitenancyIdentifier)
                        .orElseGet(null))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response save (@FormParam("companyName") @NotBlank String companyName,
                          @QueryParam("multitenancyId") String multitenancyIdentifier){

        Job job = jobBusiness.save( Job.build(companyName), multitenancyIdentifier);

        return Response
                .created(URI.create("job/"+ job.getId()))
                .build();

    }

}
