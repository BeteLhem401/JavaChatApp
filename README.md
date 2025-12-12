# JavaChatApp
A simple multi-client chat application written in Java using socket programming.
It lets multiple people chat with each other in real time.

## What it does

- Lets many clients connect to a server at the same time  
- Easy-to-use GUI (windows with text boxes and send buttons)  
- Shows when someone joins the chat  
- Messages are sent to everyone connected  

## How to use it

1. First, start the server:

   java chatserver.ChatServer 
Then, open the chat client (you can open it multiple times to simulate multiple people):

java chatclient.ChatClient
In the client window:

1 Enter your name

2 Enter server host (usually localhost)

3 Enter port (default 5000)

4 Click Connect

Type your message in the bottom box and hit Send (or press Enter).
Your message will appear in other connected clients’ windows.

Project structure
main folder
 └── src/
     ├── chatserver/
     │    └── ChatServer.java
     └── chatclient/
          └── ChatClient.java
Requirements
Java installed (JDK 8 or newer)

Any terminal or IDE to run Java programs
