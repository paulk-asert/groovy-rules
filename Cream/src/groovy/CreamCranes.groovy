import jp.ac.kobe_u.cs.cream.*

def net = new Network()
def cranes = new IntVariable(net)
def tortoises = new IntVariable(net)

cranes.ge(0)
tortoises.ge(0)
cranes.add(tortoises).equals(7)
(cranes * 2).add(tortoises * 4).equals(20)

new DefaultSolver(net).findFirst().with {
  println "Cranes = ${getIntValue(cranes)}"
  println "Tortoises = ${getIntValue(tortoises)}"
}