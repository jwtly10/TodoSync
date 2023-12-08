package com.jwtly10.TodoSync.parsers;

import static org.junit.Assert.assertEquals;

import com.jwtly10.TodoSync.models.Todo;

import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/** TodoParserTest */
public class TodoParserTest extends BaseParserTest {
    @Test
    public void testFindTodos() {
        String content =
                "public class App{ \n"
                        + "public static void main(String[] args) {\n"
                        + "// TODO: Implement this method\n"
                        + "// This is the body of the code that also needs to be parsed\n"
                        + "// This is also the case\n"
                        + "}\n"
                        + "public void testMethod() {\n"
                        + "// TODO: Implement this\n"
                        + "}\n"
                        + "}";

        File testFile = createFileWithContent("test_find_todos.java", "testDirectory", content);

        TodoParser todoParser = new TodoParserImpl();

        Optional<List<Todo>> res = todoParser.parse(testFile.getAbsolutePath());

        assert res.isPresent();

        List<Todo> todos = res.get();
        assert todos.size() == 2;

        Todo firstTodo = todos.get(0);
        assertEquals("Implement this method", firstTodo.getTitle());
        assertEquals(3, firstTodo.getLine().intValue());
    }

    @Test
    public void testBuildDescription() {
        String content =
                "public class App{ \n"
                        + "public static void main(String[] args) {\n"
                        + "// TODO: Implement this method\n"
                        + "// This is the body of the code that also needs to be parsed\n"
                        + "// This is also the case\n"
                        + "}\n"
                        + "public void testMethod() {\n"
                        + "// TODO: Implement this\n"
                        + "}\n"
                        + "}";

        File testFile =
                createFileWithContent("test_build_description.java", "testDirectory", content);

        TodoParser todoParser = new TodoParserImpl();

        Optional<List<Todo>> res = todoParser.parse(testFile.getAbsolutePath());

        assert res.isPresent();

        List<String> expectedDescription =
                Arrays.asList(
                        "This is the body of the code that also needs to be parsed",
                        "This is also the case");

        List<Todo> todos = res.get();
        assert todos.size() == 2;

        assertEquals(expectedDescription, todos.get(0).getDescription());
    }

    @Test
    public void testNoTodos() {
        String content =
                "public class App{ \n"
                        + "public static void main(String[] args) {\n"
                        + "}\n"
                        + "public void testMethod() {\n"
                        + "// This is just a normal comment\n"
                        + "}\n"
                        + "}";

        File testFile = createFileWithContent("test_no_todos.java", "testDirectory", content);

        TodoParser todoParser = new TodoParserImpl();

        Optional<List<Todo>> res = todoParser.parse(testFile.getAbsolutePath());

        assert res.isEmpty();
    }
}
