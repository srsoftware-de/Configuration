/* © SRSoftware 2024 */
package de.srsoftware.configuration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonConfigTest {
	private static final String STRING      = "string";
	private static final String HELLO_WORLD = "hello world";
	private static final String MAP	        = "map";
	private JsonConfig          config;

	@BeforeEach
	public void setup() throws IOException {
		var configFile = new File("/tmp/test.json");
		if (configFile.exists()) configFile.delete();
		config = new JsonConfig(configFile);
	}

	@Test
	public void testEmptyConfig() {
		assertTrue(config.file().exists());
		assertTrue(config.file().isFile());
		assertEquals("{}", config.flat());
	}

	@Test
	void testSetFlat() throws IOException {
		config.set("hello", "world");
		assertEquals("{\"hello\":\"world\"}", config.flat());
	}

	@Test
	void testSetNested() throws IOException {
		// set nested
		config.set("this.is.a", "test");
		assertEquals("{\"this\":{\"is\":{\"a\":\"test\"}}}", config.flat());
		// several nested attributes
		config.set("this.is.no", "joke");
		assertEquals("{\"this\":{\"is\":{\"a\":\"test\",\"no\":\"joke\"}}}", config.flat());

		// overwrite value
		config.set("this.is.a", "farce");
		assertEquals("{\"this\":{\"is\":{\"a\":\"farce\",\"no\":\"joke\"}}}", config.flat());

		// overwrite subset
		config.set("this.is", "gone");
		assertEquals("{\"this\":{\"is\":\"gone\"}}", config.flat());

		config.set("int", 3);
		assertEquals("{\"this\":{\"is\":\"gone\"},\"int\":3}", config.flat());
	}

	@Test
	public void testGetFlat() throws IOException {
		config.set("hello", "world");
		config.set("this.is.a", "test");
		Optional<String> res = config.get("hello");
		assertTrue(res.isPresent());
		assertEquals("world", res.get());
	}

	@Test
	public void testGetNested() throws IOException {
		config.set("hello", "world");
		config.set("this.is.a", "test");
		Optional<String> res = config.get("this.is.a");
		assertTrue(res.isPresent());
		assertEquals("test", res.get());
	}

	@Test
	public void testGetJson() throws IOException {
		config.set("hello", "world");
		config.set("this.is.a", "test");
		Optional<JSONObject> res = config.get("this");
		assertTrue(res.isPresent());
		assertEquals("{\"is\":{\"a\":\"test\"}}", res.get().toString());
	}

	@Test
	public void testGetOrSet() {
		String val = config.get("hello", "world");
		assertEquals("world", val);

		val = config.get("hello", "sunshine");
		assertEquals("world", val);
	}

	@Test
	public void testGetOrSetNested() {
		String val = config.get("this.is.a", "test");
		assertEquals("test", val);

		val = config.get("this.is.a", "farce");
		assertEquals("test", val);
		assertEquals("{\"this\":{\"is\":{\"a\":\"test\"}}}", config.flat());
	}

	@Test
	public void testDrop() throws IOException {
		config.set("hello", "world");
		config.set("this.is.a", "test");
		assertEquals("{\"this\":{\"is\":{\"a\":\"test\"}},\"hello\":\"world\"}", config.flat());
		config.drop("test.is.an");
		assertEquals("{\"this\":{\"is\":{\"a\":\"test\"}},\"hello\":\"world\"}", config.flat());
		config.drop("this.is.a");
		assertEquals("{\"this\":{\"is\":{}},\"hello\":\"world\"}", config.flat());
		config.drop("this");
		assertEquals("{\"hello\":\"world\"}", config.flat());
		config.drop("hello");
		assertEquals("{}", config.flat());
	}

	@Test
	public void testSubset() throws IOException {
		config.set("a.a.a", "aaa");
		config.set("a.a.b", "aab");
		config.set("a.b.a", "aba");
		config.set("a.b.b", "abb");
		assertEquals("{\"a\":{\"a\":{\"a\":\"aaa\",\"b\":\"aab\"},\"b\":{\"a\":\"aba\",\"b\":\"abb\"}}}",config.flat());
		var subset = config.subset("a");
		assertTrue(subset.isPresent());
		assertEquals("{\"a\":{\"a\":\"aaa\",\"b\":\"aab\"},\"b\":{\"a\":\"aba\",\"b\":\"abb\"}}",subset.get().flat());
		subset = config.subset("a.b");
		assertTrue(subset.isPresent());
		assertEquals("{\"a\":\"aba\",\"b\":\"abb\"}",subset.get().flat());
	}
}
