package client;

public interface Authenticatable {

	// Getters and Setters
	String getUsername();

	void setUsername(String username);

	String getPassword();

	void setPassword(String password);

	boolean isAccepted();
	
	void setAccepted(boolean accepted);

}