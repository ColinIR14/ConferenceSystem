import java.util.Scanner;

public class TextPresenter {


    public void welcome() {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to the Conference Manager program! Type 'L' to log in or 'N' to create a new attendee account.");
        String next = in.nextLine();

        if (next.equals("L")) {
            //Run login method
        } else if (next.equals("N")) {
            //Run new account method
        }
    }
}