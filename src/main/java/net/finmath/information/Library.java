/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 18.08.2014
 */

package net.finmath.information;

import java.util.Properties;

/**
 * Provides information on the finmath-lib library, e.g., the version.
 *
 * @author Christian Fries
 */
public class Library {

	private static Properties properties;
	static {
		properties = new Properties();
		try {
			properties.load(Library.class.getResourceAsStream("/finmath-lib.properties"));
		} catch (Exception e) {
			properties = null;
		}
	}

	private Library() { }

	private static Properties getProperites() {
		return properties;
	}

	/**
	 * Return the version string of this instance of finmath-lib.
	 *
	 * @return The version string of this instance of finmath-lib.
	 */
	public static String getVersionString() {
		String versionString = "UNKNOWN";
		Properties propeties = getProperites();
		if(propeties != null) {
			versionString = propeties.getProperty("finmath-lib.version");
		}
		return versionString;
	}

	/**
	 * Return the build string of this instance of finmath-lib.
	 * Currently this is the Git commit hash.
	 *
	 * @return The build string of this instance of finmath-lib.
	 */
	public static String getBuildString() {
		String versionString = "UNKNOWN";
		Properties propeties = getProperites();
		if(propeties != null) {
			versionString = propeties.getProperty("finmath-lib.build");
		}
		return versionString;
	}
}
