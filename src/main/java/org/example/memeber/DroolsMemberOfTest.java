package org.example.memeber;

import org.example.module.Person;
import org.example.module.School;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.Arrays;
import java.util.List;

public class DroolsMemberOfTest {


    @Test
    public void memberOfTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("memberOf-test-session");

        Person person = new Person(19,"jerry");
        person.setClassName("四班");
        School school = new School("六班");
        List<String> list = Arrays.asList("四班", "三班");

        kieSession.insert(person);
        kieSession.insert(school);
        kieSession.insert(list);

        kieSession.fireAllRules();
        kieSession.dispose();
    }
}
