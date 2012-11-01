//requires jsr331 jars from http://openrules.com/jsr331/downloads.htm
//and jsetl jars from http://cmt.math.unipr.it/jsetl.html

import javax.constraints.Var
import static javax.constraints.ProblemFactory.newProblem

def animalNames = ['Cranes', 'Tortoises']
newProblem("Cranes").with {
  def totalAnimals = 7
  def totalLegs = 20
  int[] numCoef = [1, 1]
  int[] legCoef = [2, 4]
  Var[] animals = animalNames.collect{ variable(it, 0, totalAnimals) }
  post(numCoef, animals, '=', totalAnimals)
  post(legCoef, animals, '=', totalLegs)
  def s = solver.findSolution()
  if (s) animalNames.each{ println "$it ${s[it]}" }
  else println "No Solutions"
}
