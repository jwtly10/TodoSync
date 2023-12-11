package com.jwtly10.TodoSync.services.Git;

import com.jwtly10.TodoSync.exceptions.GitException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.StoredConfig;

import java.io.File;

public class GitService {


    /**
     * Get the name of the repo from the git directory
     *
     * @param gitDir the git directory
     * @return the name of the repo
     */
    public static String getRepoName(String gitDir) {
        File repoDir = new File(gitDir);

        try (Git git = Git.open(repoDir)) {
            StoredConfig config = git.getRepository().getConfig();
            String remoteUrl = config.getString("remote", "origin", "url");
            String repoName = extractRepoName(remoteUrl);
            System.out.println("Repo name: " + repoName);
            return repoName;
        } catch (Exception e) {
            throw new GitException("Error getting repo name in dir: " + e);
        }
    }

    /**
     * Get the name of the repo from the git directory
     *
     * @param remoteUrl the remote url of the repo
     * @return the name of the repo
     */
    private static String extractRepoName(String remoteUrl) {
        String[] parts = remoteUrl.split("/");
        String repoWithGitExtension = parts[parts.length - 1];
        return repoWithGitExtension.replace(".git", "");
    }
}
