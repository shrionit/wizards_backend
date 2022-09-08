package com.code.wizards.tools;

public class FormError {
    private String field;
    private String message;
    public FormError(String field, String message) {
        this.field = field;
        this.message = message;
    }
    public String getType() {
        return getClass().getSimpleName();
    }
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
