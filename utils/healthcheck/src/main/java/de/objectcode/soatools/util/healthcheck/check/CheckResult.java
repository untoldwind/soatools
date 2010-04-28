package de.objectcode.soatools.util.healthcheck.check;

import java.util.Map;

import de.objectcode.soatools.util.healthcheck.HealthState;


public class CheckResult {
	final HealthState state;
	final Map<String, HealthState> checkStates;

	public CheckResult(HealthState state, Map<String, HealthState> checkStates) {

		this.state = state;
		this.checkStates = checkStates;
	}

	public HealthState getState() {
		return state;
	}

	public Map<String, HealthState> getCheckStates() {
		return checkStates;
	}

}
