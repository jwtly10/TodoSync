package com.jwtly10.TodoSync.display;

import com.jwtly10.TodoSync.models.Todo;

public class TextDisplay {
    public static void display(Todo todo, boolean verbose) {
        String fn = todo.getFilepath().substring(todo.getFilepath().lastIndexOf("/") + 1);
        System.out.println(fn + ":" + todo.getLine() + ":    " + todo.getPrefix() + " TODO" + ": " + todo.getTitle());
        if (verbose) {
            if (!todo.getDescription().isEmpty()) {
                for (String line : todo.getDescription()) {
                    System.out.println("- " + line);
                }
            }
        }
    }

    public static void display(Todo todo) {
        // Get the file name from the patho
        String fn = todo.getFilepath().substring(todo.getFilepath().lastIndexOf("/") + 1);
        System.out.println(fn + ":" + todo.getLine() + ": " + todo.getTitle());
    }
}
