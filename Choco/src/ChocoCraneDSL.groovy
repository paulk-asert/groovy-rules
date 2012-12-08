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
  List<Animal> animals
  TypesHolder(animals) { this.animals = animals }
  def include(Animal... animals) { this.animals.addAll(animals) }
}

enum Animal {
  Crane(2), Tortoise(4), Centipede(100), Millipede(1000)
  int legs
  Animal(int legs) {
    this.legs = legs
  }
}
import static Animal.*

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
  def m = new CPModel()
  def s = new CPSolver()
  IntegerVariable[] animalVars = animals.collect{ makeIntVar(it.toString(), 0, legCount[0].intdiv(it.legs)) }
  m.addConstraint(eq(scalar(animalVars, ([1] * animals.size()) as int[]), headCount[0]))
  m.addConstraint(eq(scalar(animalVars, animals.collect{ it.legs } as int[]), legCount[0]))
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

@TypeChecked
def main() {
  animal types include Crane, Tortoise, Millipede
  leg count is 1020
  head count is 8
  display solution
}

main()
