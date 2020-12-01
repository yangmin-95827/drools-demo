package org.example.delete;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsDeleteTest {

    @Test
    public void deleteTest(){

        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("deleteTest-test-session");

        kieSession.insert(new Person(21,"tom"));

        kieSession.fireAllRules();
        kieSession.dispose();

    }
}
