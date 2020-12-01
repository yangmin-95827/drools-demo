package org.example.modify;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsModifyTest {

    @Test
    public void modifyTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("modifyTest-test-session");

        kieSession.insert(new Person(18,"tom"));

        kieSession.fireAllRules();
        kieSession.dispose();
    }

}
