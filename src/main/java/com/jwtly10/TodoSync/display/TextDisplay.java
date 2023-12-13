package com.jwtly10.TodoSync.display;

import com.jwtly10.TodoSync.models.Todo;

public class TextDisplay {
    public static void display(Todo todo) {
        System.out.println("TODO" + ": " + todo.getTitle());
        if (!todo.getDescription().isEmpty()) {
            for (String line : todo.getDescription()) {
                System.out.println("- " + line);
            }
        }
    }
}
