
# Camunda Tools

![alt text](https://accso.de/app/uploads/2019/08/camunda.jpg)

Camunda tools is a set of tools for declarative work with Camunda BPM framework.
##  Dependency
```sh  
<dependency>  
 <groupId>io.github.flexibletech</groupId>  
  <artifactId>camunda-tools-starter</artifactId>  
  <version>1.0.1</version>  
</dependency>  
```  

##  Description
For complex domains, it is important to use clean or hexagonal architecture. Very often, central frameworks penetrate deep into application or even domain code. In applications built using the camunda process orchestration engine, a large amount of framework code can be observed. This library allows you to avoid redundant framework code by converting all work with the framework into a declarative style.

##   Сreating delegates

The Delegate annotation is used to declaratively create delegates. The delegate will be created from the method of the spring bean class. The bean method must be implemented according to the following pattern:
* To successfully register a delegate bean, the service class method must take a Camunda process key variable as a parameter. This key must match the domain entity identifier. The process key variable must be annotated with the ProcessKeyValue annotation.   
  For example:
```sh  
@Delegate(beanName = TestValues.TEST_DELEGATE_SECOND_NAME, key = TestValues.PROCESS_KEY)  
public String doActionSecond(@ProcessKeyValue String processKey) {  
    return processKey;  
}
```  
* A bean is created with the TemplateDelegate type. In the context of a spring this bean is registered by the beanName from the Delegate annotation. The method to be executed in this delegate is passed in the Invocation object. In expressionVariables, the names of the process variables of the Camunda and the spel of the expression will be passed to get them from the result of the execution of the method passed to the Invocation object. Thus, the result of the delegate's work will be transferred to the Camunda process, if necessary.
* Camunda process variables must contain the ID of the domain entity of the service. This ID must match the key with which the process is launched in the Camunda. The entity ID is retrieved from the "key" field of the Delegate annotation.

## Use case

We have a simple domain entity FlowEntity:

```sh  
public class FlowEntity {  static final int OK_STATUS = 1;  static final int REJECT_STATUS = 0;  static final String ID = "1";  
  private final String id;  private Integer status;  
  private FlowEntity(String id, Integer status) { this.id = id; this.status = status; }  
 public String getId() { return id; }  
 public void setStatus(Integer status) { this.status = status; }  
 public static FlowEntity newFlowEntity() { return new FlowEntity(ID, OK_STATUS); }  
 @Override public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; FlowEntity that = (FlowEntity) o; return Objects.equals(status, that.status); }  
 @Override public int hashCode() { return Objects.hash(status); }}  
```  

According to the rules of the hexagonal architecture, we must provide an application service for interacting with domain objects (ApplicationService). To start the process in camunda, we implement the method startProcess.
```sh  
 @StartProcess(businessKeyName = ProcessValues.BUSINESS_KEY, businessKeyValue = "getId()", processKey = processKey) public FlowEntity startProcess() { var flowEntity = FlowEntity.newFlowEntity(); cache.put(flowEntity.getId(), flowEntity); System.out.println("Flow entity has been created."); return flowEntity; }  
```  
In this example, the process with key processKey will be launched with the key obtained from the flowEntity object through the method getId(). This key will be placed to process variables by name ProcessValues.BUSINESS_KEY.  
Every method of ApplicationService can be registered as camunda delegate. For this we must to mark method by @Delegate annotation, for example:
```sh  
@Delegate(beanName = ProcessValues.STEP_1, key = ProcessValues.BUSINESS_KEY)  
  public FlowEntity step1(String id) {  var flowEntity = cache.get(id);  
  flowEntity.setStatus(FlowEntity.REJECT_STATUS);  
  
  System.out.println("Step 1 has been completed.");  
  
  return flowEntity; }```  
A delegate will be registered for this method that can be used in the camunda process.  
  
## License  
  
MIT  
  
**Free Software, Hell Yeah!**  
  
[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)  
  
[dill]: <https://github.com/joemccann/dillinger>  
[git-repo-url]: <https://github.com/joemccann/dillinger.git>  
[john gruber]: <http://daringfireball.net>  
[df1]: <http://daringfireball.net/projects/markdown/>  
[markdown-it]: <https://github.com/markdown-it/markdown-it>  
[Ace Editor]: <http://ace.ajax.org>  
[node.js]: <http://nodejs.org>  
[Twitter Bootstrap]: <http://twitter.github.com/bootstrap/>  
[jQuery]: <http://jquery.com>  
[@tjholowaychuk]: <http://twitter.com/tjholowaychuk>  
[express]: <http://expressjs.com>  
[AngularJS]: <http://angularjs.org>  
[Gulp]: <http://gulpjs.com>  
  
[PlDb]: <https://github.com/joemccann/dillinger/tree/master/plugins/dropbox/README.md>  
[PlGh]: <https://github.com/joemccann/dillinger/tree/master/plugins/github/README.md>  
[PlGd]: <https://github.com/joemccann/dillinger/tree/master/plugins/googledrive/README.md>  
[PlOd]: <https://github.com/joemccann/dillinger/tree/master/plugins/onedrive/README.md>  
[PlMe]: <https://github.com/joemccann/dillinger/tree/master/plugins/medium/README.md>  
[PlGa]: <https://github.com/RahulHP/dillinger/blob/master/plugins/googleanalytics/README.md