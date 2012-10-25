//@GrabResolver('https://repository.jboss.org/nexus/content/groups/public-jboss/')
//@Grab('org.drools:knowledge-api:5.4.0.Final')
//@Grab('org.drools:knowledge-internal-api:5.4.0.Final')
//@Grab('com.sun.xml.bind:jaxb-xjc:2.2.5.jboss-1')
//@GrabExclude('com.github.relaxng:relaxngDatatype')
import groovy.transform.Immutable
import org.drools.builder.ResourceType
import static org.drools.KnowledgeBaseFactory.newKnowledgeBase
import static org.drools.builder.KnowledgeBuilderFactory.newKnowledgeBuilder
import static org.drools.io.ResourceFactory.newClassPathResource

def kbuilder = newKnowledgeBuilder()
kbuilder.add(newClassPathResource("golf.drl", getClass()), ResourceType.DRL)
def kbase = newKnowledgeBase()
kbase.addKnowledgePackages(kbuilder.knowledgePackages)
def ksession = kbase.newStatefulKnowledgeSession()

def names = ["Fred", "Joe", "Bob", "Tom"]
def colors = ["red", "blue", "plaid", "orange"]
def positions = [1, 2, 3, 4]
[names, colors, positions].combinations().each { n, c, p ->
  ksession.insert(new Golfer(n, c, p))
}
ksession.fireAllRules()
ksession.dispose()

@Immutable
class Golfer {
  String name
  String color
  int position
}

