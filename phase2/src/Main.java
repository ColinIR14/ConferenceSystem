public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        Gateway g = new Gateway();
        AccountManager am = g.readAccountManagerFromFile("AccountManagerSave.ser");
        EventManager em = g.readEventManagerFromFile("EventManagerSave.ser");
        MessageManager mm = g.readMessageManagerFromFile("MessageManagerSave.ser");
        EventSystem es = new EventSystem(am, em, mm);
        es.run();
    }
}
