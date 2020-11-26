package org.example.datefromat;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DroolsDateTest {

    @Test
    public void droolsDateTest() throws ParseException {
        System.setProperty("drools.dateformat","yyyy-MM-dd");
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession session = container.newKieSession("dateformat-test-session");

        Person person = new Person(19,"tom");
        person.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("1992-10-10"));
        session.insert(person);
        session.fireAllRules();
        session.dispose();
    }

}
