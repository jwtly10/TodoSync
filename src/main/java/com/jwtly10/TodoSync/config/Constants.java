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
}
