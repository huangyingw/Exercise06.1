package jdbc;

import org.springframework.dao.DataIntegrityViolationException;

public class DuplicateKeyException extends DataIntegrityViolationException {

	private static final long serialVersionUID = 1L;

	public DuplicateKeyException(String msg) {
		super(msg);
	}

	public DuplicateKeyException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
