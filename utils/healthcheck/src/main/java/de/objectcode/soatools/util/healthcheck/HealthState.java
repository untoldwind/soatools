package de.objectcode.soatools.util.healthcheck;

public enum HealthState {
	OK(0), WARN(1), ERROR(2);
	
	final int severity;
	
	private HealthState(int severity) {
		this.severity = severity;
	}

	public int getSeverity() {
		return severity;
	}

	
	
}
