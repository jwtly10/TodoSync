package com.jwtly10.TodoSync.parsers;

import com.jwtly10.TodoSync.config.Constants;
import com.jwtly10.TodoSync.models.Todo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DirectoryParserImpl extends AbstractDirParser {

    private final TodoParser todoParser;

    public DirectoryParserImpl(TodoParser todoParser) {
        this.todoParser = todoParser;
    }

    @Override
    public Optional<List<Todo>> parseGitDir(String dir) {
        List<Todo> todos = new ArrayList<>();
        File directory = new File(dir);
        File[] files = directory.listFiles();

        if (files == null) {
            return Optional.empty();
        }

        for (File file : files) {
            if (file.isDirectory() && !Constants.IGNORE_DIRS.contains(file.getName())) {
                Optional<List<Todo>> subDirTodos = parseGitDir(file.getAbsolutePath());
                subDirTodos.ifPresent(todos::addAll);
            }

            if (file.isFile() && Constants.SUPPORTED_FILE_TYPES.contains(getFileExtension(file))) {
                Optional<List<Todo>> fileTodos = todoParser.parse(file.getAbsolutePath());
                fileTodos.ifPresent(todos::addAll);
            }
        }

        return Optional.of(todos);
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int index = fileName.lastIndexOf('.');
        return index == -1 ? "" : fileName.substring(index);
    }
}
