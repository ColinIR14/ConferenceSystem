import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
/**
 * A Message Menu and its sub-menus, part of the controller.
 */
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


    /**
     * messageMenu allows logged in user to view messages sent to them, send message to other user in contact, add user to
     * contact, remove user from contact. Only Organizer type user are allowed to send event message. And user can choose
     * to return to the main menu at the beginning of message menu or return to beginning of message menu.
     * Notes, unless organizer type, user can only send message to users that are in the user's contact.
     * Functions added: Preview messages, Archived messages.
     * @throws IOException if there's no serialized file for MessageManager.
     */
    public void messageMenu() throws IOException {
        tp.messageMenuPrompt();
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
                tp.printMessageMenu3("username");
                String usern = in.nextLine();
                if (usern.equals("back")) {
                    messageMenu();
                }
                else if (am.checkUser(usern)) {
                    mm.addMessageable(am.getUser(currentUser), am.getUser(usern));
                }
                else {
                    tp.printMessageMenu3("no user");
                }
                saveAll();
                messageMenu();
                break;
            case "4":
                System.out.println(mm.getMessageable((am.getUser(currentUser)).getMessageable()));
                tp.printMessageMenu4();
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
                        tp.printMessageMenu5("not organizer");
                        messageMenu();
                        break;
                    }
                    tp.printMessageMenu5("event list");
                    System.out.println(em.eventdetails());
                    tp.printMessageMenu5("number of events");
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
                    tp.printMessageMenu5("non-number");
                    messageMenu();
                }
                catch(IndexOutOfBoundsException e){
                    tp.printMessageMenu5("invalid index");
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
                tp.printMessageMenuDefault();
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
            tp.printSeeArchive("no messages");
            messageMenu();
        }
        tp.printSeeArchive("archive");
        int i = 1;
        for(Message m : mm.getArchivedMessage(us)){
            System.out.println(i + ". "+ m.getContentToString());
            i += 1;
        }
        tp.printSeeArchive("message selection");
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
                    tp.printSeeArchive("invalid");
                    seeArchive(us);
                }
            }catch (NumberFormatException e){
                tp.printSeeArchive("invalid");
                seeArchive(us);
            }
        }
    }

    /*
    * A menu for previewing all messages of a user.
    */
    private void viewMessageMenu(User us) throws IOException {
        tp.printViewMessageMenu("title");
        System.out.println(seeMessages(us));
        tp.printViewMessageMenu("message selection");
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
                    tp.printViewMessageMenu("invalid");
                    viewMessageMenu(us);
                }
            }catch (NumberFormatException e){
                tp.printViewMessageMenu("invalid");
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
        tp.specificMessageMenuPrompt("options");
        String at = in.nextLine();
        switch(at){
            case "1":
                currentMessage.setViewed(false);
                viewMessageMenu(us);
                saveAll();
                break;
            case "2":
                if (currentMessage.getArchived()) {
                    tp.specificMessageMenuPrompt("case 2");
                }
                else {
                    currentMessage.setArchived(true);
                    tp.specificMessageMenuPrompt("case 3 in archive");
                }
                saveAll();
                break;
            case "3":
                if (!currentMessage.getArchived()) {
                    tp.specificMessageMenuPrompt("case 3 not in archive");
                }
                else {
                    currentMessage.setArchived(false);
                    tp.specificMessageMenuPrompt("case 3 message removed");
                }
                saveAll();
                break;
            case "4":
                try {
                    if (!currentMessage.getSender().isContainedIn(us.getMessageable())) {
                        mm.addMessageable(us, am.getUser(currentMessage.getSender().getUsername()));
                    }
                    tp.specificMessageMenuPrompt("case 4 instruction");
                    String content = in.nextLine();
                    if (mm.sendMessage(us, currentMessage.getSender(), content)) {
                        if (am.checkAccountType(am.getName(currentMessage.getSender())).equals("organizer")) {
                            tp.specificMessageMenuPrompt("case 4 message organizer");
                        } else if (am.checkAccountType(am.getName(currentMessage.getSender())).equals("speaker")) {
                            tp.specificMessageMenuPrompt("case 4 message speaker");
                        }
                        tp.specificMessageMenuPrompt("message sent");
                        saveAll();
                    }
                } catch (NullPointerException e){
                    System.out.println("User does not exist or deleted.");
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
                tp.specificMessageMenuPrompt("default invalid");
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
        tp.printSendMessageSpeaker("options");
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
                    tp.printSendMessageSpeaker("case 1 instruction");
                    String content = in.nextLine();
                    for (Event event : em.getEventsOfSpeaker(sender))
                        mm.sendEventMessage(sender, event, content);
                    tp.printSendMessageSpeaker("case 1 success");
                    saveAll();
                    break;
                case "2":
                    if (em.getEventsOfSpeaker(sender).size() == 0){
                        tp.printSendMessageSpeaker("case 2 not speaking");
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
                    tp.printSendMessageSpeaker("case 2 instruction");
                    String content2 = in.nextLine();
                    mm.sendMessage1(sender, am.getUser(receiver), content2);
                    tp.printSendMessageSpeaker("case 2 success");
                    saveAll();
                    break;
                default:
                    tp.printSendMessageSpeaker("default invalid");
                    messageMenu();
            }

        }
    }

    /*
    Send message menu for organizer.
    */
    private void sendMessageOrganizer(User sender) throws IOException{
        tp.printSendMessageOrganizer("options");
        String input = in.nextLine();
        if (input.equals("back")){
            messageMenu();
        }
        if (!(input.equals("1")||input.equals("2")||input.equals("3")||input.equals("back"))){
            tp.printSendMessageOrganizer("invalid");
            sendMessageOrganizer(am.getUser(currentUser));
        }
        tp.printSendMessageOrganizer("message instruction");
        String content = in.nextLine();
        switch (input) {
            case "1":
                for(User user : am.getUserList()){
                    if (am.checkAccountType(am.getName(user)).equals("speaker"))
                        mm.sendMessage1(sender, user, content);
                }
                tp.printSendMessageOrganizer("case 1 sent");
                saveAll();
                break;
            case "2":
                for (User user : am.getUserList()){
                    if (am.checkAccountType(am.getName(user)).equals("attendee"))
                        mm.sendMessage1(sender, user, content);
                }
                tp.printSendMessageOrganizer("case 2 sent");
                saveAll();
                break;
            case "3":
                tp.printSendMessageOrganizer("case 3 username");
                String receiver = in.nextLine();
                if (am.checkUser(receiver))
                    mm.sendMessage1(sender, am.getUser(receiver), content);
                tp.printSendMessageOrganizer("case 3 sent");
        }
    }

    /*
    * Take sender user and ask for content of message and receiver and will send to message.
    */
    private void sendMessage(User us) throws IOException {
        tp.printSendMessage("instruction");
        String content = in.nextLine();
        if (content.equals("back"))
            messageMenu();
        else {
            StringBuilder str = mm.getMessageable(am.getContactList(us));
            String receiver = "";
            if (str.length() != 0) {
                System.out.println(str);
                tp.printSendMessage("recipient input");
                receiver = in.nextLine();
            }
            else{
                tp.printSendMessage("no contacts");
                messageMenu();
            }
            if (receiver.equals("back"))
                messageMenu();
            else {
                if(mm.sendMessage(us, am.getUser(receiver), content)) {
                    if (am.checkAccountType(am.getName(am.getUser(receiver))).equals("organizer")){
                        tp.printSendMessage("warning message organizer");
                    }
                    else if(am.checkAccountType(am.getName(am.getUser(receiver))).equals("speaker")){
                        tp.printSendMessage("warning message speaker");
                    }
                    tp.printSendMessage("message sent");
                } else {
                    tp.printSendMessage("message not sent");
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
