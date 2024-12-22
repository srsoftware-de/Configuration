/* © SRSoftware 2024 */
package de.srsoftware.configuration;

import java.io.File;
import java.nio.file.Path;

/**
 * Helper for getting a config file
 */
public class Locator {

	private Locator(){}

	/**
	 * Get the proper configuration file for a given application name with the desired extension
	 * @param applicationName the name of the application
	 * @param extension the extension of the requested file
	 * @return a file Object
	 */
	public static File locateConfig(String applicationName, String extension) {
		var filename = applicationName + "." + extension;
		var home     = System.getProperty("user.home");
		return Path.of(home).resolve(".config").resolve(filename).toFile();
	}
}
