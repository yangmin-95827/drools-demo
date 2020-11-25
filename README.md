# drools-demo

github地址:[https://github.com/yangmin-95827/drools-demo](https://github.com/yangmin-95827/drools-demo)

### 基于 7.0.0  

##### 1、在META-INF目录下创建kmodule.xml文件，drools在启动时会自动加载META-INF目录下的kmodule.xml文件  

    <?xml version="1.0" encoding="UTF-8"?>
    <kmodule xmlns="http://www.drools.org/xsd/kmodule">
         <kbase name="rules" packages="org.example">
             <ksession name="all-rules"/>
         </kbase>
    </kmodule>
##### 2、kmodule标签是根标签  

##### 3、一个kmodule中可以包含多个kbase标签，每个kbase可以包含多个规则文件。  
   * name属性必须为一个唯一的值，可以是任意字符串  
   * packages为drl文件所在resource目录下的路径。多个包或者drl文件使用逗号分隔，默认情况下drools会扫描resources目录下所有的规则文件
   * default 属性表示当前KieBase是不是默认的，如果是默认的则不用名称就可以查找到改KieBase，但每个kmodule下最多只能有一个默认的KieBase
   * kbase下面可以有一个或者多个ksession，ksession的name属性必须设置且必须唯一

##### 5、有状态session
通过KieContainer可以获取KieSession，在kmodule.xml配置文件中如果不指定ksession的type默认也是有状态的session。有状态session的特性是，我们可以通过建立一次session完成多次与规则引擎之间的交互，在没有调用dispose方法时，会维持会话状态。使用KieSession的一般步骤为，获取session，insert Fact对象，然后调用fireAllRules进行规则匹配，随后调用dispose方法关闭session。

kmodule.xml
    
    <kbase name="activationgroup-group-base" packages="org.activationgroup">
        <ksession name="activationgroup-rule"/>
    </kbase>
    
activationgroup.drl
    
    package org.activationgroup
    
    import org.example.module.Person
    
    rule "activationgroup-test"
        when
            p : Person(age > 20)
        then
            System.out.println("activationgroup-test-1 执行了");
     end

    
运行代码

    // 创建KieServices
    KieServices services = KieServices.Factory.get();
    // 获取KieContainer
    KieContainer container = services.getKieClasspathContainer();
    // 创建session
    KieSession session = container.newKieSession("activationgroup-rule");

    Person person = new Person(40);
    // 插入FACT 对象   
    session.insert(person);
    // 匹配规则
    session.fireAllRules();
    // 销毁session
    session.dispose();
##### 6、无状态session
StatelessKieSession提供了一个更加便利的API，是对KisSession的封装，不再调用dispose方法进行session的关闭。它隔离了每次与规则引擎的交互，不会再去维护会话的状态。同时也不再提供fireAllRules方法。
使用场景：  
 * （1）数据校验  
 * （2）运算  
 * （3）数据过滤  
 * （4）消息路由  
 * （5）任何能被描述成函数或公式的规则  

kmodule.xml

    <kbase name="rules" packages="org.example">
        <ksession name="all-rules"/>
        <ksession name="stateless-session" type="stateless"/>
    </kbase>
java

    KieServices services = KieServices.Factory.get();
    KieContainer container = services.getKieClasspathContainer();
    StatelessKieSession session = container.newStatelessKieSession("stateless-session");

    session.execute(new Person(34));    


##### 7、drools规则文件.drl

    package org.example
    import org.example.module.Car
    
    rule "drools-test-discount-age-60"
    agenda-group "drools-test-discount"
    when
        car:Car(person.age > 60)
    then
        car.setDiscount(80);
        System.out.println("drools-test-discount-age-60: " + car.getPerson().getAge() +  "执行！");
     end


## 关键字

| 关键字     |       描述                                           
| -----     | -------------                                       
| `package` |   定义规则所在逻辑包名称                                
| `import`  |   导入需要的类的类名                                   
| `rule`    |   规则开始，参数是规则的唯一属性               
| `when`    |   规则的条件部分，默认为true                                   |
| `then`    |   规则的结果部分
| `global`  |   全局变量
| `function`|   自定义函数
| `queries` |   查询
| `attributes`| 规则属性，是rule与when之间的参数，为可选项
| `end`     |   当前规则结束

## 规则属性
###### no-loop 
定义当前规则是否不允许循环执行，默认为false，可以设置为true避免update、insert、retract、modify方法引发的死循环
    
    package org.noLoop
    
    import org.example.module.Person
    
    rule "test-no-loop-01"
        no-loop true
        when
            $p : Person($pAge:age > 12);
        then
            System.out.println("执行规则test-no-loop-01" + $p +" " + $pAge );
            $p.setAge(15);
            update($p)
    end
    
###### ruleflow-group 
在使用规则流的时候要用到ruleflow-group 属性，改属性的值为一个字符串，
作用是将规则划分为一个个的组，然后在规则流中通过使用ruleflow-group属性的值从而使用对应的规则

    rule "ruleflow-group-test-01"
        ruleflow-group "ruleflow-group1"
        when
            p : Person(age > 20)
        then
            System.out.println("规则ruleflow-group-test-01已执行！");
    end
    
###### lock-on-active

避免因FACT对象被更新使得执行过的规则被再次执行，拥有no-loop的功能，同时
又能避免因其他规则改变FACT对象导致规则重新执行。触发此规则的操作有update、retract、update
、modify等。

    rule "lockOnActive-test-01"
        lock-on-active false
        when
            $p : Person(age < 20)
        then
            System.out.println("规则lockOnActive-test-01执行了");
    end

###### salience

用来设置规则执行的优先级，salience属性的值是一个数字，数字越大执行的优先级
越高，同时它的值可以是一个负数。默认情况下，规则的salience默认属性值为0。如果不设规则的salience属性，
那么执行的顺序是随机的。salience属性的值可以根据FACT对象属性的值动态的设置。

salience 属性执行顺序

    rule salience1
        salience -1
        when
        then
         System.out.println("test salience1");
    end
    
    rule salience2
        salience 2
        when
        then
         System.out.println("test salience2");
    end

动态绑定salience

根据变量sal，动态绑定salience

    
    rule salience3
        salience sal
        when
            Person(sal:age)
        then
         System.out.println("test salience3");
    end

###### agenda-group

agenda-group属性的基本作用是对规则进行分组，在使用是必须设置分组焦点

discount.drl

    rule "drools-test-discount-age-60"
    agenda-group "drools-test-discount"
    when
        car:Car(person.age > 60)
    then
        car.setDiscount(80);
        System.out.println("drools-test-discount-age-60: " + car.getPerson().getAge() +  "执行！");
     end

java 
    
    //获取默认session
    KieSession kieSession = base.newKieSession();
    // 设置分组焦点
    kieSession.getAgenda().getAgendaGroup("drools-test-discount").setFocus();   
    
###### auto-focus

对agenda-group和ruleflow-group的补充，属性值默认为false，设置为true时规则对应的分组自动
获得焦点，无需通过手动设置焦点

    rule autofocus1
        agenda-group "autofocus-test"
        auto-focus true
        when
        then
        System.out.println("autofocus1");
    end

###### activation-group 

该属性将多个规则划分为一个组，具有相同activation-group
属性值的规则只要有一个被执行，其他规则都不在执行

    rule "activationgroup-test"
        salience 1
        activation-group "activationgroup-test"
        when
            p : Person(age > 20)
        then
            System.out.println("activationgroup-test-1 执行了");
    end
    
    rule "activationgroup-test-2"
        salience 2
        activation-group "activationgroup-test"
        when
            p : Person(age > 30)
        then
            System.out.println("activationgroup-test-2 执行了");
    end

###### date-effective 和 date-expires

属性用来控制规则只有达到指定的时间之后才会触发。
与系统时间进行比对，指定时间大于等于系统时间时规则才会执行。
默认的日期格式为“dd-MMM-yyyy”，可以通过系统属性“drools.dateformat”
修改日期格式。与date-effective相反date-expires 在小于等于指定时间时才会执行。

    rule "date-effective-test1"
        date-effective "2020-11-19 07:28"
        date-expires "2020-11-19 07:38"
        when
        then
            System.out.println("date-effective-test1 执行了");
    end

###### dialect

指定要使用的语言类型,java或mvel


###### enabled

设置规则是否可用。true：表示该规则可用；false：表示该规则不可用


###### timer

基于interval（间隔）和cron表达式。间隔定时器使用int来定义，它遵循java.util.Timerd对象的使用方法。
cron定时器用cron来定义，使用Unix cron表达式

    rule "timer-test-demo01"
        // interval  延时3s执行
        timer(int:3s)
        //  延时3s执行，每次间隔5分钟
        //timer( int: 3s 5m )
    when
    
    then
        System.out.println("timer-test-demo01 已执行");
    end
    
    rule "timer-test-demo02"
        //timer ( cron: <cron expression> )
        timer(cron: 0/5 * * * * ? )
        when
        then
            System.out.println("timer-test-demo02 已执行");
    end

###### 日历

日历可以单独应用于规则中，也可以和timer结合在规则中使用，通过属性calendars来定义日历。

日历使用需要引入 quartz

    <!-- https://mvnrepository.com/artifact/org.opensymphony.quartz/quartz -->
    <dependency>
        <groupId>org.opensymphony.quartz</groupId>
        <artifactId>quartz</artifactId>
        <version>1.6.1</version>
    </dependency>


calendarsTest.drl

    rule  "calendars-test-demo01"
        // 日历规则名称
        calendars "WEEKDAY"
        when
    
        then
            System.out.println("calendars-test-demo01 已执行！");
    end
    
    rule  "calendars-test-demo02"
        // 日历规则名称
        calendars "WEEKDAY_EXCLUDE"
        when
    
        then
            System.out.println("calendars-test-demo02 已执行！");
    end

DroolsCalendarsTest.java

    KieServices services = KieServices.Factory.get();
    KieContainer container = services.getKieClasspathContainer();
    KieSession session = container.newKieSession("calendar-test-session");
    // 定义只在星期日生效的日历
    session.getCalendars().set("WEEKDAY",l -> {
        WeeklyCalendar calendar = new WeeklyCalendar();
        calendar.setDaysExcluded(new boolean[]{false,false,false,false,false,false,true});
        calendar.setDayExcluded(java.util.Calendar.SUNDAY,true);
        return calendar.isTimeIncluded(l);
    });
    // 定义排除周一到周日的日历
    session.getCalendars().set("WEEKDAY_EXCLUDE",l -> {
        WeeklyCalendar calendar = new WeeklyCalendar();
        calendar.setDaysExcluded(new boolean[]{false,false,false,false,false,false,false});
        return calendar.isTimeIncluded(l);
    });

    session.fireAllRules();

    session.dispose();


## LHS 和 RHS

LHS（left hand side）是指when和then之间的条件部分。RHS是then和end之间的结果部分，
可以写java代码，是规则条件满足时执行的操作，可以直接调用FACT对象方法操作应用。
如果LHS部分为空，自动添加一个eval(true)。LHS部分由一个或多个条件组成，条件又称为
pattern（匹配模式），多个pattern之间可以使用and或者or链接，同时还可以使用小括号来确定pattern的优先级

单个pattern

    rule "test0002"
        agenda-group "person-all-group"
        when
            // 绑定了两个变量$p和$time
            $p : Person(name == "张三", $time : time == 40)
        then
            // 可以在RHS 中使用绑定的变量
            System.out.println("传入了一个叫张三并且time === 40的人：" + $p);
    end



