# SimpleDB Projects with Java

<h2>Run the SimpleDB Engine as a Server</h2> 
A. Create a run configuration for the server program.

• Go to “Run Configurations” in the Eclipse Run menu. Add a new configuration to your
SimpleDBEngine project, called “SimpleDB Server”. In the field for the main class, enter “simpledb.server.StartServer”.

• By default, the server will use a database named “studentdb”. This is what I recommend. But if you want to use a differently-named database, use the Arguments tab in the configuration to enter the database name.

B. Run the SimpleDB Server configuration you just created. A console window should appear indicating that the SimpleDB server is running.

C. The server creates its database in a different location from the embedded client.

The folder for this database lives in the SimpleDBEngine project. Refresh the project to
see it in the Eclipse project window.

<h2>Run the Server-based Client Programs</h2> 
Look at the programs in the network folder of the SimpleDBClients project.

• While the server is running, run the CreateStudentDB and StudentMajor clients. They should print the same output as the above step.

• Go to the console window for the server, and shut it down (by clicking on the red square near the top of the console window). Run StudentMajor again. You should get an error message. • Rerun the server, then run StudentMajor. It should now work.

• Run the ChangeMajor network client. Now you have two slightly different studentdatabases. In the embedded database, Amy is a math major. In the network database, Amy is a drama major.


<h2>Run the SimpleIJ Client Demo</h2> 
• Run the program SimpleIJ, which is in the “default package” folder for the SimpleDBClients project.

• The first thing it will ask for is a connection. Enter the following string, which will establish a connection to the embedded database.
jdbc:simpledb:studentdb,

• The client will now repeatedly ask you to enter SQL queries, one per line. Type the following query, which should print the name and majorid of all students. 

select sname, majorid from student

Note that Amy has majorid = 20 in this database. • Type “exit” to terminate the program.

• Assuming that the server is still running, re-run SimpleIJ. This time, enter the following network connection string
jdbc:simpledb://localhost

This will connect you to the network database (If your server is not running, it will show an error message). Type the following query, and note that Amy has majorid = 30 in this database.

select sname, majorid from student

• If you know SQL, try entering some other queries into SimpleIJ. You can figure out the names of the tables and their fields by looking at Figure 1.1 of the text. Section 1.5 of the text describes the subset of SQL supported by SimpleDB. What happens when you try to execute an SQL statement that SimpleDB doesn't support?

• Type “exit” to terminate the program.

• Shut down the server (by clicking on the red square near the top of the console window).




