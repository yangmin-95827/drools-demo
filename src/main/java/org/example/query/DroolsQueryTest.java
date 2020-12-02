package org.example.query;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import java.util.Arrays;
import java.util.List;

public class DroolsQueryTest {


    @Test
    public void queryTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("query-test-session");

        kieSession.insert(new Person(20, "tom"));
        kieSession.insert(new Person(20, "jerry"));
        kieSession.insert(new Person(21, "jerry1"));
        kieSession.insert(new Person(22, "jerry2"));
        kieSession.insert( new Person(23, "jerry3"));

        int i = kieSession.fireAllRules();
        System.out.println(i + "规则已匹配");

        System.out.println("查询query-person-age-equal-20");
        QueryResults queryResults = kieSession.getQueryResults("query-person-age-equal-20");
        for (QueryResultsRow row: queryResults) {
            Person person = (Person) row.get("preson");
            System.out.println("person " + person.getAge());
        }

        System.out.println("查询query-person-age-greater-param");
        QueryResults queryResults1 = kieSession.getQueryResults("query-person-age-greater-param",20);
        for (QueryResultsRow row: queryResults1) {
            Person person = (Person) row.get("preson");
            System.out.println("person " + person.getAge());
        }

        kieSession.dispose();

    }

}
