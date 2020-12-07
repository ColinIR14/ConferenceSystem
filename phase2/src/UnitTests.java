//import java.util.Date;
//
//import org.junit.*;
//
//import static org.junit.Assert.*;
//
//public class UnitTests {
//
//    /**
//     * Tests the UseCase.EventManager Useclass for errors in regards to its methods.
//     *
//     */
//    @Test
//    public void EventManagerTest(){
//        UseCase.EventManager tester = new UseCase.EventManager();
//        Entities.Room testroom = new Entities.Room();
//        Entities.User testuser = new Entities.User("username1", "pass1", "presenter");
//        Date testdate = new Date(2020,12,12);
//        Entities.Room testroom2 = new Entities.Room();
//        Entities.User testuser2 = new Entities.User("username2", "pass2", "presenter");
//        Date testdate2 = new Date(2020,11,11);
//        tester.addNewEvent("Howto101", testdate, testroom, testuser);
//        tester.addNewEvent("Howto102", testdate2, testroom2, testuser2);
//        assertEquals("Wrong Entities.Event Name", "Howto101", tester.indexEvent(0).getEventName());
//        assertEquals("Wrong Entities.Event Name", "Howto102", tester.indexEvent(1).getEventName());
//        assertEquals("Wrong ID Number", 0, tester.indexEvent(0).getId(), 0);
//        assertEquals("Wrong ID Number", 1, tester.indexEvent(0).getId(), 0);
//        assertEquals("Wrong Date", testdate, tester.indexEvent(0).getEventTime());
//        assertEquals("Wrong Date", testdate2, tester.indexEvent(1).getEventTime());
//        assertEquals("Wrong Speaker", testuser, tester.indexEvent(0).getSpeaker());
//        assertEquals("Wrong Speaker", testuser2, tester.indexEvent(1).getSpeaker());
//        Entities.User testattendee = new Entities.User("username1", "pass1", "attendee");
//        assertTrue("Entities.User not able to be added", tester.signUpUsertoEvent(tester.indexEvent(0), testattendee));
//        tester.signUpUsertoEvent(tester.indexEvent(0), testattendee);
//        assertFalse("Entities.User not able to be added, already in the list", tester.signUpUsertoEvent(tester.indexEvent(0), testattendee));
//        assertTrue("Entities.User not able to be removed", tester.cancelUseratEvent(tester.indexEvent(0), testattendee));
//        tester.cancelUseratEvent(tester.indexEvent(0), testattendee);
//        assertFalse("Entities.User not able to be removed, user not present", tester.cancelUseratEvent(tester.indexEvent(0), testattendee));
//    }
//
//}