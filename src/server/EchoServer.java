package server;

import java.io.BufferedReader;
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
	private boolean running = true;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String usersFile = "clients.txt";
	private String[][] usersList;
	private Authenticatable auth;
	
	private String username;
	private String password;

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
  
	// Thread
	public void run() {

		
		System.out.println("Accepted Client : ID - " + clientID + " : Address - "
		+ clientSocket.getInetAddress().getHostName());
		
		try 
		{
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
					// 1) Authentification
					while(auth.isAccepted() == false){
						boolean isUser = false;
						boolean isPass = false;
						
						sendMessage("Enter Your Username and Password:");
						
						Authenticatable a  = (ClientAuthentication)in.readObject();
						
						username = a.getUsername();
						password = a.getPassword();
						
						if(!username.isEmpty() && username != null){
							for(int i = 0; i < usersList.length; i++){
								if(username.equals(usersList[i][0])){
									isUser = true;
									
									if(!password.isEmpty() && password != null){
										if(password.equals(usersList[i][1])){
											isPass = true;
											sendMessage("Hi " + username + ", you login is successful");
											auth.setUsername(username);
											auth.setPassword(password);
											auth.setAccepted(true);
											
											out.writeObject(auth);
										}
									}
								}
							}
							System.out.println("Clients user name is " + username);
						}
//						while(auth.getUsername() == null){
//							// Ask client to promt username
//							message = "Enter Your Username:";
//							out.writeObject(message);
//							out.flush();
//							
//							// Receive username from client
//							//username = (String)in.readObject();
//						}
//						//sendMessage("Your username is: " + username);
					}
					
					// Communication with client
//					message = (String)in.readObject();
//					System.out.println("client " + clientID + " < " + message);
//					//if (message.equals("bye"))
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
