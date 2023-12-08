package com.jwtly10.TodoSync.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Todo {

    /** The prefix of the todo, AKA the comment precursor for this file */
    private String prefix;

    /** The filepath of the todo. */
    private String filepath;

    /** The line number of the todo. */
    private Integer line;

    /** The title of the todo, AKA the line where the todo is defined. */
    private String title;

    /** The description of the todo, any subsequent lines that follow the prefix of the todo */
    private List<String> description;
}
