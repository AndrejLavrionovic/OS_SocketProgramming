package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Requester extends Thread{
	
	// Instances and variables
	private Socket requestSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String message = "";
	private String ipaddress;
	private Scanner stdin;
	private Authenticatable auth;
	
	private String username;
	private String password;
 	
 	// Empty constructor
	Requester(){}
	
	// run() method
	public void run()
	{
		auth = new UserAuthentication();
		stdin = new Scanner(System.in);
		try{
			//1. creating a socket to connect to the server
			//******************************************************************************
			System.out.println("Please Enter your IP Address");
			ipaddress = stdin.next();
			
			requestSocket = new Socket(ipaddress, 2004);
			
			System.out.println("Connected to " + ipaddress + " in port 2004");
			
			//2. get Input and Output streams
			//******************************************************************************
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			System.out.println("Hello");
			
			//3: Communicating with the server
			//******************************************************************************
			do{
				// Authentication
				while(auth.isAccepted() == false){
						message = (String)in.readObject();
						System.out.println("server < " + message);
					
					if(message.equals("Enter Your Username and Password:")){
						System.out.print("Username > ");
						username = stdin.next();
						auth.setUsername(username);
						
						System.out.print("Password > ");
						password = stdin.next();
						auth.setPassword(password);
						
						out.writeObject(auth);
					}
					
					Authenticatable a = (UserAuthentication)in.readObject();
					if(a.isAccepted() == true){
						auth.setAccepted(a.isAccepted());
						auth.setUsername(a.getUsername());
						auth.setPassword(a.getPassword());
					}
				}
				
				// Receive message
//					do{
//						message = (String)in.readObject();
//						
//						if(!message.equalsIgnoreCase("done")){
//							System.out.println("server < " + message);
//						}
//					}while(!message.equalsIgnoreCase("done"));
//					
				System.out.println("Please Enter the Message to send...");
				message = stdin.nextLine();
				sendMessage(message);
			}while(!message.equals("bye"));
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		catch(ClassNotFoundException unknownClass){
			System.err.println(unknownClass.getMessage());
		}
		finally{
			//4: Closing connection
			//******************************************************************************
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	
	// method that manages sending messages
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client > " + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	// main method
	public static void main(String args[])
	{
		Requester client = new Requester();
		client.start();
	}
}