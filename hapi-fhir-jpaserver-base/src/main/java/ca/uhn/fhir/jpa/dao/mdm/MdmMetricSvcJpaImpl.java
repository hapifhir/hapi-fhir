/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.mdm;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkJpaMetricsRepository;
import ca.uhn.fhir.mdm.api.BaseMdmMetricSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.params.GenerateMdmMetricsParameters;
import ca.uhn.fhir.mdm.model.MdmLinkMetrics;
import ca.uhn.fhir.mdm.model.MdmLinkScoreMetrics;
import ca.uhn.fhir.mdm.model.MdmMetrics;
import ca.uhn.fhir.mdm.model.MdmResourceMetrics;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MdmMetricSvcJpaImpl extends BaseMdmMetricSvc {

	private final IMdmLinkJpaMetricsRepository myJpaRepository;

	private final EntityManagerFactory myEntityManagerFactory;

	public MdmMetricSvcJpaImpl(
			IMdmLinkJpaMetricsRepository theRepository,
			DaoRegistry theDaoRegistry,
			EntityManagerFactory theEntityManagerFactory) {
		super(theDaoRegistry);
		myJpaRepository = theRepository;
		myEntityManagerFactory = theEntityManagerFactory;
	}

	protected MdmLinkMetrics generateLinkMetrics(GenerateMdmMetricsParameters theParameters) {
		List<MdmLinkSourceEnum> linkSources = theParameters.getLinkSourceFilters();
		List<MdmMatchResultEnum> matchResults = theParameters.getMatchResultFilters();

		if (linkSources.isEmpty()) {
			linkSources = Arrays.asList(MdmLinkSourceEnum.values());
		}
		if (matchResults.isEmpty()) {
			matchResults = Arrays.asList(MdmMatchResultEnum.values());
		}

		Object[][] data = myJpaRepository.generateMetrics(theParameters.getResourceType(), linkSources, matchResults);
		MdmLinkMetrics metrics = new MdmLinkMetrics();
		metrics.setResourceType(theParameters.getResourceType());
		for (Object[] row : data) {
			MdmMatchResultEnum matchResult = (MdmMatchResultEnum) row[0];
			MdmLinkSourceEnum source = (MdmLinkSourceEnum) row[1];
			long count = (Long) row[2];
			metrics.addMetric(matchResult, source, count);
		}
		return metrics;
	}

	protected MdmLinkScoreMetrics generateLinkScoreMetrics(GenerateMdmMetricsParameters theParameters) {
		String resourceType = theParameters.getResourceType();

		List<MdmMatchResultEnum> matchResultTypes = theParameters.getMatchResultFilters();

		// if no result type filter, add all result types
		if (matchResultTypes.isEmpty()) {
			matchResultTypes = Arrays.asList(MdmMatchResultEnum.values());
		}

		String sql = "SELECT %s FROM MPI_LINK ml WHERE ml.TARGET_TYPE = :resourceType "
				+ "AND ml.MATCH_RESULT in (:matchResult)";

		StringBuilder sb = new StringBuilder();
		sb.append("sum(case when ml.SCORE is null then 1 else 0 end) as B_" + NULL_VALUE);

		for (int i = 0; i < BUCKETS; i++) {
			double bucket = getBucket(i + 1);
			sb.append(",\n");
			if (i == 0) {
				// score <= .01
				sb.append(String.format("sum(case when ml.SCORE <= %.2f then 1 else 0 end) as B%d", bucket, i));
			} else {
				// score > i/100 && score <= i/100
				sb.append(String.format(
						"sum(case when ml.score > %.2f and ml.SCORE <= %.2f then 1 else 0 end) as B%d",
						getBucket(i), bucket, i));
			}
		}

		EntityManager em = myEntityManagerFactory.createEntityManager();

		Query nativeQuery = em.createNativeQuery(String.format(sql, sb.toString()));

		org.hibernate.query.Query<?> hibernateQuery = (org.hibernate.query.Query<?>) nativeQuery;

		hibernateQuery.setParameter("resourceType", resourceType);
		hibernateQuery.setParameter(
				"matchResult", matchResultTypes.stream().map(Enum::ordinal).collect(Collectors.toList()));

		List<?> results = hibernateQuery.getResultList();

		em.close();

		MdmLinkScoreMetrics metrics = new MdmLinkScoreMetrics();

		// we only get one row back
		Object[] row = (Object[]) results.get(0);
		int length = row.length;
		for (int i = 0; i < length; i++) {
			// if there's nothing in the db, these values will all be null
			Number bi = row[i] != null ? (Number) row[i] : BigInteger.valueOf(0);
			double bucket = getBucket(i);
			if (i == 0) {
				metrics.addScore(NULL_VALUE, bi.longValue());
			} else if (i == 1) {
				metrics.addScore(String.format(FIRST_BUCKET, bucket), bi.longValue());
			} else {
				metrics.addScore(String.format(NTH_BUCKET, getBucket(i - 1), bucket), bi.longValue());
			}
		}

		return metrics;
	}

	@Transactional
	@Override
	public MdmMetrics generateMdmMetrics(GenerateMdmMetricsParameters theParameters) {
		MdmResourceMetrics resourceMetrics = generateResourceMetrics(theParameters);
		MdmLinkMetrics linkMetrics = generateLinkMetrics(theParameters);
		MdmLinkScoreMetrics scoreMetrics = generateLinkScoreMetrics(theParameters);

		MdmMetrics metrics = MdmMetrics.fromSeperableMetrics(resourceMetrics, linkMetrics, scoreMetrics);
		return metrics;
	}
}
