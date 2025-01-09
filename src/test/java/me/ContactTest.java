package me;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
public class ContactTest {
    @Test
    void TestConstructor(){
        Contact test=new Contact("webemailt@gmail.com","AName","AFamilyname",
        "0771730549","a business description","a person description",0);
        assertEquals("webemailt@gmail.com",test.getEmail(),"bad email creation");
        assertEquals("AName",test.getName(),"bad name creation");
        assertEquals("AFamilyname", test.getFamilyName(),"bad family name creation");
        assertEquals("0771730549",test.getPhoneNumber(),"bad phone number creation");
        assertEquals("a business description", test.getBusiness(),"bad businesss desc creation");
        assertEquals("a person description", test.getDescription(),"bad person description");
        assertEquals(0,test.getPriority(),"bad priority");
    }
}
