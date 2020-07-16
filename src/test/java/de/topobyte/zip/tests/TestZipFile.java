package de.topobyte.zip.tests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class TestZipFile
{

	@Test
	public void test() throws IOException
	{
		Path file = Files.createTempFile("test-zip-impls", ".odt");
		InputStream is = Resources.stream("gpg13.odt");
		IOUtils.copy(is, Files.newOutputStream(file));

		Map<String, byte[]> entryToData = new HashMap<>();

		try (ZipFile zip = new ZipFile(file.toFile())) {
			Enumeration<? extends ZipEntry> entries = zip.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				long compressedSize = entry.getCompressedSize();
				long uncompressedSize = entry.getSize();
				System.out.println(String.format(
						"entry: '%s' compressed: %d, uncompressed: %d",
						entry.getName(), compressedSize, uncompressedSize));
				try (InputStream entryInput = zip.getInputStream(entry)) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					IOUtils.copy(entryInput, baos);
					entryToData.put(entry.getName(), baos.toByteArray());
				}
			}
		}

		print(entryToData, "mimetype");
		print(entryToData, "META-INF/manifest.xml");

		Assert.assertEquals(new String(Resources.load("mimetype")),
				new String(entryToData.get("mimetype")));
		Assert.assertEquals(new String(Resources.load("manifest.xml")),
				new String(entryToData.get("META-INF/manifest.xml")));
	}

	private void print(Map<String, byte[]> entryToData, String name)
	{
		byte[] data = entryToData.get(name);
		System.out.println(String.format("name: '%s' content: '%s'", name,
				new String(data)));
	}

}
