package de.codecentric.boot.admin.server.cloud.extension;

import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaAutoServiceRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.context.ApplicationContext;

public class MossEurekaAutoServiceRegistration extends EurekaAutoServiceRegistration {

    private EurekaRegistration registration;

    public MossEurekaAutoServiceRegistration(ApplicationContext context, EurekaServiceRegistry serviceRegistry, EurekaRegistration registration, EurekaRegistration registration1) {
        super(context, serviceRegistry, registration);
        this.registration = registration1;
    }

    public EurekaRegistration getRegistration() {
        return registration;
    }

    public void setRegistration(EurekaRegistration registration) {
        this.registration = registration;
    }

    @Override
    public void start() {
        super.start();
    }
}