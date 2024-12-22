/* © SRSoftware 2024 */
package de.srsoftware.configuration;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import org.json.JSONObject;


public class JsonConfig implements Configuration {
	private final File       file;
	private final JSONObject json;

	public JsonConfig(File jsonConfigurationFile) throws IOException {
		file = jsonConfigurationFile;
		if (file.isDirectory()) throw new IllegalArgumentException("%s is a directory, file expected".formatted(file));
		if (!file.exists()) try (var out = new FileWriter(file)) {
				out.write("{}\n");
			}
		json = new JSONObject(Files.readString(file.toPath()));
	}

	public JsonConfig(String applicationName) throws IOException {
		this(Locator.locateConfig(applicationName, "json"));
	}

	@Override
	public <C extends Configuration> C drop(String key) throws IOException {
		drop(json, toPath(key));
		return (C)this;
	}

	private void drop(JSONObject json, Stack<String> path) {
		String key = path.pop();
		if (!json.has(key)) return;
		if (path.isEmpty()) {
			json.remove(key);
			return;
		}
		if (json.get(key) instanceof JSONObject inner) drop(inner, path);
	}

	public File file() {
		return file;
	}

	public String flat() {
		return json.toString();
	}

	@Override
	public <T> Optional<T> get(String key) {
		return Optional.ofNullable(get(json, toPath(key), null));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(String key, T defaultValue) {
		return (T)get(json, toPath(key), defaultValue);
	}

	@SuppressWarnings("unchecked")
	private <T> T get(JSONObject json, Stack<String> path, T defaultValue) {
		String key = path.pop();
		if (path.isEmpty()) {
			if (json.has(key)) try {
					return (T)json.get(key);
				} catch (ClassCastException ignored) {
					// overwrite value
				}
			json.put(key, defaultValue);
			return defaultValue;
		} else {
			if (!json.has(key)) {
				if (defaultValue != null) {
					var inner = new JSONObject();
					set(inner, path, defaultValue);
					json.put(key, inner);
				}
				return defaultValue;
			}
			var inner = json.get(key);
			if (!(inner instanceof JSONObject)) {
				if (defaultValue != null) {
					inner = new JSONObject();
					set((JSONObject)inner, path, defaultValue);
					json.put(key, inner);
				}
				return defaultValue;
			};
			return get((JSONObject)inner, path, defaultValue);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <C extends Configuration> C set(String key, Object value) throws IOException {
		set(json, toPath(key), value);
		return (C)this;
	}

	private void set(JSONObject json, Stack<String> path, Object value) {
		var key = path.pop();
		if (path.empty()) {
			json.put(key, value);
		} else {
			if (!json.has(key)) json.put(key, new JSONObject());
			var inner = json.get(key);
			if (!(inner instanceof JSONObject)) json.put(key, inner = new JSONObject());
			set((JSONObject)inner, path, value);
		}
	}

	private Stack<String> toPath(String key) {
		var parts = key.split("\\.");
		var path  = new Stack<String>();
		for (int i = parts.length; i > 0; i--) path.push(parts[i - 1]);
		return path;
	}

	@Override
	public String toString() {
		return json.toString(2);
	}
}
