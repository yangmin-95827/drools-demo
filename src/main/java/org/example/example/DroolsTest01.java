package org.example.example;

import org.example.module.Car;
import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.io.KieResources;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
//import org.kie.internal.KnowledgeBase;
//import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.net.URL;
import java.util.Collection;

public class DroolsTest01 {
//
//    @Test
//    public void test50(){
//        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
//        builder.add(ResourceFactory.newClassPathResource("org/example/discount.drl"), ResourceType.DRL);
//        if(builder.hasErrors()){
//            System.out.println("builder Errors:" + builder.getErrors().toString());
//        }
//        KnowledgeBase base = KnowledgeBaseFactory.newKnowledgeBase();
//        base.addKnowledgePackages(builder.getKnowledgePackages());
//
//        KieSession kieSession = base.newKieSession();
//        kieSession.getAgenda().getAgendaGroup("drools-test-discount").setFocus();
//
//        Car car = new Car(100, new Person(61));
//        Car car1 = new Car(100, new Person(51));
//        kieSession.insert(car);
//        kieSession.insert(car1);
//        int i = kieSession.fireAllRules();
//        kieSession.dispose();
//        System.out.println(i + "个规则已执行");
//        System.out.println(car);
//        System.out.println(car1);
//    }


    @Test
    public void test70(){
        KieServices kieServices = KieServices.Factory.get();
        KieContainer container = kieServices.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("all-rules");


        Car car = new Car(100, new Person(61));
        Car car1 = new Car(100, new Person(51));
        kieSession.insert(car);
        kieSession.insert(car1);
        int i = kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println(i + "个规则已执行");
        System.out.println(car);
        System.out.println(car1);
    }


}
