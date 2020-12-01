package org.example.update;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsUpdateTest {

    @Test
    public void updateTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("updateTest-test-session");

        kieSession.insert(new Person(18,"tom"));

        kieSession.fireAllRules();
        kieSession.dispose();
    }

}
