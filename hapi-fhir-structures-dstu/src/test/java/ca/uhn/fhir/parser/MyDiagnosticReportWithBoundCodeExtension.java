package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.ElementUtil;

/**
 * See #120
 */
@ResourceDef(name = "DiagnosticReport")
public class MyDiagnosticReportWithBoundCodeExtension extends DiagnosticReport {

	private static final long serialVersionUID = 1L;

	public static final String SP_IMAGING_STUDY = "ImagingStudy";

	/**
	 * Each extension is defined in a field. Any valid HAPI Data Type can be used for the field type. Note that the
	 * [name=""] attribute in the @Child annotation needs to match the name for the bean accessor and mutator methods.
	 */
//	@Child(name = "taskId", order = 16)
//	@Extension(url = "http://agfa.com/extensions#taskId", definedLocally = true, isModifier = false)
//	@Description(shortDefinition = "The task id")
	private IntegerDt taskId;

	@Child(name = "workflowAction", type = CodeDt.class, order = 17)
	@Extension(url = "http://agfa.com/extensions#workflowAction", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "sign-off |sign-off-later |correctionist +", formalDefinition = "The work-flow action")
	private BoundCodeDt<WorkflowActionEnum> workflowAction;

//	@Child(name = "workflowComments", order = 18)
//	@Extension(url = "http://agfa.com/extensions#workflowComments", definedLocally = true, isModifier = false)
//	@Description(shortDefinition = "The work-flow comments")
	private StringDt workflowComments;

	/**
	 * It is important to override the isEmpty() method, adding a check for any newly added fields.
	 */
	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(taskId, workflowAction, workflowComments);
	}

	/********
	 * Accessors and mutators follow
	 *
	 * IMPORTANT: Each extension is required to have an getter/accessor and a stter/mutator. You are highly recommended
	 * to create getters which create instances if they do not already exist, since this is how the rest of the HAPI
	 * FHIR API works.
	 ********/

	/** Getter for mandatory */

	public IntegerDt getTaskId() {
		if (taskId == null) {
			taskId = new IntegerDt();
		}
		return taskId;
	}

	public MyDiagnosticReportWithBoundCodeExtension setTaskId(int taskId) {
		this.taskId = new IntegerDt(taskId);
		return this;
	}

	public MyDiagnosticReportWithBoundCodeExtension setTaskId(IntegerDt taskId) {
		this.taskId = taskId;
		return this;
	}

	public BoundCodeDt<WorkflowActionEnum> getWorkflowAction() {
		if (workflowAction == null) {
			workflowAction = new BoundCodeDt<WorkflowActionEnum>(WorkflowActionEnum.VALUESET_BINDER);
		}

		return workflowAction;
	}

	public MyDiagnosticReportWithBoundCodeExtension setWorkflowAction(BoundCodeDt<WorkflowActionEnum> workflowAction) {
		this.workflowAction = workflowAction;

		return this;
	}

	public MyDiagnosticReportWithBoundCodeExtension setWorkflowAction(WorkflowActionEnum workflowAction) {
		getWorkflowAction().setValueAsEnum(workflowAction);
		return this;
	}

	public StringDt getWorkflowComments() {
		if (workflowComments == null) {
			workflowComments = new StringDt();
		}
		return workflowComments;
	}

	public MyDiagnosticReportWithBoundCodeExtension setWorkflowComments(StringDt workflowComments) {
		this.workflowComments = workflowComments;
		return this;
	}

	public MyDiagnosticReportWithBoundCodeExtension setWorkflowComments(String workflowComments) {
		this.workflowComments = new StringDt(workflowComments);
		return this;
	}

}