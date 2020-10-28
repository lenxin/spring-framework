package org.springframework.scheduling.support;

import java.util.Date;

import org.springframework.lang.Nullable;
import org.springframework.scheduling.TriggerContext;

/**
 * Simple data holder implementation of the {@link TriggerContext} interface.
 *

 * @since 3.0
 */
public class SimpleTriggerContext implements TriggerContext {

	@Nullable
	private volatile Date lastScheduledExecutionTime;

	@Nullable
	private volatile Date lastActualExecutionTime;

	@Nullable
	private volatile Date lastCompletionTime;


	/**
	 * Create a SimpleTriggerContext with all time values set to {@code null}.
	 */
	public SimpleTriggerContext() {
	}

	/**
	 * Create a SimpleTriggerContext with the given time values.
	 * @param lastScheduledExecutionTime last <i>scheduled</i> execution time
	 * @param lastActualExecutionTime last <i>actual</i> execution time
	 * @param lastCompletionTime last completion time
	 */
	public SimpleTriggerContext(Date lastScheduledExecutionTime, Date lastActualExecutionTime, Date lastCompletionTime) {
		this.lastScheduledExecutionTime = lastScheduledExecutionTime;
		this.lastActualExecutionTime = lastActualExecutionTime;
		this.lastCompletionTime = lastCompletionTime;
	}


	/**
	 * Update this holder's state with the latest time values.
 	 * @param lastScheduledExecutionTime last <i>scheduled</i> execution time
	 * @param lastActualExecutionTime last <i>actual</i> execution time
	 * @param lastCompletionTime last completion time
	 */
	public void update(Date lastScheduledExecutionTime, Date lastActualExecutionTime, Date lastCompletionTime) {
		this.lastScheduledExecutionTime = lastScheduledExecutionTime;
		this.lastActualExecutionTime = lastActualExecutionTime;
		this.lastCompletionTime = lastCompletionTime;
	}


	@Override
	@Nullable
	public Date lastScheduledExecutionTime() {
		return this.lastScheduledExecutionTime;
	}

	@Override
	@Nullable
	public Date lastActualExecutionTime() {
		return this.lastActualExecutionTime;
	}

	@Override
	@Nullable
	public Date lastCompletionTime() {
		return this.lastCompletionTime;
	}

}
