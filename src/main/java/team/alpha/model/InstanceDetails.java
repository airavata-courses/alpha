package team.alpha.model;

public class InstanceDetails {
    String serviceName;
    String instanceId;
    String address;
    Integer port;

    public InstanceDetails() {

    }

    public InstanceDetails(InstanceDetailsBuilder builder) {
        this.serviceName = builder.serviceName;
        this.instanceId = builder.instanceId;
        this.address = builder.address;
        this.port = builder.port;
    }

    public static class InstanceDetailsBuilder {
        String serviceName;
        String instanceId;
        String address;
        Integer port;

        public InstanceDetailsBuilder withServiceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public InstanceDetailsBuilder withInstanceId(String instanceId) {
            this.instanceId = instanceId;
            return this;
        }

        public InstanceDetailsBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public InstanceDetailsBuilder withPort(Integer port) {
            this.port = port;
            return this;
        }

        public InstanceDetails build() {
            return new InstanceDetails(this);
        }
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getAddress() {
        return address;
    }

    public Integer getPort() {
        return port;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public static InstanceDetailsBuilder getBuilder(){
        return new InstanceDetailsBuilder();
    }
}
