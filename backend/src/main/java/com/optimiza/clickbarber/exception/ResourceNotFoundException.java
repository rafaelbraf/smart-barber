package com.optimiza.clickbarber.exception;

import com.optimiza.clickbarber.utils.Constants;
import lombok.Getter;

import java.util.NoSuchElementException;

@Getter
public class ResourceNotFoundException extends NoSuchElementException {

    private final String resourceName;
    private final String fieldName;
    private final String fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format(Constants.Error.RESOURCE_NOT_FOUND_EXCEPTION, resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
