package server;

public class ClientAuthentication implements Authenticatable {
	
	// Instance variables
	private String username;
	private String password;
	private boolean accepted;
	private String home;
	
	// Constructor
	public ClientAuthentication(){
		this.username = null;
		this.password = null;
		this.accepted = false;
	}
	
	// Getters and Setters
	/* (non-Javadoc)
	 * @see server.Authenticatable#getUsername()
	 */
	@Override
	public String getUsername() {
		return username;
	}

	/* (non-Javadoc)
	 * @see server.Authenticatable#setUsername(java.lang.String)
	 */
	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	/* (non-Javadoc)
	 * @see server.Authenticatable#getPassword()
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/* (non-Javadoc)
	 * @see server.Authenticatable#setPassword(java.lang.String)
	 */
	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	/* (non-Javadoc)
	 * @see server.Authenticatable#isAccepted()
	 */
	@Override
	public boolean isAccepted() {
		return accepted;
	}

	/* (non-Javadoc)
	 * @see server.Authenticatable#setAccepted(boolean)
	 */
	@Override
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	
	@Override
	public String getHome(){
		return this.home;
	}
	
	@Override
	public void setHome(String home){
		this.home = home;
	}
}
