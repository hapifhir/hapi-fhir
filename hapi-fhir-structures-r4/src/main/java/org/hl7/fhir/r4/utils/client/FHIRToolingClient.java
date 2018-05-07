package org.hl7.fhir.r4.utils.client;


/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ExpansionProfile;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.utilities.Utilities;

/**
 * Very Simple RESTful client. This is purely for use in the standalone 
 * tools jar packages. It doesn't support many features, only what the tools
 * need.
 * 
 * To use, initialize class and set base service URI as follows:
 * 
 * <pre><code>
 * FHIRSimpleClient fhirClient = new FHIRSimpleClient();
 * fhirClient.initialize("http://my.fhir.domain/myServiceRoot");
 * </code></pre>
 * 
 * Default Accept and Content-Type headers are application/fhir+xml and application/fhir+json.
 * 
 * These can be changed by invoking the following setter functions:
 * 
 * <pre><code>
 * setPreferredResourceFormat()
 * setPreferredFeedFormat()
 * </code></pre>
 * 
 * TODO Review all sad paths. 
 * 
 * @author Claude Nanjo
 *
 */
public class FHIRToolingClient {
	
	public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssK";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String hostKey = "http.proxyHost";
	public static final String portKey = "http.proxyPort";
	
	private String base;
	private ResourceAddress resourceAddress;
	private ResourceFormat preferredResourceFormat;
	private int maxResultSetSize = -1;//_count
	private CapabilityStatement capabilities;

	private ClientUtils utils = new ClientUtils();
	
	//Pass enpoint for client - URI
  public FHIRToolingClient(String baseServiceUrl) throws URISyntaxException {
    preferredResourceFormat = ResourceFormat.RESOURCE_XML;
    detectProxy();
    initialize(baseServiceUrl);
  }
  
  public FHIRToolingClient(String baseServiceUrl, String username, String password) throws URISyntaxException {
    preferredResourceFormat = ResourceFormat.RESOURCE_XML;
    utils.setUsername(username);
    utils.setPassword(password);
    detectProxy();
    initialize(baseServiceUrl);
  }
  
	public void configureProxy(String proxyHost, int proxyPort) {
		utils.setProxy(new HttpHost(proxyHost, proxyPort));
	}
	
	public void detectProxy() {
		String host = System.getenv(hostKey);
		String port = System.getenv(portKey);

		if(host==null) {
			host = System.getProperty(hostKey);
		}
		
		if(port==null) {
			port = System.getProperty(portKey);
		}
		
		if(host!=null && port!=null) {
			this.configureProxy(host, Integer.parseInt(port));
		}
	}
	
	public void initialize(String baseServiceUrl)  throws URISyntaxException {
	  base = baseServiceUrl;
		resourceAddress = new ResourceAddress(baseServiceUrl);
		this.maxResultSetSize = -1;
		checkCapabilities();
	}
	
	private void checkCapabilities() {
	  try {
      capabilities = getCapabilitiesStatementQuick();
	  } catch (Throwable e) {
	  }
   }

  public String getPreferredResourceFormat() {
    return preferredResourceFormat.getHeader();
  }
  
	public void setPreferredResourceFormat(ResourceFormat resourceFormat) {
		preferredResourceFormat = resourceFormat;
	}
	
	public int getMaximumRecordCount() {
		return maxResultSetSize;
	}
	
	public void setMaximumRecordCount(int maxResultSetSize) {
		this.maxResultSetSize = maxResultSetSize;
	}
	
	public CapabilityStatement getCapabilitiesStatement() {
	  CapabilityStatement conformance = null;
		try {
  		conformance = (CapabilityStatement)utils.issueGetResourceRequest(resourceAddress.resolveMetadataUri(false), getPreferredResourceFormat()).getReference();
		} catch(Exception e) {
			handleException("An error has occurred while trying to fetch the server's conformance statement", e);
		}
		return conformance;
	}
	
  public CapabilityStatement getCapabilitiesStatementQuick() throws EFhirClientException {
    if (capabilities != null)
      return capabilities;
    try {
      capabilities = (CapabilityStatement)utils.issueGetResourceRequest(resourceAddress.resolveMetadataUri(true), getPreferredResourceFormat()).getReference();
    } catch(Exception e) {
      handleException("An error has occurred while trying to fetch the server's conformance statement", e);
    }
    return capabilities;
  }
  
	public <T extends Resource> T read(Class<T> resourceClass, String id) {//TODO Change this to AddressableResource
		ResourceRequest<T> result = null;
		try {
			result = utils.issueGetResourceRequest(resourceAddress.resolveGetUriFromResourceClassAndId(resourceClass, id), getPreferredResourceFormat());
			result.addErrorStatus(410);//gone
			result.addErrorStatus(404);//unknown
			result.addSuccessStatus(200);//Only one for now
			if(result.isUnsuccessfulRequest()) {
				throw new EFhirClientException("Server returned error code " + result.getHttpStatus(), (OperationOutcome)result.getPayload());
			}
		} catch (Exception e) {
			handleException("An error has occurred while trying to read this resource", e);
		}
		return result.getPayload();
	}

	public <T extends Resource> T vread(Class<T> resourceClass, String id, String version) {
		ResourceRequest<T> result = null;
		try {
			result = utils.issueGetResourceRequest(resourceAddress.resolveGetUriFromResourceClassAndIdAndVersion(resourceClass, id, version), getPreferredResourceFormat());
			result.addErrorStatus(410);//gone
			result.addErrorStatus(404);//unknown
			result.addErrorStatus(405);//unknown
			result.addSuccessStatus(200);//Only one for now
			if(result.isUnsuccessfulRequest()) {
				throw new EFhirClientException("Server returned error code " + result.getHttpStatus(), (OperationOutcome)result.getPayload());
			}
		} catch (Exception e) {
			handleException("An error has occurred while trying to read this version of the resource", e);
		}
		return result.getPayload();
	}
	
	// GET fhir/ValueSet?url=http://hl7.org/fhir/ValueSet/clinical-findings&version=0.8

  public <T extends Resource> T getCanonical(Class<T> resourceClass, String canonicalURL) {
    ResourceRequest<T> result = null;
    try {
      result = utils.issueGetResourceRequest(resourceAddress.resolveGetUriFromResourceClassAndCanonical(resourceClass, canonicalURL), getPreferredResourceFormat());
      result.addErrorStatus(410);//gone
      result.addErrorStatus(404);//unknown
      result.addErrorStatus(405);//unknown
      result.addSuccessStatus(200);//Only one for now
      if(result.isUnsuccessfulRequest()) {
        throw new EFhirClientException("Server returned error code " + result.getHttpStatus(), (OperationOutcome)result.getPayload());
      }
    } catch (Exception e) {
      handleException("An error has occurred while trying to read this version of the resource", e);
    }
    Bundle bnd = (Bundle) result.getPayload();
    if (bnd.getEntry().size() == 0)
      throw new EFhirClientException("No matching resource found for canonical URL '"+canonicalURL+"'");
    if (bnd.getEntry().size() > 1)
      throw new EFhirClientException("Multiple matching resources found for canonical URL '"+canonicalURL+"'");
    return (T) bnd.getEntry().get(0).getResource();
  }
  
//	
//	public <T extends Resource> T update(Class<T> resourceClass, T resource, String id) {
//		ResourceRequest<T> result = null;
//		try {
//			List<Header> headers = null;
//			result = utils.issuePutRequest(resourceAddress.resolveGetUriFromResourceClassAndId(resourceClass, id),utils.getResourceAsByteArray(resource, false, isJson(getPreferredResourceFormat())), getPreferredResourceFormat(), headers, proxy);
//			result.addErrorStatus(410);//gone
//			result.addErrorStatus(404);//unknown
//			result.addErrorStatus(405);
//			result.addErrorStatus(422);//Unprocessable Entity
//			result.addSuccessStatus(200);
//			result.addSuccessStatus(201);
//			if(result.isUnsuccessfulRequest()) {
//				throw new EFhirClientException("Server returned error code " + result.getHttpStatus(), (OperationOutcome)result.getPayload());
//			}
//		} catch(Exception e) {
//			throw new EFhirClientException("An error has occurred while trying to update this resource", e);
//		}
//		// TODO oe 26.1.2015 could be made nicer if only OperationOutcome	locationheader is returned with an operationOutcome would be returned (and not	the resource also) we make another read
//		try {
//		  OperationOutcome operationOutcome = (OperationOutcome)result.getPayload();
//		  ResourceAddress.ResourceVersionedIdentifier resVersionedIdentifier = ResourceAddress.parseCreateLocation(result.getLocation());
//		  return this.vread(resourceClass, resVersionedIdentifier.getId(),resVersionedIdentifier.getVersionId());
//		} catch(ClassCastException e) {
//		  // if we fall throught we have the correct type already in the create
//		}
//
//		return result.getPayload();
//	}

//	
//	public <T extends Resource> boolean delete(Class<T> resourceClass, String id) {
//		try {
//			return utils.issueDeleteRequest(resourceAddress.resolveGetUriFromResourceClassAndId(resourceClass, id), proxy);
//		} catch(Exception e) {
//			throw new EFhirClientException("An error has occurred while trying to delete this resource", e);
//		}
//
//	}

//	
//	public <T extends Resource> OperationOutcome create(Class<T> resourceClass, T resource) {
//	  ResourceRequest<T> resourceRequest = null;
//	  try {
//	    List<Header> headers = null;
//	    resourceRequest = utils.issuePostRequest(resourceAddress.resolveGetUriFromResourceClass(resourceClass),utils.getResourceAsByteArray(resource, false, isJson(getPreferredResourceFormat())), getPreferredResourceFormat(), headers, proxy);
//	    resourceRequest.addSuccessStatus(201);
//	    if(resourceRequest.isUnsuccessfulRequest()) {
//	      throw new EFhirClientException("Server responded with HTTP error code " + resourceRequest.getHttpStatus(), (OperationOutcome)resourceRequest.getPayload());
//	    }
//	  } catch(Exception e) {
//	    handleException("An error has occurred while trying to create this resource", e);
//	  }
//	  OperationOutcome operationOutcome = null;;
//	  try {
//	    operationOutcome = (OperationOutcome)resourceRequest.getPayload();
//	    ResourceAddress.ResourceVersionedIdentifier resVersionedIdentifier = 
//	        ResourceAddress.parseCreateLocation(resourceRequest.getLocation());
//	    OperationOutcomeIssueComponent issue = operationOutcome.addIssue();
//	    issue.setSeverity(IssueSeverity.INFORMATION);
//	    issue.setUserData(ResourceAddress.ResourceVersionedIdentifier.class.toString(),
//	        resVersionedIdentifier);
//	    return operationOutcome;
//	  } catch(ClassCastException e) {
//	    // some server (e.g. grahams) returns the resource directly
//	    operationOutcome = new OperationOutcome();
//	    OperationOutcomeIssueComponent issue = operationOutcome.addIssue();
//	    issue.setSeverity(IssueSeverity.INFORMATION);
//	    issue.setUserData(ResourceRequest.class.toString(),
//	        resourceRequest.getPayload());
//	    return operationOutcome;
//	  }	
//	}

//	
//	public <T extends Resource> Bundle history(Calendar lastUpdate, Class<T> resourceClass, String id) {
//		Bundle history = null;
//		try {
//			history = utils.issueGetFeedRequest(resourceAddress.resolveGetHistoryForResourceId(resourceClass, id, lastUpdate, maxResultSetSize), getPreferredResourceFormat(), proxy);
//		} catch (Exception e) {
//			handleException("An error has occurred while trying to retrieve history information for this resource", e);
//		}
//		return history;
//	}

//	
//	public <T extends Resource> Bundle history(Date lastUpdate, Class<T> resourceClass, String id) {
//		Bundle history = null;
//		try {
//			history = utils.issueGetFeedRequest(resourceAddress.resolveGetHistoryForResourceId(resourceClass, id, lastUpdate, maxResultSetSize), getPreferredResourceFormat(), proxy);
//		} catch (Exception e) {
//			handleException("An error has occurred while trying to retrieve history information for this resource", e);
//		}
//		return history;
//	}
//
//	
//	public <T extends Resource> Bundle history(Calendar lastUpdate, Class<T> resourceClass) {
//		Bundle history = null;
//		try {
//			history = utils.issueGetFeedRequest(resourceAddress.resolveGetHistoryForResourceType(resourceClass, lastUpdate, maxResultSetSize), getPreferredResourceFormat(), proxy);
//		} catch (Exception e) {
//			handleException("An error has occurred while trying to retrieve history information for this resource type", e);
//		}
//		return history;
//	}
//	
//	
//	public <T extends Resource> Bundle history(Date lastUpdate, Class<T> resourceClass) {
//		Bundle history = null;
//		try {
//			history = utils.issueGetFeedRequest(resourceAddress.resolveGetHistoryForResourceType(resourceClass, lastUpdate, maxResultSetSize), getPreferredResourceFormat(), proxy);
//		} catch (Exception e) {
//			handleException("An error has occurred while trying to retrieve history information for this resource type", e);
//		}
//		return history;
//	}
//	
//	
//	public <T extends Resource> Bundle history(Class<T> resourceClass) {
//		Bundle history = null;
//		try {
//			history = utils.issueGetFeedRequest(resourceAddress.resolveGetHistoryForResourceType(resourceClass, maxResultSetSize), getPreferredResourceFormat(), proxy);
//		} catch (Exception e) {
//			handleException("An error has occurred while trying to retrieve history information for this resource type", e);
//		}
//		return history;
//	}
//	
//	
//	public <T extends Resource> Bundle history(Class<T> resourceClass, String id) {
//		Bundle history = null;
//		try {
//			history = utils.issueGetFeedRequest(resourceAddress.resolveGetHistoryForResourceId(resourceClass, id, maxResultSetSize), getPreferredResourceFormat(), proxy);
//		} catch (Exception e) {
//			handleException("An error has occurred while trying to retrieve history information for this resource", e);
//		}
//		return history;
//	}
//
//	
//	public <T extends Resource> Bundle history(Date lastUpdate) {
//		Bundle history = null;
//		try {
//			history = utils.issueGetFeedRequest(resourceAddress.resolveGetHistoryForAllResources(lastUpdate, maxResultSetSize), getPreferredResourceFormat(), proxy);
//		} catch (Exception e) {
//			handleException("An error has occurred while trying to retrieve history since last update",e);
//		}
//		return history;
//	}
//
//	
//	public <T extends Resource> Bundle history(Calendar lastUpdate) {
//		Bundle history = null;
//		try {
//			history = utils.issueGetFeedRequest(resourceAddress.resolveGetHistoryForAllResources(lastUpdate, maxResultSetSize), getPreferredResourceFormat(), proxy);
//		} catch (Exception e) {
//			handleException("An error has occurred while trying to retrieve history since last update",e);
//		}
//		return history;
//	}
//
//	
//	public <T extends Resource> Bundle history() {
//		Bundle history = null;
//		try {
//			history = utils.issueGetFeedRequest(resourceAddress.resolveGetHistoryForAllResources(maxResultSetSize), getPreferredResourceFormat(), proxy);
//		} catch (Exception e) {
//			handleException("An error has occurred while trying to retrieve history since last update",e);
//		}
//		return history;
//	}
//
//	
//	public <T extends Resource> Bundle search(Class<T> resourceClass, Map<String, String> parameters) {
//		Bundle searchResults = null;
//		try {
//			searchResults = utils.issueGetFeedRequest(resourceAddress.resolveSearchUri(resourceClass, parameters), getPreferredResourceFormat(), proxy);
//		} catch (Exception e) {
//			handleException("Error performing search with parameters " + parameters, e);
//		}
//		return searchResults;
//	}
//	
//  
//  public <T extends Resource> Bundle searchPost(Class<T> resourceClass, T resource, Map<String, String> parameters) {
//    Bundle searchResults = null;
//    try {
//      searchResults = utils.issuePostFeedRequest(resourceAddress.resolveSearchUri(resourceClass, new HashMap<String, String>()), parameters, "src", resource, getPreferredResourceFormat());
//    } catch (Exception e) {
//      handleException("Error performing search with parameters " + parameters, e);
//    }
//    return searchResults;
//  }
	
	
  public <T extends Resource> Parameters operateType(Class<T> resourceClass, String name, Parameters params) {
  	boolean complex = false;
  	for (ParametersParameterComponent p : params.getParameter())
  		complex = complex || !(p.getValue() instanceof PrimitiveType);
  	Parameters searchResults = null;
			String ps = "";
  		try {
      if (!complex)
  			for (ParametersParameterComponent p : params.getParameter())
  	  		if (p.getValue() instanceof PrimitiveType)
  	  		  ps += p.getName() + "=" + Utilities.encodeUri(((PrimitiveType) p.getValue()).asStringValue())+"&";
   		ResourceRequest<T> result;
  		if (complex)
  			result = utils.issuePostRequest(resourceAddress.resolveOperationURLFromClass(resourceClass, name, ps), utils.getResourceAsByteArray(params, false, isJson(getPreferredResourceFormat())), getPreferredResourceFormat());
  		else 
  			result = utils.issueGetResourceRequest(resourceAddress.resolveOperationURLFromClass(resourceClass, name, ps), getPreferredResourceFormat());
  			result.addErrorStatus(410);//gone
  			result.addErrorStatus(404);//unknown
  			result.addSuccessStatus(200);//Only one for now
  			if(result.isUnsuccessfulRequest()) 
  				throw new EFhirClientException("Server returned error code " + result.getHttpStatus(), (OperationOutcome)result.getPayload());
  		if (result.getPayload() instanceof Parameters)
  			return (Parameters) result.getPayload();
  		else {
  			Parameters p_out = new Parameters();
  			p_out.addParameter().setName("return").setResource(result.getPayload());
  			return p_out;
  		}
  		} catch (Exception e) {
  			handleException("Error performing operation '"+name+"' with parameters " + ps, e);  		
  		}
  		return null;
  }

  
	public Bundle transaction(Bundle batch) {
		Bundle transactionResult = null;
		try {
			transactionResult = utils.postBatchRequest(resourceAddress.getBaseServiceUri(), utils.getFeedAsByteArray(batch, false, isJson(getPreferredResourceFormat())), getPreferredResourceFormat());
		} catch (Exception e) {
			handleException("An error occurred trying to process this transaction request", e);
		}
		return transactionResult;
	}
	
	@SuppressWarnings("unchecked")
	
	public <T extends Resource> OperationOutcome validate(Class<T> resourceClass, T resource, String id) {
		ResourceRequest<T> result = null;
		try {
			result = utils.issuePostRequest(resourceAddress.resolveValidateUri(resourceClass, id), utils.getResourceAsByteArray(resource, false, isJson(getPreferredResourceFormat())), getPreferredResourceFormat());
			result.addErrorStatus(400);//gone
			result.addErrorStatus(422);//Unprocessable Entity
			result.addSuccessStatus(200);//OK
			if(result.isUnsuccessfulRequest()) {
				throw new EFhirClientException("Server returned error code " + result.getHttpStatus(), (OperationOutcome)result.getPayload());
			}
		} catch(Exception e) {
			handleException("An error has occurred while trying to validate this resource", e);
		}
		return (OperationOutcome)result.getPayload();
	}
	
	/* change to meta operations
	
	public List<Coding> getAllTags() {
		TagListRequest result = null;
		try {
			result = utils.issueGetRequestForTagList(resourceAddress.resolveGetAllTags(), getPreferredResourceFormat(), null, proxy);
		} catch (Exception e) {
			handleException("An error has occurred while trying to retrieve all tags", e);
		}
		return result.getPayload();
	}
	
	
	public <T extends Resource> List<Coding> getAllTagsForResourceType(Class<T> resourceClass) {
		TagListRequest result = null;
		try {
			result = utils.issueGetRequestForTagList(resourceAddress.resolveGetAllTagsForResourceType(resourceClass), getPreferredResourceFormat(), null, proxy);
		} catch (Exception e) {
			handleException("An error has occurred while trying to retrieve tags for this resource type", e);
		}
		return result.getPayload();
	}
	
	
	public <T extends Resource> List<Coding> getTagsForReference(Class<T> resource, String id) {
		TagListRequest result = null;
		try {
			result = utils.issueGetRequestForTagList(resourceAddress.resolveGetTagsForReference(resource, id), getPreferredResourceFormat(), null, proxy);
		} catch (Exception e) {
			handleException("An error has occurred while trying to retrieve tags for this resource", e);
		}
		return result.getPayload();
	}
	
	
	public <T extends Resource> List<Coding> getTagsForResourceVersion(Class<T> resource, String id, String versionId) {
		TagListRequest result = null;
		try {
			result = utils.issueGetRequestForTagList(resourceAddress.resolveGetTagsForResourceVersion(resource, id, versionId), getPreferredResourceFormat(), null, proxy);
		} catch (Exception e) {
			handleException("An error has occurred while trying to retrieve tags for this resource version", e);
		}
		return result.getPayload();
	}
	
//	
//	public <T extends Resource> boolean deleteTagsForReference(Class<T> resourceClass, String id) {
//		try {
//			return utils.issueDeleteRequest(resourceAddress.resolveGetTagsForReference(resourceClass, id), proxy);
//		} catch(Exception e) {
//			handleException("An error has occurred while trying to retrieve tags for this resource version", e);
//			throw new EFhirClientException("An error has occurred while trying to delete this resource", e);
//		}
//
//	}
//	
//	
//	public <T extends Resource> boolean deleteTagsForResourceVersion(Class<T> resourceClass, String id, List<Coding> tags, String version) {
//		try {
//			return utils.issueDeleteRequest(resourceAddress.resolveGetTagsForResourceVersion(resourceClass, id, version), proxy);
//		} catch(Exception e) {
//			handleException("An error has occurred while trying to retrieve tags for this resource version", e);
//			throw new EFhirClientException("An error has occurred while trying to delete this resource", e);
//		}
//	}
	
	
	public <T extends Resource> List<Coding> createTags(List<Coding> tags, Class<T> resourceClass, String id) {
		TagListRequest request = null;
		try {
			request = utils.issuePostRequestForTagList(resourceAddress.resolveGetTagsForReference(resourceClass, id),utils.getTagListAsByteArray(tags, false, isJson(getPreferredResourceFormat())), getPreferredResourceFormat(), null, proxy);
			request.addSuccessStatus(201);
			request.addSuccessStatus(200);
			if(request.isUnsuccessfulRequest()) {
				throw new EFhirClientException("Server responded with HTTP error code " + request.getHttpStatus());
			}
		} catch(Exception e) {
			handleException("An error has occurred while trying to set tags for this resource", e);
		}
		return request.getPayload();
	}
	
	
	public <T extends Resource> List<Coding> createTags(List<Coding> tags, Class<T> resourceClass, String id, String version) {
		TagListRequest request = null;
		try {
			request = utils.issuePostRequestForTagList(resourceAddress.resolveGetTagsForResourceVersion(resourceClass, id, version),utils.getTagListAsByteArray(tags, false, isJson(getPreferredResourceFormat())), getPreferredResourceFormat(), null, proxy);
			request.addSuccessStatus(201);
			request.addSuccessStatus(200);
			if(request.isUnsuccessfulRequest()) {
				throw new EFhirClientException("Server responded with HTTP error code " + request.getHttpStatus());
			}
		} catch(Exception e) {
			handleException("An error has occurred while trying to set the tags for this resource version", e);
		}
		return request.getPayload();
	}

	
	public <T extends Resource> List<Coding> deleteTags(List<Coding> tags, Class<T> resourceClass, String id, String version) {
		TagListRequest request = null;
		try {
			request = utils.issuePostRequestForTagList(resourceAddress.resolveDeleteTagsForResourceVersion(resourceClass, id, version),utils.getTagListAsByteArray(tags, false, isJson(getPreferredResourceFormat())), getPreferredResourceFormat(), null, proxy);
			request.addSuccessStatus(201);
			request.addSuccessStatus(200);
			if(request.isUnsuccessfulRequest()) {
				throw new EFhirClientException("Server responded with HTTP error code " + request.getHttpStatus());
			}
		} catch(Exception e) {
			handleException("An error has occurred while trying to delete the tags for this resource version", e);
		}
		return request.getPayload();
	}
	*/

	/**
	 * Helper method to prevent nesting of previously thrown EFhirClientExceptions
	 * 
	 * @param e
	 * @throws EFhirClientException
	 */
	protected void handleException(String message, Exception e) throws EFhirClientException {
		if(e instanceof EFhirClientException) {
			throw (EFhirClientException)e;
		} else {
			throw new EFhirClientException(message, e);
		}
	}
	
	/**
	 * Helper method to determine whether desired resource representation
	 * is Json or XML.
	 * 
	 * @param format
	 * @return
	 */
	protected boolean isJson(String format) {
		boolean isJson = false;
		if(format.toLowerCase().contains("json")) {
			isJson = true;
		}
		return isJson;
	}
		
  public Bundle fetchFeed(String url) {
		Bundle feed = null;
		try {
			feed = utils.issueGetFeedRequest(new URI(url), getPreferredResourceFormat());
		} catch (Exception e) {
			handleException("An error has occurred while trying to retrieve history since last update",e);
		}
		return feed;
  }
  
  public ValueSet expandValueset(ValueSet source, ExpansionProfile profile) {
    List<Header> headers = null;
    Parameters p = new Parameters();
    p.addParameter().setName("valueSet").setResource(source);
    if (profile != null)
      p.addParameter().setName("profile").setResource(profile);
    ResourceRequest<Resource> result = utils.issuePostRequest(resourceAddress.resolveOperationUri(ValueSet.class, "expand"), 
        utils.getResourceAsByteArray(p, false, isJson(getPreferredResourceFormat())), getPreferredResourceFormat(), headers);
    result.addErrorStatus(410);//gone
    result.addErrorStatus(404);//unknown
    result.addErrorStatus(405);
    result.addErrorStatus(422);//Unprocessable Entity
    result.addSuccessStatus(200);
    result.addSuccessStatus(201);
    if(result.isUnsuccessfulRequest()) {
      throw new EFhirClientException("Server returned error code " + result.getHttpStatus(), (OperationOutcome)result.getPayload());
    }
    return (ValueSet) result.getPayload();
  }

  
  public Parameters lookupCode(Map<String, String> params) {
    ResourceRequest<Resource> result = utils.issueGetResourceRequest(resourceAddress.resolveOperationUri(CodeSystem.class, "lookup", params), getPreferredResourceFormat());
    result.addErrorStatus(410);//gone
    result.addErrorStatus(404);//unknown
    result.addErrorStatus(405);
    result.addErrorStatus(422);//Unprocessable Entity
    result.addSuccessStatus(200);
    result.addSuccessStatus(201);
    if(result.isUnsuccessfulRequest()) {
      throw new EFhirClientException("Server returned error code " + result.getHttpStatus(), (OperationOutcome)result.getPayload());
    }
    return (Parameters) result.getPayload();
  }
  public ValueSet expandValueset(ValueSet source, ExpansionProfile profile, Map<String, String> params) {
    List<Header> headers = null;
    Parameters p = new Parameters();
    p.addParameter().setName("valueSet").setResource(source);
    if (profile != null)
      p.addParameter().setName("profile").setResource(profile);
    for (String n : params.keySet())
      p.addParameter().setName(n).setValue(new StringType(params.get(n)));
    ResourceRequest<Resource> result = utils.issuePostRequest(resourceAddress.resolveOperationUri(ValueSet.class, "expand", params), 
        utils.getResourceAsByteArray(p, false, isJson(getPreferredResourceFormat())), getPreferredResourceFormat(), headers);
    result.addErrorStatus(410);//gone
    result.addErrorStatus(404);//unknown
    result.addErrorStatus(405);
    result.addErrorStatus(422);//Unprocessable Entity
    result.addSuccessStatus(200);
    result.addSuccessStatus(201);
    if(result.isUnsuccessfulRequest()) {
      throw new EFhirClientException("Server returned error code " + result.getHttpStatus(), (OperationOutcome)result.getPayload());
    }
    return (ValueSet) result.getPayload();
  }
  
//  public ValueSet expandValueset(ValueSet source, ExpansionProfile profile, Map<String, String> params) {
//    List<Header> headers = null;
//    ResourceRequest<Resource> result = utils.issuePostRequest(resourceAddress.resolveOperationUri(ValueSet.class, "expand", params), 
//        utils.getResourceAsByteArray(source, false, isJson(getPreferredResourceFormat())), getPreferredResourceFormat(), headers, proxy);
//    result.addErrorStatus(410);//gone
//    result.addErrorStatus(404);//unknown
//    result.addErrorStatus(405);
//    result.addErrorStatus(422);//Unprocessable Entity
//    result.addSuccessStatus(200);
//    result.addSuccessStatus(201);
//    if(result.isUnsuccessfulRequest()) {
//      throw new EFhirClientException("Server returned error code " + result.getHttpStatus(), (OperationOutcome)result.getPayload());
//    }
//    return (ValueSet) result.getPayload();
//  }
  
  
  public String getAddress() {
    return base;
  }

  public ConceptMap initializeClosure(String name) {
    Parameters params = new Parameters();
    params.addParameter().setName("name").setValue(new StringType(name));
    List<Header> headers = null;
    ResourceRequest<Resource> result = utils.issuePostRequest(resourceAddress.resolveOperationUri(null, "closure", new HashMap<String, String>()),
        utils.getResourceAsByteArray(params, false, isJson(getPreferredResourceFormat())), getPreferredResourceFormat(), headers);
    result.addErrorStatus(410);//gone
    result.addErrorStatus(404);//unknown
    result.addErrorStatus(405);
    result.addErrorStatus(422);//Unprocessable Entity
    result.addSuccessStatus(200);
    result.addSuccessStatus(201);
    if(result.isUnsuccessfulRequest()) {
      throw new EFhirClientException("Server returned error code " + result.getHttpStatus(), (OperationOutcome)result.getPayload());
    }
    return (ConceptMap) result.getPayload();
  }

  public ConceptMap updateClosure(String name, Coding coding) {
    Parameters params = new Parameters();
    params.addParameter().setName("name").setValue(new StringType(name));
    params.addParameter().setName("concept").setValue(coding);
    List<Header> headers = null;
    ResourceRequest<Resource> result = utils.issuePostRequest(resourceAddress.resolveOperationUri(null, "closure", new HashMap<String, String>()),
        utils.getResourceAsByteArray(params, false, isJson(getPreferredResourceFormat())), getPreferredResourceFormat(), headers);
    result.addErrorStatus(410);//gone
    result.addErrorStatus(404);//unknown
    result.addErrorStatus(405);
    result.addErrorStatus(422);//Unprocessable Entity
    result.addSuccessStatus(200);
    result.addSuccessStatus(201);
    if(result.isUnsuccessfulRequest()) {
      throw new EFhirClientException("Server returned error code " + result.getHttpStatus(), (OperationOutcome)result.getPayload());
    }
    return (ConceptMap) result.getPayload();
  }

  public int getTimeout() {
    return utils.getTimeout();
  }

  public void setTimeout(int timeout) {
    utils.setTimeout(timeout);
  }

  public String getUsername() {
    return utils.getUsername();
  }

  public void setUsername(String username) {
    utils.setUsername(username);
  }

  public String getPassword() {
    return utils.getPassword();
  }

  public void setPassword(String password) {
    utils.setPassword(password);
  }

}
