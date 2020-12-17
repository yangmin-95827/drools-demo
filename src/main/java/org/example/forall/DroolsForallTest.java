package org.example.forall;

import org.example.module.Address;
import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsForallTest {


    @Test
    public void forallTest01(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("forallTest-test-session");

        kieSession.insert(new Person(61,"jerry"));
        kieSession.insert(new Person(40,"tom"));

        kieSession.fireAllRules();
        kieSession.dispose();
    }

    @Test
    public void forallTest02(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("forallTest-test-session");
        kieSession.getAgenda().getAgendaGroup("forall-test2").setFocus();

        kieSession.insert(new Person(40,"jerry"));
        kieSession.insert(new Person(40,"tom"));
        kieSession.insert(new Person(40,"jerry2"));
        kieSession.insert(new Person(40,"tom2"));
        kieSession.insert(new Person(40,"jerry3"));
        kieSession.insert(new Person(40,"tom3"));
        kieSession.insert(new Address("北京","0000010"));

        int i = kieSession.fireAllRules();
        kieSession.dispose();

        System.out.println( i + "个规则已执行");
    }
}
