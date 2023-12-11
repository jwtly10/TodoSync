package com.jwtly10.TodoSync.parsers;

import com.jwtly10.TodoSync.models.Todo;
import com.jwtly10.TodoSync.services.Git.GitService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDirParser implements DirectoryParser {

    public static String repo = "";

    /**
     * Parses a directory for todos.
     *
     * @param dir The directory to parse.
     * @return A list of todos.
     */
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
     * Doesn't traverse subdirectories, only checks the root
     *
     * @param dir The directory to check.
     * @return True if the directory is a git directory, false otherwise.
     */
    protected boolean isGitDir(String dir) {
        File checkGitDir = new File(dir);
        File[] files = checkGitDir.listFiles();

        boolean gitDir = false;

        if (files != null) {
            gitDir = Arrays.stream(files).anyMatch(file -> file.getName().equals(".git"));
        }

        if (gitDir) {
            repo = GitService.getRepoName(dir);
        }

        return gitDir;
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
