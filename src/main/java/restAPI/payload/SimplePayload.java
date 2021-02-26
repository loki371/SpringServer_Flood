package restAPI.payload;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SimplePayload {
    private String message;

    private Object data;

    public SimplePayload(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public SimplePayload(String message) {
        this.message = message;
        this.data = null;
    }

    public SimplePayload(Object data) {
        this.data = data;
        this.message = "ok";
    }
}
