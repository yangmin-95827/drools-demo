package org.example.forall;

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
}
