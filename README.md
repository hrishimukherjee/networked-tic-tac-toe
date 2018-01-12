# Networked Tic-Tac-Toe
**Authors:** Hrishi Mukherjee, Haamed Sultani

A networked tic tac toe game allowing multiple players to join a lobby and play against each other or against AI.

Key Features: Multi-Threading, Reactor Design Pattern, Sockets, Client-Server Architecture

- separated the UI from the network layer using multi-threading
- handled request-response using reactor design pattern both on client and server
- exchanged network information between client-server using sockets
- developed AI for user vs. computer mode
---
# Running the Program

This document guides the user to be able to successfully TEST the 
functionality of the tic-tac-toe Android application. This guide was
written based off of a Windows OS, therefore, the steps might differ
slightly if ran on a different environment.

ASSUMPTIONS:
------------

- Android Studio v2.2.3 is installed
- Android Virtual Devices can be run on user's machine
- Android mobile device can run the tic tac toe application
- Eclipse Java IDE for Web Developers (Neon Release 4.6.0)
- Eclipse is properly setup to run the server

SETUP:
------
CLIENT:

Before testing the application, we need to make sure that the
user has an Android Virtual Machine up and running. We will
be setting up a Nexus 5 AVD running on API 23 (Marshmallow).
If the user already has this setup, then they can skip straight
to the "TESTING" section. The following outlines how to setup the AVD:

1. Start Android Studio
2. In the toolbar at the top of the window, find and click 'Tools'
3. In the dropdown, click 'Android'
4. In the dropdown, click 'AVD Manager'
5. In the popup window, find and click 'Create Virtual Device...' (Bottom Left)
6. In the popup window, choose Category as 'Phone' and find 'Nexus 5'
7. Press 'Next'
8. For the System Image, choose 'Marshmallow' with API Level '23'
9. Press 'Next'
10. In the following screen, click 'Finish'

The user should successfully have a Nexus 5 AVD running on API 23 available in
the AVD Manager now!



TESTING:
--------

The following steps guide the user through testing the tic-tac-toe application.

START-UP:

Client
1. Find the provided directory 'COMP2601A2Client'
2. In the toolbar at the top of the window, find and click 'File'
3. In the dropdown menu, click 'Open...'
4. Navigate to the root directory of the folder in step 1
5. Once the project opens, wait till it finishes building (takes about a minute)
6. Find the play button in the toolbar and click it
7. In the following pop up, choose the Nexus 5 API 23 and press 'OK'
8. Wait till the AVD boots
9. Once boot has completed, the app should be running!

Server
1. Find the provided directory 'COMP2601A2Server' 
2. In the toolbar at the top of the window, find and click 'File'
3. In the dropdown menu, click 'Open Projects from File System…'
4. Navigate to the root directory of the folder in step 1
5. If you have not previously used the org.json.jar library, you'll have to import it
6. Provided in the zip file, there is a file called org.json.jar that you will need to copy
7. Open Eclipse and find the project directory
8. Paste the jar file. It should show up at the bottom of the list of files in the Package Explorer
9. Right click the jar file in Eclipse. Select 'Build Path' then select 'Add to build path'.
10. The server is ready to be run by clicking the green play button. Throughout the lifecycle of the app. You can see the server activity being printed into the Console in Eclipse.


PLAYING THE GAME:


This tic-tac-toe game is designed to allow users to play against another user using their own respective devices. In normal tic-tac-toe fashion, one player taps on a tile and waits for the other person to make their move.

1. Select a player from the list of connected users to ask them if they would like to play a game
2. Wait for the other user's response. If it is yes, both users will be taken to the tic-tac-toe game. If it is no, you will be notified.
3. When taken to the game screen, either player must hit the 'Start' button to start the game. The player who made the request is 'X' while the other is '0'. 'X' always makes the first move.
4. Click on an empty cell to place your shape there. Once clicked, you must wait for the other user to make their move.
5. Continue until an end game state is reached. Which can be one of the following:
	- "Game is over. X won!"
	- "Game is over. No one won!"
6. If a player exits the game, the other player will be notified.

WINNING THE GAME:

In order to win the game, the user needs to be able to place
three 'X's either horizontally in the same row, vertically
in the same column, OR diagonally from top left to bottom right, OR
diagonally from top right to bottom left. 

ERROR CHECKING:

1. Before pressing 'Start', all buttons are disabled
2. Once game is running, already played cells cannot be clicked on
3. Player cannot play opponent's turn
4. If 'Start' button is pressed while game is running, game is ended and
   status bar displays 'Game Ended by X'

UNIT TESTING:

These can be found within the 'GameUnitTest.java' class. This class thoroughly
tests the Game class. It can be ran by right clicking on the class file
within Android Studio, and clicking run.


WE HOPE YOU ENJOYED PLAYING TIC-TAC-TOE!


REFERENCES:

Image X: <div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>

Image O: <div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
