 package ca.uhn.fhir.rest.server.interceptor;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.NotImplementedException;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.Event;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.ObjectElement;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.Participant;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.ParticipantNetwork;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.Source;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventActionEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectLifecycleEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventOutcomeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventParticipantNetworkTypeEnum;
import ca.uhn.fhir.rest.client.interceptor.UserInfoInterceptor;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.audit.IResourceAuditor;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.store.IAuditDataStore;

/**
 * Server interceptor that provides auditing capability
 * 
 * To use, set a data store that implements the IAuditDataStore interface, specify 
 */
public class AuditingInterceptor extends InterceptorAdapter {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuditingInterceptor.class);
	
	private IAuditDataStore myDataStore = null;	
	private Map<ResourceTypeEnum, Class<? extends IResourceAuditor<? extends IResource>>> myAuditableResources = new HashMap<ResourceTypeEnum, Class<? extends IResourceAuditor<? extends IResource>>>();
	private boolean myClientParamsOptional = false;
	
	public AuditingInterceptor() {
		myClientParamsOptional = false;
	}
	
	public AuditingInterceptor(boolean theClientParamsOptional){
		myClientParamsOptional = theClientParamsOptional;
	}
	
	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, Bundle theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse)
			throws AuthenticationException {
		if(myClientParamsOptional && myDataStore == null){
			//auditing is not required or configured, so do nothing here
			log.debug("No auditing configured.");
			return true;
		}
		try{
			log.info("Auditing bundle: " + theResponseObject + " from request " + theRequestDetails);
			SecurityEvent auditEvent = new SecurityEvent();	
			auditEvent.setEvent(getEventInfo(theRequestDetails));			
			//get user info from request if available			
			Participant participant = getParticipant(theServletRequest);
			if(participant == null) return true; //no user to audit - throws exception if client params are required
						
			SecurityEventObjectLifecycleEnum lifecycle = mapResourceTypeToSecurityLifecycle(theRequestDetails.getResourceOperationType());			
			byte[] query = getQueryFromRequestDetails(theRequestDetails);
			List<ObjectElement> auditableObjects = new ArrayList<SecurityEvent.ObjectElement>();
			for(BundleEntry entry: theResponseObject.getEntries()){			
				IResource resource = entry.getResource();		
				ObjectElement auditableObject = getObjectElement(resource, lifecycle , query);
				if(auditableObject != null) auditableObjects.add(auditableObject);				
			}
			if(!auditableObjects.isEmpty()) return true; //no PHI to audit
			auditEvent.setObject(auditableObjects);
			store(auditEvent);
			return true; //success
		}catch(Exception e){
			log.error("Unable to audit resource: " + theResponseObject + " from request: " + theRequestDetails, e);
			return false; //fail request
		}
	}
	
	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, IResource theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse)
			throws AuthenticationException {
		if(myClientParamsOptional && myDataStore == null){
			//auditing is not required or configured, so do nothing here
			log.debug("No auditing configured.");
			return true;
		}
		try{
			log.info("Auditing resource: " + theResponseObject + " from request: " + theRequestDetails);
			SecurityEvent auditEvent = new SecurityEvent();
			auditEvent.setEvent(getEventInfo(theRequestDetails));	
			
			//get user info from request if available			
			Participant participant = getParticipant(theServletRequest);
			if(participant == null) return true; //no user to audit - throws exception if client params are required
			
			byte[] query = getQueryFromRequestDetails(theRequestDetails);
			SecurityEventObjectLifecycleEnum lifecycle = mapResourceTypeToSecurityLifecycle(theRequestDetails.getResourceOperationType());
			ObjectElement auditableObject = getObjectElement(theResponseObject, lifecycle , query);
			if(auditableObject == null) return true; //nothing to audit
			List<ObjectElement> auditableObjects = new ArrayList<SecurityEvent.ObjectElement>(1);
			auditableObjects.add(auditableObject);
			auditEvent.setObject(auditableObjects);
			store(auditEvent);	
			return true;
		}catch(Exception e){
			log.error("Unable to audit resource: " + theResponseObject + " from request: " + theRequestDetails, e);
			return false;
		}
	}


	protected void store(SecurityEvent auditEvent) throws Exception {
		if(myDataStore == null) throw new InternalErrorException("No data store provided to persist audit events");
		myDataStore.store(auditEvent);
	}

	protected Event getEventInfo(RequestDetails theRequestDetails) {
		Event event = new Event();
		event.setAction(mapResourceTypeToSecurityEventAction(theRequestDetails.getResourceOperationType()));
		event.setDateTimeWithMillisPrecision(new Date());
		event.setOutcome(SecurityEventOutcomeEnum.SUCCESS); //we audit successful return of PHI only, otherwise an exception is thrown and no resources are returned to be audited		
		return event;
		
	}
	
	protected byte[] getQueryFromRequestDetails(RequestDetails theRequestDetails) {
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
	 * If the resource is considered an auditable resource containing PHI, create an ObjectElement, otherwise return null
	 * @param auditEvent
	 * @param resource
	 * @param lifecycle
	 * @param query
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	protected ObjectElement getObjectElement(IResource resource, SecurityEventObjectLifecycleEnum lifecycle, byte[] query) throws InstantiationException, IllegalAccessException {

		ResourceTypeEnum resourceType = resource.getResourceType();		
		if(myAuditableResources.containsKey(resourceType)){									
			@SuppressWarnings("unchecked")
			IResourceAuditor<IResource> auditableResource = (IResourceAuditor<IResource>) myAuditableResources.get(resourceType).newInstance();			
			auditableResource.setResource(resource);
			if(auditableResource.isAuditable()){
				ObjectElement object = new ObjectElement();
				object.setReference(new ResourceReferenceDt(resource.getId()));
				object.setLifecycle(lifecycle);
				object.setQuery(query);
				object.setName(auditableResource.getName());
				object.setIdentifier(auditableResource.getIdentifier());
				object.setType(auditableResource.getType());
				object.setDescription(auditableResource.getDescription());
				object.setDetail(auditableResource.getDetail());
				object.setSensitivity(auditableResource.getSensitivity());
				return object;	
			}			
		}		
		return null; //not something we care to audit
	}
	
	protected Participant getParticipant(HttpServletRequest theServletRequest) throws InvalidRequestException, NotImplementedException {		
		if(theServletRequest.getHeader(Constants.HEADER_AUTHORIZATION) != null && theServletRequest.getHeader(Constants.HEADER_AUTHORIZATION).startsWith("OAuth")){
			//TODO: get user info from token
			throw new NotImplementedException("OAuth user auditing not yet implemented.");
		}else { //no auth or basic auth or anything else, use HTTP headers for user info
			String userId = theServletRequest.getHeader(UserInfoInterceptor.HEADER_USER_ID); 
			if(userId == null){
				if(myClientParamsOptional) return null; //no auditing
				else throw new InvalidRequestException(UserInfoInterceptor.HEADER_USER_ID + " must be specified as an HTTP header to access PHI.");			
			}
			String userName = theServletRequest.getHeader(UserInfoInterceptor.HEADER_USER_NAME);
			if(userName == null) userName = "Anonymous";
			String userIp = theServletRequest.getRemoteAddr(); 
			Participant participant = new Participant();
			participant.setUserId(userId);			
			participant.setName(userName);					
			ParticipantNetwork network = participant.getNetwork();
			network.setType(SecurityEventParticipantNetworkTypeEnum.IP_ADDRESS);
			network.setIdentifier(userIp);
			return participant;
		}
		
	}

	protected SecurityEventActionEnum mapResourceTypeToSecurityEventAction(RestfulOperationTypeEnum resourceOperationType) {
		switch (resourceOperationType) {
		case READ: return SecurityEventActionEnum.READ_VIEW_PRINT;
		case CREATE: return SecurityEventActionEnum.CREATE;
		case DELETE: return SecurityEventActionEnum.DELETE;
		case HISTORY_INSTANCE: return SecurityEventActionEnum.READ_VIEW_PRINT;
		case HISTORY_TYPE: return SecurityEventActionEnum.READ_VIEW_PRINT;
		case SEARCH_TYPE: return SecurityEventActionEnum.READ_VIEW_PRINT;
		case UPDATE: return SecurityEventActionEnum.UPDATE;
		case VALIDATE: return SecurityEventActionEnum.READ_VIEW_PRINT;
		case VREAD: return SecurityEventActionEnum.READ_VIEW_PRINT;
		default:
			return SecurityEventActionEnum.READ_VIEW_PRINT; //read/view catch all
		}
	}
	
	//do we need both SecurityEventObjectLifecycleEnum and SecurityEventActionEnum? probably not
	protected SecurityEventObjectLifecycleEnum mapResourceTypeToSecurityLifecycle(RestfulOperationTypeEnum resourceOperationType) {
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
