package com.jwtly10.TodoSync.parsers;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

/** BaseTest */
public abstract class BaseParserTest {

    /*
     * This class is used to set up the test environment for all tests.
     * Since its very file based - we need to create a test directory and
     * test files to run the tests against.
     */
    @Before
    public void setup () {
        // Create test directory
        File testDirectory = new File("testDirectory");
        if (testDirectory.exists()) {
            if (testDirectory.exists()) {
                try {
                    FileUtils.deleteDirectory(testDirectory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        testDirectory.mkdir();
    }

    @After
    public void tearDown() {
        // Delete test directory
        File testDirectory = new File("testDirectory");
        if (testDirectory.exists()) {
            try {
                FileUtils.deleteDirectory(testDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected File createFileWithContent(String fileName, String directoryName, String content) {
        File file = new File(directoryName + "/" + fileName);
        Path path = file.toPath();
        List<String> lines = Arrays.asList(content.split("\n"));
        System.out.println("Creating file: " + file.getAbsolutePath());

        try {
            Files.write(
                    path, lines, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
            e.printStackTrace();
        }

        return file;
    }
}
