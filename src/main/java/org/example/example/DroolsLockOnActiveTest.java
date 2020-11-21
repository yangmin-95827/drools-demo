package org.example.example;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsLockOnActiveTest {

    @Test
    public void lockOnActiveTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("lockOnActive");


        Person p = new Person(10);

        kieSession.insert(p);
        kieSession.fireAllRules();
        kieSession.dispose();


    }
}
