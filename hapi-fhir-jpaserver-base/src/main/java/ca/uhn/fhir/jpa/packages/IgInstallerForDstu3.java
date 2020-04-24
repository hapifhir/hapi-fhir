package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoStructureDefinition;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.utilities.cache.NpmPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class IgInstallerForDstu3 {

	private static final Logger ourLog = LoggerFactory.getLogger(IgInstallerForDstu3.class);

	@Autowired
	private DaoConfig daoConfig;
	@Autowired
	private IFhirResourceDaoStructureDefinition<StructureDefinition> resourceDaoStructureDefinition;
	@Autowired
	private FhirContext fhirContext;

	@EventListener(ContextStartedEvent.class)
	public void installIg() {
		String url = daoConfig.getMyImplementationGuideURL();
		if (url == null) {
			ourLog.debug("No implementation guide URL set.");
			return;
		}
		NpmPackage ig;
		if (url.startsWith("http")) {
			try {
				ig = loadIgFromURL(url);
			} catch (IOException e) {
				ourLog.error("Error fetching implementation guide from URL " + url, e); // todo : error handling
				return;
			}
		} else {

			// todo : load from package cache/ Simplifier repo using PackageCacheManager
			ig = null;

		}

		// generate snapshots
		for (StructureDefinition sd : getStructureDefinitions(ig)) {
			// StructureDefinition snapshot = resourceDaoStructureDefinition.generateSnapshot(sd, null, null, null);

		}

		// install to db
	}

	@VisibleForTesting
	public Collection<StructureDefinition> getStructureDefinitions(NpmPackage ig) {
		if (!ig.getFolders().containsKey("package")) {
			return Collections.EMPTY_LIST;
		}
		return ig.getFolders().get("package").getContent().entrySet().stream()
			.filter(e -> e.getKey().startsWith("StructureDefinition-"))
			.map(e -> fhirContext.newJsonParser()
				.parseResource(StructureDefinition.class, new String(e.getValue())))
			.collect(Collectors.toList());
	}

	@VisibleForTesting
	public NpmPackage loadIgFromURL(String url) throws IOException {
		return NpmPackage.fromPackage(toInputStream(url));
	}

	private InputStream toInputStream(String url) throws IOException {
		URL u = new URL(url);
		URLConnection c = u.openConnection();
		return c.getInputStream();
	}
}
