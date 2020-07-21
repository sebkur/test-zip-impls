package de.topobyte.zip.tests.jdk8143613;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import de.schlichtherle.truezip.zip.ZipEntry;
import de.schlichtherle.truezip.zip.ZipFile;

public class TestTrueZip
{

	@Test
	public void testTrue() throws IOException
	{
		byte[] contents = TestData.get(true);
		Path file = Files.createTempFile("test-zip-impls", ".odt");
		IOUtils.copy(new ByteArrayInputStream(contents),
				Files.newOutputStream(file));

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
	}

}
