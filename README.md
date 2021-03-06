# datarx-challenge

See [Word Search Programming Problem.pdf](https://bitbucket.org/jasonmm/datarx-challenge-clj/raw/91c8697b900ca3c3c61fa65cb9fd09192d07a634/Word%20Search%20Programming%20Problem.pdf).

## Requirements

* [Leiningen](http://leiningen.org/)

## Usage

```bash
$ git clone https://bitbucket.org/jasonmm/datarx-challenge-clj
$ cd datarx-challenge-cl
$ cat grid.txt | lein run bread
```

## Examples

```bash
$ cat grid.txt | lein run bread
The word 'bread' occurs 5 times.

$ cat grid.txt | lein run food
The word 'food' occurs 0 times.

$ cat grid-not-equal.txt | lein run bread
ERROR: The rows of the grid must be the same length.  There may be as many rows as you wish, but each row must have the same number of characters.
```

## Notes

To create the `grid-big.txt` test file the following line of Clojure is used
in the REPL and then `grid.txt` is pasted at the top.

```clojure
(spit "grid-big.txt" (clojure.string/join "\n" (map #(clojure.string/join "" %) (partition 500 (for [i (range 100000)] (nth alphabet (rand-int 26)))))))
```

## License

Copyright © 2016

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
