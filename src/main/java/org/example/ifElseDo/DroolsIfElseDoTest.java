package org.example.ifElseDo;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsIfElseDoTest {


    @Test
    public void ifElseDoTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("ifElseDo-test-session");

        kieSession.insert(new Person(19,"jerry"));
        kieSession.insert(new Person(21));

        kieSession.fireAllRules();
        kieSession.dispose();
    }

}
