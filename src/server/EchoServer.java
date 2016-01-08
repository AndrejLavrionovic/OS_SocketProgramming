package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// Main
//**********************************************************************************************
public class EchoServer {
	public static void main(String[] args) throws Exception {
		
		// Create a socket to connect to client
		@SuppressWarnings("resource")
		ServerSocket m_ServerSocket = new ServerSocket(2004,10);
		int id = 0;
		
		while (true) {
			Socket clientSocket = m_ServerSocket.accept();
			ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++);
			cliThread.start();
		}
	} // main method
} // main class

// ClientServerThread
//*********************************************************************************************
class ClientServiceThread extends Thread {
	
	// Instances and Variables
	private Socket clientSocket;
	private String message = null;
	private int clientID = -1;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String usersFile = "clients.txt";
	private String[][] usersList;
	private Authenticatable auth;
	private File userDir;
	private String home;

	// Constructor
	ClientServiceThread(Socket s, int i) {
		clientSocket = s;
		clientID = i;
	}

	// method that accepts message from the client and sends respond
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			
			System.out.println("server > " + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
  
	// Method that reads users.txt file
	public String[][] users(String file) throws IOException{
	  
		String[][] u = new String[2][2];
		BufferedReader br = new BufferedReader(new FileReader(file));
	  
		String line = null;
		int rows = 0;
	  
		while((line = br.readLine()) != null){
			String[] arr = line.split(" ");
		  
			if(!line.isEmpty()){
				u[rows][0] = arr[0];
				u[rows][1] = arr[1];
			}
			else{
				System.out.println("There is no records in the file");
			}
		  
			rows++;
		}
	  
		br.close();
	  
		return u;
	}
	
	// User validation
	public boolean validUser(String username, String password, String[][] users){
		
		boolean valid = false;
		int i;
		
		for(i = 0; i < users.length; i++){
			if(username.equals(users[i][0])){
				if(password.equals(users[i][1])){
					valid = true;
					break;
				}
			}
		}
		return valid;
	}
  
	// Thread
	public void run() {
		
		System.out.println("Accepted Client : ID - " + clientID + " : Address - "
		+ clientSocket.getInetAddress().getHostName());
		
		try 
		{

			usersList = users(usersFile);
			
			// Instance of authentication bunch
			auth = new ClientAuthentication();
			
			// Get Input and Output Streams
	    	out = new ObjectOutputStream(clientSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(clientSocket.getInputStream());
			
			System.out.println("Accepted Client : ID - " + clientID + " : Address - "
		        + clientSocket.getInetAddress().getHostName());
		

			// Reading from file clients.txt
			usersList = users(usersFile);
			
			// Comfirmation that connection successfull
			sendMessage("Connection successful");
			
			do{
				try
				{
					int row = 0;
					while(!auth.isAccepted()){
						
						message = (String)in.readObject();
						System.out.println("client " + clientID + " < " + message);
						
						
						if(message.equals("user")){
							sendMessage("Username:");
							System.out.println("client " + clientID + " is not authorised!");
						}
						else{
							if(auth.getUsername() == null && auth.getPassword() == null){
								boolean flag = true;
								auth.setUsername(message);
								
								for(int i = 0; i < usersList.length; i++){
									if(auth.getUsername().equals(usersList[i][0])){
										message = "Password:";
										flag = false;
										row = i;
										sendMessage(message);
										break;
									}
								}
								if(flag){
									System.out.println("Username " + auth.getUsername() + " is not valid!");
									auth.setUsername(null);
									sendMessage("Username:");
									row = 0;
								}
							}
							else if(auth.getUsername() != null && auth.getPassword() == null){
								auth.setPassword(message);
								if(auth.getPassword().equals(usersList[row][1])){
									auth.setAccepted(true);
									
									// CREATE USER.HOME DIRECTORY
									home = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + auth.getUsername();
									userDir = new File(home);
									message = "Hello " + auth.getUsername() + "; your dirrectory is: " + home;
									sendMessage(message);
								}
								else{
									System.out.println("Password " + auth.getPassword() + " is not valid!");
									auth.setPassword(null);
									message = "Password:";
									sendMessage(message);
								}
							}
						}
					}
					message = (String)in.readObject();
					
					if(message.equals("bye")){
						auth.setUsername(null);
						auth.setPassword(null);
						auth.setHome(null);
						auth.setAccepted(false);
						
						System.out.println("Username: " + auth.getUsername());
						System.out.println("Password: " + auth.getPassword());
						System.out.println("Home: " + auth.getHome());
						System.out.println("Username: " + auth.isAccepted());
					}
					else if(message.equals("list")){
							File[] fList = userDir.listFiles();
							out.writeObject(fList);
					}
					else if(message.equals("")){
//						boolean running = true;
//						int step = 0;
//						
						sendMessage("Send me a file");
//						
//						File f = (File)in.readObject();
//						if(f != null){
//							String path
//						}
					}
					else if(message.equals("newdir")){
						boolean running = true;
						sendMessage("Name of Directory");
						
						do{
							String dirName = (String)in.readObject();
							if(new File(home + File.separator + dirName).mkdir()){
								sendMessage("Directory " + dirName + " is created!");
								running = false;
							}
							else
								sendMessage("Making directory faild!");
						}while(running);
					}
//					
//					// Communication with client
//					message = (String)in.readObject();
//					System.out.println("client " + clientID + " < " + message);
//					sendMessage("server got the following: " + message);
				}
				catch(ClassNotFoundException classnot){
					System.err.println("Data received in unknown format");
				}
				
	    	}while(!message.equals("bye"));
	      
			System.out.println("Ending Client : ID - " + clientID + " : Address - "
		        + clientSocket.getInetAddress().getHostName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // run
} // class
