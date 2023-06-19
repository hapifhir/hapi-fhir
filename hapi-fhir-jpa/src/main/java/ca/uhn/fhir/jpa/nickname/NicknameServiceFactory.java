package ca.uhn.fhir.jpa.nickname;

import org.springframework.core.io.Resource;

public class NicknameServiceFactory {

	private INicknameSvc myNicknameSvc;

	private Resource myCustomNicknames;

	public NicknameServiceFactory() {

	}

	public INicknameSvc getNicknameSvc() {
		ensureNicknameSvc();
		return myNicknameSvc;
	}

	public void setCustomNicknameResource(Resource theNicknameResource) {
		assert myNicknameSvc == null : "Cannot set custom nicknames for already initialized nickname svc";
		myCustomNicknames = theNicknameResource;
	}

	private void ensureNicknameSvc() {
		if (myNicknameSvc == null) {
			myNicknameSvc = new NicknameSvc();
			if (myCustomNicknames != null) {
				myNicknameSvc.setNicknameResource(myCustomNicknames);
			}
		}
	}
}
