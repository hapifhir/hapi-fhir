package ca.uhn.fhir.cr.config;


import org.cqframework.cql.cql2elm.CqlTranslatorOptions;
import org.opencds.cqf.cql.evaluator.CqlOptions;
import org.opencds.cqf.cql.evaluator.engine.CqlEngineOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hapi.fhir.cql")
public class CqlProperties {

	private boolean enabled = true;
	private boolean useEmbeddedLibraries = true;

	private CqlEngineOptions cqlEngineOptions = CqlEngineOptions.defaultOptions();
	private CqlTranslatorOptions cqlTranslatorOptions = CqlTranslatorOptions.defaultOptions();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean useEmbeddedLibraries() {
		return this.useEmbeddedLibraries;
	}

	public void setUseEmbeddedLibraries(boolean useEmbeddedLibraries) {
		this.useEmbeddedLibraries = useEmbeddedLibraries;
	}

	public CqlEngineOptions getEngine() {
		return this.cqlEngineOptions;
	}

	public void setEngine(CqlEngineOptions engine) {
		this.cqlEngineOptions = engine;
	}

	public CqlTranslatorOptions getTranslator() {
		return this.cqlTranslatorOptions;
	}

	public void setTranslator(CqlTranslatorOptions translator) {
		this.cqlTranslatorOptions = translator;
	}

	public CqlOptions getOptions() {
		CqlOptions cqlOptions = new CqlOptions();
		cqlOptions.setUseEmbeddedLibraries(this.useEmbeddedLibraries());
		cqlOptions.setCqlEngineOptions(this.getEngine());
		cqlOptions.setCqlTranslatorOptions(this.getTranslator());
		return cqlOptions;
	}
}
