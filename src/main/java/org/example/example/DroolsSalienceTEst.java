package org.example.example;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsSalienceTEst {

    @Test
    public void DroolsSalienceTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("salience-rule");

        kieSession.fireAllRules();

        kieSession.dispose();

    }

    @Test
    public void DroolsSalienceTest2(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("salience-rule");

        Person person = new Person(6);
        kieSession.insert(person);
        kieSession.fireAllRules();

        kieSession.dispose();

    }

}
