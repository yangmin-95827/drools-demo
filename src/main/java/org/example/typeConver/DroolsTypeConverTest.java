package org.example.typeConver;

import org.example.module.Car;
import org.example.module.Man;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsTypeConverTest {

    @Test
    public void typeConverTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession session = container.newKieSession("typeConversion-test-session");

        Car car = new Car(100,new Man(12,"tomcat","男"));

        session.insert(car);
        session.fireAllRules();
        session.dispose();

    }
}
