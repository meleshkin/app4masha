package com.example.app4masha.app4masha.service.exception;

import java.io.IOException;

public class StorageException extends Exception{

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, IOException exception) {
        super(message, exception);
    }
}
