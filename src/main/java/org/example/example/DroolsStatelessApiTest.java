package org.example.example;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

public class DroolsStatelessApiTest {


    @Test
    public void statelessApiTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
//        KieBase kieBase = container.getKieBase("");
//        kieBase.newStatelessKieSession()
        StatelessKieSession session = container.newStatelessKieSession("stateless-session");

        session.execute(new Person(34));
    }

}
