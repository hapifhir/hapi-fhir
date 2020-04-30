package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchUtilitiesImpl {

	private static final Logger ourLog = LoggerFactory.getLogger(SearchUtilitiesImpl.class);

	private static final String WARNING_MULTIPLE_METADATA_RESOURCES_WITH_UNIQUE_URL =
		"Expected 1 MetadataResource with globally unique URL {}, found {}. Will not attempt to update resource.";
	private static final String WARNING_MULTIPLE_NAMINGSYSTEM_RESOURCES_WITH_UNIQUE_ID =
		"Expected 1 NamingSystem with unique ID {}, found {}. Will not attempt to update resource.";
	private static final String WARNING_MULTIPLE_SUBSCRIPTION_RESOURCES_WITH_ID =
		"Expected 1 Subscription with ID {}, found {}. Will not attempt to update resource.";

	public static SearchUtilities forR5() {
		return new SearchUtilities() {
			@Override
			public SearchParameterMap createSearchParameterMapFor(IBaseResource resource) {
				if (resource instanceof org.hl7.fhir.r5.model.NamingSystem) {
					org.hl7.fhir.r5.model.NamingSystem r = (org.hl7.fhir.r5.model.NamingSystem) resource;
					if (!r.hasUniqueId()) { return null; }
					return new SearchParameterMap().add("value",
						new StringParam(r.getUniqueIdFirstRep().getValue()).setExact(true));
				} else if (resource instanceof org.hl7.fhir.r5.model.Subscription) {
					org.hl7.fhir.r5.model.Subscription s = (org.hl7.fhir.r5.model.Subscription) resource;
					return new SearchParameterMap().add("_id", new TokenParam(s.getId()));
				} else {
					// Resource is of type CodeSystem, ValueSet, StructureDefinition, ConceptMap or SearchParameter
					org.hl7.fhir.r5.model.MetadataResource r = (org.hl7.fhir.r5.model.MetadataResource) resource;
					return new SearchParameterMap().add("url", new UriParam(r.getUrl()));
				}
			}

			@Override
			public IBaseResource verifySearchResultFor(IBaseResource resource, IBundleProvider searchResult) {
				if (resource instanceof org.hl7.fhir.r5.model.NamingSystem) {
					if (searchResult.size() > 1) {
						org.hl7.fhir.r5.model.NamingSystem r = (org.hl7.fhir.r5.model.NamingSystem) resource;
						ourLog.warn(WARNING_MULTIPLE_NAMINGSYSTEM_RESOURCES_WITH_UNIQUE_ID,
							r.getUniqueIdFirstRep().getValue(), searchResult.size());
						return null;
					}
					return getFirstResourceFrom(searchResult);
				} else if (resource instanceof org.hl7.fhir.r5.model.Subscription) {
					if (searchResult.size() > 1) {
						org.hl7.fhir.r5.model.Subscription s = (org.hl7.fhir.r5.model.Subscription) resource;
						ourLog.warn(WARNING_MULTIPLE_SUBSCRIPTION_RESOURCES_WITH_ID, s.getId(), searchResult.size());
						return null;
					}
					return getFirstResourceFrom(searchResult);
				} else {
					// Resource is of type CodeSystem, ValueSet, StructureDefinition, ConceptMap or SearchParameter
					if (searchResult.size() > 1) {
						org.hl7.fhir.r5.model.MetadataResource r = (org.hl7.fhir.r5.model.MetadataResource) resource;
						ourLog.warn(WARNING_MULTIPLE_METADATA_RESOURCES_WITH_UNIQUE_URL, r.getUrl(), searchResult.size());
						return null;
					}
					return getFirstResourceFrom(searchResult);
				}
			}
		};
	}

	public static SearchUtilities forR4() {
		return new SearchUtilities() {
			@Override
			public SearchParameterMap createSearchParameterMapFor(IBaseResource resource) {
				if (resource instanceof org.hl7.fhir.r4.model.NamingSystem) {
					org.hl7.fhir.r4.model.NamingSystem r = (org.hl7.fhir.r4.model.NamingSystem) resource;
					if (!r.hasUniqueId()) { return null; }
					return new SearchParameterMap().add("value",
						new StringParam(r.getUniqueIdFirstRep().getValue()).setExact(true));
				} else if (resource instanceof org.hl7.fhir.r4.model.Subscription) {
					org.hl7.fhir.r4.model.Subscription s = (org.hl7.fhir.r4.model.Subscription) resource;
					return new SearchParameterMap().add("_id", new TokenParam(s.getId()));
				} else {
					// Resource is of type CodeSystem, ValueSet, StructureDefinition, ConceptMap or SearchParameter
					org.hl7.fhir.r4.model.MetadataResource r = (org.hl7.fhir.r4.model.MetadataResource) resource;
					return new SearchParameterMap().add("url", new UriParam(r.getUrl()));
				}
			}

			@Override
			public IBaseResource verifySearchResultFor(IBaseResource resource, IBundleProvider searchResult) {
				if (resource instanceof org.hl7.fhir.r4.model.NamingSystem) {
					if (searchResult.size() > 1) {
						org.hl7.fhir.r4.model.NamingSystem r = (org.hl7.fhir.r4.model.NamingSystem) resource;
						ourLog.warn(WARNING_MULTIPLE_NAMINGSYSTEM_RESOURCES_WITH_UNIQUE_ID,
							r.getUniqueIdFirstRep().getValue(), searchResult.size());
						return null;
					}
					return getFirstResourceFrom(searchResult);
				} else if (resource instanceof org.hl7.fhir.r4.model.Subscription) {
					if (searchResult.size() > 1) {
						org.hl7.fhir.r4.model.Subscription s = (org.hl7.fhir.r4.model.Subscription) resource;
						ourLog.warn(WARNING_MULTIPLE_SUBSCRIPTION_RESOURCES_WITH_ID, s.getId(), searchResult.size());
						return null;
					}
					return getFirstResourceFrom(searchResult);
				} else {
					// Resource is of type CodeSystem, ValueSet, StructureDefinition, ConceptMap or SearchParameter
					if (searchResult.size() > 1) {
						org.hl7.fhir.r4.model.MetadataResource r = (org.hl7.fhir.r4.model.MetadataResource) resource;
						ourLog.warn(WARNING_MULTIPLE_METADATA_RESOURCES_WITH_UNIQUE_URL, r.getUrl(), searchResult.size());
						return null;
					}
					return getFirstResourceFrom(searchResult);
				}
			}
		};
	}

	public static SearchUtilities forDstu3() {
		return new SearchUtilities() {
			@Override
			public SearchParameterMap createSearchParameterMapFor(IBaseResource resource) {
				if (resource instanceof org.hl7.fhir.dstu3.model.NamingSystem) {
					org.hl7.fhir.dstu3.model.NamingSystem r = (org.hl7.fhir.dstu3.model.NamingSystem) resource;
					if (!r.hasUniqueId()) { return null; }
					return new SearchParameterMap().add("value",
						new StringParam(r.getUniqueIdFirstRep().getValue()).setExact(true));
				} else if (resource instanceof org.hl7.fhir.dstu3.model.Subscription) {
					org.hl7.fhir.dstu3.model.Subscription s = (org.hl7.fhir.dstu3.model.Subscription) resource;
					return new SearchParameterMap().add("_id", new TokenParam(s.getId()));
				} else {
					// Resource is of type CodeSystem, ValueSet, StructureDefinition, ConceptMap or SearchParameter
					org.hl7.fhir.dstu3.model.MetadataResource r = (org.hl7.fhir.dstu3.model.MetadataResource) resource;
					return new SearchParameterMap().add("url", new UriParam(r.getUrl()));
				}
			}

			@Override
			public IBaseResource verifySearchResultFor(IBaseResource resource, IBundleProvider searchResult) {
				if (resource instanceof org.hl7.fhir.dstu3.model.NamingSystem) {
					if (searchResult.size() > 1) {
						org.hl7.fhir.dstu3.model.NamingSystem r = (org.hl7.fhir.dstu3.model.NamingSystem) resource;
						ourLog.warn(WARNING_MULTIPLE_NAMINGSYSTEM_RESOURCES_WITH_UNIQUE_ID,
							r.getUniqueIdFirstRep().getValue(), searchResult.size());
						return null;
					}
					return getFirstResourceFrom(searchResult);
				} else if (resource instanceof org.hl7.fhir.dstu3.model.Subscription) {
					if (searchResult.size() > 1) {
						org.hl7.fhir.dstu3.model.Subscription s = (org.hl7.fhir.dstu3.model.Subscription) resource;
						ourLog.warn(WARNING_MULTIPLE_SUBSCRIPTION_RESOURCES_WITH_ID, s.getId(), searchResult.size());
						return null;
					}
					return getFirstResourceFrom(searchResult);
				} else {
					// Resource is of type CodeSystem, ValueSet, StructureDefinition, ConceptMap or SearchParameter
					if (searchResult.size() > 1) {
						org.hl7.fhir.dstu3.model.MetadataResource r = (org.hl7.fhir.dstu3.model.MetadataResource) resource;
						ourLog.warn(WARNING_MULTIPLE_METADATA_RESOURCES_WITH_UNIQUE_URL, r.getUrl(), searchResult.size());
						return null;
					}
					return getFirstResourceFrom(searchResult);
				}
			}
		};
	}

	private static IBaseResource getFirstResourceFrom(IBundleProvider searchResult) {
		return searchResult.getResources(0, 0).get(0);
	}
}
