groovy-rules
============

Source code examples for this talk:

http://www.slideshare.net/paulk_asert/groovy-rules

Solves various logic puzzles using a variety of approaches and tools.
Some of the examples use various DSL techniques to provide a friendly
readable layer above the underlying tool/library api.

Einstein's Riddle
-----------------

Einstein's Riddle (a Zebra puzzle variant) is solved using:

* prolog directly (for comparative purposes)
* prolog underneath a Groovy DSL
* the __choco__ constraint solving library beneath a Groovy DSL
* the __jacop__ constraint solving library beneath a Groovy DSL

The interesting thing to note is that the "business rules" are the same for each DSL solution.
The "DSL helper code" would typically be hidden from the user.

The [prolog4j](https://github.com/espakm/prolog4j) generic prolog interface api is used along with the [tuprolog](http://tuprolog.alice.unibo.it/) prolog engine but
you can try some of the other engines supported by prolog4j if you wish.

The [choco][1] and [JaCoP][2] constraint solving libraries are used.
These libraries offer similar features as far as this problem is concerned. Given that the JaCoP package isn't available in a public
Maven repository and has a restrictive GPL license, we have a preference for Choco for this example; but see the respective
documentation of the two packages to see which better suits your needs. Follow the instructions in the JaCoP directory for downloading
the needed jars if you want to try it. The other examples should run automatically.

Menu
----

Inspired by this xkcd webcomic:
![NP-Complete Restaurant Orders](http://xkcd.com/287/)

Tortoises and Cranes
--------------------

Solved using [choco][1] natively, Drools Expert natively and via a DSL and via the JSR331 api using [choco][1], Container, JSetL

Tools/Libraries
---------------

[1]: http://www.emn.fr/z-info/choco-solver/ "Choco"
[2]: http://jacop.osolpro.com/ "JaCoP"
