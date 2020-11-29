package org.example.strtest;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsStrTestDemo {

    @Test
    public void droolsStrTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession session = container.newKieSession("strTest-test-session");

        Person person = new Person(20 , "张三丰");
        session.insert(person);
        session.fireAllRules();
        session.dispose();
    }

}
