## Microservice as Library

### Introduction
We know microservices are generally confined to a business domain or backend entity. They
 are deployed as self contained service without any external dependencies. But can
we develop a microservice as a library? If so why and what are the advantages?

Let's try to explore the answers to those questions here with a sample implementation of product service.


### What is Microservice as Library?
When a microservice is developed with all it's core functionality but without
any API interface such as Rest, GraphQL or gRPC then we can call it a service as library.
Once built, it can be pushed to your artifactory. Which can then be imported either into a modulith(Modular Monolith)
or one/many API interfaces to meet your business need.

### Advantages

#### Reusability
Reusability is an obvious advantage for this pattern. Same service can now be used in multiple scenarios.

#### Division of Responsibility
This pattern allows us to segregate various responsibility such security, testing etc between
service library and api interfaces/modulith in front of it. 

### Constraints
When a service is developed as a library, it's consuming api interfaces or modulith
have to be developed in the same program/framework, in this case SpringBoot.


### Anatomy
#### [App Security Common](app-security-common)
A reusable library across all microservices with useful functionalities like login, token generation,
user authentication etc. User can be authenticated against a DB or LDAP.

#### [Product Service](product-service)
A simple service with crud operations against product entity stored in MySQL server

#### [Product Rest API](product-rest-api)
Rest API interface with Spring MVC rest controllers with dependency on the product service library and app security common.

#### [Product GraphQL API](product-graph-api)
GraphQL interface with Spring Graph Starter and MVC controllers with dependency on the product service library and app security common.

#### [Product gRPC API](product-grpc-api)
gRPC protobuf interface with dependency on the product service library and app security common. Extremely high performant
due to byte encoded message exchange and persistent open socket communication.

#### [Modular Monolith](monolith-application)
Full fledged monolithic application with dependency on product service library. Monolith application may choose to use and app security common 
or have their own security module.

### Security Considerations
With this pattern, different layers can perform different security functions to improve flexibility.

#### Product Service
At service layer we need to turn on spring method security and annotate service methods 
with appropriate [@Secured](product-service/src/main/java/io/pbhuyan/product/service/ProductService.java) annotations.

This will ensure your service methods are fully secured no matter where they are used from.


```java
    @Secured(ROLE.GA_ADMIN)
    public Product add(@NotNull final Product product);

    @Secured(ROLE.GA_USER)
    public List<Product> getAllProducts();
```

```java
    @Configuration
    @EnableMethodSecurity(securedEnabled = true)
    public class MethodSecurityConfig {
    }
```

**IMPORTANT:** Make sure to annotate all the methods unless some of them need unrestricted access.



#### RestAPI
RestAPI doesn't need to validate the authorization as it is enforced by the service itself. We just need to validate
authentication and make sure all end points required to have proper authentication except for login/logout endpoints.

Check [ProductSecurityConfig](product-rest-api%2Fsrc%2Fmain%2Fjava%2Fio%2Fpbhuyan%2Fproduct%2Fsecurity%2FProductSecurityConfig.java) for more details.












