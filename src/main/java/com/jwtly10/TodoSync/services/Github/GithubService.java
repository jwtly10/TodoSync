package com.jwtly10.TodoSync.services.Github;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueBuilder;
import org.kohsuke.github.GitHub;

import java.util.List;

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
     * @param title the title of the issue
     * @param body  the body of the issue
     * @return the issue number
     */
    public Integer createIssue(String title, List<String> body) {
        String bodyString = String.join("\n", body);
        try {
            GHIssueBuilder issueBuilder = github.getRepository("jwtly10/TodoSync").createIssue(title).body(bodyString);
            GHIssue issue = issueBuilder.create();

            System.out.println("Created issue: " + issue.getNumber() + " @ " + issue.getUrl());

            return issue.getNumber();


        } catch (Exception e) {
            // Failing to create an issue is not a critical error
            // We can continue processing the todo, but we should return 0
            // and let the user know that the issue was not created

            System.out.println("Error creating issue: " + e.getMessage());
            System.out.println("For issue title: " + title + " and body: " + bodyString);

            return 0;
        }
    }

}
