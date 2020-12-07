package ControllersAndOuterLayers;

import UseCase.AccountManager;
import UseCase.EventManager;
import UseCase.MessageManager;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A ControllersAndOuterLayers.Gateway class. Used for communicating to the database. (Under construction)
 */

public class Gateway {
    private static final Logger logger = Logger.getLogger("logs");

    public Gateway(){}


    /**
     * Reads AccountManger from file. If there is no existing file, it will create a new UseCase.AccountManager with the
     * default Organizer account in it.
     * @param path The path of the file.
     * @return An instance of the saved UseCase.AccountManager.
     * @throws ClassNotFoundException Throws the exception if no UseCase.AccountManager save is found. *Should be avoided*
     */
    public AccountManager readAccountManagerFromFile(String path) throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            AccountManager am = (AccountManager) input.readObject();
            input.close();
            return am;
        } catch (IOException ex) {
            logger.log(Level.INFO, "Cannot read from input file, returning" + " a new UseCase.AccountManager.", ex);
            AccountManager am = new AccountManager();
            am.addNewUser("admin", "prime", "organizer");
            return am;
        }
    }

    /**
     * Save the UseCase.AccountManager to file.
     * @param am the UseCase.AccountManager to be saved.
     * @param filePath the path of the save file. Creates a new .ser file there is no existing saves.
     * @throws IOException if problems encountered
     */
    public void saveAccountManagerToFile(AccountManager am, String filePath) throws IOException {
        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);
        output.writeObject(am);
        output.close();
    }

    /**
     * Reads UseCase.EventManager from file.
     * @param path The path of the file.
     * @return An instance of the saved UseCase.EventManager.
     * @throws ClassNotFoundException Throws the exception if no UseCase.EventManager save is found. *Should be avoided*
     */
    public EventManager readEventManagerFromFile(String path) throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            EventManager em = (EventManager) input.readObject();
            input.close();
            return em;
        } catch (IOException ex) {
            logger.log(Level.INFO, "Cannot read from input file, returning" + " a new UseCase.EventManager.", ex);
            return new EventManager();
        }
    }

    /**
     * Save the UseCase.EventManager to file.
     * @param em the UseCase.EventManager to be saved.
     * @param filePath the path of the save file. Creates a new .ser file there is no existing saves.
     * @throws IOException if problems encountered
     */
    public void saveEventManagerToFile(EventManager em, String filePath) throws IOException {
        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);
        output.writeObject(em);
        output.close();
    }

    /**
     * Reads UseCase.MessageManager from file.
     * @param path The path of the file.
     * @return An instance of the saved UseCase.MessageManager.
     * @throws ClassNotFoundException Throws the exception if no UseCase.MessageManager save is found. *Should be avoided*
     */
    public MessageManager readMessageManagerFromFile(String path) throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            MessageManager mm = (MessageManager) input.readObject();
            input.close();
            return mm;
        } catch (IOException ex) {
            logger.log(Level.INFO, "Cannot read from input file, returning" + " a new UseCase.MessageManager.", ex);
            return new MessageManager();
        }
    }

    /**
     * Save the UseCase.MessageManager to file.
     * @param mm the UseCase.MessageManager to be saved.
     * @param filePath the path of the save file. Creates a new .ser file there is no existing saves.
     * @throws IOException if problems encountered
     */
    public void saveMessageManagerToFile(MessageManager mm, String filePath) throws IOException {
        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);
        output.writeObject(mm);
        output.close();
    }

}
