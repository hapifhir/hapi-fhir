package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ReferenceIndexTable extends BaseIndexTable {

	private final SearchSqlBuilder mySearchSqlBuilder;
	private final DbColumn myColumnSrcType;
	private final DbColumn myColumnSrcPath;
	private final DbColumn myColumnTargetResourceId;
	private final DbColumn myColumnTargetResourceUrl;
	private final DbColumn myColumnSrcResourceId;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	/**
	 * Constructor
	 */
	public ReferenceIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_RES_LINK"));
		mySearchSqlBuilder = theSearchSqlBuilder;
		myColumnSrcResourceId = getTable().addColumn("SRC_RESOURCE_ID");
		myColumnSrcType = getTable().addColumn("SOURCE_RESOURCE_TYPE");
		myColumnSrcPath = getTable().addColumn("SRC_PATH");
		myColumnTargetResourceId = getTable().addColumn("TARGET_RESOURCE_ID");
		myColumnTargetResourceUrl = getTable().addColumn("TARGET_RESOURCE_URL");
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnSrcResourceId;
	}

	public void addPredicate(List<? extends IQueryParameterType> theReferenceOrParamList) {

		List<IIdType> targetIds = new ArrayList<>();
		List<String> targetQualifiedUrls = new ArrayList<>();

		for (int orIdx = 0; orIdx < theReferenceOrParamList.size(); orIdx++) {
			IQueryParameterType nextOr = theReferenceOrParamList.get(orIdx);

			if (nextOr instanceof ReferenceParam) {
				ReferenceParam ref = (ReferenceParam) nextOr;

				if (isBlank(ref.getChain())) {

					/*
					 * Handle non-chained search, e.g. Patient?organization=Organization/123
					 */

					IIdType dt = new IdDt(ref.getBaseUrl(), ref.getResourceType(), ref.getIdPart(), null);

					if (dt.hasBaseUrl()) {
						if (myDaoConfig.getTreatBaseUrlsAsLocal().contains(dt.getBaseUrl())) {
							dt = dt.toUnqualified();
							targetIds.add(dt);
						} else {
							targetQualifiedUrls.add(dt.getValue());
						}
					} else {
						targetIds.add(dt);
					}

				} else {

					/*
					 * Handle chained search, e.g. Patient?organization.name=Kwik-e-mart
					 */

					// FIXME: implement
					throw new UnsupportedOperationException();
//					return addPredicateReferenceWithChain(theResourceName, theParamName, theReferenceOrParamList, join, new ArrayList<>(), ref, theRequest, theRequestPartitionId);

				}

			} else {
				throw new IllegalArgumentException("Invalid token type (expecting ReferenceParam): " + nextOr.getClass());
			}

		}


		if (getRequestPartitionId() != null) {
			addPartitionIdPredicate(getRequestPartitionId().getPartitionId());
		}

		List<Condition> codePredicates = new ArrayList<>();
		for (IIdType next : targetIds) {
			if (!next.hasResourceType()) {
				warnAboutPerformanceOnUnqualifiedResources(theParamName, theRequest, null);
			}
		}

		List<String> pathsToMatch = createResourceLinkPathPredicate(theResourceName, theParamName);
		boolean inverse;
		if ((operation == null) || (operation == SearchFilterParser.CompareOperation.eq)) {
			inverse = false;
		} else {
			inverse = true;
		}

		List<ResourcePersistentId> targetPids = myIdHelperService.resolveResourcePersistentIdsWithCache(theRequestPartitionId, targetIds);
		List<Long> targetPidList = ResourcePersistentId.toLongList(targetPids);

		if (targetPidList.isEmpty() && targetQualifiedUrls.isEmpty()) {
			getSqlBuilder().setMatchNothing();
		} else {
			join.addPredicateReference(theParamName, inverse, pathsToMatch, targetPidList, targetQualifiedUrls);
		}

	}

	public void addPredicateReference(String theParamName, boolean theInverse, List<String> thePathsToMatch, List<Long> theTargetPidList, List<String> theTargetQualifiedUrls) {

		Condition targetPidCondition = null;
		if (!theTargetPidList.isEmpty()) {
			targetPidCondition = new InCondition(myColumnTargetResourceId, generatePlaceholders(theTargetPidList));
		}

		Condition targetUrlsCondition = null;
		if (!theTargetQualifiedUrls.isEmpty()) {
			targetUrlsCondition = new InCondition(myColumnTargetResourceUrl, generatePlaceholders(theTargetQualifiedUrls));
		}

		Condition joinedCondition;
		if (targetPidCondition != null && targetUrlsCondition != null) {
			joinedCondition = ComboCondition.or(targetPidCondition, targetUrlsCondition);
		} else if (targetPidCondition != null) {
			joinedCondition = targetPidCondition;
		} else {
			joinedCondition = targetUrlsCondition;
		}

		InCondition pathPredicate = new InCondition(myColumnSrcPath, generatePlaceholders(thePathsToMatch));
		joinedCondition = ComboCondition.and(pathPredicate, joinedCondition);

		if (theInverse) {
			addCondition(new NotCondition(joinedCondition));
		} else {
			addCondition(joinedCondition);
		}

	}

	private void warnAboutPerformanceOnUnqualifiedResources(String theParamName, RequestDetails theRequest, @Nullable List<Class<? extends IBaseResource>> theCandidateTargetTypes) {
		StringBuilder builder = new StringBuilder();
		builder.append("This search uses an unqualified resource(a parameter in a chain without a resource type). ");
		builder.append("This is less efficient than using a qualified type. ");
		if (theCandidateTargetTypes != null) {
			builder.append("[" + theParamName + "] resolves to [" + theCandidateTargetTypes.stream().map(Class::getSimpleName).collect(Collectors.joining(",")) + "].");
			builder.append("If you know what you're looking for, try qualifying it using the form ");
			builder.append(theCandidateTargetTypes.stream().map(cls -> "[" + cls.getSimpleName() + ":" + theParamName + "]").collect(Collectors.joining(" or ")));
		} else {
			builder.append("If you know what you're looking for, try qualifying it using the form: '");
			builder.append(theParamName).append(":[resourceType]");
			builder.append("'");
		}
		String message = builder
			.toString();
		StorageProcessingMessage msg = new StorageProcessingMessage()
			.setMessage(message);
		HookParams params = new HookParams()
			.add(RequestDetails.class, theRequest)
			.addIfMatchesType(ServletRequestDetails.class, theRequest)
			.add(StorageProcessingMessage.class, msg);
		JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_WARNING, params);
	}

}
