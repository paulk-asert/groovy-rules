package sugar

import jp.ac.kobe_u.cs.sugar.csp.IntegerVariable
import jp.ac.kobe_u.cs.sugar.csp.IntegerDomain
import jp.ac.kobe_u.cs.sugar.csp.CSP
import jp.ac.kobe_u.cs.sugar.csp.Clause
import jp.ac.kobe_u.cs.sugar.csp.LinearSum
import jp.ac.kobe_u.cs.sugar.csp.Literal
import jp.ac.kobe_u.cs.sugar.csp.LinearLiteral

// There are some cranes and tortoises.
// They are 7 in total, and their legs are 20 in total.
// How many cranes and tortoises are there?

def csp = new CSP()

def zeroToSeven = new IntegerDomain(0, 7)
def numCranes = new IntegerVariable('numCranes', zeroToSeven)
def numTortoises = new IntegerVariable('numTortoises', zeroToSeven)
csp.add(numCranes)
csp.add(numTortoises)
def numAnimals = new LinearSum(numCranes)
numAnimals.add(new LinearSum(numTortoises))
def animalsClause = new Clause(new LinearLiteral(numAnimals))
csp.add(animalsClause)
def numFeet = new LinearSum(2, numCranes, 0)
numFeet.add(new LinearSum(4, numTortoises, 0))
def feetClause = new Clause(new LinearLiteral(numFeet))
csp.add(feetClause)

//def solver = new Solver(csp)
//def solution = solver.findFirst()

def solution = null
//// I think the scala version cheats as per below - at least when
//// I try this it also prints a bunch of internal working variables
// println solution
if (solution)
    println "numCranes = ${solution.getIntValue(numCranes)}, " +
            "numTortoises = ${solution.getIntValue(numTortoises)}"
