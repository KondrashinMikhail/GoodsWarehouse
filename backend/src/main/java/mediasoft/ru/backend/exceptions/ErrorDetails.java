package mediasoft.ru.backend.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
    private String message;
    private String exceptionClassFrom;
    private String exception;
}
