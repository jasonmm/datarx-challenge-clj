(ns datarx-challenge.core
  (:gen-class))

; go - start listening channel that receives built words
;  if built-word == word
;   add to occurrences
; loop over board letters
;  if board-letter == word[0]
;   loop over directions
;    go - build word from board-letter in the direction for word-len letters and write built word to the channel


(def directions
  [{:x 1 :y 0}
   {:x 1 :y 1}
   {:x 0 :y 1}
   {:x -1 :y 1}
   {:x -1 :y 0}
   {:x -1 :y -1}
   {:x 0 :y -1}
   {:x 1 :y -1}])

(defn coordinates-from-board-index
  "Converts a given board index into x/y coordinates (e.g. {:x 3 :y 2})."
  [board-index board-size]
  (def y (int (/ board-index board-size)))
  (def x (int (mod board-index board-size)))
  {:x x :y y})

(defn board-index-from-coordinates
  "Converts the given coordinates into an index into the flattened grid."
  [x y board-size]
  (+ (* board-size y) x))

(defn next-board-index
  "Calculates a new board index from a starting board index and a direction."
  [board-index direction board-size]
  (def coords (coordinates-from-board-index board-index board-size))
  ;(print (str "Input board-index " board-index " (" coords ") with direction " direction " and board-size " board-size))
  (def y (+ (coords :y) (direction :y)))
  (def x (+ (coords :x) (direction :x)))
  ;(println (str " becomes ouput coords " x "," y))
  (if (or (>= y board-size) (>= x board-size) (< y 0) (< x 0))
    nil
    (board-index-from-coordinates x y board-size)))

(defn build-word
  "Returns the word created by starting at 'board-index' and heading in the 
  'direction' direction for (count word) characters."
  [board-index direction word board]
  (def board-size (Math/floor (Math/sqrt (count board))))
  (loop [built-word (str (nth board board-index)) bi board-index d direction w word b board]
    ;(println (str built-word " " bi "...next index is " (next-board-index bi d board-size)))
    (if (or (= (count built-word) (count word)) (= (next-board-index bi d board-size) nil))
      built-word
      (recur (str built-word (nth board (next-board-index bi d board-size)))
             (next-board-index bi d board-size)
             d
             w
             b))))

(defn search
  "Returns the number of times 'word' appears in the 'board'."
  [word board]
  (def occurrences 0)
  (loop [n 0]
    (def board-letter (nth board n))
    (if (= board-letter (nth word 0))
      (doseq [d directions]
        (let [built-word (build-word n d word board)]
          (if (= built-word word)
            (def occurrences (inc occurrences))))))
    (if (< n (- (count board) 1))
      (recur (inc n))))
  occurrences)

(defn -main
  [& args]
  (let [board (clojure.string/join ""
                                   (line-seq (java.io.BufferedReader. *in*)))]
    (def word (first *command-line-args*))
    (def flattened-board (seq (char-array board)))
    (def occurrences (search word flattened-board))
    (println (str "The word '" word "' occurs " occurrences " time"
                  (if (= occurrences 1) "" "s") "."))))
