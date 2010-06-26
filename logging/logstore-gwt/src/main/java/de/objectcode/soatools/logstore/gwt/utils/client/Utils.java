package de.objectcode.soatools.logstore.gwt.utils.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;

public class Utils {
	public static void redirect(String uri) {
		StringBuffer url = new StringBuffer(GWT.getHostPageBaseURL() + uri);
		boolean first = true;

		for (Map.Entry<String, List<String>> entry : Window.Location
				.getParameterMap().entrySet()) {
			String key = entry.getKey();
			for (String value : entry.getValue()) {
				if (first)
					url.append("?");
				else
					url.append("&");
				first = false;
				url.append(key);
				url.append("=");
				url.append(URL.encode(value));
			}

		}
		
		Window.open(url.toString(), "_self", "");
	}
}
