package com.jwtly10.TodoSync.parsers;

import com.jwtly10.TodoSync.models.Todo;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.List;
import java.util.Optional;

/** DirectoryParserTest */
public class DirectoryParserTest extends BaseParserTest {

    @Test
    public void testParse() {
        TodoParser todoParserMock = Mockito.mock(TodoParser.class);
        DirectoryParserImpl directoryParser = new DirectoryParserImpl(todoParserMock);

        File testDirectory = createTestDirectoryStructure();

        Optional<List<Todo>> result = directoryParser.parseGitDir(testDirectory.getAbsolutePath());
        assert result.isPresent();

        Mockito.verify(todoParserMock, Mockito.times(3)).parse(Mockito.anyString());
    }

    private File createTestDirectoryStructure() {
        File testDirectory = new File("testDirectory");
        testDirectory.mkdir();
        File testFile1 = new File(testDirectory, "testFile1.txt");
        File testFile2 = new File(testDirectory, "testFile2.txt");
        File testFile3 = new File(testDirectory, "testFile3.txt");

        File gitDirectory = new File(testDirectory, ".git");
        gitDirectory.mkdir();

        File node_modules = new File(testDirectory, "node_modules");
        node_modules.mkdir();
        File testFile4 = new File(node_modules, "testFile4.txt");

        try {
            testFile1.createNewFile();
            testFile2.createNewFile();
            testFile3.createNewFile();
            testFile4.createNewFile();
        } catch (Exception e) {
            System.out.println("Error creating test files");
        }
        return testDirectory;
    }
}
