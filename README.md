groovy-rules
============

Source code examples for this talk:

http://www.slideshare.net/paulk_asert/groovy-rules

Solves various logic puzzles using a variety of approaches and tools.
Some of the examples use various DSL techniques to provide a friendly
readable layer above the underlying tool/library api.

Tortoises and Cranes
--------------------

> There are some cranes and tortoises.
> They are 7 in total, and their legs are 20 in total.
> How many cranes and tortoises are there?

This is a basic "hello world" puzzle. It is solved using:

* [Choco][1] using the native api
* [Choco][1] using the JSR-331 api
* [Drools Expert][3] natively
* [Drools Expert][3] beneath a Groovy DSL
* [JSetL][4] using the JSR-331 api
* [Constrainer Light][5] using the JSR-331 api
* [Constrainer Light][5] with special JSR-331 Groovy syntax
* [OrTools][6] constraint solving library

Menu Puzzle
-----------

This puzzle is inspired by this [xkcd webcomic](http://xkcd.com/287/):

<img src="http://imgs.xkcd.com/comics/np_complete.png" width="50%" height="50%">

It is solved using:

* the [Choco][1] constraint solving library
* the [OrTools][6] constraint solving library

Einstein's Riddle
-----------------

Einstein's Riddle (a [Zebra Puzzle](http://en.wikipedia.org/wiki/Zebra_puzzle) variant).
This version is modelled on the one from this [book of the same name](http://www.amazon.co.uk/Einsteins-Riddle-Riddles-Puzzles-Conundrums/dp/1408801493).

> __Background__
>
> There are five houses painted five different colors.
> A person with a different nationality lives in each house.
> The five house owners each drink a different beverage, play a
> different sport, and keep a different pet. Who owns the fish?
>
> __The Rules__
>
> * The man from the center house drinks milk
> * The Norwegian owns the first house
> * The Dane drinks tea
> * The German plays hockey
> * The Swede keeps dogs
> * The Briton has a red house
> * The owner of the green house drinks coffee
> * The owner of the yellow house plays baseball
> * The person known to play football rears birds
> * The man known to play tennis drinks beer
> * The green house is on the left side of the white house
> * The man known to play volleyball lives next to the one who keeps cats
> * The man known to keep horses lives next to the man who plays baseball
> * The man known to play volleyball lives next to the one who drinks water
> * The Norwegian lives next to the blue house

It is solved using:

* prolog directly (for comparative purposes)
* prolog underneath a Groovy DSL
* the [Choco][1] constraint solving library beneath a Groovy DSL
* the [JaCoP][2] constraint solving library beneath a Groovy DSL

The interesting thing to note is that the "business rules" are the same for each DSL solution.
The "DSL helper code" would typically be hidden from the user.

The [prolog4j](https://github.com/espakm/prolog4j) generic prolog interface api is used along with the [tuprolog](http://tuprolog.alice.unibo.it/) prolog engine but
you can try some of the other engines supported by prolog4j if you wish.

[Choco][1] and [JaCoP][2] offer similar features as far as this problem is concerned. Given that the JaCoP package isn't available in a public
Maven repository and has a restrictive GPL license, we have a preference for Choco for this example; but see the respective
documentation of the two packages to see which better suits your needs. Follow the instructions in the JaCoP directory for downloading
the needed jars if you want to try it. The other examples should run automatically.

Further Information
-------------------

* http://openrules.com/jsr331/index.htm
* http://cpstandard.wordpress.com/
* http://jcp.org/en/jsr/detail?id=331
* http://groovy.codehaus.org
* http://www.slideshare.net/glaforge/groovy-domain-specific-languages-springone2gx-2012

[1]: http://www.emn.fr/z-info/choco-solver/ "Choco"
[2]: http://jacop.osolpro.com/ "JaCoP"
[3]: http://www.jboss.org/drools/drools-expert "Drools Expert"
[4]: http://cmt.math.unipr.it/jsetl.html "JSetL"
[5]: http://openrules.com/jsr331/index.htm "Constrainer Light"
[6]: https://code.google.com/p/or-tools/ "OrTools"
