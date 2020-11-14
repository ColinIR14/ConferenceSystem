import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class UnitTests {

    //@Test
    //public void AccountManagerTest() {
    //    AccountManager tester = new AccountManager();
    //    assertEquals();
    //}

    @Test
    public void EventManagerTest(){
        EventManager tester = new EventManager();
        Room testroom = new Room();
        User testuser = new User("username1", "pass1", "presenter");
        Date testdate = new Date(2020,12,12);
        Room testroom2 = new Room();
        User testuser2 = new User("username2", "pass2", "presenter");
        Date testdate2 = new Date(2020,11,11);
        tester.addNewEvent("Howto101", testdate, testroom, testuser);
        tester.addNewEvent("Howto102", testdate2, testroom2, testuser2);
        assertEquals("Howto101", tester.indexEvent(0).getEventName(), "Wrong Event Name");
        assertEquals("Howto102", tester.indexEvent(1).getEventName(), "Wrong Event Name");
        assertEquals(0, tester.indexEvent(0).getId(), "Wrong ID Number");
        assertEquals(1, tester.indexEvent(1).getId(), "Wrong ID Number");
        assertEquals(testdate, tester.indexEvent(0).getEventTime(), "Wrong Date");
        assertEquals(testdate2, tester.indexEvent(1).getEventTime(), "Wrong Date");
        assertEquals(testuser, tester.indexEvent(0).getSpeaker(), "Wrong Speaker");
        assertEquals(testuser2, tester.indexEvent(1).getSpeaker(), "Wrong Speaker");

        User testattendee = new User("username1", "pass1", "attendee");
        assertEquals(true, tester.signUpUsertoEvent(tester.indexEvent(0), testattendee), "User not able to be added");
        tester.signUpUsertoEvent(tester.indexEvent(0), testattendee);
        assertEquals(false, tester.signUpUsertoEvent(tester.indexEvent(0), testattendee), "User not able to be added, already in the list");
        assertEquals(true, tester.cancelUseratEvent(tester.indexEvent(0), testattendee), "User not able to be removed");
        tester.cancelUseratEvent(tester.indexEvent(0), testattendee);
        assertEquals(false, tester.cancelUseratEvent(tester.indexEvent(0), testattendee), "User not able to be removed, user not present");
    }

}