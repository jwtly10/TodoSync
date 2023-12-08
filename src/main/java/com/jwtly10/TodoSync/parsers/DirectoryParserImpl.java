package com.jwtly10.TodoSync.parsers;

import com.jwtly10.TodoSync.config.Constants;
import com.jwtly10.TodoSync.models.Todo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DirectoryParserImpl extends AbstractDirParser {

    private TodoParser todoParser;

    public DirectoryParserImpl(TodoParser todoParser) {
        this.todoParser = todoParser;
    }

    @Override
    public Optional<List<Todo>> parseGitDir(String dir) {

        File directory = new File(dir);

        File[] files = directory.listFiles();

        List<Todo> todos = new ArrayList<>();

        for (File file : files) {
            if (file.isDirectory() && !Constants.IGNORE_DIRS.contains(file.getName())) {
                parseGitDir(file.getAbsolutePath());
            } else if (file.isFile()) {
                Optional<List<Todo>> result = todoParser.parse(file.getAbsolutePath());
                if (result.isPresent()) {
                    todos.addAll(result.get());
                }
            }
        }

        return Optional.ofNullable(todos);
    }
}
