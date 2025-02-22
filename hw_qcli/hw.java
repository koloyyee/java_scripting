///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 17+
// Update the Quarkus version to what you want here or run jbang with
// `-Dquarkus.version=<version>` to override it.
//DEPS io.quarkus:quarkus-bom:${quarkus.version:3.7.2}@pom
//DEPS io.quarkus:quarkus-picocli
//Q:CONFIG quarkus.banner.enabled=false
//Q:CONFIG quarkus.log.level=WARN

import com.oracle.svm.core.annotate.Inject;

/**
 * Run with `jbang hw.java "your message here."
 * 
 * or `chmod +x ./hw.java && ./hw.java "message here."`
 * 
 * 
 * create native executable with GraalVM
 * `jbang export native hw.java`
 */
@CommandLine.Command
public class hw implements Runnable {

    @CommandLine.Parameters(index = "0", description = "The greeting to print", defaultValue = "World!")
    String name;

    @Inject
    CommandLine.IFactory factory;

    private final GreetingService greetingService;

    public hw(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @Override
    public void run() {
        greetingService.sayHello(name);
    }

}

@Dependent
class GreetingService {
    void sayHello(String name) {
        System.out.println("Hello " + name + "!");
    }
}