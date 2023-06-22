package ca.uhn.fhir.jpa.nickname;

import org.springframework.core.io.Resource;

import java.util.Collection;
import java.util.List;

public interface INicknameSvc {

	/**
	 * Set a custom nickname resource to use.
	 * If not used, a default will be used instead.
	 */
	void setNicknameResource(Resource theNicknameResource);

	/**
	 * The number of nicknames in the nickname svc
	 */
	int size();

	/**
	 * If using a custom nickname resource,
	 * processing will keep track of any badly
	 * formatted rows.
	 * These badly formatted rows can be accessed with this api.
	 */
	List<String> getBadRows();

	/**
	 * Gets a list of nicknames for the provided name
	 * @param theName - the provided name
	 * @return - a collection of similar/nicknames
	 */
	Collection<String> getEquivalentNames(String theName);
}
