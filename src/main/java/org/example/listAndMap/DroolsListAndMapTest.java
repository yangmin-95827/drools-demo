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

        list.get(0).getChildList().add(new Person(20,"吴建超"));
        list.get(0).getChildList().add(new Person(21,"老吴"));
        list.get(0).getCredentialMap().put("mouse","雷蛇");
        list.get(0).getCredentialMap().put("house","70年");
        list.get(0).getCredentialMap().put("valid",true);

        session.insert(map);
        session.insert(list);
        session.insert(list.get(0));

        session.fireAllRules();
        session.dispose();
    }

    public static class Valid{
        private boolean valid;

        public Valid(boolean valid) {
            this.valid = valid;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }
    }

}
