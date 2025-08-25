/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;
import java.util.Map;

/**
 * FHIR version independent representation of a Bundle Entry. This class can hold
 * Bundle Entry data from any FHIR version (DSTU3+) and convert it back to the
 * appropriate Bundle Entry type.
 */
// Created by Sonnet 4
public class CanonicalBundleEntry {

	private String myFullUrl;
	private IBaseResource myResource;

	// Request fields
	private String myRequestMethod;
	private String myRequestUrl;
	private String myRequestIfNoneMatch;
	private String myRequestIfModifiedSince;
	private String myRequestIfMatch;
	private String myRequestIfNoneExist;

	// Response fields
	private String myResponseStatus;
	private String myResponseLocation;
	private String myResponseEtag;
	private String myResponseLastModified;

	// Search fields
	private String mySearchMode;
	private String mySearchScore;

	// Link fields (less common but part of spec)
	private List<Map<String, String>> myLinks;
	private IBaseOperationOutcome myResponseOutcome;

	public CanonicalBundleEntry() {
		// Default constructor
	}

	public String getFullUrl() {
		return myFullUrl;
	}

	public void setFullUrl(String theFullUrl) {
		myFullUrl = theFullUrl;
	}

	public IBaseResource getResource() {
		return myResource;
	}

	public void setResource(IBaseResource theResource) {
		myResource = theResource;
	}

	public String getRequestMethod() {
		return myRequestMethod;
	}

	public void setRequestMethod(String theRequestMethod) {
		myRequestMethod = theRequestMethod;
	}

	public String getRequestUrl() {
		return myRequestUrl;
	}

	public void setRequestUrl(String theRequestUrl) {
		myRequestUrl = theRequestUrl;
	}

	public String getRequestIfNoneMatch() {
		return myRequestIfNoneMatch;
	}

	public void setRequestIfNoneMatch(String theRequestIfNoneMatch) {
		myRequestIfNoneMatch = theRequestIfNoneMatch;
	}

	public String getRequestIfModifiedSince() {
		return myRequestIfModifiedSince;
	}

	public void setRequestIfModifiedSince(String theRequestIfModifiedSince) {
		myRequestIfModifiedSince = theRequestIfModifiedSince;
	}

	public String getRequestIfMatch() {
		return myRequestIfMatch;
	}

	public void setRequestIfMatch(String theRequestIfMatch) {
		myRequestIfMatch = theRequestIfMatch;
	}

	public String getRequestIfNoneExist() {
		return myRequestIfNoneExist;
	}

	public void setRequestIfNoneExist(String theRequestIfNoneExist) {
		myRequestIfNoneExist = theRequestIfNoneExist;
	}

	public String getResponseStatus() {
		return myResponseStatus;
	}

	public void setResponseStatus(String theResponseStatus) {
		myResponseStatus = theResponseStatus;
	}

	public String getResponseLocation() {
		return myResponseLocation;
	}

	public void setResponseLocation(String theResponseLocation) {
		myResponseLocation = theResponseLocation;
	}

	public String getResponseEtag() {
		return myResponseEtag;
	}

	public void setResponseEtag(String theResponseEtag) {
		myResponseEtag = theResponseEtag;
	}

	public String getResponseLastModified() {
		return myResponseLastModified;
	}

	public void setResponseLastModified(String theResponseLastModified) {
		myResponseLastModified = theResponseLastModified;
	}

	public String getSearchMode() {
		return mySearchMode;
	}

	public void setSearchMode(String theSearchMode) {
		mySearchMode = theSearchMode;
	}

	public String getSearchScore() {
		return mySearchScore;
	}

	public void setSearchScore(String theSearchScore) {
		mySearchScore = theSearchScore;
	}

	public List<Map<String, String>> getLinks() {
		return myLinks;
	}

	public void setLinks(List<Map<String, String>> theLinks) {
		myLinks = theLinks;
	}

	public void setResponseOutcome(IBaseOperationOutcome theResponseOutcome) {
		myResponseOutcome = theResponseOutcome;
	}

	public IBaseOperationOutcome getResponseOutcome() {
		return myResponseOutcome;
	}

	/**
	 * Factory method to create a CanonicalBundleEntry from a Bundle Entry
	 * @param theFhirContext The FHIR context
	 * @param theEntry The Bundle Entry to convert
	 * @return A new CanonicalBundleEntry instance
	 */
	public static CanonicalBundleEntry fromBundleEntry(FhirContext theFhirContext, IBaseBackboneElement theEntry) {
		CanonicalBundleEntry retVal = new CanonicalBundleEntry();

		FhirTerser terser = theFhirContext.newTerser();

		// Extract fullUrl
		IPrimitiveType<?> fullUrlElement = terser.getSingleValueOrNull(theEntry, "fullUrl", IPrimitiveType.class);
		if (fullUrlElement != null) {
			retVal.setFullUrl(fullUrlElement.getValueAsString());
		}

		// Extract resource
		IBaseResource resource = terser.getSingleValueOrNull(theEntry, "resource", IBaseResource.class);
		if (resource != null) {
			retVal.setResource(resource);
		}

		// Extract request fields
		IBaseBackboneElement request = terser.getSingleValueOrNull(theEntry, "request", IBaseBackboneElement.class);
		if (request != null) {
			IPrimitiveType<?> method = terser.getSingleValueOrNull(request, "method", IPrimitiveType.class);
			if (method != null) {
				retVal.setRequestMethod(method.getValueAsString());
			}

			IPrimitiveType<?> url = terser.getSingleValueOrNull(request, "url", IPrimitiveType.class);
			if (url != null) {
				retVal.setRequestUrl(url.getValueAsString());
			}

			IPrimitiveType<?> ifNoneMatch = terser.getSingleValueOrNull(request, "ifNoneMatch", IPrimitiveType.class);
			if (ifNoneMatch != null) {
				retVal.setRequestIfNoneMatch(ifNoneMatch.getValueAsString());
			}

			IPrimitiveType<?> ifModifiedSince =
					terser.getSingleValueOrNull(request, "ifModifiedSince", IPrimitiveType.class);
			if (ifModifiedSince != null) {
				retVal.setRequestIfModifiedSince(ifModifiedSince.getValueAsString());
			}

			IPrimitiveType<?> ifMatch = terser.getSingleValueOrNull(request, "ifMatch", IPrimitiveType.class);
			if (ifMatch != null) {
				retVal.setRequestIfMatch(ifMatch.getValueAsString());
			}

			IPrimitiveType<?> ifNoneExist = terser.getSingleValueOrNull(request, "ifNoneExist", IPrimitiveType.class);
			if (ifNoneExist != null) {
				retVal.setRequestIfNoneExist(ifNoneExist.getValueAsString());
			}
		}

		// Extract response fields
		IBaseBackboneElement response = terser.getSingleValueOrNull(theEntry, "response", IBaseBackboneElement.class);
		if (response != null) {
			IPrimitiveType<?> status = terser.getSingleValueOrNull(response, "status", IPrimitiveType.class);
			if (status != null) {
				retVal.setResponseStatus(status.getValueAsString());
			}

			IPrimitiveType<?> location = terser.getSingleValueOrNull(response, "location", IPrimitiveType.class);
			if (location != null) {
				retVal.setResponseLocation(location.getValueAsString());
			}

			IPrimitiveType<?> etag = terser.getSingleValueOrNull(response, "etag", IPrimitiveType.class);
			if (etag != null) {
				retVal.setResponseEtag(etag.getValueAsString());
			}

			IPrimitiveType<?> lastModified =
					terser.getSingleValueOrNull(response, "lastModified", IPrimitiveType.class);
			if (lastModified != null) {
				retVal.setResponseLastModified(lastModified.getValueAsString());
			}

			IBaseOperationOutcome outcome =
					terser.getSingleValueOrNull(response, "outcome", IBaseOperationOutcome.class);
			if (outcome != null) {
				retVal.setResponseOutcome(outcome);
			}
		}

		// Extract search fields
		IBaseBackboneElement search = terser.getSingleValueOrNull(theEntry, "search", IBaseBackboneElement.class);
		if (search != null) {
			IPrimitiveType<?> mode = terser.getSingleValueOrNull(search, "mode", IPrimitiveType.class);
			if (mode != null) {
				retVal.setSearchMode(mode.getValueAsString());
			}

			IPrimitiveType<?> score = terser.getSingleValueOrNull(search, "score", IPrimitiveType.class);
			if (score != null) {
				retVal.setSearchScore(score.getValueAsString());
			}
		}

		return retVal;
	}

	/**
	 * Convert this CanonicalBundleEntry back to a Bundle Entry of the specified type
	 * @param theFhirContext The FHIR context
	 * @param theBundleEntryComponentClass The target Bundle Entry class
	 * @return A new Bundle Entry instance
	 */
	public <T extends IBase> T toBundleEntry(FhirContext theFhirContext, Class<T> theBundleEntryComponentClass) {
		@SuppressWarnings("unchecked")
		T entry = (T) theFhirContext
				.getElementDefinition(theBundleEntryComponentClass)
				.newInstance();

		// Get the element definition for the Bundle Entry
		BaseRuntimeElementCompositeDefinition<?> entryDef = (BaseRuntimeElementCompositeDefinition<?>)
				theFhirContext.getElementDefinition(theBundleEntryComponentClass);

		// Set fullUrl
		if (myFullUrl != null) {
			BaseRuntimeChildDefinition fullUrlChild = entryDef.getChildByName("fullUrl");
			if (fullUrlChild != null) {
				IPrimitiveType<?> fullUrlValue = (IPrimitiveType<?>)
						fullUrlChild.getChildByName("fullUrl").newInstance();
				fullUrlValue.setValueAsString(myFullUrl);
				fullUrlChild.getMutator().setValue(entry, fullUrlValue);
			}
		}

		// Set resource
		if (myResource != null) {
			BaseRuntimeChildDefinition resourceChild = entryDef.getChildByName("resource");
			if (resourceChild != null) {
				resourceChild.getMutator().setValue(entry, myResource);
			}
		}

		// Set request fields
		if (myRequestMethod != null
				|| myRequestUrl != null
				|| myRequestIfNoneMatch != null
				|| myRequestIfModifiedSince != null
				|| myRequestIfMatch != null
				|| myRequestIfNoneExist != null) {

			BaseRuntimeChildDefinition requestChild = entryDef.getChildByName("request");
			if (requestChild != null) {
				IBase request = requestChild.getChildByName("request").newInstance();
				requestChild.getMutator().setValue(entry, request);

				BaseRuntimeElementCompositeDefinition<?> requestDef =
						(BaseRuntimeElementCompositeDefinition<?>) requestChild.getChildByName("request");

				if (myRequestMethod != null) {
					BaseRuntimeChildDefinition methodChild = requestDef.getChildByName("method");
					if (methodChild != null) {
						IPrimitiveType<?> methodValue = (IPrimitiveType<?>) methodChild
								.getChildByName("method")
								.newInstance(methodChild.getInstanceConstructorArguments());
						methodValue.setValueAsString(myRequestMethod);
						methodChild.getMutator().setValue(request, methodValue);
					}
				}

				if (myRequestUrl != null) {
					BaseRuntimeChildDefinition urlChild = requestDef.getChildByName("url");
					if (urlChild != null) {
						IPrimitiveType<?> urlValue = (IPrimitiveType<?>)
								urlChild.getChildByName("url").newInstance();
						urlValue.setValueAsString(myRequestUrl);
						urlChild.getMutator().setValue(request, urlValue);
					}
				}

				if (myRequestIfNoneMatch != null) {
					BaseRuntimeChildDefinition ifNoneMatchChild = requestDef.getChildByName("ifNoneMatch");
					if (ifNoneMatchChild != null) {
						IPrimitiveType<?> ifNoneMatchValue = (IPrimitiveType<?>)
								ifNoneMatchChild.getChildByName("ifNoneMatch").newInstance();
						ifNoneMatchValue.setValueAsString(myRequestIfNoneMatch);
						ifNoneMatchChild.getMutator().setValue(request, ifNoneMatchValue);
					}
				}

				if (myRequestIfModifiedSince != null) {
					BaseRuntimeChildDefinition ifModifiedSinceChild = requestDef.getChildByName("ifModifiedSince");
					if (ifModifiedSinceChild != null) {
						IPrimitiveType<?> ifModifiedSinceValue = (IPrimitiveType<?>) ifModifiedSinceChild
								.getChildByName("ifModifiedSince")
								.newInstance();
						ifModifiedSinceValue.setValueAsString(myRequestIfModifiedSince);
						ifModifiedSinceChild.getMutator().setValue(request, ifModifiedSinceValue);
					}
				}

				if (myRequestIfMatch != null) {
					BaseRuntimeChildDefinition ifMatchChild = requestDef.getChildByName("ifMatch");
					if (ifMatchChild != null) {
						IPrimitiveType<?> ifMatchValue = (IPrimitiveType<?>)
								ifMatchChild.getChildByName("ifMatch").newInstance();
						ifMatchValue.setValueAsString(myRequestIfMatch);
						ifMatchChild.getMutator().setValue(request, ifMatchValue);
					}
				}

				if (myRequestIfNoneExist != null) {
					BaseRuntimeChildDefinition ifNoneExistChild = requestDef.getChildByName("ifNoneExist");
					if (ifNoneExistChild != null) {
						IPrimitiveType<?> ifNoneExistValue = (IPrimitiveType<?>)
								ifNoneExistChild.getChildByName("ifNoneExist").newInstance();
						ifNoneExistValue.setValueAsString(myRequestIfNoneExist);
						ifNoneExistChild.getMutator().setValue(request, ifNoneExistValue);
					}
				}
			}
		}

		// Set response fields
		if (myResponseStatus != null
				|| myResponseLocation != null
				|| myResponseEtag != null
				|| myResponseLastModified != null) {
			BaseRuntimeChildDefinition responseChild = entryDef.getChildByName("response");
			if (responseChild != null) {
				IBase response = responseChild.getChildByName("response").newInstance();
				responseChild.getMutator().setValue(entry, response);

				BaseRuntimeElementCompositeDefinition<?> responseDef =
						(BaseRuntimeElementCompositeDefinition<?>) responseChild.getChildByName("response");

				if (myResponseStatus != null) {
					BaseRuntimeChildDefinition statusChild = responseDef.getChildByName("status");
					if (statusChild != null) {
						IPrimitiveType<?> statusValue = (IPrimitiveType<?>)
								statusChild.getChildByName("status").newInstance();
						statusValue.setValueAsString(myResponseStatus);
						statusChild.getMutator().setValue(response, statusValue);
					}
				}

				if (myResponseLocation != null) {
					BaseRuntimeChildDefinition locationChild = responseDef.getChildByName("location");
					if (locationChild != null) {
						IPrimitiveType<?> locationValue = (IPrimitiveType<?>)
								locationChild.getChildByName("location").newInstance();
						locationValue.setValueAsString(myResponseLocation);
						locationChild.getMutator().setValue(response, locationValue);
					}
				}

				if (myResponseEtag != null) {
					BaseRuntimeChildDefinition etagChild = responseDef.getChildByName("etag");
					if (etagChild != null) {
						IPrimitiveType<?> etagValue = (IPrimitiveType<?>)
								etagChild.getChildByName("etag").newInstance();
						etagValue.setValueAsString(myResponseEtag);
						etagChild.getMutator().setValue(response, etagValue);
					}
				}

				if (myResponseLastModified != null) {
					BaseRuntimeChildDefinition lastModifiedChild = responseDef.getChildByName("lastModified");
					if (lastModifiedChild != null) {
						IPrimitiveType<?> lastModifiedValue = (IPrimitiveType<?>)
								lastModifiedChild.getChildByName("lastModified").newInstance();
						lastModifiedValue.setValueAsString(myResponseLastModified);
						lastModifiedChild.getMutator().setValue(response, lastModifiedValue);
					}
				}

				if (myResponseOutcome != null) {
					BaseRuntimeChildDefinition outcomeChild = responseDef.getChildByName("outcome");
					if (outcomeChild != null) {
						outcomeChild.getMutator().setValue(response, myResponseOutcome);
					}
				}
			}
		}

		// Set search fields
		if (mySearchMode != null || mySearchScore != null) {
			BaseRuntimeChildDefinition searchChild = entryDef.getChildByName("search");
			if (searchChild != null) {
				IBase search = searchChild.getChildByName("search").newInstance();
				searchChild.getMutator().setValue(entry, search);

				BaseRuntimeElementCompositeDefinition<?> searchDef =
						(BaseRuntimeElementCompositeDefinition<?>) searchChild.getChildByName("search");

				if (mySearchMode != null) {
					BaseRuntimeChildDefinition modeChild = searchDef.getChildByName("mode");
					if (modeChild != null) {
						IPrimitiveType<?> modeValue = (IPrimitiveType<?>)
								modeChild.getChildByName("mode").newInstance();
						modeValue.setValueAsString(mySearchMode);
						modeChild.getMutator().setValue(search, modeValue);
					}
				}

				if (mySearchScore != null) {
					BaseRuntimeChildDefinition scoreChild = searchDef.getChildByName("score");
					if (scoreChild != null) {
						IPrimitiveType<?> scoreValue = (IPrimitiveType<?>)
								scoreChild.getChildByName("score").newInstance();
						scoreValue.setValueAsString(mySearchScore);
						scoreChild.getMutator().setValue(search, scoreValue);
					}
				}
			}
		}

		return entry;
	}
}
