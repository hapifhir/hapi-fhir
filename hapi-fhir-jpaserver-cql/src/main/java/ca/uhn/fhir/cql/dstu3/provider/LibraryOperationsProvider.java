package ca.uhn.fhir.cql.dstu3.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cql.common.provider.LibraryResolutionProvider;
import ca.uhn.fhir.cql.common.provider.LibrarySourceProvider;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Library;
import org.hl7.fhir.dstu3.model.Narrative;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.tooling.library.stu3.NarrativeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class LibraryOperationsProvider implements LibraryResolutionProvider<Library> {

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IFhirResourceDao<Library> myLibraryDao;
	@Autowired
	private NarrativeProvider narrativeProvider;

	private final DataRequirementsProvider dataRequirementsProvider = new DataRequirementsProvider();

	private ModelManager getModelManager() {
		return new ModelManager();
	}

	private LibraryManager getLibraryManager(ModelManager modelManager) {
		LibraryManager libraryManager = new LibraryManager(modelManager);
		libraryManager.getLibrarySourceLoader().clearProviders();
		libraryManager.getLibrarySourceLoader().registerProvider(getLibrarySourceProvider());

		return libraryManager;
	}

	private LibrarySourceProvider<Library, Attachment> librarySourceProvider;

	private LibrarySourceProvider<Library, Attachment> getLibrarySourceProvider() {
		if (librarySourceProvider == null) {
			librarySourceProvider = new LibrarySourceProvider<Library, Attachment>(this.getLibraryResolutionProvider(),
				x -> x.getContent(), x -> x.getContentType(), x -> x.getData());
		}
		return librarySourceProvider;
	}

	private LibraryResolutionProvider<Library> getLibraryResolutionProvider() {
		return this;
	}

	@Operation(name = "$refresh-generated-content", type = Library.class)
	public MethodOutcome refreshGeneratedContent(RequestDetails theRequestDetails,
																@IdParam IdType theId) {
		Library theResource = myLibraryDao.read(theId);
		// this.formatCql(theResource);

		ModelManager modelManager = this.getModelManager();
		LibraryManager libraryManager = this.getLibraryManager(modelManager);

		CqlTranslator translator = this.dataRequirementsProvider.getTranslator(theResource, libraryManager,
			modelManager);
		if (translator.getErrors().size() > 0) {
			throw new RuntimeException("Errors during library compilation.");
		}

		this.dataRequirementsProvider.ensureElm(theResource, translator);
		this.dataRequirementsProvider.ensureRelatedArtifacts(theResource, translator, this);
		this.dataRequirementsProvider.ensureDataRequirements(theResource, translator);

		Narrative n = this.narrativeProvider.getNarrative(myFhirContext, theResource);
		theResource.setText(n);

		return myLibraryDao.update(theResource, theRequestDetails.getConditionalUrl(RestOperationTypeEnum.UPDATE), theRequestDetails);
	}

	@Operation(name = "$get-elm", idempotent = true, type = Library.class)
	public Parameters getElm(@IdParam IdType theId, @OperationParam(name = "format") String format) {
		Library theResource = myLibraryDao.read(theId);
		// this.formatCql(theResource);

		ModelManager modelManager = this.getModelManager();
		LibraryManager libraryManager = this.getLibraryManager(modelManager);

		String elm = "";
		CqlTranslator translator = this.dataRequirementsProvider.getTranslator(theResource, libraryManager,
			modelManager);
		if (translator != null) {
			if (format.equals("json")) {
				elm = translator.toJson();
			} else {
				elm = translator.toXml();
			}
		}
		Parameters p = new Parameters();
		p.addParameter().setValue(new StringType(elm));
		return p;
	}

	@Operation(name = "$get-narrative", idempotent = true, type = Library.class)
	public Parameters getNarrative(@IdParam IdType theId) {
		Library theResource = myLibraryDao.read(theId);
		Narrative n = this.narrativeProvider.getNarrative(myFhirContext, theResource);
		Parameters p = new Parameters();
		p.addParameter().setValue(new StringType(n.getDivAsString()));
		return p;
	}

	// TODO: Figure out if we should throw an exception or something here.
	@Override
	public void update(Library library) {
		myLibraryDao.update(library);
	}

	@Override
	public Library resolveLibraryById(String libraryId) {
		try {
			return myLibraryDao.read(new IdType(libraryId));
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format("Could not resolve library id %s", libraryId));
		}
	}

	@Override
	public Library resolveLibraryByCanonicalUrl(String url) {
		Objects.requireNonNull(url, "url must not be null");

		String[] parts = url.split("\\|");
		String resourceUrl = parts[0];
		String version = null;
		if (parts.length > 1) {
			version = parts[1];
		}

		SearchParameterMap map = new SearchParameterMap();
		map.add("url", new UriParam(resourceUrl));
		if (version != null) {
			map.add("version", new TokenParam(version));
		}

		ca.uhn.fhir.rest.api.server.IBundleProvider bundleProvider = myLibraryDao.search(map);

		if (bundleProvider.size() == 0) {
			return null;
		}
		List<IBaseResource> resourceList = bundleProvider.getResources(0, bundleProvider.size());
		return LibraryResolutionProvider.selectFromList(resolveLibraries(resourceList), version, x -> x.getVersion());
	}

	@Override
	public Library resolveLibraryByName(String libraryName, String libraryVersion) {
		Iterable<org.hl7.fhir.dstu3.model.Library> libraries = getLibrariesByName(libraryName);
		org.hl7.fhir.dstu3.model.Library library = LibraryResolutionProvider.selectFromList(libraries, libraryVersion,
			x -> x.getVersion());

		if (library == null) {
			throw new IllegalArgumentException(String.format("Could not resolve library name %s", libraryName));
		}

		return library;
	}

	private Iterable<org.hl7.fhir.dstu3.model.Library> getLibrariesByName(String name) {
		// Search for libraries by name
		SearchParameterMap map = new SearchParameterMap();
		map.add("name", new StringParam(name, true));
		ca.uhn.fhir.rest.api.server.IBundleProvider bundleProvider = myLibraryDao.search(map);

		if (bundleProvider.size() == 0) {
			return new ArrayList<>();
		}
		List<IBaseResource> resourceList = bundleProvider.getResources(0, bundleProvider.size());
		return resolveLibraries(resourceList);
	}

	private Iterable<org.hl7.fhir.dstu3.model.Library> resolveLibraries(List<IBaseResource> resourceList) {
		List<org.hl7.fhir.dstu3.model.Library> ret = new ArrayList<>();
		for (IBaseResource res : resourceList) {
			Class<?> clazz = res.getClass();
			ret.add((org.hl7.fhir.dstu3.model.Library) clazz.cast(res));
		}
		return ret;
	}
}
