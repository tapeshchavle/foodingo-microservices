# 📚 **EUREKA SERVER API REFERENCE**

> Complete API documentation for Eureka Server endpoints

---

## 🔍 **SERVICE REGISTRY APIS**

### **Get All Applications**
```http
GET /eureka/apps
```

**Description**: Retrieves all registered applications and their instances.

**Response**:
```xml
<applications>
  <versions__delta>1</versions__delta>
  <apps__hashcode>UP_1_</apps__hashcode>
  <application>
    <name>USER-SERVICE</name>
    <instance>
      <instanceId>user-service:8081</instanceId>
      <hostName>localhost</hostName>
      <app>USER-SERVICE</app>
      <ipAddr>192.168.1.100</ipAddr>
      <status>UP</status>
      <overriddenstatus>UNKNOWN</overriddenstatus>
      <port enabled="true">8081</port>
      <securePort enabled="false">443</securePort>
      <countryId>1</countryId>
      <dataCenterInfo class="com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo">
        <name>MyOwn</name>
      </dataCenterInfo>
      <leaseInfo>
        <renewalIntervalInSecs>30</renewalIntervalInSecs>
        <durationInSecs>90</durationInSecs>
        <registrationTimestamp>1640995200000</registrationTimestamp>
        <lastRenewalTimestamp>1640995200000</lastRenewalTimestamp>
        <evictionTimestamp>0</evictionTimestamp>
        <serviceUpTimestamp>1640995200000</serviceUpTimestamp>
      </leaseInfo>
    </instance>
  </application>
</applications>
```

---

### **Get Specific Application**
```http
GET /eureka/apps/{app-name}
```

**Parameters**:
- `app-name`: Name of the application (e.g., `USER-SERVICE`)

**Response**:
```xml
<application>
  <name>USER-SERVICE</name>
  <instance>
    <instanceId>user-service:8081</instanceId>
    <hostName>localhost</hostName>
    <app>USER-SERVICE</app>
    <ipAddr>192.168.1.100</ipAddr>
    <status>UP</status>
    <port enabled="true">8081</port>
    <securePort enabled="false">443</securePort>
    <leaseInfo>
      <renewalIntervalInSecs>30</renewalIntervalInSecs>
      <durationInSecs>90</durationInSecs>
    </leaseInfo>
  </instance>
</application>
```

---

### **Get Application Instance**
```http
GET /eureka/apps/{app-name}/{instance-id}
```

**Parameters**:
- `app-name`: Name of the application
- `instance-id`: ID of the specific instance

**Response**:
```xml
<instance>
  <instanceId>user-service:8081</instanceId>
  <hostName>localhost</hostName>
  <app>USER-SERVICE</app>
  <ipAddr>192.168.1.100</ipAddr>
  <status>UP</status>
  <port enabled="true">8081</port>
  <securePort enabled="false">443</securePort>
  <leaseInfo>
    <renewalIntervalInSecs>30</renewalIntervalInSecs>
    <durationInSecs>90</durationInSecs>
  </leaseInfo>
</instance>
```

---

## 📝 **REGISTRATION APIS**

### **Register Application Instance**
```http
POST /eureka/apps/{app-name}
Content-Type: application/json
```

**Request Body**:
```json
{
  "instance": {
    "instanceId": "user-service:8081",
    "hostName": "localhost",
    "app": "USER-SERVICE",
    "ipAddr": "192.168.1.100",
    "status": "UP",
    "port": {
      "$": "8081",
      "@enabled": "true"
    },
    "securePort": {
      "$": "443",
      "@enabled": "false"
    },
    "dataCenterInfo": {
      "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
      "name": "MyOwn"
    },
    "leaseInfo": {
      "renewalIntervalInSecs": 30,
      "durationInSecs": 90
    }
  }
}
```

**Response**: `204 No Content` (Success)

---

### **Renew Instance Registration**
```http
PUT /eureka/apps/{app-name}/{instance-id}
```

**Description**: Heartbeat to renew the instance registration.

**Response**: `200 OK` (Success)

---

### **Cancel Instance Registration**
```http
DELETE /eureka/apps/{app-name}/{instance-id}
```

**Description**: Cancels the instance registration.

**Response**: `200 OK` (Success)

---

## 🔄 **STATUS UPDATE APIS**

### **Update Instance Status**
```http
PUT /eureka/apps/{app-name}/{instance-id}/status?value={status}
```

**Parameters**:
- `status`: New status (`UP`, `DOWN`, `STARTING`, `OUT_OF_SERVICE`, `UNKNOWN`)

**Response**: `200 OK` (Success)

---

### **Remove Instance Status Override**
```http
DELETE /eureka/apps/{app-name}/{instance-id}/status
```

**Description**: Removes the status override, allowing Eureka to determine the status.

**Response**: `200 OK` (Success)

---

## 📊 **ADMINISTRATIVE APIS**

### **Get Registry Information**
```http
GET /eureka/apps/delta
```

**Description**: Gets the delta changes in the registry since the last fetch.

**Response**:
```xml
<applications>
  <versions__delta>2</versions__delta>
  <apps__hashcode>UP_2_</apps__hashcode>
  <!-- Delta changes -->
</applications>
```

---

### **Get Registry Versions**
```http
GET /eureka/versions
```

**Response**:
```json
{
  "versions": ["1.0", "2.0"]
}
```

---

### **Get VIPS (Virtual IPs)**
```http
GET /eureka/vips/{vip-address}
```

**Parameters**:
- `vip-address`: Virtual IP address

**Response**:
```xml
<application>
  <name>USER-SERVICE</name>
  <!-- Instance details -->
</application>
```

---

## 🏥 **HEALTH & MONITORING APIS**

### **Health Check**
```http
GET /actuator/health
```

**Response**:
```json
{
  "status": "UP",
  "components": {
    "eurekaServer": {
      "status": "UP"
    }
  }
}
```

---

### **Service Information**
```http
GET /actuator/info
```

**Response**:
```json
{
  "app": {
    "name": "eureka-server",
    "version": "1.0.0"
  },
  "eureka": {
    "server": {
      "version": "1.10.17"
    }
  }
}
```

---

### **Metrics**
```http
GET /actuator/metrics
```

**Response**:
```json
{
  "names": [
    "eureka.registry.size",
    "eureka.registry.renewals",
    "eureka.registry.cancellations",
    "jvm.memory.used",
    "process.cpu.usage"
  ]
}
```

---

### **Specific Metric**
```http
GET /actuator/metrics/{metric-name}
```

**Example**:
```http
GET /actuator/metrics/eureka.registry.size
```

**Response**:
```json
{
  "name": "eureka.registry.size",
  "description": "Number of registered instances",
  "baseUnit": "instances",
  "measurements": [
    {
      "statistic": "VALUE",
      "value": 5
    }
  ]
}
```

---

## 🔐 **AUTHENTICATION**

### **Basic Authentication**
If authentication is enabled, include credentials in requests:

```http
GET /eureka/apps
Authorization: Basic YWRtaW46cGFzc3dvcmQ=
```

---

## 📝 **ERROR RESPONSES**

### **404 Not Found**
```json
{
  "error": "Application not found",
  "message": "No application found with name: INVALID-SERVICE"
}
```

### **400 Bad Request**
```json
{
  "error": "Invalid request",
  "message": "Missing required field: instanceId"
}
```

### **500 Internal Server Error**
```json
{
  "error": "Internal server error",
  "message": "Failed to process registration request"
}
```

---

## 🧪 **TESTING EXAMPLES**

### **Register a Service**
```bash
curl -X POST http://localhost:8761/eureka/apps/USER-SERVICE \
  -H "Content-Type: application/json" \
  -d '{
    "instance": {
      "instanceId": "user-service:8081",
      "hostName": "localhost",
      "app": "USER-SERVICE",
      "ipAddr": "192.168.1.100",
      "status": "UP",
      "port": {"$": "8081", "@enabled": "true"},
      "securePort": {"$": "443", "@enabled": "false"},
      "dataCenterInfo": {
        "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
        "name": "MyOwn"
      },
      "leaseInfo": {
        "renewalIntervalInSecs": 30,
        "durationInSecs": 90
      }
    }
  }'
```

### **Get All Services**
```bash
curl http://localhost:8761/eureka/apps
```

### **Renew Registration**
```bash
curl -X PUT http://localhost:8761/eureka/apps/USER-SERVICE/user-service:8081
```

### **Update Status**
```bash
curl -X PUT "http://localhost:8761/eureka/apps/USER-SERVICE/user-service:8081/status?value=DOWN"
```

---

## 📚 **SDK EXAMPLES**

### **Java Client**
```java
@RestController
public class ServiceDiscoveryController {
    
    @Autowired
    private DiscoveryClient discoveryClient;
    
    @GetMapping("/services")
    public List<ServiceInstance> getServices(@RequestParam String serviceName) {
        return discoveryClient.getInstances(serviceName);
    }
    
    @GetMapping("/service-url")
    public String getServiceUrl(@RequestParam String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (!instances.isEmpty()) {
            ServiceInstance instance = instances.get(0);
            return "http://" + instance.getHost() + ":" + instance.getPort();
        }
        return null;
    }
}
```

### **Spring Cloud LoadBalancer**
```java
@Configuration
public class LoadBalancerConfig {
    
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@Service
public class UserService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    public String callRestaurantService() {
        return restTemplate.getForObject(
            "http://restaurant-service/api/restaurants", 
            String.class
        );
    }
}
```

---

## 🎯 **BEST PRACTICES**

### **1. Service Registration**
- Always register services with meaningful names
- Use consistent naming conventions
- Include proper metadata

### **2. Health Checks**
- Implement proper health check endpoints
- Return meaningful status codes
- Handle dependencies gracefully

### **3. Error Handling**
- Handle service discovery failures
- Implement fallback mechanisms
- Log service discovery events

### **4. Performance**
- Cache service instances
- Use connection pooling
- Monitor service discovery latency

---

**📖 For more information, visit the [Spring Cloud Netflix documentation](https://spring.io/projects/spring-cloud-netflix)**

