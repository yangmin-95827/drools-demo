package org.example.global;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DroolsGlobalTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DroolsGlobalTest.class);

    @Test
    public void globalTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("global-test-session");
        List<Person> lst = new ArrayList<>();
        // 设置全局变量 LOGGER
        kieSession.setGlobal("LOGGER",LOGGER);
        // 设置全局变量 list
        kieSession.setGlobal("list",lst);

        kieSession.insert(new Person(20,"tom1"));
        kieSession.insert(new Person(19,"tom2"));
        kieSession.insert(new Person(21,"tom3"));
        kieSession.insert(new Person(22,"tom4"));
        kieSession.insert(new Person(23,"tom5"));

        kieSession.fireAllRules();

        System.out.println(lst);

        kieSession.dispose();
    }

}
