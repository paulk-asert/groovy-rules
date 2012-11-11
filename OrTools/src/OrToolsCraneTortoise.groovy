import com.google.ortools.constraintsolver.IntVar
import com.google.ortools.constraintsolver.Solver
import groovy.transform.CompileStatic

class OrToolsCraneTortoise {
  @CompileStatic
  static void main(args) {
    System.loadLibrary("jniconstraintsolver")
    solve()
  }

  static solve() {
    new Solver(getClass().name).with {
      def totalAnimals = 7
      def cranes = makeIntVar(0, totalAnimals, 'Cranes')
      def tortoises = makeIntVar(0, totalAnimals, 'Tortoises')
      addConstraint(makeEquality(makeSum(cranes, tortoises), totalAnimals))

      def totalLegs = 20
      def craneLegs = makeProd(cranes, 2)
      def tortoiseLegs = makeProd(tortoises, 4)
      addConstraint(makeEquality(makeSum(craneLegs, tortoiseLegs), totalLegs))

      IntVar[] animals = [cranes, tortoises]
      newSearch(makePhase(animals, CHOOSE_FIRST_UNBOUND, ASSIGN_MIN_VALUE))
      while (nextSolution()) {
        println animals
      }
      endSearch()

      // stats
      println """
        Solutions : ${solutions()}
        Failures  : ${failures()}
        Branches  : ${branches()}
        Wall time : ${wallTime()} ms
      """.stripIndent()
    }
  }
}
