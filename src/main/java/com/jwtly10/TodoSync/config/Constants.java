package com.jwtly10.TodoSync.config;

import java.util.Arrays;
import java.util.List;

/** Constants */
public class Constants {

    public static final List<String> IGNORE_DIRS =
            Arrays.asList(
                    ".git",
                    "node_modules",
                    "build",
                    "dist",
                    "target",
                    ".settings",
                    ".idea",
                    ".vscode",
                    "out",
                    "bin",
                    "obj");

    public static final List<String> SUPPORTED_FILE_TYPES =
            Arrays.asList(
                    ".java", // Java source code
                    ".cpp", // C++ source code
                    ".py", // Python source code
                    ".js", // JavaScript source code
                    ".html", // HTML files
                    ".css", // CSS stylesheets
                    ".php", // PHP source code
                    ".rb", // Ruby source code
                    ".swift", // Swift source code
                    ".c", // C source code
                    ".h", // Header files
                    ".ts", // TypeScript source code
                    ".rs" // Rust source code
                    );
}
