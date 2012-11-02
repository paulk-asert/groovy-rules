//@GrabResolver('http://www.emn.fr/z-info/choco-repo/mvn/repository')
//@Grab('choco:choco-solver:2.1.5')
import static choco.Choco.*
import choco.cp.model.CPModel
import choco.cp.solver.CPSolver
//import choco.kernel.model.variables.integer.IntegerVariable

def m = new CPModel()
def s = new CPSolver()

def totalAnimals = 7
def totalLegs = 20
def c = makeIntVar('Cranes', 0, totalAnimals)
def t = makeIntVar('Tortoises', 0, totalAnimals)
m.addConstraint(eq(plus(c, t), totalAnimals))
m.addConstraint(eq(plus(mult(c, 2), mult(t, 4)), totalLegs))
//variant to above
//m.addConstraint(eq(scalar([c, t] as IntegerVariable[], [2, 4] as int[]), totalLegs))
s.read(m)

def more = s.solve()
while (more) {
    println "Found a solution:"
    [c, t].each {
        def v = s.getVar(it)
        if (v.val) println "  $v.val * $v.name"
    }
    more = s.nextSolution()
}
