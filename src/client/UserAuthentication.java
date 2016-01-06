package client;

public class UserAuthentication implements Authenticatable {

	// Instance variables
	private String username;
	private String password;
	private boolean accepted;
	
	// Constructor
	public UserAuthentication(){
		this.username = null;
		this.password = null;
		this.accepted = false;
	}
	
	// Getters and Setters
	/* (non-Javadoc)
	 * @see client.Authenticatable#getUsername()
	 */
	@Override
	public String getUsername() {
		return username;
	}

	/* (non-Javadoc)
	 * @see client.Authenticatable#setUsername(java.lang.String)
	 */
	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	/* (non-Javadoc)
	 * @see client.Authenticatable#getPassword()
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/* (non-Javadoc)
	 * @see client.Authenticatable#setPassword(java.lang.String)
	 */
	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	/* (non-Javadoc)
	 * @see client.Authenticatable#isAccepted()
	 */
	@Override
	public boolean isAccepted() {
		return accepted;
	}

	/* (non-Javadoc)
	 * @see client.Authenticatable#setAccepted()
	 */
	@Override
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
}
