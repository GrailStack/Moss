//package de.codecentric.boot.admin.server.cloud.extension;
//
//import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
//import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
//import org.springframework.cloud.client.serviceregistry.Registration;
//import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
//import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperRegistration;
//
//public class MossAutoServiceRegistration extends AbstractAutoServiceRegistration {
//
//    private final AutoServiceRegistrationProperties properties;
//    private Registration registration;
//
//    public MossAutoServiceRegistration(ServiceRegistry serviceRegistry,
//                                       AutoServiceRegistrationProperties properties,
//                                       Registration registration) {
//        super(serviceRegistry, properties);
//        this.registration = registration;
//        this.properties = properties;
//    }
//
//
//    @Override
//    protected AutoServiceRegistrationProperties getConfiguration() {
//        return properties;
//    }
//
//    @Override
//    protected boolean isEnabled() {
//        return this.properties.isEnabled();
//    }
//
//    public Registration getRegistration() {
//        return registration;
//    }
//
//    @Override
//    protected Registration getManagementRegistration() {
//        return null;
//    }
//
//    public void setRegistration(ZookeeperRegistration registration) {
//        this.registration = registration;
//    }
//}