package org.hl7.fhir.instance.model;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.INarrative;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

public abstract class BaseNarrative extends Type implements INarrative {

	@Override
  public INarrative setStatusAsString(String theString) {
	  getStatusElement().setValueAsString(theString);
    return this;
  }

  @Override
  public String getStatusAsString() {
    return getStatusElement().getValueAsString();
  }

  /**
	 * Sets the value of
	 *
	 * @param theString
	 * @throws Exception
	 */
	public void setDivAsString(String theString) throws Exception {
		XhtmlNode div;
		if (StringUtils.isNotBlank(theString)) {
			div = new XhtmlNode();
			div.setValueAsString(theString);
		} else {
			div = null;
		}
		setDiv(div);
	}

	protected abstract BaseNarrative setDiv(XhtmlNode theDiv);

	public String getDivAsString() throws Exception {
		XhtmlNode div = getDiv();
		if (div != null && !div.isEmpty()) {
			return div.getValueAsString();
		} else {
			return null;
		}
	}

	protected abstract XhtmlNode getDiv();

  public abstract Enumeration<?> getStatusElement(); 

}
