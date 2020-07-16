package de.topobyte.zip.tests;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.LocalFileHeader;

public class TestZip4jInputStream
{

	/**
	 * Zip4j stops after styles.xml with zero size
	 */
	@Test
	public void test() throws IOException
	{
		InputStream is = Resources.stream("gpg13.odt");
		try (ZipInputStream zis = new ZipInputStream(is)) {
			while (true) {
				LocalFileHeader header = zis.getNextEntry();
				if (header == null) {
					break;
				}
				long uncompressedSize = header.getUncompressedSize();
				long compressedSize = header.getCompressedSize();
				System.out.println(String.format(
						"entry: '%s' compressed: %d, uncompressed: %d",
						header.getFileName(), compressedSize,
						uncompressedSize));
			}
		}

	}

}
