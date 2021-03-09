package restAPI.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter @Getter @NoArgsConstructor
public class ExtraPayload {
    private String message;

    private Object data;

    @JsonIgnore
    private HttpStatus httpStatus;

    public ExtraPayload(String message, Object data, HttpStatus httpStatus) {
        this.message = message;
        this.data = data;
        this.httpStatus = httpStatus;
    }
}
