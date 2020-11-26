import java.util.Scanner;

public class TextPresenter {

    /**
     * Show the welcome screen to user when the program is started
     */
    public void welcome() {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to the Conference Manager program! Type 'L' to log in or 'N' to create a new attendee account.");
        String next = in.nextLine();

        if (next.equals("L")) {
            //login
        } else if (next.equals("N")) {
            //new account
        }
    }

    /*
    Prompts user for username and password, and returns them in an array to help other functions.
    */
    public void userInfoPrompt(String input) {
        if (input.equals("username")) {
            System.out.println("Input username:");
        }
        else if(input.equals("password")) {
            System.out.println("Input password:");


        }
    }

}
