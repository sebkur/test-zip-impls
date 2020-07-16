package de.topobyte.zip.tests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class Resources
{

	public static InputStream stream(String name)
	{
		return Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(name);
	}

	public static byte[] load(String name) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (InputStream input = stream(name)) {
			IOUtils.copy(input, baos);
		}
		return baos.toByteArray();
	}

}
