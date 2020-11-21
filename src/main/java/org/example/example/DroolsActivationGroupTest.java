package org.example.example;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsActivationGroupTest {


    @Test
    public void activationGroupTes(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession session = container.newKieSession("activationgroup-rule");

        Person person = new Person(40);

        session.insert(person);
        session.fireAllRules();
        session.dispose();
    }

}
