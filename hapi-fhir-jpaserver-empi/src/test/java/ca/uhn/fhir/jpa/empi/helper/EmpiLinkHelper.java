package ca.uhn.fhir.jpa.empi.helper;

import ca.uhn.fhir.jpa.dao.data.IEmpiLinkDao;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.model.primitive.IdDt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpiLinkHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiLinkHelper.class);

	@Autowired
	IEmpiLinkDao myEmpiLinkDao;

	@Transactional
	public void logEmpiLinks() {
		List<EmpiLink> links = myEmpiLinkDao.findAll();
		ourLog.info("All EMPI Links:");
		for (EmpiLink link : links) {
			IdDt personId = link.getSourceResource().getIdDt().toVersionless();
			IdDt targetId = link.getTarget().getIdDt().toVersionless();
			ourLog.info("{}: {}, {}, {}, {}", link.getId(), personId, targetId, link.getMatchResult(), link.getLinkSource());
		}
	}
}
