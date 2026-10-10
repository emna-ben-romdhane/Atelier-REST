package resources;

import entities.Etudiant;
import entities.EtudiantList;
import entities.Option;
import metiers.EtudiantBusiness;
import metiers.OptionBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("etudiants")
public class EtudiantRessource {

    private static final EtudiantBusiness business = new EtudiantBusiness();
    private static final OptionBusiness optionBusiness = new OptionBusiness();

    private static Response notFound() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // B.1 : POST /etudiants
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEtudiant(Etudiant e) {
        if (e == null || e.getOption() == null) return notFound();
        // addEtudiant retourne false si l'option n'existe pas
        if (!business.addEtudiant(e)) return notFound();
        return Response.ok(e).build();
    }

    // B.2 : GET /etudiants
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEtudiants() {
        return Response.ok(business.getAllEtudiants()).build();
    }

    // B.6 : GET /etudiants/option?codeOption=1  (réponse XML)
    @GET
    @Path("option")
    @Produces(MediaType.APPLICATION_XML)
    public Response getEtudiantsByOption(@QueryParam("codeOption") int codeOption) {
        Option option = optionBusiness.getOptionByCode(codeOption);
        if (option == null) return notFound();
        EtudiantList liste = new EtudiantList(business.getEtudiantsByOption(option));
        return Response.ok(liste).build();
    }

    // B.3 : GET /etudiants/{id}
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEtudiant(@PathParam("id") String id) {
        Etudiant e = business.getEtudiantByIdentifiant(id);
        if (e == null) return notFound();
        return Response.ok(e).build();
    }

    // B.4 : DELETE /etudiants/{id}
    @DELETE
    @Path("{id}")
    public Response deleteEtudiant(@PathParam("id") String id) {
        if (business.deleteEtudiant(id)) return Response.noContent().build();
        return notFound();
    }

    // B.5 : PUT /etudiants/{id}
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEtudiant(@PathParam("id") String id, Etudiant e) {
        if (e == null || e.getOption() == null) return notFound();
        Option o = optionBusiness.getOptionByCode(e.getOption().getCodeOption());
        if (o == null) return notFound();
        e.setOption(o);
        e.setIdentifiant(id);
        if (business.updateEtudiant(id, e)) return Response.ok(e).build();
        return notFound();
    }
}