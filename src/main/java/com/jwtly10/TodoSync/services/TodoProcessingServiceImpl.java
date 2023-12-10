package com.jwtly10.TodoSync.services;

import com.jwtly10.TodoSync.models.Todo;
import com.jwtly10.TodoSync.services.Github.GithubService;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

public class TodoProcessingServiceImpl implements TodoProcessingService {
    private final GithubService githubService;

    public TodoProcessingServiceImpl(GithubService githubService) {
        this.githubService = githubService;
    }

    /**
     * Process a todo, creating a github issue for it, and updating the todo with the issue number
     *
     * @param todo the todo to process
     * @return true if successful, false otherwise
     */
    @Override
    public boolean processTodo(Todo todo) {
        System.out.println("Processing todo");
        String fileToProcess = todo.getFilepath();
        String title = todo.getTitle();
        List<String> body = todo.getDescription();

        File file = new File(fileToProcess);

        if (!file.exists()) {
            System.out.println("File does not exist: " + fileToProcess);
            return false;
        }

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            String lineToReplace = lines.get(todo.getLine() - 1);
            Optional<Integer> githubIssueNumber = createIssue(title, body);
            if (githubIssueNumber.isEmpty()) {
                System.out.println("Failed to create github issue in line: " + todo.getLine() + " in file: " + fileToProcess);
                return false;
            }
            lines.set(todo.getLine() - 1, lineToReplace.replace("TODO", "TODO(#" + githubIssueNumber.get() + ")"));

            if (lines.get(todo.getLine() - 1).equals(lineToReplace)) {
                System.out.println("Unknown Failure: Failed to update line " + todo.getLine() + " in file: " + fileToProcess);
                System.out.println("Github issue has been raised. Please check your repository for the issue.");

                return false;
            }

            Files.write(file.toPath(), lines);
        } catch (Exception e) {
            System.out.println("Error reading file: " + fileToProcess);
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private Optional<Integer> createIssue(String title, List<String> body) {
        return Optional.of(githubService.createIssue(title, body));
    }

}
