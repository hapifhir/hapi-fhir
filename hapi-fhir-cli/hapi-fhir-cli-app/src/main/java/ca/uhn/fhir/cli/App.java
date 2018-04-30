package ca.uhn.fhir.cli;

import ca.uhn.fhir.util.VersionUtil;

public class App extends BaseApp {

	@Override
	protected String provideCommandName() {
		return "hapi-fhir-cli";
	}

	@Override
	protected String provideProductName() {
		return "HAPI FHIR";
	}

	@Override
	protected String provideProductVersion() {
		return VersionUtil.getVersion();
	}

	public static void main(String[] theArgs) {
		new App().run(theArgs);
	}
}
