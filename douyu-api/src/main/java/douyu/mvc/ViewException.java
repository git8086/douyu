package douyu.mvc;

public class ViewException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ViewException() {
		super();
	}

	public ViewException(String message) {
		super(message);
	}

	public ViewException(Throwable cause) {
		super(cause);
	}

	public ViewException(String message, Throwable cause) {
		super(message, cause);
	}
}