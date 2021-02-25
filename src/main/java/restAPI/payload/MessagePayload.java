package restAPI.payload;

public class MessagePayload {
	private String message;

	public MessagePayload(String message) {
	    this.message = message;
	  }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
