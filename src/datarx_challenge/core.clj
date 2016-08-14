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

(defn board-index-from-coordinates
  "Converts the given coordinates into an index into the flattened grid. This
  function is only used once, but is left in for readability."
  [x y board-size]
  (+ (* board-size y) x))

(defn next-board-index
  "Calculates a new board index from a starting board index and a direction."
  [board-index direction board-size]
  (let [coords {:x (mod board-index board-size)
                :y (quot board-index board-size)}
        y (+ (coords :y) (direction :y))
        x (+ (coords :x) (direction :x))]
    ;(print (str "Input board-index " board-index " (" coords ") with direction " direction " and board-size " board-size))
    ;(println (str " becomes ouput coords " x "," y))
    (when (and (< -1 y board-size)
               (< -1 x board-size))
      (board-index-from-coordinates x y board-size))))

(defn build-word
  "Returns the word created by starting at 'board-index' and heading in the 
  'direction' direction for (count word) characters."
  [board-index direction word board]
  (let [board-size (int (Math/sqrt (count board)))]
    (loop [built-word (str (nth board board-index))
           bi board-index]
      ;(println (str built-word " " bi "...next index is " (next-board-index bi d board-size)))
      (if (or (= (count built-word) (count word))
              (= (next-board-index bi direction board-size) nil))
        built-word
        (recur (str built-word (nth board (next-board-index bi direction board-size)))
               (next-board-index bi direction board-size))))))

(defn search
  "Returns the number of times 'word' appears in the 'board'."
  [word board]
  (def occurrences 0)
  (loop [n 0]
    (let [board-letter (nth board n)]
      (if (= board-letter (nth word 0))
        (doseq [d directions]
          (let [built-word (build-word n d word board)]
            (if (= built-word word)
              (def occurrences (inc occurrences))))))
      (if (< n (- (count board) 1))
        (recur (inc n)))))
  occurrences)

(defn lines-are-equal?
  "True if every line is the same length. 'board' is a seq of strings."
  [board]
  (apply = (map count board)))

(defn -main
  [& args]
  (let [board (line-seq (java.io.BufferedReader. *in*))]
    (if-not (lines-are-equal? board)
      (println "ERROR: The rows of the grid must be the same length.  There may be as many rows as you wish, but each row must have the same number of characters.")
      (let [word (first *command-line-args*)
            flattened-board (seq (char-array (clojure.string/join "" board)))
            occurrences (search word flattened-board)]
        (println (str "The word '" word "' occurs " occurrences " time"
                      (if (= occurrences 1) "" "s") "."))))))
