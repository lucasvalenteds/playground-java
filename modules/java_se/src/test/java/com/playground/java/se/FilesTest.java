package com.playground.java.se;

import com.playground.java.se.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@Tag("jdk-8")
class FilesTest {

    private final Path resources = Paths.get("src/test/resources");

    @DisplayName("It can be used to read lines of multiple files")
    @Test
    void testCountingLinesOfMultipleFiles() throws IOException {
        var folderWithFilesToRead = resources.resolve("regular-files");

        var totalLinesRead = Files.list(folderWithFilesToRead)
                .map(Path::toFile)
                .filter(File::isFile)
                .map(file -> {
                    try (var stream = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                        return stream.lines().count();
                    } catch (IOException ex) {
                        return 0L;
                    }
                })
                .map(Math::toIntExact)
                .reduce(0, Integer::sum);

        assertThat(totalLinesRead).isEqualTo(10);
    }

    @DisplayName("It can read huge file without compromising performance")
    @Test
    void testReadingOneHugeFileAsFastAsPossible() {
        var hugeFile = resources.resolve("huge-files/huge-file.txt");

        var timeWhenReadingStarted = System.currentTimeMillis();
        try (var stream = new BufferedReader(new InputStreamReader(new FileInputStream(hugeFile.toFile())))) {
            var totalOfLinesRead = stream.lines().collect(Collectors.toList());
            var timeTakenToReadAllLines = System.currentTimeMillis() - timeWhenReadingStarted;

            assertThat(totalOfLinesRead).hasSize(50000);
            assertThat(timeTakenToReadAllLines).isBetween(50L, 5000L);
        } catch (IOException exception) {
            fail("Test should pass, but failed with message: " + exception.getMessage());
        }
    }

    @DisplayName("It can read the size of a file in kilobytes")
    @Test
    void testSortFilesBySizeAscending() throws IOException {
        var folderWithFiles = resources.resolve("regular-files");

        var filesSortedBySize = Files.list(folderWithFiles)
                .map(path -> {
                    try {
                        return new Pair<>(path.toFile().getName(), Files.size(path));
                    } catch (IOException exception) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .sorted((a, b) -> {
                    if (a.second < b.second) return -1;
                    else if (Objects.equals(a.second, b.second)) return 0;
                    else return 1;
                })
                .map(pair -> String.format("%s (%skb)", pair.first, pair.second))
                .collect(Collectors.toList());

        assertThat(filesSortedBySize).containsExactly("file3.txt (6kb)", "file2.txt (22kb)", "file1.txt (46kb)");
    }
}
