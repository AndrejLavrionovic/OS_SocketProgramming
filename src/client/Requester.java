package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Requester extends Thread{
	
	// CONSTANCE
	private final String HOME = System.getProperty("user.home") + File.separator + "Desktop";
	
	// Instances and variables
	private Socket requestSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String message = "";
	private String ipaddress;
	private Scanner stdin;
	private boolean state;
	private int option;
 	
 	// Empty constructor
	Requester(){}
	
	// run() method
	public void run()
	{
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
			
			state = false;
			
			//3: Communicating with the server
			//******************************************************************************
			do{
				// Authentication
				//********************************************
				while(!state){
					String str = (String)in.readObject();
					System.out.println("server < " + str);
					
					// Authentication
					switch(str){
					case "Connection successful":
						message = "user";
						sendMessage(message);
						break;
					case "Username:":
						System.out.print("> ");
						message = stdin.next();
						sendMessage(message);
						break;
					case "Password:":
						System.out.print("> ");
						message = stdin.next();
						sendMessage(message);
						break;
					default:
						state = true;
					}
				}
				
				String str = null;
				int step;
				
				option = choose();
				
				switch(option){
				case 2:
					message = "push";
					sendMessage(message);
					step = 0;
						if(step == 0){
							str = (String)in.readObject();
							System.out.println("server < " + str);
						}
					break;
				case 3:
					message = "list";
					sendMessage(message);
					do{
						File[] f = (File[])in.readObject();
						if(f != null){
							for(File file : f){
								System.out.println(file.getName());
							}
						}
						else{
							System.out.println("Directory is empty");
						}
						str = "done";
					}while(!str.equals("done"));
					break;
				case 5:
					message = "newdir";
					sendMessage(message);
					
					str = (String)in.readObject();
					System.out.println("server < " + str);
					
					System.out.print("> ");
					message = stdin.next();
					
					break;
				case 6:
					message = "bye";
					sendMessage(message);
					break;
				}
				
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
	
	// Choose method
	private int choose(){
		int opt = 0;
		
		System.out.println("\n\n1 - Copy file from the server");
		System.out.println("2 - Move file to the server");
		System.out.println("3 - List all the files");
		System.out.println("4 - Move to a different directory");
		System.out.println("5 - Make a new directory");
		System.out.println("6 - Logout");
		
		System.out.print("> ");
		opt = stdin.nextInt();
		
		while(opt < 1 || opt > 6){
			System.out.print("Choose between 1 and 6 > ");
			opt = stdin.nextInt();
		}
		
		return opt;
	}
	
	// main method
	public static void main(String args[])
	{
		Requester client = new Requester();
		client.start();
	}
}