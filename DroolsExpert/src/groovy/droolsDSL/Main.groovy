//5.4.0.Final
@GrabResolver('https://repository.jboss.org/nexus/content/groups/public-jboss/')
@Grab('org.drools:knowledge-api:5.4.0.Final')
@Grab('org.drools:drools-compiler:5.4.0.Final')
@Grab('org.drools:drools-core:5.4.0.Final')
@Grab('com.sun.xml.bind:jaxb-xjc:2.2.5.jboss-1')
@GrabExclude('com.github.relaxng:relaxngDatatype')
//5.5.0-CR1
//@GrabResolver('https://repository.jboss.org/nexus/content/groups/public-jboss/')
//@Grab('org.drools:knowledge-api:5.5.0.CR1')
//@Grab('org.drools:drools-compiler:5.5.0.CR1')
//@Grab('org.drools:drools-core:5.5.0.CR1')
//@Grab('com.sun.xml.bind:jaxb-xjc:2.2.5.jboss-1')
//@GrabExclude('com.github.relaxng:relaxngDatatype')
//@GrabResolver('http://repo2.maven.org/maven2/')
//@Grab('org.mvel#mvel2;2.1.2.Final')
//@Grab('org.slf4j#slf4j-simple;1.6.4')
import groovy.transform.Immutable
import static org.drools.KnowledgeBaseFactory.newKnowledgeBase
import static org.drools.builder.KnowledgeBuilderFactory.newKnowledgeBuilder
import static org.drools.builder.ResourceType.DSL
import static org.drools.builder.ResourceType.DSLR
import static org.drools.io.ResourceFactory.newReaderResource

def numAnimals = 7
def numLegs = 20
def kbuilder = newKnowledgeBuilder()
def dslr = """
dialect "mvel"

rule "deduce animal counts"
  when
    There are some "Cranes"
    There are some "Tortoises"
    There are $numAnimals animals in total
    There are $numLegs legs in total
  then
    Display the number of "Cranes"
    Display the number of "Tortoises"
end
"""
def dsl = '''
[when][]There are some "{animals}"=${animals!lc} : {animals}( )
[when][]There are {numAnimals} animals in total=eval( $cranes.quantity + $tortoises.quantity == {numAnimals} )
[when][]There are {numLegs} legs in total=eval( $cranes.quantity * $cranes.numLegs + $tortoises.quantity * $tortoises.numLegs == {numLegs} )
[then][]Display the number of "{animals}"=System.out.println( "{animals} " + ${animals!lc}.quantity );
'''
kbuilder.add(newReaderResource(new StringReader(dsl)), DSL)
kbuilder.add(newReaderResource(new StringReader(dslr)), DSLR)
def kbase = newKnowledgeBase()
kbase.addKnowledgePackages(kbuilder.knowledgePackages)
def ksession = kbase.newStatefulKnowledgeSession()

(numAnimals + 1).times { n ->
  if (numLegs.intdiv(Cranes.numLegs) >= n) {
    ksession.insert(new Cranes(n))
  }
  if (numLegs.intdiv(Tortoises.numLegs) >= n) {
    ksession.insert(new Tortoises(n))
  }
}

ksession.fireAllRules()
ksession.dispose()

@Immutable
class Cranes {
  static int numLegs = 2
  int quantity
}

@Immutable
class Tortoises {
  static int numLegs = 4
  int quantity
}