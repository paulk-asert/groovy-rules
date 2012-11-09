import com.google.ortools.constraintsolver.IntVar
import com.google.ortools.constraintsolver.Solver
import groovy.transform.CompileStatic

@CompileStatic
class OrToolsMenu {
  static { System.loadLibrary("jniconstraintsolver") }

  static void main(args) {
    def solver = new Solver("OrToolsMenu")
    // in cents so we can use ints
    int[] priceEach = [215, 275, 335, 355, 420, 580]
    int sum = 1505
    def numOrdered = solver.makeIntVarArray(priceEach.size(), 0, sum.intdiv(priceEach.min()))
    solver.addConstraint(solver.makeEquality(solver.makeScalProd(numOrdered, priceEach).var(), sum))
    def db = solver.makePhase(numOrdered, solver.CHOOSE_FIRST_UNBOUND, solver.ASSIGN_MIN_VALUE)
    solver.newSearch(db)
    while (solver.nextSolution()) {
      def vals = numOrdered.collect { IntVar v -> v.value() }
      println "numOrdered: ${vals.join(' ')}"
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