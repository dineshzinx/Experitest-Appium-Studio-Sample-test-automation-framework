package com.seetest.propertyfilereader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileReader {

	/** The property file name. */
	private String propFileName;

	/** The property object. */
	private Properties properties;

	/**
	 * @param propertyfile
	 *            the text file
	 */
	public PropertyFileReader(final String propertyfile) {
		this.propFileName = propertyfile;
		loadProperty();
	}

	/**
	 * @param key
	 * @return
	 */
	public String getproperty(String key) {
		String value = properties.getProperty(key);
		return value;
	}

	/**
	 * Load the properties
	 */
	private final void loadProperty() {

		InputStream is = null;

		try {
			is = new FileInputStream(propFileName);
			properties = new Properties();
			properties.load(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
