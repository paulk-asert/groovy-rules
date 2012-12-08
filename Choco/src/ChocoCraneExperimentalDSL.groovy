// Currently requires an experimental branch of Groovy - anticipated release: 2.1.0
//@GrabResolver('http://www.emn.fr/z-info/choco-repo/mvn/repository')
//@Grab('choco:choco-solver:2.1.5')
import groovy.transform.Field
import groovy.transform.TypeChecked

import static choco.Choco.*
import choco.cp.model.CPModel
import choco.cp.solver.CPSolver
import choco.kernel.model.variables.integer.IntegerVariable

@Field animals = []
@Field headCount = []
@Field legCount = []

class CountHolder {
  List<Integer> counter
  CountHolder(counter) {
    this.counter = counter
  }
  void is(int count) {
    counter << count
  }
}

class TypesHolder {
  List<String> animals
  TypesHolder(animals) { this.animals = animals }
  def include(String... animals) { this.animals.addAll(animals) }
}

enum TypesStopWord { types }
import static TypesStopWord.*

enum CountStopWord { count }
import static CountStopWord.*

enum SolutionStopWord { solution }
import static SolutionStopWord.*

CountHolder leg(CountStopWord _count) {
  new CountHolder(legCount)
}

CountHolder head(CountStopWord _count) {
  new CountHolder(headCount)
}

TypesHolder animal(TypesStopWord _types) {
  new TypesHolder(animals)
}

def display(SolutionStopWord _solution) {
  def legCalculator = { String name ->
    switch(name.toLowerCase()) {
      case ~/.*crane/: return 2
      case ~/.*tortoise/: return 4
      case ~/.*millipede/: return 1000
    }
  }
  def m = new CPModel()
  def s = new CPSolver()
  IntegerVariable[] animalVars = animals.collect{ makeIntVar(it.toString(), 0, legCount[0].intdiv(legCalculator(it))) }
  m.addConstraint(eq(scalar(animalVars, ([1] * animals.size()) as int[]), headCount[0]))
  m.addConstraint(eq(scalar(animalVars, animals.collect(legCalculator) as int[]), legCount[0]))
  s.read(m)

  def more = s.solve()
  while (more) {
      println "Found a solution:"
      animalVars.each {
          def v = s.getVar(it)
          if (v.val) println "  $v.val * $v.name"
      }
      more = s.nextSolution()
  }
}

@TypeChecked(extensions='NatureServeAnimalProvider.groovy')
def main() {
  animal types include SandhillCrane, GopherTortoise, ChihuahuanMillipede
  leg count is 1020
  head count is 8
  display solution
}

main()
