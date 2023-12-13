package com.jwtly10.TodoSync.services.Github;

import com.jwtly10.TodoSync.models.Todo;
import com.jwtly10.TodoSync.services.Git.GitService;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueBuilder;
import org.kohsuke.github.GitHub;

public class GithubService {
    private GitHub github;

    public GithubService(String token) {
        try {
            github = GitHub.connectUsingOAuth(token);
        } catch (Exception e) {
            System.out.println("Error connecting to GitHub");
            e.printStackTrace();
        }
    }

    /**
     * Create a GitHub issue
     *
     * @param todo the todo to create the issue for
     * @return the issue number if successful, 0 otherwise
     */
    public Integer createIssue(Todo todo) {
        String repo = GitService.getRepoName(GitService.findGitDirectoryFromFile(todo.getFilepath()));
        String bodyString = String.join("\n", todo.getDescription());
        try {
            GHIssueBuilder issueBuilder = github.getRepository(repo).createIssue(todo.getTitle()).body(bodyString);
            GHIssue issue = issueBuilder.create();

            System.out.println("Issue created @ " + buildIssueUrl(repo, issue.getNumber()));

            return issue.getNumber();


        } catch (Exception e) {
            // Failing to create an issue is not a critical error
            // We can continue processing the todo, but we should return 0
            // and let the user know that the issue was not created

            System.out.println("Error creating issue: " + e.getMessage());
            System.out.println("For issue title: " + todo.getTitle() + " and body: " + bodyString);

            return 0;
        }
    }

    private String buildIssueUrl(String repo, Integer issueNumber) {
        return "https://www.github.com/" + repo + "/issues/" + issueNumber;
    }
}
