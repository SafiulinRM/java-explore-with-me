package ewm.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiError {
    private Object errors;
    private String message;
    private String reason;
    private String status;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiError(Object errors, String message, String reason, String status) {
        this.errors = errors;
        this.message = message;
        this.reason = reason;
        this.status = status;
    }
}
