package org.example.accumulate;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsAccumulateTest {


    @Test
    public void accumulateTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("accoumulateTest-test-session");

        kieSession.insert(new Person(11,"tom11"));
        kieSession.insert(new Person(20,"tom12"));
        kieSession.insert(new Person(21,"tom13"));
        kieSession.insert(new Person(22,"tom14"));
        kieSession.insert(new Person(23,"tom15"));
        kieSession.insert(new Person(24,"tom16"));
        kieSession.insert(new Person(25,"tom17"));
        kieSession.insert(new Person(26,"tom18"));
        kieSession.insert(new Person(27,"tom19"));

        kieSession.fireAllRules();
        kieSession.dispose();
    }

}
