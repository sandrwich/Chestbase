package pl.daffit.chestbase.validation;

import java.io.IOException;

public class DataValidationException extends IOException {

    public DataValidationException() {
        super();
    }

    public DataValidationException(String s) {
        super(s);
    }

    public DataValidationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DataValidationException(Throwable throwable) {
        super(throwable);
    }
}
