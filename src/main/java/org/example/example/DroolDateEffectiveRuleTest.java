package org.example.example;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolDateEffectiveRuleTest {

    @Test
    public void droolDateEffectiveRuleTest(){

        System.setProperty("drools.dateformat","yyyy-MM-dd HH:mm");
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("dateeffective-rule");


        kieSession.fireAllRules();
        kieSession.dispose();
    }

}
