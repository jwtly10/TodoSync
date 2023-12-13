package com.jwtly10.TodoSync.services;

import com.jwtly10.TodoSync.FileTestBase;
import com.jwtly10.TodoSync.exceptions.TodoProcessingException;
import com.jwtly10.TodoSync.models.Todo;
import com.jwtly10.TodoSync.services.Github.GithubService;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class TodoProcessingServiceTest extends FileTestBase {

    @Test
    public void testProcessTodo() {
        GithubService githubServiceMock = Mockito.mock(GithubService.class);
        String content = """
                public class App{
                public static void main(String[] args) {
                // TODO: Implement this method
                // This is the body of the code that also needs to be parsed
                // This is also the case
                }
                }""";

        // We don't want to test the todo parsing here, just the processing
        File testFile = createFileWithContent("test_process_todo.java", "testDirectory", content);

        Todo testTodo = Todo.builder()
                .title("Implement this method")
                .line(3)
                .filepath(testFile.getAbsolutePath())
                .build();


        when(githubServiceMock.createIssue(Mockito.any(Todo.class))).thenReturn(1);
        TodoProcessingService todoProcessingService = new TodoProcessingServiceImpl(githubServiceMock);

        boolean res = todoProcessingService.processTodo(testTodo);

        try {
            List<String> updatedFileLines = Files.readAllLines(testFile.toPath());

            assertEquals("// TODO(#1): Implement this method", updatedFileLines.get(2));
            assertTrue(res);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testFailedProcessTodo() {
        // If for some reason we have a todo
        // object that doesnt exist, we should fail gracefully

        GithubService githubServiceMock = Mockito.mock(GithubService.class);

        String content = """
                public class App{
                public static void main(String[] args) {
                // Implement this method
                // This is the body of the code that also needs to be parsed
                // This is also the case
                }
                }""";

        // We don't want to test the todo parsing here, just the processing
        File testFile = createFileWithContent("test_process_todo.java", "testDirectory", content);

        Todo testTodo = Todo.builder()
                .title("Implement this method")
                .line(3)
                .filepath(testFile.getAbsolutePath())
                .build();

        when(githubServiceMock.createIssue(Mockito.any(Todo.class))).thenReturn(1);

        TodoProcessingService todoProcessingService = new TodoProcessingServiceImpl(githubServiceMock);

        Exception exception = assertThrows(TodoProcessingException.class, () -> {
            boolean res = todoProcessingService.processTodo(testTodo);
        });

        assertEquals("Failed to update line 3 in file: " + testFile.getAbsolutePath() + " But github issue #1 was created.", exception.getMessage());


        try {
            List<String> updatedFileLines = Files.readAllLines(testFile.toPath());

            assertEquals("// Implement this method", updatedFileLines.get(2));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
