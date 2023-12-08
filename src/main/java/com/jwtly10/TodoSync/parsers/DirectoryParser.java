package com.jwtly10.TodoSync.parsers;

import com.jwtly10.TodoSync.models.Todo;

import java.util.List;

public interface DirectoryParser {
    /**
     * Parses a directory for todos.
     *
     * @param dir The directory to parse.
     * @return A list of todos.
     */
    public List<Todo> parseDir(String dir);
}
