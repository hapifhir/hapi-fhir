 package ca.uhn.fhir.rest.server.interceptor;

/*
 * #%L
 * HAPI FHIR Structures - DSTU (FHIR 0.80)
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
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.NotImplementedException;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.Event;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.ObjectDetail;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.ObjectElement;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.Participant;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.ParticipantNetwork;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.Source;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventActionEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectLifecycleEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventOutcomeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventParticipantNetworkTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventSourceTypeEnum;
import ca.uhn.fhir.model.primitive.StringDt;
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
	private Map<String, Class<? extends IResourceAuditor<? extends IResource>>> myAuditableResources = new HashMap<String, Class<? extends IResourceAuditor<? extends IResource>>>();
	private boolean myClientParamsOptional = false;
	protected String mySiteId;
	
	public AuditingInterceptor() {
		mySiteId = "NONE";
		myClientParamsOptional = false;		
	}
	
	public AuditingInterceptor(String theSiteId) {
		mySiteId = theSiteId;
		myClientParamsOptional = false;		
	}
	
	public AuditingInterceptor(String theSiteId, boolean theClientParamsOptional){
		mySiteId = theSiteId;
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
			if(participant == null){
				log.debug("No participant to audit");
				return true; //no user to audit - throws exception if client params are required
			}
			List<Participant> participants = new ArrayList<SecurityEvent.Participant>(1);
			participants.add(participant);
			auditEvent.setParticipant(participants);
			
			SecurityEventObjectLifecycleEnum lifecycle = mapResourceTypeToSecurityLifecycle(theRequestDetails.getResourceOperationType());			
			byte[] query = getQueryFromRequestDetails(theRequestDetails);
			List<ObjectElement> auditableObjects = new ArrayList<SecurityEvent.ObjectElement>();
			for(BundleEntry entry: theResponseObject.getEntries()){			
				IResource resource = entry.getResource();		
				ObjectElement auditableObject = getObjectElement(resource, lifecycle , query);
				if(auditableObject != null) auditableObjects.add(auditableObject);				
			}
			if(auditableObjects.isEmpty()){
				log.debug("No auditable resources to audit.");
				return true; //no PHI to audit
			}else{
				log.debug("Auditing " + auditableObjects.size() + " resources.");
			}
			auditEvent.setObject(auditableObjects);
			auditEvent.setSource(getSourceElement(theServletRequest));
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
			if(participant == null){
				log.debug("No participant to audit");
				return true; //no user to audit - throws exception if client params are required
			}
			List<Participant> participants = new ArrayList<SecurityEvent.Participant>(1);
			participants.add(participant);
			auditEvent.setParticipant(participants);
			
			byte[] query = getQueryFromRequestDetails(theRequestDetails);
			SecurityEventObjectLifecycleEnum lifecycle = mapResourceTypeToSecurityLifecycle(theRequestDetails.getResourceOperationType());
			ObjectElement auditableObject = getObjectElement(theResponseObject, lifecycle , query);
			if(auditableObject == null){
				log.debug("No auditable resources to audit");
				return true; 
			}
			List<ObjectElement> auditableObjects = new ArrayList<SecurityEvent.ObjectElement>(1);
			auditableObjects.add(auditableObject);
			auditEvent.setObject(auditableObjects);
			auditEvent.setSource(getSourceElement(theServletRequest));
			log.debug("Auditing one resource.");
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

		String resourceType = resource.getResourceName();		
		if(myAuditableResources.containsKey(resourceType)){		
			log.debug("Found auditable resource of type: " + resourceType);
			@SuppressWarnings("unchecked")
			IResourceAuditor<IResource> auditableResource = (IResourceAuditor<IResource>) myAuditableResources.get(resourceType).newInstance();			
			auditableResource.setResource(resource);
			if(auditableResource.isAuditable()){
				ObjectElement object = new ObjectElement();
				object.setReference(new ResourceReferenceDt(resource.getId()));
				object.setLifecycle(lifecycle);
				object.setQuery(query);
				object.setName(auditableResource.getName());
				object.setIdentifier((IdentifierDt) auditableResource.getIdentifier());
				object.setType(auditableResource.getType());
				object.setDescription(auditableResource.getDescription());
				Map<String, String> detailMap = auditableResource.getDetail();
				if(detailMap != null && !detailMap.isEmpty()){
					List<ObjectDetail> details = new ArrayList<SecurityEvent.ObjectDetail>();
					for(Entry<String, String> entry: detailMap.entrySet()){
						ObjectDetail detail = makeObjectDetail(entry.getKey(), entry.getValue());
						details.add(detail);
					}				
					object.setDetail(details);	
				}				
				object.setSensitivity(auditableResource.getSensitivity());				
				return object;	
			}else{
				log.debug("Resource is not auditable");
			}
		}else{
			log.debug("No auditor configured for resource type " + resourceType);
		}
		return null; //not something we care to audit
	}
	
	protected ObjectDetail makeObjectDetail(String type, String value) {	
		ObjectDetail detail = new ObjectDetail();
		if(type != null)
			detail.setType(type);
		if(value != null)
			detail.setValue(value.getBytes());
		return detail;
	}
	
	protected Participant getParticipant(HttpServletRequest theServletRequest) throws InvalidRequestException, NotImplementedException {		
		if(theServletRequest.getHeader(Constants.HEADER_AUTHORIZATION) != null && theServletRequest.getHeader(Constants.HEADER_AUTHORIZATION).startsWith("OAuth")){
			if(myClientParamsOptional){
				log.debug("OAuth request received but no auditing required.");
				return null; 
			}
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
	
	protected Source getSourceElement(HttpServletRequest theServletRequest) {
		if(theServletRequest.getHeader(Constants.HEADER_AUTHORIZATION) != null && theServletRequest.getHeader(Constants.HEADER_AUTHORIZATION).startsWith("OAuth")){
			if(myClientParamsOptional) return null; //no auditing required
			//TODO: get application info from token
			throw new NotImplementedException("OAuth auditing not yet implemented.");
		}else { //no auth or basic auth or anything else, use HTTP headers for audit info
			String appId = theServletRequest.getHeader(UserInfoInterceptor.HEADER_APPLICATION_NAME); 			
			Source source = new Source();
			source.setIdentifier(appId);
			source.setType(getAccessType(theServletRequest));
			source.setSite(getSiteId(theServletRequest));
			return source;
		}		
	}

	protected StringDt getSiteId(HttpServletRequest theServletRequest) {
		return new StringDt(mySiteId); //override this method to pull the site id from the request info
	}

	protected List<CodingDt> getAccessType(HttpServletRequest theServletRequest) {
		List<CodingDt> types = new ArrayList<CodingDt>();		
		if(theServletRequest.getHeader(Constants.HEADER_AUTHORIZATION) != null && theServletRequest.getHeader(Constants.HEADER_AUTHORIZATION).startsWith("OAuth")){
			types.add(new CodingDt(SecurityEventSourceTypeEnum.USER_DEVICE.getSystem(), SecurityEventSourceTypeEnum.USER_DEVICE.getCode()));			
		}else{
			String userId = theServletRequest.getHeader(UserInfoInterceptor.HEADER_USER_ID); 
			String appId = theServletRequest.getHeader(UserInfoInterceptor.HEADER_APPLICATION_NAME);
			if (userId == null && appId != null) types.add(new CodingDt(SecurityEventSourceTypeEnum.APPLICATION_SERVER.getSystem(), SecurityEventSourceTypeEnum.APPLICATION_SERVER.getCode()));
			else types.add(new CodingDt(SecurityEventSourceTypeEnum.USER_DEVICE.getSystem(), SecurityEventSourceTypeEnum.USER_DEVICE.getCode()));
		}
		return types;		
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
		
	public Map<String, Class<? extends IResourceAuditor<? extends IResource>>> getAuditableResources() {
		return myAuditableResources;
	}
	
	public void setAuditableResources(Map<String, Class<? extends IResourceAuditor<? extends IResource>>> theAuditableResources) {
		myAuditableResources = theAuditableResources;
	}
	
	public void addAuditableResource(String resourceType, Class<? extends IResourceAuditor<? extends IResource>> auditableResource){
		if(myAuditableResources == null) myAuditableResources = new HashMap<String, Class<? extends IResourceAuditor<? extends IResource>>>();
		myAuditableResources.put(resourceType, auditableResource);
	}
}
