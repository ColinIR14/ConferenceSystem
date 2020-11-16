Default organizer account
username: admin
Password: prime
You need to log in as organizer(admin initially) to create speaker and organizer accounts, or create attendee accounts at the welcome menu.


Controller
To run the program, please run the main method in Main class which will create a new eventSystem object and run the run() method that displays our welcome menu. Then you can iterate with the program. Our controller interacts with the user input and also prints menu options and messages. To close the program, you must find your way back to the welcome menu by signing out (if you have signed in) and Typing ‘C’ to close the program and save all the accounts, events and messages made.


Message
You need to add the user to your contacts before sending them a message
Contacts are one-sided, i.e. adding a user to your contacts doesn’t add you to theirs
Adding the user to contact doesn’t require a friend request. 


Events
To create an event, you must first add a speaker and a room to the system. Only organizers can create new events, add and remove attendees of the events as well as add and change the speaker of an event. Events are uniquely identified by their name.


Accounts
You can only create an account with a username five characters or more, and it will only succeed when this username is not taken. Only an organizer can create an organizer or speaker account. You can only remove other accounts as an organizer, else you will remove your own account, and return to the welcome menu.
Speakers need to create an attendee account if they want to attend events as attendees.