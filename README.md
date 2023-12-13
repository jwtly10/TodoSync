# TodoSync

**TodoSync** is a command-line tool written in Java that automates the management of TODO comments
in a Git repository. It scans your source code for TODO comments, creates GitHub issues for each
TODO, and updates the source code by replacing the original TODO comments with references to the
corresponding GitHub issues, before finally committing the changes to the Git repository.

For the app to pickup your TODO comments, they must be formatted like this:

```
TODO: YOUR_TODO_HEADER
YOUR_TODO_BODY
```

This app is langauge agnostic, and can be used with any programming language, as long as the prefix or
'comment' for the entire content of your TODO. For example, if you use `//` for comments in Java,
then all your TODO comments must be formatted like this:

```java
public static void main(String[] args) {
    System.out.println("Hello, World!");
    // TODO: This will be your title of your todo or can be the only content.
    // This can be additional information which will be included in the github issue.
}
```

TodoSync will create a GitHub issue for each TODO comment, and replace the original TODO comment with a
reference to the corresponding GitHub issue. For example, the above TODO comment will be replaced with
something like this:

```java
public static void main(String[] args) {
    System.out.println("Hello, World!");
    // TODO(#1): This will be your title of your todo or can be the only content.
    // This can be additional information which will be included in the github issue.
}
```    

And each TODO will create its own commit automatically which you are free to push or consolidate into a single commit.

## Features

- Input: Takes a directory as input, checking if it's a Git repository.
- Analysis: Scans the source code for TODO comments.
- GitHub Interaction: Uses the GitHub API to create issues for each TODO comment.
- Source Code Update: Updates the source code by replacing TODO comments with a format referencing the corresponding
  GitHub issue.
- [In Progress] Headless mode as part of a CI/CD pipeline.

## Requirements

- Java 17 or higher is required to run TodoSync.
- Git

## Installation

You can download the latest release from the [releases page](TODO), or build the project yourself.

## Building

1. Clone the repository
2. Run `mvn clean package` in the root directory
3. The JAR file will be generated in the `target` directory
4. Copy the JAR file to a location of your choice
5. Create a configuration file ([see below](#configuration))

## Usage

To run TodoSync, use the following command:

```bash
java -jar TodoSync.jar [options] <directory>

    --v / --verbose: Enables verbose output.
```

If you use popular shells such as Bash or Zsh, you can create an alias to run TodoSync from anywhere:

```bash
alias todosync="java -jar /path/to/TodoSync.jar"
```

Which allows you to run TodoSync like this:

```bash
todosync [options] <directory>
```

... Simple :)

## Configuration

TodoSync requires a GitHub token for interaction with the GitHub API. You can configure the token by creating a
configuration file at one of the following locations:

    $HOME/.todosync
    $HOME/.config/.todosync

The configuration file should contain:

```

github.token=YOUR_GITHUB_TOKEN

```

Replace YOUR_GITHUB_TOKEN with your actual GitHub personal access token.

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)

