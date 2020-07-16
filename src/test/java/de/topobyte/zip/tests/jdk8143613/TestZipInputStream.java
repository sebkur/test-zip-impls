package de.topobyte.zip.tests.jdk8143613;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import org.junit.Test;

/**
 * Test case adapted from here: https://bugs.openjdk.java.net/browse/JDK-8143613
 */
public class TestZipInputStream
{

	@Test(expected = ZipException.class)
	public void testTrue() throws IOException
	{
		System.out.println("general_purpose_bit_flag_bit3_on = true");
		byte[] contents = TestData.get(true);

		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			bos.write(contents);
			byte[] bytes = bos.toByteArray();
			ByteArrayInputStream input = new ByteArrayInputStream(bytes);
			try (ZipInputStream zis = new ZipInputStream(input)) {
				ZipEntry entry = zis.getNextEntry();
				System.out.println(entry.getName());
			}
		}
	}

	@Test
	public void testFalse() throws IOException
	{
		System.out.println("general_purpose_bit_flag_bit3_on = false");
		byte[] contents = TestData.get(false);

		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			bos.write(contents);
			byte[] bytes = bos.toByteArray();
			ByteArrayInputStream input = new ByteArrayInputStream(bytes);
			try (ZipInputStream zis = new ZipInputStream(input)) {
				ZipEntry entry = zis.getNextEntry();
				System.out.println(entry.getName());
			}
		}
	}

}