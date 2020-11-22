package org.example.example;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class LhsSessionTest {


    @Test
    public void lhsSessionTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("lhs-test-session");

        Person p1 = new Person(11,"tomcat");
        Person p2 = new Person(20,"tomcat2");

        kieSession.insert(p1);
        kieSession.insert(p2);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

}
