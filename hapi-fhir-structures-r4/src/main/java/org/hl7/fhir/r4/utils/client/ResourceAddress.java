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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.fhir.ucum.Canonical;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.utilities.Utilities;

//Make resources address subclass of URI
/**
 * Helper class to manage FHIR Resource URIs
 * 
 * @author Claude Nanjo
 *
 */
public class ResourceAddress {
	
	public static final String REGEX_ID_WITH_HISTORY = "(.*)(/)([a-zA-Z0-9]*)(/)([a-z0-9\\-\\.]{1,64})(/_history/)([a-z0-9\\-\\.]{1,64})$";
	
	private URI baseServiceUri;
	
	public ResourceAddress(String endpointPath) throws URISyntaxException {//TODO Revisit this exception
		this.baseServiceUri = ResourceAddress.buildAbsoluteURI(endpointPath);
	}
	
	public ResourceAddress(URI baseServiceUri) {
		this.baseServiceUri = baseServiceUri;
	}
	
	public URI getBaseServiceUri() {
		return this.baseServiceUri;
	}
	
	public <T extends Resource> URI resolveOperationURLFromClass(Class<T> resourceClass, String name, String parameters) {
		return baseServiceUri.resolve(nameForClassWithSlash(resourceClass) +"$"+name+"?"+ parameters);
	}
	
	public <T extends Resource> URI resolveSearchUri(Class<T> resourceClass, Map<String,String> parameters) {
		return appendHttpParameters(baseServiceUri.resolve(nameForClassWithSlash(resourceClass) +"_search"), parameters);
	}
	
  private <T extends Resource> String nameForClassWithSlash(Class<T> resourceClass) {
    String n = nameForClass(resourceClass);
    return n == null ? "" : n +"/";
	}
	
  public <T extends Resource> URI resolveOperationUri(Class<T> resourceClass, String opName) {
    return baseServiceUri.resolve(nameForClassWithSlash(resourceClass) +"/"+opName);
  }
  
  public <T extends Resource> URI resolveOperationUri(Class<T> resourceClass, String opName, Map<String,String> parameters) {
    return appendHttpParameters(baseServiceUri.resolve(nameForClassWithSlash(resourceClass) +"$"+opName), parameters);
  }
  
	public <T extends Resource> URI resolveValidateUri(Class<T> resourceClass, String id) {
		return baseServiceUri.resolve(nameForClassWithSlash(resourceClass) +"$validate/"+id);
	}
	
	public <T extends Resource> URI resolveGetUriFromResourceClass(Class<T> resourceClass) {
		return baseServiceUri.resolve(nameForClass(resourceClass));
	}
	
	public <T extends Resource> URI resolveGetUriFromResourceClassAndId(Class<T> resourceClass, String id) {
		return baseServiceUri.resolve(nameForClass(resourceClass) +"/"+id);
	}
	
	public <T extends Resource> URI resolveGetUriFromResourceClassAndIdAndVersion(Class<T> resourceClass, String id, String version) {
		return baseServiceUri.resolve(nameForClass(resourceClass) +"/"+id+"/_history/"+version);
	}
	
  public <T extends Resource> URI resolveGetUriFromResourceClassAndCanonical(Class<T> resourceClass, String canonicalUrl) {
    if (canonicalUrl.contains("|"))
      return baseServiceUri.resolve(nameForClass(resourceClass)+"?url="+canonicalUrl.substring(0, canonicalUrl.indexOf("|"))+"&version="+canonicalUrl.substring(canonicalUrl.indexOf("|")+1));      
    else
      return baseServiceUri.resolve(nameForClass(resourceClass)+"?url="+canonicalUrl);
  }
  
	public URI resolveGetHistoryForAllResources(int count) {
		if(count > 0) {
			return appendHttpParameter(baseServiceUri.resolve("_history"), "_count", ""+count);
		} else {
			return baseServiceUri.resolve("_history");
		}
}
	
	public <T extends Resource> URI resolveGetHistoryForResourceId(Class<T> resourceClass, String id, int count) {
		return resolveGetHistoryUriForResourceId(resourceClass, id, null, count);
	}
	
	protected <T extends Resource> URI resolveGetHistoryUriForResourceId(Class<T> resourceClass, String id, Object since, int count) {
		Map<String,String>  parameters = getHistoryParameters(since, count);
		return appendHttpParameters(baseServiceUri.resolve(nameForClass(resourceClass) + "/" + id + "/_history"), parameters);
	}
	
	public <T extends Resource> URI resolveGetHistoryForResourceType(Class<T> resourceClass, int count) {
		Map<String,String>  parameters = getHistoryParameters(null, count);
		return appendHttpParameters(baseServiceUri.resolve(nameForClass(resourceClass) + "/_history"), parameters);
	}
	
	public <T extends Resource> URI resolveGetHistoryForResourceType(Class<T> resourceClass, Object since, int count) {
		Map<String,String>  parameters = getHistoryParameters(since, count);
		return appendHttpParameters(baseServiceUri.resolve(nameForClass(resourceClass) + "/_history"), parameters);
	}
	
	public URI resolveGetHistoryForAllResources(Calendar since, int count) {
		Map<String,String>  parameters = getHistoryParameters(since, count);
		return appendHttpParameters(baseServiceUri.resolve("_history"), parameters);
	}
	
	public URI resolveGetHistoryForAllResources(Date since, int count) {
		Map<String,String>  parameters = getHistoryParameters(since, count);
		return appendHttpParameters(baseServiceUri.resolve("_history"), parameters);
	}
	
	public Map<String,String> getHistoryParameters(Object since, int count) {
		Map<String,String>  parameters = new HashMap<String,String>();
		if (since != null) {
			parameters.put("_since", since.toString());
		}
		if(count > 0) {
			parameters.put("_count", ""+count);
		}
		return parameters;
	}
	
	public <T extends Resource> URI resolveGetHistoryForResourceId(Class<T> resourceClass, String id, Calendar since, int count) {
		return resolveGetHistoryUriForResourceId(resourceClass, id, since, count);
	}
	
	public <T extends Resource> URI resolveGetHistoryForResourceId(Class<T> resourceClass, String id, Date since, int count) {
		return resolveGetHistoryUriForResourceId(resourceClass, id, since, count);
	}
	
	public <T extends Resource> URI resolveGetHistoryForResourceType(Class<T> resourceClass, Calendar since, int count) {
		return resolveGetHistoryForResourceType(resourceClass, getCalendarDateInIsoTimeFormat(since), count);
	}
	
	public <T extends Resource> URI resolveGetHistoryForResourceType(Class<T> resourceClass, Date since, int count) {
		return resolveGetHistoryForResourceType(resourceClass, since.toString(), count);
	}
	
	public <T extends Resource> URI resolveGetAllTags() {
		return baseServiceUri.resolve("_tags");
	}
	
	public <T extends Resource> URI resolveGetAllTagsForResourceType(Class<T> resourceClass) {
		return baseServiceUri.resolve(nameForClass(resourceClass) + "/_tags");
	}
	
	public <T extends Resource> URI resolveGetTagsForReference(Class<T> resourceClass, String id) {
		return baseServiceUri.resolve(nameForClass(resourceClass) + "/" + id + "/_tags");
	}
	
	public <T extends Resource> URI resolveGetTagsForResourceVersion(Class<T> resourceClass, String id, String version) {
		return baseServiceUri.resolve(nameForClass(resourceClass) +"/"+id+"/_history/"+version + "/_tags");
	}
	
	public <T extends Resource> URI resolveDeleteTagsForResourceVersion(Class<T> resourceClass, String id, String version) {
		return baseServiceUri.resolve(nameForClass(resourceClass) +"/"+id+"/_history/"+version + "/_tags/_delete");
	}
	

	public <T extends Resource> String nameForClass(Class<T> resourceClass) {
	  if (resourceClass == null)
	    return null;
		String res = resourceClass.getSimpleName();
		if (res.equals("List_"))
			return "List";
		else
			return res;
	}
	
	public URI resolveMetadataUri(boolean quick) {
		return baseServiceUri.resolve(quick ? "metadata?_summary=true" : "metadata");
	}
	
	/**
	 * For now, assume this type of location header structure.
	 * Generalize later: http://hl7connect.healthintersections.com.au/svc/fhir/318/_history/1
	 * 
	 * @param serviceBase
	 * @param locationHeader
	 */
	public static ResourceAddress.ResourceVersionedIdentifier parseCreateLocation(String locationResponseHeader) {
		Pattern pattern = Pattern.compile(REGEX_ID_WITH_HISTORY);
		Matcher matcher = pattern.matcher(locationResponseHeader);
		ResourceVersionedIdentifier parsedHeader = null;
		if(matcher.matches()){
			String serviceRoot = matcher.group(1);
			String resourceType = matcher.group(3);
			String id = matcher.group(5);
			String version = matcher.group(7);
			parsedHeader = new ResourceVersionedIdentifier(serviceRoot, resourceType, id, version);
		}
		return parsedHeader;
	}
	
	public static URI buildAbsoluteURI(String absoluteURI) {
		
		if(StringUtils.isBlank(absoluteURI)) {
			throw new EFhirClientException("Invalid URI", new URISyntaxException(absoluteURI, "URI/URL cannot be blank"));
		} 
		
		String endpoint = appendForwardSlashToPath(absoluteURI);

		return buildEndpointUriFromString(endpoint);
	}
	
	public static String appendForwardSlashToPath(String path) {
		if(path.lastIndexOf('/') != path.length() - 1) {
			path += "/";
		}
		return path;
	}
	
	public static URI buildEndpointUriFromString(String endpointPath) {
		URI uri = null; 
		try {
			URIBuilder uriBuilder = new URIBuilder(endpointPath);
			uri = uriBuilder.build();
			String scheme = uri.getScheme();
			String host = uri.getHost();
			if(!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https")) {
				throw new EFhirClientException("Scheme must be 'http' or 'https': " + uri);
			}
			if(StringUtils.isBlank(host)) {
				throw new EFhirClientException("host cannot be blank: " + uri);
			}
		} catch(URISyntaxException e) {
			throw new EFhirClientException("Invalid URI", e);
		}
		return uri;
	}
	
	public static URI appendQueryStringToUri(URI uri, String parameterName, String parameterValue) {
		URI modifiedUri = null;
		try {
			URIBuilder uriBuilder = new URIBuilder(uri);
			uriBuilder.setQuery(parameterName + "=" + parameterValue);
			modifiedUri = uriBuilder.build();
		} catch(Exception e) {
			throw new EFhirClientException("Unable to append query parameter '" + parameterName + "=" + parameterValue + " to URI " + uri, e);
		}
		return modifiedUri;
	}
	
	public static String buildRelativePathFromResourceType(ResourceType resourceType) {
		//return resourceType.toString().toLowerCase()+"/";
		return resourceType.toString() + "/";
	}
	
	public static String buildRelativePathFromResourceType(ResourceType resourceType, String id) {
		return buildRelativePathFromResourceType(resourceType)+ "@" + id;
	}
	
	public static String buildRelativePathFromReference(Resource resource) {
		return buildRelativePathFromResourceType(resource.getResourceType());
	}
	
	public static String buildRelativePathFromReference(Resource resource, String id) {
		return buildRelativePathFromResourceType(resource.getResourceType(), id);
	}
	
	public static class ResourceVersionedIdentifier {
		
		private String serviceRoot;
		private String resourceType;
		private String id;
		private String version;
		private URI resourceLocation;
		
		public ResourceVersionedIdentifier(String serviceRoot, String resourceType, String id, String version, URI resourceLocation) {
			this.serviceRoot = serviceRoot;
			this.resourceType = resourceType;
			this.id = id;
			this.version = version;
			this.resourceLocation = resourceLocation;
		}
		
		public ResourceVersionedIdentifier(String resourceType, String id, String version, URI resourceLocation) {
			this(null, resourceType, id, version, resourceLocation);
		}
		
		public ResourceVersionedIdentifier(String serviceRoot, String resourceType, String id, String version) {
			this(serviceRoot, resourceType, id, version, null);
		}
		
		public ResourceVersionedIdentifier(String resourceType, String id, String version) {
			this(null, resourceType, id, version, null);
		}
		
		public ResourceVersionedIdentifier(String resourceType, String id) {
			this.id = id;
		}
		
		public String getId() {
			return this.id;
		}
		
		protected void setId(String id) {
			this.id = id;
		}
		
		public String getVersionId() {
			return this.version;
		}
		
		protected void setVersionId(String version) {
			this.version = version;
		}
		
		public String getResourceType() {
			return resourceType;
		}

		public void setResourceType(String resourceType) {
			this.resourceType = resourceType;
		}
		
		public String getServiceRoot() {
			return serviceRoot;
		}

		public void setServiceRoot(String serviceRoot) {
			this.serviceRoot = serviceRoot;
		}
		
		public String getResourcePath() {
			return this.serviceRoot + "/" + this.resourceType + "/" + this.id;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public URI getResourceLocation() {
			return this.resourceLocation;
		}
		
		public void setResourceLocation(URI resourceLocation) {
			this.resourceLocation = resourceLocation;
		}
	}
	
	public static String getCalendarDateInIsoTimeFormat(Calendar calendar) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");//TODO Move out
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
	    return format.format(calendar.getTime());
	}
	
	public static URI appendHttpParameter(URI basePath, String httpParameterName, String httpParameterValue) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(httpParameterName, httpParameterValue);
		return appendHttpParameters(basePath, parameters);
	}
	
	public static URI appendHttpParameters(URI basePath, Map<String,String> parameters) {
        try {
        	Set<String> httpParameterNames = parameters.keySet();
        	String query = basePath.getQuery();
        	
        	for(String httpParameterName : httpParameterNames) {
		        if(query != null) {
			        query += "&";
		        } else {
		        	query = "";
		        }
		        query += httpParameterName + "=" + Utilities.encodeUri(parameters.get(httpParameterName));
        	}
	
	        return new URI(basePath.getScheme(), basePath.getUserInfo(), basePath.getHost(),basePath.getPort(), basePath.getPath(), query, basePath.getFragment());
        } catch(Exception e) {
        	throw new EFhirClientException("Error appending http parameter", e);
        }
    }
	
}
