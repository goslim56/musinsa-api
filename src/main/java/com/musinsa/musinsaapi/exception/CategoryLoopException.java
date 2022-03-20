package com.musinsa.musinsaapi.exception;

public class CategoryLoopException extends Exception{
    public CategoryLoopException() {
        super("Circular reference category structures are not allowed.");
    }
}
