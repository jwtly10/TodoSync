package com.jwtly10.TodoSync.parsers;

import com.jwtly10.TodoSync.models.Todo;

import java.util.List;
import java.util.Optional;

public class TodoParserImpl implements TodoParser {

    @Override
    public Optional<List<Todo>> parse(String filepath) {
        System.out.println("Parsing file: " + filepath);
        return Optional.empty();
    }
}
