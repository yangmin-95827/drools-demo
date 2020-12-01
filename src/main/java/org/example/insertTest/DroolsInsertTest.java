package org.example.insertTest;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsInsertTest {

    @Test
    public void insertTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("insertTest-test-session");

        int i = kieSession.fireAllRules();
        System.out.println("总共执行了" + i + "规则");
        kieSession.dispose();
    }

}
