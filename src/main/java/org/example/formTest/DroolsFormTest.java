package org.example.formTest;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.List;

public class DroolsFormTest {


    public static List<Person> createPerson(String name,Integer num){
        List<Person> list = new ArrayList<>();
        for (int i = 0; i < num ; i++) {
            list.add(new Person(12 + i ,name + i));
        }

        return list;
    }


    @Test
    public void fromTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        Results verify = container.verify();
//        List<Message> messages = verify.getMessages(Message.Level.WARNING);
//        System.out.println(messages);
        KieSession kieSession = container.newKieSession("formTest2-test-session");

        kieSession.fireAllRules();
        kieSession.dispose();
    }


}
