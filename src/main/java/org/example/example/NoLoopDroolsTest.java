package org.example.example;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class NoLoopDroolsTest {


    @Test
    public void noLoopDroolsTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("all-rules-no-loop");

        Person p = new Person(13);
        kieSession.insert(p);
        kieSession.fireAllRules();

        kieSession.dispose();
    }

}
