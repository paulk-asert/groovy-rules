Prolog Examples
===============

Solves a logic puzzle using the following approaches:
 
* prolog directly (for comparative purposes)
* prolog underneath a Groovy DSL

The interesting thing to note is that the "business rules" are the same in all cases. The "DSL helper code" would typically be hidden from the user.

The [prolog4j](https://github.com/espakm/prolog4j) generic prolog interface api is used along with the [tuprolog](http://tuprolog.alice.unibo.it/) prolog engine but
you can try some of the other engines supported by prolog4j if you wish.
