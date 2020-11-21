package org.example.example;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.AgendaGroup;

public class DroolsNonPersonTest {


    @Test
    public void nonInsertPersonTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("all-rules");

        kieSession.getAgenda().getAgendaGroup("person-all-group").setFocus();

        Person person = new Person(18);

        kieSession.fireAllRules();

        kieSession.dispose();

    }

    @Test
    public void insertPersonTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("all-rules");

        kieSession.getAgenda().getAgendaGroup("person-all-group").setFocus();

        Person person = new Person(18);
        kieSession.insert(person);
        kieSession.fireAllRules();

        kieSession.dispose();

    }


    @Test
    public void insertPersonDoubleTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession session = container.newKieSession("all-rules");
        session.getAgenda().getAgendaGroup("person-all-group").setFocus();

        Person person = new Person(31);
        person.setName("张三");
        person.setTime("31");
        session.insert(person);
        session.fireAllRules();

        session.dispose();
    }



}
