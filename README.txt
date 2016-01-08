Operating System Project

Student: Andrejs Lavrinovics
Student Number: G00196984

The project is to write a Multi-threaded TCP Server Application which allows
multiple client application to transfer files to and from the server. The client
application can use command line input from the user to implement user functions.

1. After successful connection user asked to prompt a username. If username is in
the list, then user must to enter password. List of users and their passwords
located in the server side on the desktop in the file clients.txt.

Server application reads the clients.txt file and creates double dimensional
array usersList[users_number][username/password].

When username and password are match it stores in the ClientAuthentication object.

2. After user being logged in, server application creates a home path for that user
to directory named as username. It stores in ClientAuthentication object as well.
Then client application propose to user some options such as transfer files from
server to client or other way, list files, create directory, move to the new
directory.

3. One of the option is to log out. When user choose to exit, all credentials on the
server sets to the nulls including path. 
