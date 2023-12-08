package com.jwtly10.TodoSync.parsers;

import com.jwtly10.TodoSync.models.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDirParser implements DirectoryParser {

    @Override
    public final List<Todo> parseDir(String dir) {
        List<Todo> res = new ArrayList<>();

        if (isGitDir(dir)) {
            Optional<List<Todo>> opt = parseGitDir(dir);
            if (opt.isPresent()) {
                res = opt.get();
            }
        } else {
            parseNonGitDir(dir);
        }

        return res;
    }

    /**
     * Determines if a directory is a git directory.
     *
     * @param dir The directory to check.
     * @return True if the directory is a git directory, false otherwise.
     */
    protected boolean isGitDir(String dir) {
        return dir.contains(".git");
    }

    /**
     * Parses a git directory for todos.
     *
     * @param dir The directory to parse.
     */
    protected abstract Optional<List<Todo>> parseGitDir(String dir);

    /**
     * Implements the logic for parsing a non-git directory.
     *
     * @param dir The directory to parse.
     */
    protected void parseNonGitDir(String dir) {
        System.out.println("Not a git directory: " + dir);
        System.exit(1);
    }
}
