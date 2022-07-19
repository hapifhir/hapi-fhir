package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptPropertyDao;
import ca.uhn.fhir.jpa.term.job.TermCodeSystemSvc;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TermCodeSystemSvcTest {

	@Mock
	private ITermConceptDao myConceptDao;

	@Mock
	private ITermCodeSystemDao myCodeSystemDao;

	@Mock
	private ITermCodeSystemVersionDao myTermCodeSystemVersionDao;

	@Mock
	private ITermConceptParentChildLinkDao myConceptParentChildLinkDao;

	@Mock
	private ITermConceptPropertyDao myConceptPropertyDao;

	@Mock
	private ITermConceptDesignationDao myConceptDesignationDao;

	@Mock
	private ITermCodeSystemDao myTermCodeSystemDao;

	@InjectMocks
	private TermCodeSystemSvc myTermCodeSystemSvc;


}
