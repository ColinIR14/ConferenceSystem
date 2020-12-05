import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MessageMenu {

    private AccountManager am;
    private EventManager em;
    private MessageManager mm;
    private String currentUser;
    private EventSystem es;
    private Scanner in = new Scanner(System.in);
    private Gateway g = new Gateway();
    private TextPresenter tp = new TextPresenter();

    public MessageMenu(AccountManager am, EventManager em, MessageManager mm, String currentUser) {
        this.am = am;
        this.em = em;
        this.mm = mm;
        this.currentUser = currentUser;
        this.es = new EventSystem(am, em, mm, currentUser);
    }

    // TODO: Add Javadoc for messageMenu()
    /*
    * messageMenu allows logged in user to view messages sent to them, send message to other user in contact, add user to
    * contact, remove user from contact. Only Organizer type user are allowed to send event message. And user can choose
    * to return to the main menu at the beginning of message menu or return to beginning of message menu.
    * Notes, unless organizer type, user can only send message to users that are in the user's contact.
    * Functions added: Preview messages, Archived messages.
    */
    public void messageMenu() throws IOException {
        tp.messageMenuPrompt();
        System.out.println("Please enter an one-character input selection. (Enter 'back' at anypoint if you want to go " +
                "cancel action in further steps)");
        String input = in.nextLine();
        switch (input) {
            case "1":
                viewMessageMenu(am.getUser(currentUser));
                break;
            case "2":
                if (am.checkAccountType(currentUser).equals("speaker"))
                    sendMessageSpeaker(am.getUser(currentUser));
                else if(am.checkAccountType(currentUser).equals("organizer"))
                    sendMessageOrganizer(am.getUser(currentUser));
                else
                    sendMessage(am.getUser(currentUser));
                break;
            case "3":
                System.out.println(mm.getMessageable((am.getUser(currentUser)).getMessageable()));
                System.out.println("Please enter the user(username) you want to add to contact or enter \"back\" if you wish to go back:");

                String usern = in.nextLine();
                if (usern.equals("back")) {
                    messageMenu();
                }
                else if (am.checkUser(usern)) {
                    mm.addMessageable(am.getUser(currentUser), am.getUser(usern));
                }
                else {
                    System.out.println("User doesn't exist");
                }
                saveAll();
                messageMenu();
                break;
            case "4":
                System.out.println(mm.getMessageable((am.getUser(currentUser)).getMessageable()));
                System.out.println("Please enter the user(username) you want to remove from your contact, enter \"back\" to go back:");
                String username = in.nextLine();
                if (username.equals("back"))
                    messageMenu();
                else {
                    am.removeMessageable(am.getUser(currentUser), am.getUser(username));
                }
                saveAll();
                break;
            case "5":
                try {
                    if (!am.checkAccountType(currentUser).equals("organizer")) {
                        System.out.println("You're not an organizer");
                        messageMenu();
                        break;
                    }
                    System.out.println("Current event list:\n");
                    System.out.println(em.eventdetails());
                    System.out.println("Enter Number of Event you want to manipulate");
                    String in2 = in.nextLine();
                    if (in2.equals("back")) {
                        messageMenu();
                        break;
                    }
                    int i = Integer.parseInt(in2);
                    EventMenu eMenu = new EventMenu(am, em, mm, currentUser);
                    eMenu.sendMessageToEventMembers(em.indexEvent(i));
                    saveAll();
                    break;
                }
                catch(NumberFormatException e){
                    System.out.println("Enter a number please");
                    messageMenu();
                }
                catch(IndexOutOfBoundsException e){
                    System.out.println("Enter a valid index please");
                    messageMenu();
                }
                break;
            case "6":
                seeArchive(am.getUser(currentUser));

            case "7":
                saveAll();
                es.mainMenu();
                break;
            default:
                System.out.println("Invalid input, please retry");
                messageMenu();
        }
        saveAll();
        ArrayList<String> repeat = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5"));
        if (repeat.contains(input))
            messageMenu();
    }

    /*
    * See archived messages for user.
    */
    private void seeArchive(User us) throws IOException {
        if (mm.getArchivedMessage(us).size()==0) {
            System.out.println("\nYou have no archived messages!");
            messageMenu();
        }
        System.out.println("\nYour archived messages:");
        int i = 1;
        for(Message m : mm.getArchivedMessage(us)){
            System.out.println(i + ". "+ m.getContentToString());
            i += 1;
        }
        System.out.println("\n----------");
        System.out.println("Select the message by the number in the front for further actions or type \"back\" to go back");
        String ans = in.nextLine();
        if (ans.equals("back")) {
            messageMenu();
        }
        else{
            try {
                int a = Integer.parseInt(ans);
                if (1 <= a && a <= mm.getArchivedMessage(us).size()){
                    specificMessageMenu(us, a, true);
                }
                else{
                    System.out.println("Invalid Input");
                    seeArchive(us);
                }
            }catch (NumberFormatException e){
                System.out.println("Invalid Input.");
                seeArchive(us);
            }
        }
    }

    /*
    * A menu for previewing all messages of a user.
    */
    private void viewMessageMenu(User us) throws IOException {
        System.out.println("\nMessage Preview \n");
        System.out.println(seeMessages(us));
        System.out.println("\n" + "Select message by entering the number in the front for details and further actions or type \"back\" to go back");
        String ans = in.nextLine();
        if (ans.equals("back")) {
            messageMenu();
        }
        else{
            try {
                int a = Integer.parseInt(ans);
                if (1 <= a && a <= seeMessagesList(us).size()){
                    specificMessageMenu(us, a, false);
                }
                else{
                    System.out.println("Invalid Input");
                    viewMessageMenu(us);
                }
            }catch (NumberFormatException e){
                System.out.println("Invalid Input.");
                viewMessageMenu(us);
            }
        }
    }

    /*
    * A specific Menu when dealing with individual messages.
    */
    private void specificMessageMenu(User us, int a, boolean inarchive) throws IOException {
        Message currentMessage;
        if (inarchive){
            currentMessage = mm.getArchivedMessage(us).get(a-1);
        }
        else {
            currentMessage = seeMessagesList(us).get(a - 1);
        }
        currentMessage.setViewed(true);
        System.out.println(currentMessage.getContentToString());
        tp.specificMessageMenuPrompt();
        String at = in.nextLine();
        switch(at){
            case "1":
                currentMessage.setViewed(false);
                viewMessageMenu(us);
                saveAll();
                break;
            case "2":
                if (currentMessage.getArchived()) {
                    System.out.println("The message is already in archive.");
                }
                else {
                    currentMessage.setArchived(true);
                    System.out.println("Message archived!");
                }
                saveAll();
                break;
            case "3":
                if (!currentMessage.getArchived()) {
                    System.out.println("The message is not in archive.");
                }
                else {
                    currentMessage.setArchived(false);
                    System.out.println("Message removed from archive!");
                }
                saveAll();
                break;
            case "4":
                if (!currentMessage.getSender().isContainedIn(us.getMessageable())) {
                    mm.addMessageable(us, am.getUser(currentMessage.getSender().getUsername()));
                }
                System.out.println("Please enter your message:");
                String content = in.nextLine();
                if(mm.sendMessage(us, currentMessage.getSender(), content)) {
                    if (am.checkAccountType(am.getName(currentMessage.getSender())).equals("organizer")) {
                        System.out.println("Warning: You have sent a message to an Organizer. You may not get a reply.");
                    } else if (am.checkAccountType(am.getName(currentMessage.getSender())).equals("speaker")) {
                        System.out.println("Warning: You have sent a message to a Speaker. You may not get a reply if you are not attending his/her talk.");
                    }
                    System.out.println("Message sent successfully.");
                    saveAll();
                }
                break;
            case "5":
                mm.deleteMessage(currentMessage);
            case "6":
                if (inarchive){
                    seeArchive(us);
                }
                viewMessageMenu(us);
                break;
            default:
                System.out.println("Invalid input, try again.");
                specificMessageMenu(us, a, inarchive);
                break;
        }
        saveAll();
        if (inarchive){
            seeArchive(us);
        }
        viewMessageMenu(us);
    }

    /*
    send message menu for speaker.
    */
    private void sendMessageSpeaker(User sender) throws IOException {
        tp.sendMessageSpeakerPrompt();
        ArrayList<User> validUser = new ArrayList<>();
        for (Event event : em.getEventsOfSpeaker(sender)){
            validUser.addAll(event.getAttendees());
        }
        String input = in.nextLine();
        if (input.equals("back"))
            messageMenu();
        else {
            switch (input) {
                case "1":
                    System.out.println("Please enter your message:");
                    String content = in.nextLine();
                    for (Event event : em.getEventsOfSpeaker(sender))
                        mm.sendEventMessage(sender, event, content);
                    System.out.println("Success!");
                    saveAll();
                    break;
                case "2":
                    if (em.getEventsOfSpeaker(sender).size() == 0){
                        System.out.println("You are not speaking at any events.");
                        messageMenu();
                        break;
                    }
                    String str = mm.getMessageableOfEvents(em.getEventsOfSpeaker(sender));
                    System.out.println(str);
                    String receiver = in.nextLine();
                    if (!validUser.contains(am.getUser(receiver))) {
                        messageMenu();
                        break;
                    }
                    System.out.println("Please enter your message:");
                    String content2 = in.nextLine();
                    mm.sendMessage1(sender, am.getUser(receiver), content2);
                    System.out.println("Success!");
                    saveAll();
                    break;
                default:
                    System.out.println("Invalid input, try again");
                    messageMenu();
            }

        }
    }

    /*
    Send message menu for organizer.
    */
    private void sendMessageOrganizer(User sender) throws IOException{
        tp.sendMessageOrganizerPrompt();
        String input = in.nextLine();
        if (input.equals("back")){
            messageMenu();
        }
        if (!(input.equals("1")||input.equals("2")||input.equals("3")||input.equals("back"))){
            System.out.println("Invalid input, try again");
            sendMessageOrganizer(am.getUser(currentUser));
        }
        System.out.println("Please enter your message:");
        String content = in.nextLine();
        switch (input) {
            case "1":
                for(User user : am.getUserList()){
                    if (am.checkAccountType(am.getName(user)).equals("speaker"))
                        mm.sendMessage1(sender, user, content);
                }
                System.out.println("Message sent");
                saveAll();
                break;
            case "2":
                for (User user : am.getUserList()){
                    if (am.checkAccountType(am.getName(user)).equals("attendee"))
                        mm.sendMessage1(sender, user, content);
                }
                System.out.println("Message sent");
                saveAll();
                break;
            case "3":
                System.out.println("Enter the username of user you want to send");
                String receiver = in.nextLine();
                if (am.checkUser(receiver))
                    mm.sendMessage1(sender, am.getUser(receiver), content);
                System.out.println("Message sent");
        }
    }

    /*
    * Take sender user and ask for content of message and receiver and will send to message.
    */
    private void sendMessage(User us) throws IOException {
        System.out.println("Please enter your message:");
        String content = in.nextLine();
        if (content.equals("back"))
            messageMenu();
        else {
            StringBuilder str = mm.getMessageable(am.getContactList(us));
            String receiver = "";
            if (str.length() != 0) {
                System.out.println(str);
                System.out.println("Please enter the recipient of the message.");
                receiver = in.nextLine();
            }
            else{
                System.out.println("You have no contact please add contact first ");
                messageMenu();
            }
            if (receiver.equals("back"))
                messageMenu();
            else {
                if(mm.sendMessage(us, am.getUser(receiver), content)) {
                    if (am.checkAccountType(am.getName(am.getUser(receiver))).equals("organizer")){
                        System.out.println("Warning: You have sent a message to an Organizer. You may not get a reply.");
                    }
                    else if(am.checkAccountType(am.getName(am.getUser(receiver))).equals("speaker")){
                        System.out.println("Warning: You have sent a message to a Speaker. You may not get a reply if you are not attending his/her talk.");
                    }
                    System.out.println("Message sent successfully.");
                } else {
                    System.out.println("Message not sent.");
                }
            }
        }
        saveAll();
    }

    /*
    * Takes in user receiver and generates list of messages that are sent to the taken user and prints it.
    */
    private StringBuilder seeMessages(User us) {
        StringBuilder strbNew = new StringBuilder();
        ArrayList<Message> messages = mm.getUserMessages(us);
        if (messages.size() == 0){
            strbNew.append("You have no incoming messages");
            return strbNew;
        }
        int i = 1;
        for (Message m : messages) {
            if (!m.getViewed()) {
                strbNew.append(i).append(". ").append(mm.messageStrBuilder(m));
                strbNew.append("\n");
                i += 1;
            }
        }
        for (Message m : messages) {
            if (m.getViewed()) {
                strbNew.append(i).append(". ").append(mm.messageStrBuilder(m));
                strbNew.append("\n");
                i += 1;
            }
        }

        return strbNew;
    }

    /*
    * Takes in a user and returns the Arraylist of messages in the exact same order as displayed.
    */
    private ArrayList<Message> seeMessagesList(User us) {
        ArrayList<Message> ml = new ArrayList<>();
        ArrayList<Message> messages = mm.getUserMessages(us);
        if (messages.size() == 0){
            return ml;
        }
        for (Message m : messages) {
            if (!m.getViewed()) {
                ml.add(m);
            }
        }
        for (Message m : messages) {
            if (m.getViewed()) {
                ml.add(m);
            }
        }
        return ml;
    }

    /*
    * Saves all the managers.
    */
    private void saveAll() throws IOException {
        g.saveAccountManagerToFile(am, "AccountManagerSave.ser");
        g.saveEventManagerToFile(em, "EventManagerSave.ser");
        g.saveMessageManagerToFile(mm, "MessageManagerSave.ser");
    }
}
