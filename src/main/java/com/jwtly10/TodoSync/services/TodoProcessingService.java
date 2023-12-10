package com.jwtly10.TodoSync.services;

import com.jwtly10.TodoSync.models.Todo;

public interface TodoProcessingService {

    /**
     * Process a todo
     *
     * @param todo the todo to process
     * @return true if successful, false otherwise
     */
    boolean processTodo(Todo todo);
}
