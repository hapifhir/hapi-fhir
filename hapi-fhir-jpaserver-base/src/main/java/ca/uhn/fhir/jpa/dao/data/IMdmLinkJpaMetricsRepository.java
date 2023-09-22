package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("metricsRepository")
public interface IMdmLinkJpaMetricsRepository extends JpaRepository<MdmLink, Long>, IHapiFhirJpaRepository {

	@Query("SELECT ml.myMatchResult AS match_result, ml.myLinkSource AS link_source, count(*) AS c "
			+ "FROM MdmLink ml "
			+ "WHERE ml.myMdmSourceType = :resourceName "
			+ "AND ml.myLinkSource in (:linkSources) "
			+ "AND ml.myMatchResult in (:matchTypes) "
			+ "GROUP BY match_result, link_source "
			+ "ORDER BY match_result")
	Object[][] generateMetrics(
			@Param("resourceName") String theResourceType,
			@Param("linkSources") List<MdmLinkSourceEnum> theLinkSources,
			@Param("matchTypes") List<MdmMatchResultEnum> theMatchTypes);

//	@Query("SElECT "
//			+ ":buckets "
//			+ "FROM MdmLink ml "
//			+ "WHERE ml.myMdmSourceType = :resourceName "
//			+ "AND ml.myMatchResult in (:matchTypes) "
//			+ "GROUP BY score "
//			+ "ORDER BY score")
//	Object[][] generateScoreMetrics(
//		@Param("buckets") String theBucketsSql,
//		@Param("resourceName") String theResourceType,
//		@Param("matchTypes") List<MdmMatchResultEnum> theMatchTypes
//	);
}
