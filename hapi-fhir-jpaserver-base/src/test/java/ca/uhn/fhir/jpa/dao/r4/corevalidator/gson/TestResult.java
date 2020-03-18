package ca.uhn.fhir.jpa.dao.r4.corevalidator.gson;

import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.sf.saxon.value.IntegerRange;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestResult {

    public static final int NO_WARNING = Integer.MIN_VALUE;

    @SerializedName("errorCount")
    @Expose
    private Integer errorCount = 0;
    @SerializedName("warningCount")
    @Expose
    private Integer warningCount = NO_WARNING;
    @SerializedName("output")
    @Expose
    private List<String> output = new ArrayList<>();

    public Integer getErrorCount() {
        return errorCount;
    }

    public TestResult setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
        return this;
    }

    public Integer getWarningCount() {
        return warningCount;
    }

    public TestResult setWarningCount(Integer warningCount) {
        this.warningCount = warningCount;
        return this;
    }

    public List<String> getOutput() {
        return output;
    }

    public TestResult setOutput(List<String> output) {
        this.output = output;
        return this;
    }

    @Override
    public String toString() {
        return "TestResult {" +
                "\nerrorCount=" + errorCount +
                (warningCount == NO_WARNING ? "" : "\nwarningCount=" + warningCount) +
                "\noutput=\n" + output.stream().collect(Collectors.joining("\n")) +
                "\n}";
    }

	/**
	 * Merges the test results with another passed in {@link TestResult}.
	 * @param testResult {@link TestResult}
	 *
	 * @return {@link TestResult}
	 */
	public TestResult merge(TestResult testResult) {
    	this.errorCount = this.errorCount + testResult.getErrorCount();
    	if (this.getWarningCount() == Integer.MIN_VALUE && testResult.getWarningCount() == Integer.MIN_VALUE) {
    		this.warningCount = Integer.MIN_VALUE;
		} else {
			this.warningCount = (this.warningCount == Integer.MIN_VALUE ? 0 : this.warningCount)
				+ (testResult.getWarningCount() == Integer.MIN_VALUE ? 0 : testResult.getWarningCount());
		}
    	this.output.addAll(testResult.getOutput());
    	return this;
	 }
}
