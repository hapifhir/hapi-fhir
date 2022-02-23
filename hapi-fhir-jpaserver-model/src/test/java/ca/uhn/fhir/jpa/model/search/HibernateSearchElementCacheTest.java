package ca.uhn.fhir.jpa.model.search;

import org.hamcrest.Matchers;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.engine.backend.document.IndexObjectFieldReference;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class HibernateSearchElementCacheTest {
	static class TestDocumentElement implements DocumentElement {
		final TestDocumentElement myParent;

		TestDocumentElement(TestDocumentElement myParent) {
			this.myParent = myParent;
		}

		@Override
		public <F> void addValue(IndexFieldReference<F> fieldReference, F value) {
			// nop
		}

		@Override
		public DocumentElement addObject(IndexObjectFieldReference fieldReference) {
			// nop
			return null;
		}

		@Override
		public void addNullObject(IndexObjectFieldReference fieldReference) {
			// not used
		}

		@Override
		public void addValue(String relativeFieldName, Object value) {
			// not used
		}

		@Override
		public DocumentElement addObject(String relativeFieldName) {
			return new TestDocumentElement(this);
		}

		@Override
		public void addNullObject(String relativeFieldName) {
			// not used;
		}
	}

	TestDocumentElement myRoot = new TestDocumentElement(null);
	HibernateSearchElementCache mySvc = new HibernateSearchElementCache(myRoot);

	@Test
	public void emptyPathReturnsRoot() {
		assertThat(mySvc.getObjectElement(), Matchers.sameInstance(myRoot));
	}

	@Test
	public void simpleChildIsRemembered() {
		DocumentElement child = mySvc.getObjectElement("child");

		assertThat(mySvc.getObjectElement("child"), Matchers.sameInstance(child));
	}

	@Test
	public void deeperPathRemembered() {
		DocumentElement child = mySvc.getObjectElement("child",  "grandchild");

		assertThat(mySvc.getObjectElement("child", "grandchild"), Matchers.sameInstance(child));
	}

	@Test
	public void grandchildParentIsChild() {
		DocumentElement child = mySvc.getObjectElement("child");
		TestDocumentElement grandChild = (TestDocumentElement) mySvc.getObjectElement("child", "grandchild");
		assertThat(grandChild.myParent, Matchers.sameInstance(child));
	}

}
