package de.topobyte.zip.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import org.junit.Test;

public class TestZipInputStream
{

	@Test(expected = ZipException.class)
	public void test() throws IOException
	{
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("gpg13.odt");
		ZipInputStream zis = new ZipInputStream(is);
		while (true) {
			ZipEntry entry = zis.getNextEntry();
			if (entry == null) {
				break;
			}
			System.out.println(entry.getName());
		}
	}

}
