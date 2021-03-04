package es.centic.taller1.taller1.users;

public class UserExistsException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserExistsException(String username) {
        super("User " + username + " already exists");
    }
}
