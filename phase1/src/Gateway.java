import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * A Gateway class. Used for communicating to the database. (Under construction)
 */

public class Gateway {
    public Gateway(){}


    public AccountManager readAccountManagerFromFile(String path) throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            // deserialize the StudentManager
            AccountManager am = (AccountManager) input.readObject();
            input.close();
            return am;
        } catch (IOException ex) {
            //logger.log(Level.SEVERE, "Cannot read from input file, returning" + "a new StudentManager.", ex);
            System.out.println("a new Account Manager");
            return new AccountManager();
        }
    }

    public void saveAccountManagerToFile(AccountManager am, String filePath) throws IOException {
        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);
        output.writeObject(am);
        output.close();
    }

}
