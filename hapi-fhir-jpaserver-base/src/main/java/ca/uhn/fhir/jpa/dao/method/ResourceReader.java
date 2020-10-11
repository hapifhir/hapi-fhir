package ca.uhn.fhir.jpa.dao.method;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.BaseHasResource;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.InstantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@Scope("prototype")
public class ResourceReader<T extends IBaseResource> extends BaseMethodService<T> {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceReader.class);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	public ResourceReader(BaseHapiFhirResourceDao<T> theDao) {
		super(theDao);
	}

	public <T extends IBaseResource> T read(IIdType theId) {
		return read(theId, null);
	}

	public <T extends IBaseResource> T read(IIdType theId, RequestDetails theRequestDetails) {
		return read(theId, theRequestDetails, false);
	}

	public <T extends IBaseResource> T read(IIdType theId, RequestDetails theRequest, boolean theDeletedOk) {
		validateResourceTypeAndThrowInvalidRequestException(theId);

		return myTransactionService.execute(theRequest, tx-> doRead(theId, theRequest, theDeletedOk));
	}

	private <T extends IBaseResource> T doRead(IIdType theId, RequestDetails theRequest, boolean theDeletedOk) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		// Notify interceptors
		if (theRequest != null) {
			IServerInterceptor.ActionRequestDetails requestDetails = new IServerInterceptor.ActionRequestDetails(theRequest, getResourceName(), theId);
			RestOperationTypeEnum operationType = theId.hasVersionIdPart() ? RestOperationTypeEnum.VREAD : RestOperationTypeEnum.READ;
			myDao.notifyInterceptors(operationType, requestDetails);
		}

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theId, theRequest);
		validateResourceType(entity);

		T retVal = (T) myDao.toResource(getResourceType(), entity, null, false);

		if (theDeletedOk == false) {
			if (entity.getDeleted() != null) {
				throw createResourceGoneException(entity);
			}
		}

		// Interceptor broadcast: STORAGE_PREACCESS_RESOURCES
		{
			SimplePreResourceAccessDetails accessDetails = new SimplePreResourceAccessDetails(retVal);
			HookParams params = new HookParams()
				.add(IPreResourceAccessDetails.class, accessDetails)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest);
			JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);
			if (accessDetails.isDontReturnResourceAtIndex(0)) {
				throw new ResourceNotFoundException(theId);
			}
		}

		// Interceptor broadcast: STORAGE_PRESHOW_RESOURCES
		{
			SimplePreResourceShowDetails showDetails = new SimplePreResourceShowDetails(retVal);
			HookParams params = new HookParams()
				.add(IPreResourceShowDetails.class, showDetails)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest);
			JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PRESHOW_RESOURCES, params);
			//noinspection unchecked
			retVal = (T) showDetails.getResource(0);
		}

		ourLog.debug("Processed read on {} in {}ms", theId.getValue(), w.getMillisAndRestart());
		return retVal;
	}

	private void validateResourceTypeAndThrowInvalidRequestException(IIdType theId) {
		if (theId.hasResourceType() && !theId.getResourceType().equals(getResourceName())) {
			// Note- Throw a HAPI FHIR exception here so that hibernate doesn't try to translate it into a database exception
			throw new InvalidRequestException("Incorrect resource type (" + theId.getResourceType() + ") for this DAO, wanted: " + getResourceName());
		}
	}

	public BaseHasResource readEntity(IIdType theId, RequestDetails theRequest) {
		return readEntity(theId, true, theRequest);
	}

	public BaseHasResource readEntity(IIdType theId, boolean theCheckForForcedId, RequestDetails theRequest) {
		validateResourceTypeAndThrowInvalidRequestException(theId);

		RequestPartitionId requestPartitionId = myRequestPartitionHelperService.determineReadPartitionForRequest(theRequest, getResourceName());
		ResourcePersistentId pid = myIdHelperService.resolveResourcePersistentIds(requestPartitionId, getResourceName(), theId.getIdPart());
		BaseHasResource entity = myEntityManager.find(ResourceTable.class, pid.getIdAsLong());

		// Verify that the resource is for the correct partition
		if (!requestPartitionId.isAllPartitions()) {
			if (requestPartitionId.getPartitionId() == null) {
				if (entity.getPartitionId().getPartitionId() != null) {
					ourLog.debug("Performing a read for PartitionId={} but entity has partition: {}", requestPartitionId, entity.getPartitionId());
					entity = null;
				}
			} else if (entity.getPartitionId().getPartitionId() != null) {
				if (!requestPartitionId.getPartitionId().equals(entity.getPartitionId().getPartitionId())) {
					ourLog.debug("Performing a read for PartitionId={} but entity has partition: {}", requestPartitionId, entity.getPartitionId());
					entity = null;
				}
			} else {
				ourLog.debug("Performing a read for PartitionId=null but entity has partition: {}", entity.getPartitionId());
				entity = null;
			}
		}

		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}

		if (theId.hasVersionIdPart()) {
			if (theId.isVersionIdPartValidLong() == false) {
				throw new ResourceNotFoundException(myFhirContext.getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "invalidVersion", theId.getVersionIdPart(), theId.toUnqualifiedVersionless()));
			}
			if (entity.getVersion() != theId.getVersionIdPartAsLong()) {
				entity = null;
			}
		}

		if (entity == null) {
			if (theId.hasVersionIdPart()) {
				TypedQuery<ResourceHistoryTable> q = myEntityManager.createQuery("SELECT t from ResourceHistoryTable t WHERE t.myResourceId = :RID AND t.myResourceType = :RTYP AND t.myResourceVersion = :RVER", ResourceHistoryTable.class);
				q.setParameter("RID", pid.getId());
				q.setParameter("RTYP", getResourceName());
				q.setParameter("RVER", theId.getVersionIdPartAsLong());
				try {
					entity = q.getSingleResult();
				} catch (NoResultException e) {
					throw new ResourceNotFoundException(myFhirContext.getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "invalidVersion", theId.getVersionIdPart(), theId.toUnqualifiedVersionless()));
				}
			}
		}

		validateResourceType(entity);

		if (theCheckForForcedId) {
			validateGivenIdIsAppropriateToRetrieveResource(theId, entity);
		}
		return entity;
	}

	public ResourceTable readEntityLatestVersion(IIdType theId, RequestPartitionId theRequestPartitionId) {
		validateResourceTypeAndThrowInvalidRequestException(theId);

		ResourcePersistentId persistentId = myIdHelperService.resolveResourcePersistentIds(theRequestPartitionId, getResourceName(), theId.getIdPart());
		ResourceTable entity = myEntityManager.find(ResourceTable.class, persistentId.getId());
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}
		validateGivenIdIsAppropriateToRetrieveResource(theId, entity);
		entity.setTransientForcedId(theId.getIdPart());
		return entity;
	}

	private void validateResourceType(BaseHasResource entity) {
		BaseHapiFhirDao.validateResourceType(entity, getResourceName());
	}

	public T readByPid(ResourcePersistentId thePid) {
		StopWatch w = new StopWatch();

		Optional<ResourceTable> entity = myResourceTableDao.findById(thePid.getIdAsLong());
		if (!entity.isPresent()) {
			throw new ResourceNotFoundException("No resource found with PID " + thePid);
		}
		if (entity.get().getDeleted() != null) {
			throw createResourceGoneException(entity.get());
		}

		T retVal = myDao.toResource(getResourceType(), entity.get(), null, false);

		ourLog.debug("Processed read on {} in {}ms", thePid, w.getMillis());
		return retVal;
	}

	@NotNull
	protected ResourceGoneException createResourceGoneException(IBasePersistedResource theResourceEntity) {
		StringBuilder b = new StringBuilder();
		b.append("Resource was deleted at ");
		b.append(new InstantType(theResourceEntity.getDeleted()).getValueAsString());
		ResourceGoneException retVal = new ResourceGoneException(b.toString());
		retVal.setResourceId(theResourceEntity.getIdDt());
		return retVal;
	}

	private void validateGivenIdIsAppropriateToRetrieveResource(IIdType theId, BaseHasResource entity) {
		if (entity.getForcedId() != null) {
			if (myDaoConfig.getResourceClientIdStrategy() != DaoConfig.ClientIdStrategyEnum.ANY) {
				if (theId.isIdPartValidLong()) {
					// This means that the resource with the given numeric ID exists, but it has a "forced ID", meaning that
					// as far as the outside world is concerned, the given ID doesn't exist (it's just an internal pointer
					// to the
					// forced ID)
					throw new ResourceNotFoundException(theId);
				}
			}
		}
	}
}
