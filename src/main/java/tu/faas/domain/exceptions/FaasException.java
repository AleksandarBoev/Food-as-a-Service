package tu.faas.domain.exceptions;

public class FaasException extends RuntimeException {
    public FaasException() {
    }

    public FaasException(String message) {
        super(message);
    }
}
