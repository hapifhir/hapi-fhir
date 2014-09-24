 package ca.uhn.fhir.rest.server.interceptor;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.NotImplementedException;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.ObjectElement;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.Participant;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.ParticipantNetwork;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectLifecycleEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventParticipantNetworkTypeEnum;
import ca.uhn.fhir.rest.client.interceptor.UserInfoInterceptor;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.audit.IResourceAuditor;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.store.IAuditDataStore;

public class AuditingInterceptor extends InterceptorAdapter {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuditingInterceptor.class);
	
	private IAuditDataStore myDataStore;	
	private Map<ResourceTypeEnum, Class<? extends IResourceAuditor<? extends IResource>>> myAuditableResources = new HashMap<ResourceTypeEnum, Class<? extends IResourceAuditor<? extends IResource>>>();
	
	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, Bundle theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse)
			throws AuthenticationException {
		try{
			log.info("Auditing bundle: " + theResponseObject + " from request " + theRequestDetails);
			SecurityEvent auditEvent = new SecurityEvent();		
			
			SecurityEventObjectLifecycleEnum lifecycle = mapResourceTypeToSecurityLifecycle(theRequestDetails.getResourceOperationType());
			boolean hasAuditableEntry = false;
			byte[] query = getQueryFromRequestDetails(theRequestDetails);
			for(BundleEntry entry: theResponseObject.getEntries()){			
				IResource resource = entry.getResource();			
				boolean hasAuditableEntryInResource = addResourceObjectToEvent(auditEvent, resource, lifecycle, query);
				if(hasAuditableEntryInResource) hasAuditableEntry = true;
			}
			if(!hasAuditableEntry) return true; //no PHI to audit 			
			addParticipantToEvent(theServletRequest, auditEvent);
			store(auditEvent);
			return true;
		}catch(Exception e){
			log.error("Unable to audit resource: " + theResponseObject + " from request: " + theRequestDetails, e);
			return false;
		}
	}

	private void store(SecurityEvent auditEvent) {
		if(myDataStore == null) throw new InternalErrorException("No data store provided to persist audit events");
		myDataStore.store(auditEvent);
	}

	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, IResource theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse)
			throws AuthenticationException {
		try{
			log.info("Auditing resource: " + theResponseObject + " from request: " + theRequestDetails);
			SecurityEvent auditEvent = new SecurityEvent();
			byte[] query = getQueryFromRequestDetails(theRequestDetails);
			SecurityEventObjectLifecycleEnum lifecycle = mapResourceTypeToSecurityLifecycle(theRequestDetails.getResourceOperationType());
			boolean hasAuditableEntry = addResourceObjectToEvent(auditEvent, theResponseObject, lifecycle , query);
			if(!hasAuditableEntry) return true; //nothing to audit
			store(auditEvent);	
			return true;
		}catch(Exception e){
			log.error("Unable to audit resource: " + theResponseObject + " from request: " + theRequestDetails, e);
			return false;
		}
	}
	
	private byte[] getQueryFromRequestDetails(RequestDetails theRequestDetails) {
		byte[] query;
		try {
			query = theRequestDetails.getCompleteUrl().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			log.warn("Unable to encode URL to bytes in UTF-8, defaulting to platform default charset.", e1);
			query = theRequestDetails.getCompleteUrl().getBytes();
		}
		return query;
	}

	/**
	 * If the resource is considered an auditable resource containing PHI, add it as an object to the Security Event
	 * @param auditEvent
	 * @param resource
	 * @param lifecycle
	 * @param query
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	protected boolean addResourceObjectToEvent(SecurityEvent auditEvent, IResource resource, SecurityEventObjectLifecycleEnum lifecycle, byte[] query) throws InstantiationException, IllegalAccessException {
		//TODO: get resource name from IResource -- James will put this in the model
		//reference ResourceTypeEnum
		ResourceTypeEnum resourceType = null; //resource.getResourceType();		
		if(myAuditableResources.containsKey(resourceType)){									
			@SuppressWarnings("unchecked")
			IResourceAuditor<IResource> auditableResource = (IResourceAuditor<IResource>) myAuditableResources.get(resourceType).newInstance();			
			auditableResource.setResource(resource);
			if(auditableResource.isAuditable()){
				ObjectElement object = auditEvent.addObject();
				object.setReference(new ResourceReferenceDt(resource.getId()));
				object.setLifecycle(lifecycle);
				object.setQuery(query);
				object.setName(auditableResource.getName());
				object.setIdentifier(auditableResource.getIdentifier());
				object.setType(auditableResource.getType());
				object.setDescription(auditableResource.getDescription());
				object.setDetail(auditableResource.getDetail());
				object.setSensitivity(auditableResource.getSensitivity());
				return true;	
			}			
		}		
		return false; //not something we care to audit
	}
	
	private void addParticipantToEvent(HttpServletRequest theServletRequest, SecurityEvent auditEvent) {		
		if(theServletRequest.getHeader(Constants.HEADER_AUTHORIZATION) != null && theServletRequest.getHeader(Constants.HEADER_AUTHORIZATION).startsWith("OAuth")){
			//TODO: get user info from token
			throw new NotImplementedException("OAuth user auditing not yet implemented.");
		}else { //no auth or basic auth or anything else, use HTTP headers for user info
			String userId = theServletRequest.getHeader(UserInfoInterceptor.HEADER_USER_ID); 
			if(userId == null) userId = "anonymous"; //TODO: throw new InvalidParameterException(UserInfoInterceptor.HEADER_USER_ID + " must be specified as an HTTP header to access PHI.");			
			String userName = theServletRequest.getHeader(UserInfoInterceptor.HEADER_USER_NAME);
			if(userName == null) userName = "Anonymous";
			String userIp = theServletRequest.getRemoteAddr(); 
			Participant participant = auditEvent.addParticipant();
			participant.setUserId(userId);			
			participant.setName(userName);					
			ParticipantNetwork network = participant.getNetwork();
			network.setType(SecurityEventParticipantNetworkTypeEnum.IP_ADDRESS);
			network.setIdentifier(userIp);
		}
		
		
	}

	private SecurityEventObjectLifecycleEnum mapResourceTypeToSecurityLifecycle(RestfulOperationTypeEnum resourceOperationType) {
		switch (resourceOperationType) {
		case READ: return SecurityEventObjectLifecycleEnum.ACCESS_OR_USE;
		case CREATE: return SecurityEventObjectLifecycleEnum.ORIGINATION_OR_CREATION;
		case DELETE: return SecurityEventObjectLifecycleEnum.LOGICAL_DELETION;
		case HISTORY_INSTANCE: return SecurityEventObjectLifecycleEnum.ACCESS_OR_USE;
		case HISTORY_TYPE: return SecurityEventObjectLifecycleEnum.ACCESS_OR_USE;
		case SEARCH_TYPE: return SecurityEventObjectLifecycleEnum.ACCESS_OR_USE;
		case UPDATE: return SecurityEventObjectLifecycleEnum.AMENDMENT;
		case VALIDATE: return SecurityEventObjectLifecycleEnum.VERIFICATION;
		case VREAD: return SecurityEventObjectLifecycleEnum.ACCESS_OR_USE;
		default:
			return SecurityEventObjectLifecycleEnum.ACCESS_OR_USE; //access/use catch all
		}
	}
	
	public void setDataStore(IAuditDataStore theDataStore) {
		myDataStore = theDataStore;
	}
		
	public Map<ResourceTypeEnum, Class<? extends IResourceAuditor<? extends IResource>>> getAuditableResources() {
		return myAuditableResources;
	}
	
	public void setAuditableResources(Map<ResourceTypeEnum, Class<? extends IResourceAuditor<? extends IResource>>> theAuditableResources) {
		myAuditableResources = theAuditableResources;
	}
	
	public void addAuditableResource(ResourceTypeEnum resourceType, Class<? extends IResourceAuditor<? extends IResource>> auditableResource){
		if(myAuditableResources == null) myAuditableResources = new HashMap<ResourceTypeEnum, Class<? extends IResourceAuditor<? extends IResource>>>();
		myAuditableResources.put(resourceType, auditableResource);
	}
}
