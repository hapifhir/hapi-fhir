package ca.uhn.fhir.jpa.sched;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.JobFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

class NullScheduler implements Scheduler {
	@Override
	public String getSchedulerName() {
		return null;
	}

	@Override
	public String getSchedulerInstanceId() {
		return null;
	}

	@Override
	public SchedulerContext getContext() {
		return null;
	}

	@Override
	public void start() {

	}

	@Override
	public void startDelayed(int seconds) {

	}

	@Override
	public boolean isStarted() {
		return false;
	}

	@Override
	public void standby() {

	}

	@Override
	public boolean isInStandbyMode() {
		return false;
	}

	@Override
	public void shutdown() {

	}

	@Override
	public void shutdown(boolean waitForJobsToComplete) {

	}

	@Override
	public boolean isShutdown() {
		return false;
	}

	@Override
	public SchedulerMetaData getMetaData() {
		return null;
	}

	@Override
	public List<JobExecutionContext> getCurrentlyExecutingJobs() {
		return null;
	}

	@Override
	public void setJobFactory(JobFactory factory) {

	}

	@Override
	public ListenerManager getListenerManager() {
		return null;
	}

	@Override
	public Date scheduleJob(JobDetail jobDetail, Trigger trigger) {
		return null;
	}

	@Override
	public Date scheduleJob(Trigger trigger) {
		return null;
	}

	@Override
	public void scheduleJobs(Map<JobDetail, Set<? extends Trigger>> triggersAndJobs, boolean replace) {

	}

	@Override
	public void scheduleJob(JobDetail jobDetail, Set<? extends Trigger> triggersForJob, boolean replace) {

	}

	@Override
	public boolean unscheduleJob(TriggerKey triggerKey) {
		return false;
	}

	@Override
	public boolean unscheduleJobs(List<TriggerKey> triggerKeys) {
		return false;
	}

	@Override
	public Date rescheduleJob(TriggerKey triggerKey, Trigger newTrigger) {
		return null;
	}

	@Override
	public void addJob(JobDetail jobDetail, boolean replace) {

	}

	@Override
	public void addJob(JobDetail jobDetail, boolean replace, boolean storeNonDurableWhileAwaitingScheduling) {

	}

	@Override
	public boolean deleteJob(JobKey jobKey) {
		return false;
	}

	@Override
	public boolean deleteJobs(List<JobKey> jobKeys) {
		return false;
	}

	@Override
	public void triggerJob(JobKey jobKey) {

	}

	@Override
	public void triggerJob(JobKey jobKey, JobDataMap data) {

	}

	@Override
	public void pauseJob(JobKey jobKey) {

	}

	@Override
	public void pauseJobs(GroupMatcher<JobKey> matcher) {

	}

	@Override
	public void pauseTrigger(TriggerKey triggerKey) {

	}

	@Override
	public void pauseTriggers(GroupMatcher<TriggerKey> matcher) {

	}

	@Override
	public void resumeJob(JobKey jobKey) {

	}

	@Override
	public void resumeJobs(GroupMatcher<JobKey> matcher) {

	}

	@Override
	public void resumeTrigger(TriggerKey triggerKey) {

	}

	@Override
	public void resumeTriggers(GroupMatcher<TriggerKey> matcher) {

	}

	@Override
	public void pauseAll() {

	}

	@Override
	public void resumeAll() {

	}

	@Override
	public List<String> getJobGroupNames() {
		return null;
	}

	@Override
	public Set<JobKey> getJobKeys(GroupMatcher<JobKey> matcher) {
		return null;
	}

	@Override
	public List<? extends Trigger> getTriggersOfJob(JobKey jobKey) {
		return null;
	}

	@Override
	public List<String> getTriggerGroupNames() {
		return null;
	}

	@Override
	public Set<TriggerKey> getTriggerKeys(GroupMatcher<TriggerKey> matcher) {
		return null;
	}

	@Override
	public Set<String> getPausedTriggerGroups() {
		return null;
	}

	@Override
	public JobDetail getJobDetail(JobKey jobKey) {
		return null;
	}

	@Override
	public Trigger getTrigger(TriggerKey triggerKey) {
		return null;
	}

	@Override
	public Trigger.TriggerState getTriggerState(TriggerKey triggerKey) {
		return null;
	}

	@Override
	public void resetTriggerFromErrorState(TriggerKey triggerKey) {

	}

	@Override
	public void addCalendar(String calName, Calendar calendar, boolean replace, boolean updateTriggers) {

	}

	@Override
	public boolean deleteCalendar(String calName) {
		return false;
	}

	@Override
	public Calendar getCalendar(String calName) {
		return null;
	}

	@Override
	public List<String> getCalendarNames() {
		return null;
	}

	@Override
	public boolean interrupt(JobKey jobKey) throws UnableToInterruptJobException {
		return false;
	}

	@Override
	public boolean interrupt(String fireInstanceId) throws UnableToInterruptJobException {
		return false;
	}

	@Override
	public boolean checkExists(JobKey jobKey) {
		return false;
	}

	@Override
	public boolean checkExists(TriggerKey triggerKey) {
		return false;
	}

	@Override
	public void clear() {

	}
}
