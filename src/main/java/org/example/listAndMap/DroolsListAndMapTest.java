package org.example.listAndMap;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DroolsListAndMapTest {

    @Test
    public void listAndMap(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession session = container.newKieSession("listAndMap-test-session");

        Map<String,Object> map = new HashMap<>();
        map.put("name","tom");
        map.put("age",19);

        List<Person> list = new ArrayList<>();
        list.add(new Person(18,"jerry"));
        list.add(new Person(19,"tom"));

        session.insert(map);
        session.insert(list);

        session.fireAllRules();
        session.dispose();
    }
}
