package ca.uhn.fhir.jpa.mdm.helper;

import ca.uhn.fhir.jpa.dao.data.IMdmLinkDao;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.model.primitive.IdDt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MdmLinkHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmLinkHelper.class);

	@Autowired
	IMdmLinkDao myMdmLinkDao;

	@Transactional
	public void logMdmLinks() {
		List<MdmLink> links = myMdmLinkDao.findAll();
		ourLog.info("All MDM Links:");
		for (MdmLink link : links) {
			IdDt goldenResourceId = link.getGoldenResource().getIdDt().toVersionless();
			IdDt targetId = link.getSource().getIdDt().toVersionless();
			ourLog.info("{}: {}, {}, {}, {}", link.getId(), goldenResourceId, targetId, link.getMatchResult(), link.getLinkSource());
		}
	}
}
