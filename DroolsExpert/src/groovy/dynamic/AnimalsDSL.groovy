 package dynamic
@GrabResolver('https://repository.jboss.org/nexus/content/groups/public-jboss/')
//@GrabResolver('http://repo2.maven.org/maven2/')
@Grab('org.drools:knowledge-api:5.3.3.Final')
@Grab('org.drools:drools-compiler:5.3.3.Final')
@Grab('org.drools:drools-core:5.3.3.Final')
@Grab('com.sun.xml.bind:jaxb-xjc:2.2.5.jboss-1')
@GrabExclude('com.github.relaxng:relaxngDatatype')
//@Grab('org.mvel#mvel2;2.1.2.Final')
//@Grab('org.slf4j#slf4j-simple;1.6.4')
import org.drools.KnowledgeBaseFactory
import org.drools.builder.KnowledgeBuilderFactory
import org.drools.builder.ResourceType
import org.drools.io.ResourceFactory

class Solver {
  static main(Map animals, int totalAnimals, int totalLegs, ClassLoader loader) {
    def whenClauses = ''
    def thenClauses = ''
    def numAnimalsClause = ''
    def numLegsClause = ''
    def lastIndex = animals.size() - 1
    animals.eachWithIndex { entry, index ->
      def key = entry.key
      def capKey = key.capitalize()
      whenClauses += '        $' + "$key : $capKey ("
      thenClauses += "        System.out.println( \"$capKey \"" + ' + $' + key + '.getQuantity() )\n'
      if (index != lastIndex) {
        numAnimalsClause += ' + $' + key + '.quantity'
        numLegsClause += ' + $' + key + '.quantity * $' + key + '.numLegs'
        whenClauses += ' )\n'
      } else {
        whenClauses += '\n            quantity' + numAnimalsClause + ' == ' + totalAnimals + ','
        whenClauses += '\n            quantity * numLegs' + numLegsClause + ' == ' + totalLegs
        whenClauses += '\n        )\n'
      }
    }
    def drl = '''
dialect "mvel"
rule "deduce animal counts"
    when
''' + whenClauses + '''    then
''' + thenClauses + '''end
'''

    def kbuilderConf = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(null, loader)
    def kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(kbuilderConf)
    kbuilder.add(ResourceFactory.newReaderResource(new StringReader(drl)), ResourceType.DRL)
    def kbaseConf = KnowledgeBaseFactory.newKnowledgeBaseConfiguration(null, loader)
    def kbase = KnowledgeBaseFactory.newKnowledgeBase(kbaseConf)
    kbase.addKnowledgePackages(kbuilder.knowledgePackages)
    def ksession = kbase.newStatefulKnowledgeSession()

    (totalAnimals + 1).times { n ->
      animals.each { key, val ->
        def capKey = key.capitalize()
        Class animal = loader.loadClass(capKey)
        if (totalLegs.intdiv(animal.numLegs) >= n) {
          ksession.insert(animal.newInstance(n))
        }
      }
    }

    ksession.fireAllRules()
    ksession.dispose()
  }
}

def totalAnimals = 27
def totalLegs = 2020
def animals = [cranes: 2, tortoises: 4, centipedes: 100]
new GroovyShell([animals: animals] as Binding).evaluate(
    animals.collect { key, val ->
      def capKey = key.capitalize()
      """
          @groovy.transform.Immutable
          class $capKey {
            static int numLegs = $val
            int quantity
          }
      """
    }.join('\n') + "import dynamic.Solver\nSolver.main(animals, $totalAnimals, $totalLegs, getClass().classLoader)"
)
