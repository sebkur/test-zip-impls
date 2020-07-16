package de.topobyte.zip.tests.jdk8143613;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.zip.UnsupportedZipFeatureException;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class TestCommonsZipInputStream
{

	/**
	 * If we use the default ZipArchiveInputStream constructor, we get an
	 * exception while reading the entries.
	 */
	@Test(expected = UnsupportedZipFeatureException.class)
	public void testFailing() throws IOException
	{
		byte[] contents = TestData.get(true);
		InputStream is = new ByteArrayInputStream(contents);
		try (ZipArchiveInputStream zis = new ZipArchiveInputStream(is)) {
			while (true) {
				ZipArchiveEntry entry = zis.getNextZipEntry();
				if (entry == null) {
					break;
				}
				System.out
						.println(String.format("entry: '%s'", entry.getName()));
			}
		}
	}

	/**
	 * If we use the extended ZipArchiveInputStream constructor, and pass the
	 * default options, we get an exception while reading the entries as with
	 * the default constructor.
	 */
	@Test(expected = UnsupportedZipFeatureException.class)
	public void testFailingWithExtendedConstructor() throws IOException
	{
		byte[] contents = TestData.get(true);
		InputStream is = new ByteArrayInputStream(contents);
		try (ZipArchiveInputStream zis = new ZipArchiveInputStream(is,
				StandardCharsets.UTF_8.toString(), true, false)) {
			while (true) {
				ZipArchiveEntry entry = zis.getNextZipEntry();
				if (entry == null) {
					break;
				}
				System.out
						.println(String.format("entry: '%s'", entry.getName()));
			}
		}
	}

	/**
	 * If we use the extended ZipArchiveInputStream constructor and pass true
	 * for the parameter allowStoredEntriesWithDataDescriptor, we can read the
	 * names of each entry.
	 * 
	 * Ideally this test would succeed, but it doesn't.
	 */
	@Test
	public void testSuccess() throws IOException
	{
		Map<String, byte[]> entryToData = new HashMap<>();

		byte[] contents = TestData.get(true);
		InputStream is = new ByteArrayInputStream(contents);
		try (ZipArchiveInputStream zis = new ZipArchiveInputStream(is,
				StandardCharsets.UTF_8.toString(), true, true)) {
			while (true) {
				ZipArchiveEntry entry = zis.getNextZipEntry();
				if (entry == null) {
					break;
				}
				long uncompressedSize = entry.getSize();
				long compressedSize = entry.getCompressedSize();
				System.out.println(String.format(
						"entry: '%s' compressed: %d, uncompressed: %d",
						entry.getName(), compressedSize, uncompressedSize));
				if (uncompressedSize >= 0) {
					byte[] data = new byte[(int) uncompressedSize];
					IOUtils.readFully(zis, data);
					entryToData.put(entry.getName(), data);
				}
			}
		}
	}

}
