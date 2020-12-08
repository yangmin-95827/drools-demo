package org.example.notAndExists;

import org.example.module.Car;
import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.CommandExecutor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.command.CommandFactory;

public class DroolsNotAndExistsTest {

    @Test
    public void notAndExistsTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("notAndExists-test-session");

        kieSession.insert(new Person(19,"李四"));
        kieSession.insert(new Car(100,new Person(19,"李四")));

        kieSession.fireAllRules();
        kieSession.dispose();
    }
}
