package org.example.example;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsAutofocusTest {

    @Test
    public void autofocusTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("autofocus-rule");

        kieSession.fireAllRules();
        kieSession.dispose();


    }

}
