package com.jwtly10.TodoSync.parsers;

import com.jwtly10.TodoSync.models.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoParser {
    /**
     * Parses a file for todos.
     *
     * @param filepath The filepath of the file to parse.
     * @return A list of todos if any are found, otherwise an empty list.
     */
    public Optional<List<Todo>> parse(String filepath);
}
