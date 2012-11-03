import javax.constraints.groovy.ProblemGroovy

//requires jsr331 jars from http://openrules.com/jsr331/downloads.htm

new ProblemGroovy("Animals").with {
  def totalAnimals = 7
  def totalLegs = 20
  def c = variable('Cranes', 0, totalAnimals)
  def t = variable('Tortoises', 0, totalAnimals)
  post(c + t, '=', totalAnimals)
  post(2 * c + 4 * t, '=', totalLegs)
  def s = solver.findSolution()
  if (s) { s.log(); println "Cranes ${s['Cranes']}, Tortoises ${s['Tortoises']}" }
  else println "No Solutions"
}
