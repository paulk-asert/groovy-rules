import com.google.ortools.constraintsolver.Solver
import groovy.transform.CompileStatic

class OrToolsMenu {
  @CompileStatic
  static void main(args) {
    System.loadLibrary("jniconstraintsolver")
    solve()
  }

  static solve() {
    def priceEach = [215, 275, 335, 355, 420, 580]
    int sum = 1505
    int maxNumOrdered = sum.intdiv(priceEach.min())
    new Solver("OrToolsMenu").with {
      // in cents so we can use ints
      def numOrdered = makeIntVarArray(priceEach.size(), 0, maxNumOrdered)
      addConstraint(makeEquality(makeScalProd(numOrdered, priceEach as int[]).var(), sum))
      newSearch(makePhase(numOrdered, CHOOSE_FIRST_UNBOUND, ASSIGN_MIN_VALUE))
      while (nextSolution()) {
        println "numOrdered: ${numOrdered*.value()}"
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