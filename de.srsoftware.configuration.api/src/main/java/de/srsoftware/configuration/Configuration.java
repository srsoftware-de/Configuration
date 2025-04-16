/* © SRSoftware 2024 */
package de.srsoftware.configuration;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

/**
 * Configuration Interface.
 * Provieds methods to add values to a configuration and read values from it.
 */
public interface Configuration {
	/**
	 * remove a value from the confuguration
	 * @param key specifies, which value shall be deleted
	 * @return the altered configuration
	 * @param <C> the class of the configuration implementation
	 * @throws IOException if altering the config is not possible
	 */
	<C extends Configuration> C drop(String key) throws IOException;

	/**
	 * read a value from the configuration
	 * @param key specifies, which value is requested
	 * @return an Optional containing the value, if it is present, empty otherwise
	 * @param <T> the expected type of the value
	 */
	<T> Optional<T> get(String key);

	/**
	 * read a value from the configuration and set it, if it is not present
	 * @param key specifies, which value is requested
	 * @param defaultValue the value which will be set and returned, if the requested value is not available
	 * @return the value assigned with the key or defaultValue, if no value was assigned with the key
	 * @param <T> the expected type of the return value
	 */
	<T> T get(String key, T defaultValue);

	/**
	 * Get the configuration`s key set
	 * @return a collection of strings which are the keys to the configuration
	 */
	Collection<String> keys();

	/**
	 * Assign a specific key with a new value. If the key was assigned with another value before, the old value is overwritten
	 * @param key specifies, which value is to be assigned
	 * @param value the new value
	 * @return the altered configuration
	 * @param <C> the type of the configuration
	 * @throws IOException if altering the configuration fails
	 */
	<C extends Configuration> C set(String key, Object value) throws IOException;

	/**
	 * get a subset of this configuration
	 *
	 * @param key specifies, which subset is requested
	 * @return the part of the Configuration which is located at the key
	 */
	Optional<? extends Configuration> subset(String key);
}