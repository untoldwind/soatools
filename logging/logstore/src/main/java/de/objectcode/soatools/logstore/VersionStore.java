package de.objectcode.soatools.logstore;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class VersionStore {
	private final static VersionStore INSTANCE = new VersionStore();

	private Map<String, String> versions;

	private VersionStore() {
		versions = Collections.synchronizedMap(new TreeMap<String, String>());
	}

	public void registerVersion(String name, String version) {
		versions.put(name, version);
	}

	public void unregisterVersion(String name) {
		versions.remove(name);
	}

	public String getVersion(String name) {
		return versions.get(name);
	}

	public Map<String, String> getVersions() {
		return Collections.unmodifiableMap(versions);
	}

	public static VersionStore getInstance() {
		return INSTANCE;
	}

}
