//@GrabResolver('http://www.emn.fr/z-info/choco-repo/mvn/repository')
//@Grab('choco:choco-solver:2.1.5')
import static choco.Choco.*
import choco.cp.model.CPModel
import choco.cp.solver.CPSolver
import choco.kernel.model.variables.integer.IntegerVariable

def m = new CPModel()
def s = new CPSolver()

def menu = [
    'Mixed fruit'       : 215,
    'French fries'      : 275,
    'Side salad'        : 335,
    'Hot wings'         : 355,
    'Mozzarella sticks' : 420,
    'Sampler plate'     : 580
]
def numOrdered = new IntegerVariable[menu.size()]
def priceEach = new int[menu.size()]
def sum = 1505
menu.eachWithIndex { name, price, i ->
    // number ordered >= 0
    // number ordered * price <= sum
    numOrdered[i] = makeIntVar(name, 0, sum.intdiv(price))
    priceEach[i] = price
}
m.addConstraint(eq(scalar(numOrdered, priceEach), sum))
s.read(m)

def more = s.solve()
while (more) {
    println "Found a solution:"
    numOrdered.each {
        def v = s.getVar(it)
        if (v.val) println "  $v.val * $v.name"
    }
    more = s.nextSolution()
}
