package org.example.example;

import org.example.module.Cat;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsApiCatTest {

    @Test
    public void droolsApiCatTest(){
        KieServices kieServices = KieServices.Factory.get();
        KieContainer container = kieServices.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("all-rules");
        kieSession.getAgenda().getAgendaGroup("catSetGet").setFocus();

        kieSession.insert(new Cat("tomcat"));

        int i = kieSession.fireAllRules();
        System.out.println(i);
        kieSession.dispose();


    }

}
