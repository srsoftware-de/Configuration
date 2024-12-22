/* © SRSoftware 2024 */
package de.srsoftware.configuration.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.srsoftware.configuration.Locator;
import org.junit.jupiter.api.Test;

public class LocatorTest {
	@Test
	public void testLocator() {
		var file = Locator.locateConfig("Test", "json");
		var home = System.getProperty("user.home");
		assertEquals(home + "/.config/Test.json", file.toString());
	}
}
