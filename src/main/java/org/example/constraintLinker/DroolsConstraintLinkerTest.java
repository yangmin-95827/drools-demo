package org.example.constraintLinker;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsConstraintLinkerTest {

    @Test
    public void constraintLinker(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession session = container.newKieSession("constraintLinker-test-session");

        Person person = new Person(30,"jerry");

        session.insert(person);
        session.fireAllRules();
        session.dispose();

    }
}
