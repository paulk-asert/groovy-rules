//@GrabResolver('https://repository.jboss.org/nexus/content/groups/public-jboss/')
//@Grab('org.drools:knowledge-api:5.4.0.Final')
//@Grab('org.drools:knowledge-internal-api:5.4.0.Final')
//@Grab('com.sun.xml.bind:jaxb-xjc:2.2.5.jboss-1')
//@GrabExclude('com.github.relaxng:relaxngDatatype')
import groovy.transform.Immutable
import org.drools.builder.ResourceType
import static org.drools.KnowledgeBaseFactory.newKnowledgeBase
import static org.drools.builder.KnowledgeBuilderFactory.newKnowledgeBuilder
import static org.drools.io.ResourceFactory.newReaderResource

def numAnimals = 7
def numLegs = 20
def kbuilder = newKnowledgeBuilder()
kbuilder.add(newReaderResource(new StringReader('''
dialect "mvel"
rule "deduce animal counts"
  when
    $crane : Crane( )
    $tortoise : Tortoise(
      quantity + $crane.quantity == ''' + numAnimals + ''',
      quantity * numLegs + $crane.quantity * $crane.numLegs == ''' + numLegs + '''
    )
  then
    System.out.println( "Cranes " + $crane.getQuantity() )
    System.out.println( "Tortoises " + $tortoise.getQuantity() )
end
''')), ResourceType.DRL)
def kbase = newKnowledgeBase()
kbase.addKnowledgePackages(kbuilder.knowledgePackages)
def ksession = kbase.newStatefulKnowledgeSession()

(numAnimals + 1).times { n ->
  if (numLegs.intdiv(Crane.numLegs) >= n) {
    ksession.insert(new Crane(n))
  }
  if (numLegs.intdiv(Tortoise.numLegs) >= n) {
    ksession.insert(new Tortoise(n))
  }
}

ksession.fireAllRules()
ksession.dispose()

@Immutable
class Crane {
  static int numLegs = 2
  int quantity
}

@Immutable
class Tortoise {
  static int numLegs = 4
  int quantity
}
