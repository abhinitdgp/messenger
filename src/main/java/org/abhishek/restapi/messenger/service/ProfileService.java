package org.abhishek.restapi.messenger.service;

import org.abhishek.restapi.messenger.database.DatabaseClass;
import org.abhishek.restapi.messenger.model.Profile;

import java.util.*;

public class ProfileService {
	DatabaseClass db;

	public ProfileService() {
		db = new DatabaseClass();
	}

	public List<Profile> getAllProfiles() {
		List<Profile> profiles = new ArrayList<>();
		profiles = db.getAllProfiles();
		return profiles;
	}

	public Profile getProfileByName(String name) {
		Profile profile = db.getProfileByName(name);
		return profile;
	}

	public Profile addProfile(Profile profile) {
		Profile pro = db.addProfile(profile);
		return pro;
	}

	public boolean updateProfile(Profile profile) {
		boolean profileUpdated = db.updateProfile(profile);
		return profileUpdated;
	}

	public boolean deleteProfile(String name) {
		boolean profileDeleted = db.deleteProfile(name);
		return profileDeleted;
	}

}
