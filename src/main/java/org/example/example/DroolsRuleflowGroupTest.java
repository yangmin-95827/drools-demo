package org.example.example;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsRuleflowGroupTest {

    @Test
    public void DroolsRuleFlowGroupTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("ruleflowgroup");
        kieSession.getAgenda().getAgendaGroup("ruleflow-group1").setFocus();

        Person p = new Person(30);
        kieSession.insert(p);
        kieSession.fireAllRules();
        kieSession.dispose();

    }

}
