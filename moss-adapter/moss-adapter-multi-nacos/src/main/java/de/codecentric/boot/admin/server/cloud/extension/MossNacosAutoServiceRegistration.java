package de.codecentric.boot.admin.server.cloud.extension;

import org.springframework.cloud.alibaba.nacos.registry.NacosAutoServiceRegistration;
import org.springframework.cloud.alibaba.nacos.registry.NacosRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

public class MossNacosAutoServiceRegistration extends NacosAutoServiceRegistration {

    private NacosRegistration registration;

    public MossNacosAutoServiceRegistration(ServiceRegistry<NacosRegistration> serviceRegistry,
                                            AutoServiceRegistrationProperties autoServiceRegistrationProperties, NacosRegistration registration, NacosRegistration registration1) {
        super(serviceRegistry, autoServiceRegistrationProperties, registration);
        this.registration = registration1;
    }

    @Override
    public NacosRegistration getRegistration() {
        return registration;
    }

    public void setRegistration(NacosRegistration registration) {
        this.registration = registration;
    }
    @Override
    public void start() {
        super.start();
    }
}