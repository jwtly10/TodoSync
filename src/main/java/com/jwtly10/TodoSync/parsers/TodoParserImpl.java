package com.jwtly10.TodoSync.parsers;

import com.jwtly10.TodoSync.models.Todo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TodoParserImpl implements TodoParser {
    private static final String TODO_REGEX = "^(.*)TODO: (.*)$";

    @Override
    public Optional<List<Todo>> parse(String filepath) {
        Optional<List<Todo>> todos = Optional.empty();
        File file = new File(filepath);
        if (file.exists()) {
            try {
                List<String> lines = Files.readAllLines(file.toPath());
                todos = parseTodos(lines);

                // We can set filepath here because we know the file exists
                if (todos.isPresent()) {
                    List<Todo> todoList = todos.get();

                    for (Todo todo : todoList) {
                        todo.setFilepath(filepath);
                    }

                    todos = Optional.of(todoList);
                }

            } catch (IOException e) {
                System.out.println("Error reading file: " + filepath);
                System.exit(1);
            }
        }

        return todos;
    }

    private Optional<List<Todo>> parseTodos(List<String> lines) {
        List<Todo> todos = new ArrayList<>();
        int i = 0;
        Pattern pattern = Pattern.compile(TODO_REGEX);

        while (i < lines.size()) {
            String line = lines.get(i);
            Matcher matcher = pattern.matcher(line);

            if (matcher.matches()) {
                String prefix = matcher.group(1);
                String title = matcher.group(2);
                Integer lineNo = i + 1;

                Todo todo = Todo.builder().prefix(prefix).title(title).line(lineNo).build();

                i = parseAdditionalLines(lines, i, todo);

                todos.add(todo);
            }
            i++;
        }

        if (todos.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(todos);
    }

    /**
     * Parse additional lines that follow the todo, and append them to the todo description
     *
     * @param lines
     * @param index
     * @param todo
     * @return the index of the line that is not a comment
     */
    private int parseAdditionalLines(List<String> lines, int index, Todo todo) {
        List<String> description = new ArrayList<>();
        // Skip the first line because we already set it as the title
        index++;
        while (index < lines.size() && lines.get(index).trim().startsWith(todo.getPrefix())) {
            String additionalLine = lines.get(index).trim().replaceFirst(todo.getPrefix(), "");
            description.add(additionalLine);
            index++;
        }

        todo.setDescription(description);
        return index;
    }
}
