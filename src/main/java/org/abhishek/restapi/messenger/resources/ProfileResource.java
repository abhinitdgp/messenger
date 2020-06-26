package org.abhishek.restapi.messenger.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.*;

import org.abhishek.restapi.messenger.model.Profile;
import org.abhishek.restapi.messenger.service.ProfileService;

@Path("/profiles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProfileResource {
	private ProfileService pService;

	public ProfileResource() {
		pService = new ProfileService();
	}

	@GET
	public List<Profile> getProfiles() {
		return pService.getAllProfiles();
	}

	@GET
	@Path("/{profileName}")
	public Profile getProfileById(@PathParam("profileName") String name) {
		return pService.getProfileByName(name);
	}

	@DELETE
	@Path("/{profileName}")
	public boolean deleteProfile(@PathParam("profileName") String name) {
		return pService.deleteProfile(name);
	}

	@POST
	public Profile addProfile(Profile profile) {
		return pService.addProfile(profile);
	}

	@PUT
	@Path("/{profileName}")
	public boolean updateProfile(@PathParam("profileName") String name, Profile profile) {
		profile.setProfileName(name);
		return pService.updateProfile(profile);
	}

}
