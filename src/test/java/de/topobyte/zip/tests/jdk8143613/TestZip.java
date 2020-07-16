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
public class TestZip
{

	@Test(expected = ZipException.class)
	public void test() throws IOException
	{
		final boolean general_purpose_bit_flag_bit3_on = true;
		final byte gpbf = general_purpose_bit_flag_bit3_on ? 0x08 : 0x00;

		final byte[] contents = new byte[] {
				// Local File header
				'P', 'K', 3, 4, // Local File Header Signature
				13, 0, // Version needed to extract
				gpbf, 8, // General purpose bit flag
				ZipEntry.STORED, 0, // Compression method
				'q', 'l', 't', 'G', // Last Modification time & date
				0, 0, 0, 0, // CRC32
				0, 0, 0, 0, // Compressed Size
				0, 0, 0, 0, // Uncompressed Size
				12, 0, // File name length
				0, 0, // Extra field length
				'F', 'o', 'l', 'd', 'e', 'r', '_', 'n', 'a', 'm', 'e', '/',
				// File name
				// Central directory file header
				'P', 'K', 1, 2, // Central Directory File Header Signature
				13, 0, // Version made by
				13, 0, // Version needed to extract
				gpbf, 8, // General purpose bit flag
				ZipEntry.STORED, 0, // Compression method
				'q', 'l', 't', 'G', // Last Modification time & date
				0, 0, 0, 0, // CRC32
				0, 0, 0, 0, // Compressed Size
				0, 0, 0, 0, // Uncompressed Size
				12, 0, // File name length
				0, 0, // Extra field length
				0, 0, // File comment length
				0, 0, // Disk number where file starts
				0, 0, // Internal File attributes
				0, 0, 0, 0, // External File attributes
				0, 0, 0, 0, // Relative offset of local header file
				'F', 'o', 'l', 'd', 'e', 'r', '_', 'n', 'a', 'm', 'e', '/',
				// File name
				// End of Central Directory Record
				'P', 'K', 5, 6, // Local File Header Signature
				0, 0, // Number of this disk
				0, 0, // Disk where CD starts
				1, 0, // Number of CD records on this disk
				1, 0, // Total number of records
				58, 0, 0, 0, // Size of CD
				42, 0, 0, 0, // Offset of start of CD
				0, 0, // Comment length
		};

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