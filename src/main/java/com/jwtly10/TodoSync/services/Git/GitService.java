package com.jwtly10.TodoSync.services.Git;

import com.jwtly10.TodoSync.config.AppConfig;
import com.jwtly10.TodoSync.exceptions.GitException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.StoredConfig;

import java.io.File;

public class GitService {

    public static void commitTodo(String filePath, String message) {
        String gitDir = findGitDirectoryFromFile(filePath);

        if (gitDir == null) {
            throw new GitException("Could not find git directory for file: " + filePath);
        }

        try (Git git = Git.open(new File(gitDir))) {
            git.add().addFilepattern(".").call();
            git.commit().setMessage(message).call();
        } catch (Exception e) {
            throw new GitException("Error committing todo: " + e);
        }
    }

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
            return extractRepoName(remoteUrl);
        } catch (RepositoryNotFoundException e) {
            // This is just so we can run tests outside a real git repo
            if (AppConfig.getInstance().getProp("ignore.git") != null) {
                return "";
            }

            throw new GitException("Error getting repo name in dir: " + e);
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
        String repoName = parts[parts.length - 1];
        String userName = parts[parts.length - 2].split(":")[1];
        String repoWithGitExtension = userName + "/" + repoName;
        return repoWithGitExtension.replace(".git", "");
    }

    public static String findGitDirectoryFromFile(String filePath) {
        File currentDirectory = new File(filePath).getParentFile();

        while (currentDirectory != null) {
            File gitDirectory = new File(currentDirectory, ".git");

            if (gitDirectory.exists() && gitDirectory.isDirectory()) {
                return gitDirectory.getParentFile().getAbsolutePath();
            }

            currentDirectory = currentDirectory.getParentFile();
        }

        return null;  // Git directory not found
    }
}
