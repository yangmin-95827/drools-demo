package org.example.contains;

import org.example.module.Person;
import org.example.module.School;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.Arrays;

public class DroolsContainsTestDemo {

    @Test
    public void containsTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession session = container.newKieSession("contains-test-session");

        Person person= new Person(18,"tom");
        person.setClassName("一班");
        School school = new School("一班");

        session.insert(person);
        session.insert(school);
        session.insert(new School("二班"));
        session.insert(Arrays.asList("三班","四班","一班"));

        session.fireAllRules();
        session.dispose();
    }


}
