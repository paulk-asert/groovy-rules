import com.google.ortools.constraintsolver.IntVar
import com.google.ortools.constraintsolver.Solver
import groovy.transform.CompileStatic

@CompileStatic
class OrToolsCraneTortoise {
  static { System.loadLibrary("jniconstraintsolver") }

  static void main(args) {
    def solver = new Solver("OrToolsCraneTortoise")
    def totalAnimals = 7
    def totalLegs = 20
    IntVar cranes = solver.makeIntVar(0, totalAnimals, 'Cranes')
    IntVar tortoises = solver.makeIntVar(0, totalAnimals, 'Tortoises')
    solver.addConstraint(solver.makeEquality(solver.makeSum(cranes, tortoises), totalAnimals))
    def craneLegs = solver.makeProd(cranes, 2)
    def tortoiseLegs = solver.makeProd(tortoises, 4)
    solver.addConstraint(solver.makeEquality(solver.makeSum(cranes, tortoises), totalAnimals))
    solver.addConstraint(solver.makeEquality(solver.makeSum(craneLegs, tortoiseLegs), totalLegs))
    IntVar[] animals = [cranes, tortoises]
    def db = solver.makePhase(animals, solver.CHOOSE_FIRST_UNBOUND, solver.ASSIGN_MIN_VALUE)
    solver.newSearch(db)
    while (solver.nextSolution()) {
      println animals
    }
    solver.endSearch()

    // stats
    println """
      Solutions : ${solver.solutions()}
      Failures  : ${solver.failures()}
      Branches  : ${solver.branches()}
      Wall time : ${solver.wallTime()} ms
    """.stripIndent()
  }
}