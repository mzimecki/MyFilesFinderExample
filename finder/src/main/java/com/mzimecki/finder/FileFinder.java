package com.mzimecki.finder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileFinder {

	public List<File> findFilesModifiedDaysAgo(Path start, int numberOfDays) throws IOException {
		final long dayInMillis = 24 * 60 * 60 * 1000;
		final long daysAgoMillis = System.currentTimeMillis() - numberOfDays * dayInMillis;
		
		try (final Stream<Path> files = Files.find(start, 1, (path, attribute) -> {
			final long modifiedMillis = attribute.lastModifiedTime().toMillis(); 
			final LocalDateTime modifiedDate = getLocalDateTimeForMillis(modifiedMillis);
			final LocalDateTime daysAgoDate = getLocalDateTimeForMillis(daysAgoMillis);
			return modifiedDate.truncatedTo(ChronoUnit.DAYS).compareTo(daysAgoDate.truncatedTo(ChronoUnit.DAYS)) < 0;
		})) {
			return files.map(path -> path.toFile()).collect(Collectors.toList());
		}
	}
	
	private LocalDateTime getLocalDateTimeForMillis(final long timeInMillis) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeInMillis), ZoneId.systemDefault());
	}

}
