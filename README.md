# drools-demo

github地址:[https://github.com/yangmin-95827/drools-demo](https://github.com/yangmin-95827/drools-demo)

### 基于 7.46.0  

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
| `package` |   定义规则所在逻辑包名称，在最新的版本中使用`kmoudle.xml`需要和所在包名保持一致                                
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
pattern（匹配模式），多个pattern之间可以使用and或者or链接，同时还可以使用小括号来确定pattern的优先级。
pattern也可以使用java 表达式

单个pattern

    rule "test0002"
        agenda-group "person-all-group"
        when
            // 绑定了两个变量$p和$time 前缀$只是一个约定
            $p : Person(name == "张三", $time : time == 40)
        then
            // 可以在RHS 中使用绑定的变量
            System.out.println("传入了一个叫张三并且time === 40的人：" + $p);
    end

java表达式
    
    Person( age > 100 && ( age % 10 == 0 ) )
    
也可以使用java工具

    Person( Math.round( weight / ( height * height ) ) < 25.0 )

任何一个JavaBean中的属性都可以访问，不过对应的属性必须提供getter方法或isProperty方法，


###### 内部类分组访问

访问内部类多个属性时可以使用一下两种方式
    
    //1. Car(discount < 20,person.age > 60)
    
    //2. Car(discount > 20,person.(age > 60))

###### 强制类型转换

在使用内部类的时候，往往需要将其转换为父类，在规则中可以通过“#”来进行强制转换

    package org.typeConversion
    
    import org.example.module.Car
    import org.example.module.Man
    
    rule "type-conversion-test"
    
    when
        $c : Car(discount == 100,person#Man.sex == '男');
    then
        System.out.println("type-conversion-test 已执行：" + $c );
    end
    
###### 日期字符

规则语法中除了支持JAVA标准字符，同时也支持日期字符。
Drools默认支持的日期格式为“dd-mmm-yyyy”，
可以通过设置系统变量“drools.dateformat”的值来改变默认的日期格式

dateformat.drl

    package org.dateformat
    
    import org.example.module.Person
    
    rule "date-format-test-demo"
    
    when
        $p : Person(age > 18,birthday == "1992-10-10")
    then
        System.out.println("date-format-test-demo:" + $p );
    end

DroolsDateTest.java

    System.setProperty("drools.dateformat","yyyy-MM-dd");
    KieServices services = KieServices.Factory.get();
    KieContainer container = services.getKieClasspathContainer();
    KieSession session = container.newKieSession("dateformat-test-session");

    Person person = new Person(19,"tom");
    person.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("1992-10-10"));
    session.insert(person);
    session.fireAllRules();
    session.dispose();
    
###### List 和 Map 支持

listAndMap.drl

    package  org.listAndMap
    
    import org.example.module.Person
    import org.example.listAndMap.DroolsListAndMapTest.Valid
    
    import java.util.Map
    import java.util.List
    
    rule "list-map-test-demo"
    
    when
        $m1 : Map()
        $m2 : Map(this["name"] == "tom")
        $m3 : Map(this["age"] >= 18)
        $l1 : List();
        $l2 : List(size == 2);
        $p : Person(age == 19) from $l1;
        $p1 : Person(childList[0].age == 20);
        $p2  : Person(credentialMap["mouse"] == "雷蛇")
        $p3  : Person(credentialMap["valid"] == true)
    then
        System.out.println("list-map-test-demo->m1:" + $m1 );
        System.out.println("list-map-test-demo->m1:" + $m2 );
        System.out.println("list-map-test-demo->m1:" + $m3 );
        System.out.println("list-map-test-demo->$l1:" + $l1 );
        System.out.println("list-map-test-demo->$l2:" + $l2 );
        System.out.println("list-map-test-demo->$p:" + $p );
        System.out.println("list-map-test-demo->$p1:" + $p1 );
        System.out.println("list-map-test-demo->$p2:" + $p2 );
        System.out.println("list-map-test-demo->$p3:" + $p3 );
     end

DroolsListAndMapTest.java
    
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

###### not和exists

条件元素 not 是一阶逻辑的不存在（ non-existential）量词，用于检查在工作内存中不存在某东西。
把"not"看作“ 一定没有……” 的意思。关键字 not 紧跟着用扩号扩起它所应用的条件元素。
在单模式的最简情况下（如下所示），你可以选择忽略扩号`not Person()`，不可绑定变量。

条件元素 exists 是一阶逻辑的存在（ existential）量词，用于检查在工作内存中存在某东西。
把"exists"看作“ 至少有一个……” 的意思。关键字 exits 紧跟着用扩号扩起它所应用的条件元素。
在单模式的最简情况下（如下所示），你可以选择忽略扩号`exists Person(age > 19)`，不可绑定变量。

notAndExists.drl

    package org.notAndExists
    
    import org.example.module.Person
    import org.example.module.Car
    
    rule "not-exists-test-demo"
    when
        not (Person(age > 20))
        exists(Car(discount == 100))
    then
        System.out.println("not-exists-test-demo 已执行" );
    end

java代码

    @Test
    public void notAndExistsTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("notAndExists-test-session");

        kieSession.insert(new Person(19,"李四"));
        kieSession.insert(new Car(100,new Person(19,"李四")));

        kieSession.fireAllRules();
        kieSession.dispose();
    }    

###### forall
    
使用它来验证是否所有的Fact对象都与所有其余模式匹配。当`forall`结构满足，
规则计算结果为`true`。该元素是作用域定界符，因此它可以使用任何以前绑定的变量，
但是在其内部没有绑定的变量可在其外部使用。working memory中的所有Fact对象（类型匹配）都与`forall`中的条件匹配时结果为`true`



forall.drl

    package org.forallTest
    
    import org.example.module.Person
    import org.example.module.Car
    
    rule "forall-test-demo-01"
    when
        $p : Person(age > 60)
        $p1 : Person(this ==$p,name == "tom");
    
    then
        System.out.println("forall-test-demo-01已执行" );
     end
    
    
    rule "forall-test-demo-02"
    when
        exists Person()
        forall ($p : Person(age > 60)
                     Person(this ==$p ,name == "tom") )
    then
        System.out.println("forall-test-demo-02已执行" );
     end

java代码

     @Test
    public void forallTest01(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("forallTest-test-session");

        kieSession.insert(new Person(61,"jerry"));
        kieSession.insert(new Person(40,"tom"));

        kieSession.fireAllRules();
        kieSession.dispose();
    }


###### from 

条件元素 from 让用户指定任意的资源，用于 LHS 模式的数据匹配。这允许引擎在非工作
内存数据的基础上进行推断。数据源可以是一个绑定变量的一个子字段，或者方法调用的结
果。它是一个超强结构，允许开箱即可与其他应用程序组件或框架集成使用。常见的集成例
子是使用 hibernate 命名查询随时从数据库索取数据。用于定义数据源的表达式是任何遵
守标准 MVEL 语法的表达式。 它允许你轻松地使用对象属性导航，执行方法调用，以及访问映射和集合的元素。

validateZipcode.drl

    package org.validateZipcode
    
    import org.example.module.Person
    import org.example.module.Address
    
    rule "validate-Zipcode-test-demo01"
    
    when
        $p : Person($address : addresses)
        $a : Address(zipcode == '23920W') from $address
        $a1 : Address(zipcode == '23920W') from $p.addresses
    then
        System.out.println("validate-Zipcode-test-demo01-已执行:" + $p );
        System.out.println("validate-Zipcode-test-demo01-已执行:" + $a );
        System.out.println("validate-Zipcode-test-demo01-已执行:" + $a1 );
    end
    
    
    rule "validate-Zipcode-test-demo02"
    
    when
        $p : Person($address : name)
        $a : String(charAt(0) == 't') from $address
    then
        System.out.println("validate-Zipcode-test-demo02-已执行:" + $p );
        System.out.println("validate-Zipcode-test-demo02-已执行:" + $a );
    end

java代码

    @Test
    public void validateZipcode(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("validateZipcode-test-session");

        Person person = new Person(20,"tom");
        person.setAddresses(new ArrayList<>());
        person.getAddresses().add(new Address("北京","000010"));
        person.getAddresses().add(new Address("上海","23920W"));

        kieSession.insert(person);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

###### collect 

使用它定义一个对象集合，规则可以将这些对象作为条件的一部分使用。该规则从指定的源或Drools引擎的工作内存中获取集合。
collect元素的结果模式可以是实现java.util的任何具体类。集合接口，并提供默认的无参数公共构造函数。
您可以使用Java集合，如List、LinkedList和HashSet，或者您自己的类。如果变量在条件中的collect元素之前绑定，
则可以使用这些变量来约束源模式和结果模式。但是，在collect元素内部进行的任何绑定都不能在其外部使用。

collectTest.drl

    package org.collectTest
    
    import org.example.module.Person
    import org.example.module.Address
    import java.util.LinkedHashSet
    
    rule "collect-test-demo01"
    when
        $l : LinkedHashSet() from collect(Person())
    then
        System.out.println("collect-test-demo01 已执行！");
        System.out.println($l.getClass());
        System.out.println($l);
    end
    
    
    rule "collect-test-demo02"
    when
        $l : LinkedHashSet() from collect(Person(name str[startsWith] "tom"))
    then
        System.out.println("collect-test-demo02 已执行！");
        System.out.println($l.getClass());
        System.out.println($l);
    end

java代码
    
    @Test
    public void collectTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession session = container.newKieSession("collectTest-test-session");

        session.insert(new Person(11,"tom1",new Address("成都","0000012")));
        session.insert(new Person(12,"tom2",new Address("上海","0000013")));
        session.insert(new Person(13,"tom3",new Address("北京","0000010")));
        session.insert(new Person(14,"tom4",new Address("南京","0000011")));
        session.insert(new Person(15,"jerry",new Address("北京","0000010")));
        session.insert(new Person(16,"jerry",new Address("北京","0000010")));

        session.fireAllRules();
        session.dispose();

    }

###### accumulate

条件元素`accumulate`是一个更为灵活的`collect`，它可以实现`collect`做不了的事。如条件元素与`accumulate`
的参数可以求一些不同的值，这是collect做不了的事。它主要做的事是允许规则迭代整个集合对象，为每个元素制定执行的动作，
并在结束时返回一个结果对象，accumulate不仅支持预定义的函数使用，而且可以使用其他内置函数，重要的是可以使用自定义的函数
进行特殊化操作。Drools附带的内置accumulate功能，包括average、max、min、count、sum、collectSet、collectList




## 约束链接

对于对象内部的多个约束的链接，可以采用“&&”、“||”来实现。这三个链接符号如果没有用小括号
来显示定义优先级的话，那么他们的执行顺序是：“&&”、“||”。Drools提供了12种类型比较操作符。
如果进行常量比较，必须通过`eval(条件)`或者对象引用比较对象属性。
Drools提供的比较操作符：

    >、<、<=、>=、==、==
    contains 、 not contains
    memberOf 、 not memberOf
    matches 、 not matches

&&和||

    package org.constraintLinker
    
    import org.example.module.Person
    
    
    rule "constraint-linker-test-demo"
    
    when
        $p : Person(age < 40 && >20 , name == "jerry"  )
        $p1 : Person(age (< 40 && >20) || (< 50 && >45)  )
    then
        System.out.println($p);
        System.out.println($p1);
    end


###### contains 和 not contains比较操作符

contains是用来检查一个Fact对象的某个属性值是否包含一个指定的对象值。其语法格式为：

`Object(field[Collection|Array] contains|not contains value )`

contains.drl

    package org.containsTest
    
    
    import org.example.module.Person
    import org.example.module.School
    import java.util.List
    
    // contains
    rule "contains-test-demo"
    
    when
        $s : School()// 匹配班级
        $str : List()// 匹配班级名称集合
        // 匹配学生的班级是否和输入的班级一致
        $p : Person(className contains $s.className)
        // 匹配班级名称集合里包含班级的学生
        $p1 : Person($str contains className)
    then
        System.out.println("contains-test-demo-$s:" + $s );
        System.out.println("contains-test-demo-$p:" + $p );
        System.out.println("contains-test-demo-$str:" + $str );
        System.out.println("contains-test-demo-$p1:" + $p1 );
    end
    
    // not contains
    rule "not-contains-test-demo"
    
    when
        $s : School()// 匹配班级没有在班级名称集合中的学生
        $p : Person(className not contains $s.className)
    then
        System.out.println("not-contains-test-demo-$s:" + $s );
        System.out.println("not-contains-test-demo-$p:" + $p );
    end

DroolsContainsTestDemo.java

    KieServices services = KieServices.Factory.get();
    KieContainer container = services.getKieClasspathContainer();
    KieSession session = container.newKieSession("contains-test-session");

    Person person= new Person(18,"tom");
    person.setClassName("一班");
    School school = new School("一班");

    session.insert(person);
    session.insert(school);
    session.insert(new School("二班"));
    session.insert(Arrays.asList("三班","四班","一班"));

    session.fireAllRules();
    session.dispose();
    
###### memberOf和not memberOf比较操作符

memberOf用来判断Fact对象的某个字段是否在一个集合中，集合可以是List、Set、Map。

memberOfTest.drl
    
    package org.memeber
    
    import org.example.module.Person
    import org.example.module.School
    import java.util.List
    
    rule "memberOf-test-demo1"
    
    when
        $list : List()
        $s : School(className not memberOf $list)
        $p : Person(className memberOf $list)
    then
        System.out.println("memberOf-test-demo1-$list:" + $list );
        System.out.println("memberOf-test-demo1-$s:" + $s );
        System.out.println("memberOf-test-demo1-$p:" + $p );
    end
 
DroolsMemberOfTest.java

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

###### matches和not matches比较操作符

matches用来对某个Fact对象的字段与标准的Java正则表达式进行相匹配，被比较的字符串可以是一个标准的java正则表达式。

matches.drl

    package org.matchesTest
    
    import org.example.module.Person
    
    rule "matches-test-demo"
    
    when
        $p : Person(name matches "^.*tom$")
        $p1 : Person(name not matches "^jerry$")
    then
        System.out.println("matches-test-demo-$p:" + $p );
        System.out.println("matches-test-demo-$p1:" + $p1 );
    end
    
DroolsMatchesTest.java

    KieServices services = KieServices.Factory.get();
    KieContainer container = services.getKieClasspathContainer();
    KieSession session = container.newKieSession("matches-test-session");
    
    Person person= new Person( 19 , "tom" );
    
    session.insert(person);
    
    session.fireAllRules();
    session.dispose();
    }
    
###### soundslike 比较运算符

soundslike用来检查单词是否具有与给定值几乎相同的声音（英文）。

soundslike.drl
    
    package  org.soundslikeTest
    
    import org.example.module.Person
    
    rule "soundslike-test-demo"
    when
        $p : Person(name soundslike "foobar")
    then
        System.out.println("soundslike-test-demo-$p:" + $p );
    end

DroolsSoundslikeTest.java
    
    KieServices services = KieServices.Factory.get();
    KieContainer container = services.getKieClasspathContainer();
    KieSession kieSession = container.newKieSession("soundslike-test-session");

    Person person = new Person(20,"fubar");
    kieSession.insert(person);

    kieSession.fireAllRules();
    kieSession.dispose();
    
###### str比较运算符

str不仅检查String字段的是否以某一值开头/结尾，还可以判断字符的长度：
`Object(fieldName str[startsWith|endsWith|length] "String"|1)`    

strTest.drl

    package org.str
    
    import org.example.module.Person
    
    rule "str-test-demo"
    when
        $p : Person(name str[startsWith] "张三")
        $p1 : Person(name str[endsWith] "丰")
        $p2 : Person(name str[length] 3)
    then
        System.out.println("str-test-demo-$p:" + $p );
        System.out.println("str-test-demo-$p1:" + $p1 );
        System.out.println("str-test-demo-$p2:" + $p2 );
    end
    
DroolsStrTestDemo.java
    
    KieServices services = KieServices.Factory.get();
    KieContainer container = services.getKieClasspathContainer();
    KieSession session = container.newKieSession("strTest-test-session");

    Person person = new Person(20 , "张三丰");
    session.insert(person);
    session.fireAllRules();
    session.dispose();
    

## insert、update、delete和modify函数

###### insert

insert函数的作用与我们在Java类中调用KieSession对象的insert方法的作用相同，都是用来将一个Fact对象
插入到当前Working Memory中。一旦调用insert宏函数，那么Drools会重新与所有的规则在重新匹配一次，对于
没有设施no-loop属性位true的规则，如果条件满足，不管其之前是否执行过都会在执行一次，这个特性不仅存在与insert
宏函数上，update、modify、delete宏函数同样具有改特性，所以在某些情况下考虑不周调用insert、update、delete
或者modify容易发生死循环。

insert.drl

    package org.insertTest
    
    import org.example.module.Person
    
    rule "insert-test-demo01"
    when
    
    then
        System.out.println("insert-test-demo01-已执行");
        insert(new Person(19,"tom"));
    end
    
    rule "insert-test-demo03"
    when
    
    then
        System.out.println("insert-test-demo03-已执行");
        //insertLogical 函数的作用和insert类似
        insertLogical(new Person(19,"jerry"));
    end
    
    rule "insert-test-demo02"
    when
        $p : Person(age == 19)
    then
        System.out.println("insert-test-demo02-已执行:" + $p );
    end
    
Java代码

    @Test
    public void insertTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("insertTest-test-session");

        int i = kieSession.fireAllRules();
        System.out.println("总共执行了" + i + "规则");
        kieSession.dispose();
    }

###### update

update函数用来实现对当前Working Memory当中的Fact进行更新，update宏函数的作用与StatefulSession
对象的update方法的作用基本相同，都是用来告诉当前的Working Memory该Fact对象已经发生了变化。update的
用法有两种形式，一种是在直接更新一个Fact对象，另一种是更新指定FactHandle对应的Fact对象

1、第一种方式

update.drl

    package  org.updateTest
    
    import org.example.module.Person
    
    rule "update-test-demo01"
    
    when
        $p : Person()
    then
        System.out.println("update-test-demo01-已执行");
        $p.setAge(20);
        update($p)
    end
    
    rule "update-test-demo02"
    
    when
        $p : Person(age == 20,name == "tom")
    then
        System.out.println("update-test-demo02-已执行");
    end

java代码

    @Test
    public void updateTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("updateTest-test-session");

        kieSession.insert(new Person(18,"tom"));

        kieSession.fireAllRules();
        kieSession.dispose();
    }
    
2、第二种方式
    
modify.drl

    package org.modifyTest
    
    import org.example.module.Person
    
    rule "modify-test-demo01"
    
    when
        $p : Person()
    then
        System.out.println("modify-test-demo01已执行");
        modify($p){
            setAge(19)
        }
    end
    
    rule "modify-test-demo02"
    
    when
        $p : Person(age == 19)
    then
        System.out.println("modify-test-demo02已执行");
    end
    
java代码

    @Test
    public void modifyTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("modifyTest-test-session");

        kieSession.insert(new Person(18,"tom"));

        kieSession.fireAllRules();
        kieSession.dispose();
    }        

###### delete 函数

delete函数用来实现对Working Memory中的Fact对象进行删除操作，和insert、update、modify函数一样
该函数也会触发规则的重新匹配。

delete.drl

    package org.deleteTest
    
    import org.example.module.Person
    
    rule "delete-test-demo01"
    when
        $p : Person(age == 21)
    then
        System.out.println("delete-test-demo01");
        delete($p);
    end
    
    
    rule "delete-test-demo02"
    when
        $p : Person(age == 21)
    then
        System.out.println("delete-test-demo02");
        delete($p);
    end
    
java代码

    @Test
    public void deleteTest(){

        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("deleteTest-test-session");

        kieSession.insert(new Person(21,"tom"));

        kieSession.fireAllRules();
        kieSession.dispose();

    }
    
## extends规则扩展

extends可以在指定的规则上进行扩展

extends.drl

    package org.extendsTest
    
    import  org.example.module.Person
    
    
    rule "extends-test-demo01"
        agenda-group "extends-test-demo01"
        when
            $p : Person(age == 21)
        then
            System.out.println("extends-test-demo01 已执行");
    end
    
    
    rule "extends-test-demo02"
        extends "extends-test-demo01"
        ruleflow-group "extends-test-demo02"
    when
        Person(name == "tom")
    then
        System.out.println("extends-test-demo02 已执行：" + $p );
    end
    
java 代码

        @Test
        public void extendsTest(){
            KieServices services = KieServices.Factory.get();
            KieContainer container = services.getKieClasspathContainer();
            KieSession kieSession = container.newKieSession("extendsTest-test-session");
            kieSession.getAgenda().getAgendaGroup("extends-test-demo02").setFocus();
    
            kieSession.insert(new Person(21,"tom"));
    
            kieSession.fireAllRules();
            kieSession.dispose();
        }
        
## if、else、do和标记

在RHS中可以有多个`then`模块，可以给每个模块打上标记`then[xxxx]`。在LHS可以使用if、else来进行条件判断，然后使用do调用特定的结果（`then`），
RHS中必须有一个默认的`then`，并且紧随在`when`之后。
        
ifElseDo.drl

    package org.ifElseDo
    
    import org.example.module.Person
    
    rule "ifElseDo-test-demo01"
    
        when
            $p : Person()
                if(age == 19) do[setAge]
                else if(age == 21) do[setName1]
        then
            System.out.println("ifElseDo-test-demo01");
        then[setName1]
            $p.setName("tom");
            System.out.println("ifElseDo-test-demo01-setName已执行 ：" + $p );
        then[setAge]
            $p.setAge(23);
            System.out.println("ifElseDo-test-demo01-setName已执行 ：" + $p );
     end

java代码

    @Test
    public void ifElseDoTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("ifElseDo-test-session");

        kieSession.insert(new Person(19,"jerry"));
        kieSession.insert(new Person(21));

        kieSession.fireAllRules();
        kieSession.dispose();
    }
    
## 查询query

query语法提供了一种查询working memory 中符合约束条件的Fact对象的简单方法。query仅包含
规则的LHS部分，不用指定`when`和`then`部分。Query有一个可选参数列表，每一个参数都有可选的类型，
如果没有指定参数类型，则默认为Object类型。Query名称对于`KBase`需要全局唯一.


query.drl

    package  org.queryTest
    
    import org.example.module.Person
    
    rule "queryTest-test-demo01"
    
        when
            $p : Person(age > 20)
        then
            System.out.println("queryTest-test-demo01-已执行：" + $p );
         end
    
    // 查询 age == 20 的Person 无参数
    query "query-person-age-equal-20"
        preson : Person(age == 20)
    end
    
    // 查询 age > age1 的Person  有参数
    query "query-person-age-greater-param"(int age1)
        preson : Person(age > age1)
    end

java代码

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

## function函数
 
函数是在规则文件中放置语义代码的一种方法，和普通的java类中的方法所做到的事情没有区别。
使用关键字`function`，导入外部的java静态方法或者定义一个方法。

function.drl

    package org.functionTest
    
    import org.example.module.Person
    // 导入静态方法
    import function org.example.functionTest.DroolsFunctionTest.personNameEqual
    
    rule "function-test-demo-01"
    
    when
        $p : Person()
        // 在LHS中不能直接使用函数需要使用eval对返回值进行表达式计算
        eval(personNameEqual($p.getName(),"tom"))
        eval(isPerson($p))
    then
        System.out.println(hello($p.getName()));
    end
    
    // age == 21
    function boolean isPerson(Person p){
        return p.getAge() == 21;
    }
    
    function String hello(String name){
    
        return "hello " + name;
    }
    
java 代码

     @Test
        public void functionTest(){
            KieServices services = KieServices.Factory.get();
            KieContainer container = services.getKieClasspathContainer();
            KieSession kieSession = container.newKieSession("function-test-session");
    
            kieSession.insert(new Person(21,"tom"));
    
            kieSession.fireAllRules();
            kieSession.dispose();
    
        }
    

## Global 全局变量

global语法是用来定义全局变量，被用于提供应用程序对象的规则，他能让应用程序的对象在规则文件中能够被访问。
可以用来为规则文件提供数据或者服务。可以用来操作规则执行结果的处理和从规则返回数据，比如执行结果的日志或者值，
或者与运用程序进行交互的规则的回调处理（不建议）。
全局变量不会插入到Working Memory中，因此除非常量值，否则不应该将全局变量用于规则约束的判断中。
如果多个包中声明具有相同标识符的全局变量，则必须是相同的类型，并且他们都将引用相同的全局值

global.drl

    package org.globalTest
    
    import org.example.module.Person
    
    global  org.slf4j.Logger LOGGER // 日志对象
    global  java.util.List<Person> list     // person 集合
    
    rule "global-test-demo"
    
    when
        $p : Person(age >= 20)
    then
        list.add($p);
        LOGGER.info("规则global-test-demo已执行:{}",$p);
     end

java代码

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

## 类型声明和元数据

在DRL规则文件中声明新的FACT类型或者FACT类型的元数据

###### 声明类型   

Drools默认的Fact对象类型是`java.lang.Object`，但是可以根据需要在DRL文件中声明其他类型，
在DRL文件中声明FACT类型能够直接在Drools引擎中定义新的FACT模型，而不用像Java这样的底层语言创建模型


 declare.drl
 
    package org.declareTest
    
    import java.util.Date
    // 声明 类型
    declare Address
        address : String
    end
    // 声明枚举
    declare enum ScoreLevel
        FIRST("1"),SECOND("2");
        level : String
    end
    // 声明 类型
    declare Person
        name : String
        age : Integer
        birthday : Date
    end
    // 继承
    declare Student extends Person
        className : String
        score :Integer
        address: Address
        level: ScoreLevel
    end
    
    
    rule "declare-test-demo01"
    
    when
    
    then
        Student stu = new Student();
        stu.setClassName("二班");
        stu.setScore(90);
        stu.setAge(19);
        stu.setName("tom");
        stu.setBirthday(new Date());
        Address address = new Address();
        address.setAddress("北京");
        stu.setLevel(ScoreLevel.SECOND);
        stu.setAddress(address);
        insert(stu);
        System.out.println("declare-test-demo01 已执行");
    end
    
    rule "declare-test-demo02"
    when
        $s : Student(score >= 90)
    then
        System.out.println("declare-test-demo02: " + $s );
    end
    
java代码

    @Test
    public void declareTest() throws IllegalAccessException, InstantiationException {
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("declare-test-session");
        // 获取DRL定义的类型
        FactType student = kieSession.getKieBase().getFactType("org.declareTest", "Student");
        FactType address = kieSession.getKieBase().getFactType("org.declareTest", "Address");
        // 实例化对象
        Object a = address.newInstance();
        Object nStu = student.newInstance();

        // 设置属性值
        address.set(a,"address","上海");
        student.set(nStu,"className","三班");
        student.set(nStu,"score",91);
        student.set(nStu,"age",18);
        student.set(nStu,"name","李四");
        student.set(nStu,"birthday",new Date());
        student.set(nStu,"address",a);

        kieSession.insert(nStu);

        kieSession.fireAllRules();
        kieSession.dispose();
    }
 