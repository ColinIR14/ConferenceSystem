package ControllersAndOuterLayers;

import Entities.User;
import UseCase.AccountManager;
import UseCase.EventManager;
import UseCase.MessageManager;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Scanner;

/**
 * An Account Menu and its sub-menus, part of the controller.
 */
public class AccountMenu {

    private final AccountManager am;
    private final EventManager em;
    private final MessageManager mm;
    private final String currentUser;
    private final EventSystem es;
    private final Scanner in = new Scanner(System.in);
    private final Gateway g = new Gateway();
    private final TextPresenter tp = new TextPresenter();
    private final PropertyChangeSupport support;

    public AccountMenu(AccountManager am, EventManager em, MessageManager mm, String currentUser) {
        this.am = am;
        this.em = em;
        this.mm = mm;
        this.currentUser = currentUser;
        this.es = new EventSystem(am, em, mm, currentUser);
        support = new PropertyChangeSupport(this);
        support.addPropertyChangeListener(em);
        support.addPropertyChangeListener(am);
    }

    /**
     * If the logged in user is an organizer, allows the user to enter a single-digit input between 1-6 to select the
     * operation they want to take.
     * (1.Add Organizer account  2.delete account  3.Reset account  4.Reset password  5.List users  6.Main menu)
     * If the logged in user is an attendee or a speaker, allows the user to enter a single-digit input between 1-3
     * to select the operation they want to take.
     * (1.delete account  2.Reset account 3.Main menu)
     * Each option selected redirects to a corresponding private method previously defined.
     *
     * @throws IOException if there's no serialized file for AccountManager.
     */
    public void accountMenu() throws IOException {
        tp.printAccountMenu("title");
        if (am.checkAccountType(currentUser).equals("organizer")) {
            tp.printAccountMenu("organizer");
            String next = in.nextLine();
            switch (next) {
                case "1":
                    addAccount("organizer");
                    accountMenu();
                    break;
                case "2":
                    addAccount("speaker");
                    accountMenu();
                    break;
                case "3":
                    addAccount("attendee");
                    accountMenu();
                    break;
                case "4":
                    addAccount("vip");
                    accountMenu();
                    break;
                case "5":
                    removeAccount();
                    accountMenu();
                    break;
                case "6":
                    changePassword();
                    accountMenu();
                    break;
                case "7":
                    tp.printAccountMenu(am.toString());
                    accountMenu();
                    break;
                case "8":
                    es.mainMenu();
                    break;
                default:
                    tp.printInvalidInput();
                    accountMenu();
                    break;
            }
        } else {
            tp.printAccountMenu("non organizer");
            String next2 = in.nextLine();
            switch (next2) {
                case "1":
                    removeAccount();
                    es.welcome();
                    break;
                case "2":
                    changePassword();
                    saveAll();
                    accountMenu();
                    break;
                case "3":
                    es.mainMenu();
                    break;
                default:
                    tp.printInvalidInput();
                    accountMenu();
                    break;
            }
        }
    }

    /*
    Add a certain type account by creating the user name and password if the user logged in is an organizer,
    otherwise the request is refused.
     */
    private void addAccount(String accountType) throws IOException {
        tp.printAddAccount("username");
        String username = in.nextLine();
        if (username.equals("back")) {
            accountMenu();
        }
        tp.printAddAccount("password");
        String password = in.nextLine();
        if (password.equals("back")) {
            accountMenu();
        }
        if (am.addNewUser(username, password, accountType)) {
            tp.printSuccess();
        } else {
            tp.printAddAccount("add error");
        }
        saveAll();
    }

    /*
    If the user logged in is an organizer, remove a certain account by entering the user name when the user name
    entered exists, otherwise the request is refused.
    If the user logged in is a speaker or attendee, remove the logged in account by entering user name and password,
    otherwise the request is refused.
     */
    private void removeAccount() throws IOException {
        if (am.checkAccountType(currentUser).equals("organizer")) {
            tp.printRemoveAccount("username");
            String username = in.nextLine();
            if (username.equals("back")) {
                accountMenu();
            }
            if (am.getUser(username) == null) {
                tp.printRemoveAccount("user error");
            } else {
                support.firePropertyChange("currentUser", am.getUser(username), null);
                am.deleteUser(username, am.getUser(username).getPassword());
                tp.printSuccess();
            }
            accountMenu();
        } else {
            tp.printRemoveAccount("password");
            String password = in.nextLine();
            if (password.equals("back")) {
                accountMenu();
            }
            User u = am.getUser(currentUser);
            if (am.deleteUser(currentUser, password)) {
                tp.printSuccess();
                support.firePropertyChange("currentUser", u, null);
                es.welcome();
            } else {
                tp.printRemoveAccount("self error");
                accountMenu();
            }
        }
        saveAll();
    }

    /*
    Change the current user's password to the newly entered one.
     */
    private void changePassword() throws IOException {
        tp.printChangePassword();
        String password = in.nextLine();
        if (password.equals("back")) {
            accountMenu();
        }
        am.resetPassword(am.getUser(currentUser), password);
        tp.printSuccess();
        saveAll();
    }

    /*
    Saves all the managers.
     */
    private void saveAll() throws IOException {
        g.saveAccountManagerToFile(am, "AccountManagerSave.ser");
        g.saveEventManagerToFile(em, "EventManagerSave.ser");
        g.saveMessageManagerToFile(mm, "MessageManagerSave.ser");
    }
}
