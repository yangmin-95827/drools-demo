package org.example.functionTest;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsFunctionTest {


    public static boolean personNameEqual(String name,String targetName){

        return true;
    }

    @Test
    public void functionTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("function-test-session");

        kieSession.insert(new Person(21,"tom"));

        kieSession.fireAllRules();
        kieSession.dispose();

    }

}
