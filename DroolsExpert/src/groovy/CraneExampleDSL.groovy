@GrabResolver('https://repository.jboss.org/nexus/content/groups/public-jboss/')
@Grab('org.drools:drools-compiler:5.5.0.Final')
@Grab('org.drools:drools-core:5.5.0.Final')
@Grab('com.sun.xml.bind:jaxb-xjc:2.2.5.jboss-1;transitive=false')
@Grab('com.google.protobuf:protobuf-java:2.4.1')
@Grab('org.slf4j:slf4j-simple:1.6.4')
// org.mvel:mvel2:2.1.3.Final has an invalid SHA1 checksum
// set JAVA_OPTS="-Divy.checksums="
import groovy.transform.Field
import org.drools.builder.ResourceType

import static org.drools.KnowledgeBaseFactory.*
import static org.drools.builder.KnowledgeBuilderFactory.*
import static org.drools.io.ResourceFactory.newReaderResource

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

    def kbuilderConf = newKnowledgeBuilderConfiguration(null, loader)
    def kbuilder = newKnowledgeBuilder(kbuilderConf)
    kbuilder.add(newReaderResource(new StringReader(drl)), ResourceType.DRL)
    def kbaseConf = newKnowledgeBaseConfiguration(null, loader)
    def kbase = newKnowledgeBase(kbaseConf)
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

@Field animalProps = [:]
def props = [:]

def methodMissing(String name, _have) {
  new AnimalHolder(animals: animalProps, name: name)
}

def propertyMissing(String name) {
  name
}

class ThereHolder {
  def props
  def methodMissing(String name, args) {
    props['total' + args[0].capitalize()] = name.toInteger()
  }
}

class AnimalHolder {
  def animals, name
  def methodMissing(String number, args) {
    animals[name] = number.toInteger()
  }
}

def there = { _are ->
  new ThereHolder(props: props)
}

cranes have 2 legs
tortoises have 4 legs
millipedes have 1000 legs
there are 8 animals
there are 1020 legs

new GroovyShell([animals: animalProps] as Binding).evaluate(
    animalProps.collect { key, val ->
      def capKey = key.capitalize()
      """
          @groovy.transform.Immutable
          class $capKey {
            static int numLegs = $val
            int quantity
          }
      """
    }.join('\n') + "Solver.main(animals, $props.totalAnimals, $props.totalLegs, getClass().classLoader)"
)
