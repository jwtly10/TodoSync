package com.jwtly10.TodoSync.parsers;

import com.jwtly10.TodoSync.models.Todo;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * TodoParserTest
 */
public class TodoParserTest extends BaseParserTest {
    @Test
    public void testFindTodos() {
        String content =
                """
                        public class App{
                        public static void main(String[] args) {
                        // TODO: Implement this method
                        // This is the body of the code that also needs to be parsed
                        // This is also the case
                        }
                        public void testMethod() {
                        // TODO: Implement this
                        }
                        }""";

        File testFile = createFileWithContent("test_find_todos.java", "testDirectory", content);

        TodoParser todoParser = new TodoParserImpl();

        Optional<List<Todo>> res = todoParser.parse(testFile.getAbsolutePath());

        assertTrue(res.isPresent());

        List<Todo> todos = res.get();
        assertEquals(2, todos.size());

        Todo firstTodo = todos.get(0);
        assertEquals("Implement this method", firstTodo.getTitle());
        assertEquals(3, firstTodo.getLine().intValue());
    }

    @Test
    public void testBuildDescription() {
        String content =
                """
                        public class App{
                        public static void main(String[] args) {
                        // TODO: Implement this method
                        // This is the body of the code that also needs to be parsed
                        // This is also the case
                        }
                        public void testMethod() {
                        // TODO: Implement this
                        }
                        }""";

        File testFile =
                createFileWithContent("test_build_description.java", "testDirectory", content);

        TodoParser todoParser = new TodoParserImpl();

        Optional<List<Todo>> res = todoParser.parse(testFile.getAbsolutePath());

        assertTrue(res.isPresent());

        List<String> expectedDescription =
                Arrays.asList(
                        "This is the body of the code that also needs to be parsed",
                        "This is also the case");

        List<Todo> todos = res.get();
        assertEquals(2, todos.size());

        assertEquals(expectedDescription, todos.get(0).getDescription());
    }

    @Test
    public void testNoTodos() {
        String content =
                """
                        public class App{
                        public static void main(String[] args) {
                        }
                        public void testMethod() {
                        // This is just a normal comment
                        }
                        }""";

        File testFile = createFileWithContent("test_no_todos.java", "testDirectory", content);

        TodoParser todoParser = new TodoParserImpl();

        Optional<List<Todo>> res = todoParser.parse(testFile.getAbsolutePath());

        assertTrue(res.isEmpty());
    }

    @Test
    public void testIgnoreTodo() {
        String content =
                """
                        public class App{
                        public static void main(String[] args) {
                        // TODO:ignore This todo should ignored
                        // Including all the description content
                        // This is also the case
                        // TODO: This should be parsed however
                        // With this data captured
                        // And this
                        }
                        }""";

        File testFile = createFileWithContent("test_ignore_todo.java", "testDirectory", content);

        TodoParser todoParser = new TodoParserImpl();

        Optional<List<Todo>> res = todoParser.parse(testFile.getAbsolutePath());

//        assert res.isPresent();
        assertTrue(res.isPresent());

        List<Todo> todos = res.get();
        assertEquals(1, todos.size());
        assertEquals(2, todos.get(0).getDescription().size());
        assertEquals("With this data captured", todos.get(0).getDescription().get(0));
        assertEquals("And this", todos.get(0).getDescription().get(1));

    }

    @Test
    public void testInvalidTodos() {
        String content =
                "public class App{ \n"
                        + "public static void main(String[] args) {\n"
                        + "// \"^(.*)TODO:(.*)$\"\n"
                        + "// This is invalid, its used as part of a regex string\n"
                        + "// This is just an edge case of my project, but should be covered\n"
                        + "// TODO: This should be parsed however\n"
                        + "// With this data captured\n"
                        + "}\n"
                        + "}";

        File testFile = createFileWithContent("test_ignore_todo.java", "testDirectory", content);

        TodoParser todoParser = new TodoParserImpl();

        Optional<List<Todo>> res = todoParser.parse(testFile.getAbsolutePath());

        assertTrue(res.isPresent());

        List<Todo> todos = res.get();

        assertEquals(1, todos.size());
        assertEquals(1, todos.get(0).getDescription().size());
        assertEquals("With this data captured", todos.get(0).getDescription().get(0));
    }


    @Test
    public void testMultilineComment() {
        String content =
                "public class App{ \n"
                        + "public static void main(String[] args) {\n" +
                        "/**\n" +
                        "* TODO: This todo should be caught\n" +
                        "* Along side any additional lines\n" +
                        "*/\n" +
                        "}";

        File testFile = createFileWithContent("test_multiline_comment.java", "testDirectory", content);

        TodoParser todoParser = new TodoParserImpl();

        Optional<List<Todo>> res = todoParser.parse(testFile.getAbsolutePath());

        assertTrue(res.isPresent());

        List<Todo> todos = res.get();
        assertEquals(1, todos.size());
        assertEquals(1, todos.get(0).getDescription().size());
        assertEquals("Along side any additional lines", todos.get(0).getDescription().get(0));
        assertEquals("This todo should be caught", todos.get(0).getTitle());

    }


}
