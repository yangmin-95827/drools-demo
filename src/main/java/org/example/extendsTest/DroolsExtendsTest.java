package org.example.extendsTest;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsExtendsTest {


    @Test
    public void extendsTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("extendsTest-test-session");
        kieSession.getAgenda().getAgendaGroup("extends-test-demo02").setFocus();

        kieSession.insert(new Person(21,"tom"));

        kieSession.fireAllRules();
        kieSession.dispose();
    }

}
