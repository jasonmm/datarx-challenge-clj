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
  [board-index direction word board line-length]
;;  (println (str "building word at " board-index " " direction))
  (let [n (count word)]
;;    (let [x (map #(next-board-index 
;;                    board-index 
;;                    (take n (iterate (fn [r] (merge-with + direction r)) {:x 0 :y 0})) line-length))] 
;;      (println x))
    (let [arr
          (map board
          (take-while some?
                      (map #(next-board-index board-index % line-length)
                           (take n
                                 (iterate #(merge-with + direction %) {:x 0 :y 0}))))
          )
]
      (clojure.string/join arr)))
;;    (->> (iterate #(merge-with + direction %) {:x 0 :y 0})
;;         (take n)
;;         (map #(next-board-index board-index % line-length))
;;         (take-while some?)
;;         (map board)
;;         (clojure.string/join))))
)

(defn search
  "Returns the number of times 'word' appears in the 'board'. 'board' is a 
  vector of characters. 'line-length' the length of each line in the board."
  [word board line-length]
  (->> (for [idx (range 0 (count board))
             dir directions]
         (build-word idx dir word board line-length))
       (filter #(= word %))
       (count)))

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
            line-length (count (nth board 0))
            flattened-board (vec (clojure.string/join "" board))
            occurrences (search word flattened-board line-length)]
        (println (str "The word '" word "' occurs " occurrences " time"
                      (if (= occurrences 1) "" "s") "."))))))
