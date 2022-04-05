package model.validators;

public class ValidationException extends RuntimeException {
    /**
     * Variatiuni de constructori ale clasei care genereaza o exceptie de tipul ValidationException
     * care mosteneste din clasa RuntimeException
     */


    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

