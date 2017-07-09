package com.mzimecki.finder;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FileFinderTest {
	
	private static final int DAY_IN_MILLIS = 24 * 60 * 60 * 1000;
	
	@Rule
	public TemporaryFolder tempTestFolder = new TemporaryFolder();
	
	@Test
	public void should_find_files_modified_2_days_ago() throws IOException {
		final File fileModifiedOneDayAgo = tempTestFolder.newFile("testFile1");
		fileModifiedOneDayAgo.setLastModified(daysAgoInMillis(1));
		
		final File fileModifiedTwoDaysAgo = tempTestFolder.newFile("testFile2");
		fileModifiedTwoDaysAgo.setLastModified(daysAgoInMillis(2));
		
		final File fileModifiedFourDaysAgo = tempTestFolder.newFile("testFile4");
		fileModifiedFourDaysAgo.setLastModified(daysAgoInMillis(4));
		
		final File fileModifiedFiveDaysAgo = tempTestFolder.newFile("testFile5");
		fileModifiedFiveDaysAgo.setLastModified(daysAgoInMillis(5));
		
		final FileFinder fileFinder = new FileFinder();
		final List<File> files = fileFinder.findFilesModifiedDaysAgo(asPath(tempTestFolder), 2);
		
		assertTrue(files.size() == 2);
		
		assertTrue(files.contains(fileModifiedFourDaysAgo));
		assertTrue(files.contains(fileModifiedFiveDaysAgo));
		
		assertFalse(files.contains(fileModifiedOneDayAgo));
		assertFalse(files.contains(fileModifiedTwoDaysAgo));
	}

	private Path asPath(TemporaryFolder tempFolder) {
		return Paths.get(tempFolder.getRoot().toURI());
	}
	
	private long daysAgoInMillis(int days) {
		return System.currentTimeMillis() - days * DAY_IN_MILLIS;
	}
}
