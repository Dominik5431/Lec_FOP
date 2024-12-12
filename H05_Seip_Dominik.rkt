;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-advanced-reader.ss" "lang")((modname H05_Seip_Dominik) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #t)))
; FOP-Hausübung H5
; Bitte denken Sie daran, die Datei vor der Abgabe entsprechend der Namenskonvention umzubenennen!
; Zuweisungen mittels set!, let, begin usw. sind in keiner Racket-Hausübung erlaubt.
; Ebenso darf dynamische Codeausführung, bspw. per eval, in keiner Racket-Hausübung verwendet werden.
; Funktionen höherer Ordnung sind in dieser Hausübung nicht erlaubt.

;
; Structs
;
(define-struct permutation (input output))

;
; H1
;
;; Type: (list of number) number -> (list of number)
;; Returns: Liste ohne das erste Vorkommen des übergebenen Elements
(define (lst-without-first-occurrence-of lst element)
  (if (empty? lst)
      '()
      (if (equal? element(car lst))
          (cdr lst)
          (cons (car lst)(lst-without-first-occurrence-of (cdr lst) element)))
      )
  )
;; Tests:
(check-expect (lst-without-first-occurrence-of (list 1 2 3 4 3) 3) (list 1 2 4 3))
(check-expect (lst-without-first-occurrence-of (list ) 3) (list ))
(check-expect (lst-without-first-occurrence-of (list 1 1 1) 3) (list 1 1 1))


;; Type: (list of number) (list of number) -> boolean
;; Returns: Überprüft ob die übergebenen Listen eine Permutation darstellen
(define (is-permutation? input output)
  (cond
    [(not (equal? (length input) (length output))) #f]
    [(equal? input '()) #t]
    [(is-permutation?  (lst-without-first-occurrence-of input (first output)) (lst-without-first-occurrence-of output (first output))) #t]
    [else #f])
  )
;; Tests:
(check-expect (is-permutation? (list 1 2 3)(list 3 2 1)) #t)
(check-expect (is-permutation? (list 1 2 3)(list 1 2 4)) #f)
(check-expect (is-permutation? (list 1 2 3)(list 3 1)) #f)


;
; H2
;

;; Type: (list of number) -> (list of (list of number))
;; Returns: Gibt alle Permutationen einer Menge aus.
(define (all-permutations lst)
  (cond
    [(empty? lst) (list empty)]
    [(empty? (rest lst)) (list lst)]
    [else (all-permutations-helper lst empty)]
  )
)
;; Tests:
(check-expect (all-permutations empty) (list empty))
(check-expect (all-permutations (list 1 2 3)) (list (list 1 2 3)
                                                    (list 1 3 2)
                                                    (list 2 1 3)
                                                    (list 2 3 1)
                                                    (list 3 1 2)
                                                    (list 3 2 1)
                                               )
)
(check-expect (all-permutations (list 2)) (list (list 2)))

;; Type: (list of number) (list of number) -> (list of number)
;; Returns: Gibt alle Permutationen der Menge lst aus.
(define (all-permutations-helper lst done)
  (cond
    [(empty? lst) empty]
    [else
     (append
      (list-builder
       (first lst)
       (all-permutations (append done (rest lst))))
      (all-permutations-helper
       (rest lst)
       (append done (list (first lst)))
       
       )
      )
     ]
    )
)
;; Tests:
(check-expect (all-permutations-helper empty empty) empty)
(check-expect (all-permutations-helper (list 1 2 3) empty) (list (list 1 2 3)
                                                    (list 1 3 2)
                                                    (list 2 1 3)
                                                    (list 2 3 1)
                                                    (list 3 1 2)
                                                    (list 3 2 1)
                                               )
)
(check-expect (all-permutations-helper (list 2) empty) (list (list 2)))


;; Type number (list of (list of number)) -> (list of (list of number))
;; Returns: Hängt an alle Listen einer Liste das übergebene Element vorne dran.
(define (list-builder item lstlst)
  (cond
    [(empty? lstlst) empty]
    [else
     (cons
      (cons item (first lstlst))
      (list-builder item (rest lstlst))
      )
     ]
    )
)
;; Tests:
(check-expect (list-builder 3 (list (list 1 2)(list 2 3))) (list (list 3 1 2)(list 3 2 3)))
(check-expect (list-builder 2 (list empty)) (list (list 2)))
(check-expect (list-builder 4 (list (list 3))) (list (list 4 3)))



;
; H3
;

;; Type: permutation number -> number
;; Precondition: Um valide Ausgabe zu bekommen, muss das Element in der Menge sein, über die die Permutation definiert ist.
;; Returns: Berechnet Funktionswert einer gegebenen FUnktion für gegebene Zahl
(define (two-line-lookup p elem)
(cond
 [(equal? (permutation-output p) empty) null]
 [(equal? (first (permutation-input p)) elem) (first(permutation-output p))]
 [else
   (two-line-lookup
        (make-permutation
         (rest (permutation-input p))
         (rest (permutation-output p))
         )
        elem
    )
  ]
 )
 )
;; Tests:
(check-expect (two-line-lookup (make-permutation
                                (list 1 2 3)
                                (list 1 3 2)) 2) 3)
(check-expect (two-line-lookup (make-permutation
                                (list 1 2 3)
                                (list 1 3 2)) 4) null)
(check-expect (two-line-lookup (make-permutation
                                empty
                                empty) 1) null)



(define p1 (make-permutation (list 2 4 6 8) (list 6 8 2 4)))
(define p2 (make-permutation (list 2 4 6 8) (list 2 6 4 8)))
(define p3 (make-permutation (list 2 4 6 8) (list 8 2 4 6)))
(define p4 (make-permutation (list 1 2 3) (list 2 3 1)))
(define p5 (make-permutation (list 1 2 3) (list 2 1 3)))
(define p6 (make-permutation (list 1 2 3) (list 3 1 2)))



               
;; Type: permutation (list of number) (list of number) -> (list of number)
;; Precondition: Um eine valide Ausgabe zu bekommen, müssen die Permutationen über der gleichen Menge definiert sein.
;; Returns: Ergibt Ausgabe von zwei Permutationen, die verknüpft wurden.
(define (compose-two-permutations-helper p lst done)
  (cond
    [(empty? lst) done]
    [else
     (compose-two-permutations-helper
      p
      (rest lst)
      (append
       done
       (list (two-line-lookup p (first lst)))
       )
     )
     ]
  )
)
;; Tests:
(check-expect (compose-two-permutations-helper p1 (list 2 4 6 8) empty) (list 6 8 2 4))
(check-expect (compose-two-permutations-helper (make-permutation (list 1 2 3)(list 2 3 1)) (list 1 2 4) empty) (list 2 3 empty))
(check-expect (compose-two-permutations-helper (make-permutation empty empty) empty empty) empty)


;; Type: permutation permutation -> permutation
;; Precondition: Um nicht eine leere Permutation zu erhalten, muss die Komposition definiert sein.
;; Returns: Verknüpfung zweier Kompositionen
(define (compose-two-permutations p1 p2)
  (cond
    [(is-permutation? (permutation-output p1) (permutation-input p2))
     (make-permutation
      (permutation-input p1)
      (compose-two-permutations-helper p2 (permutation-output p1) empty))
    ]
    [else (make-permutation empty empty)]
   )
 )
;; Tests:  
(check-expect (compose-two-permutations p1 p2) (make-permutation (list 2 4 6 8)(list 4 8 2 6)))
(check-expect (compose-two-permutations (make-permutation (list 1 2 3)(list 2 3 1)) (make-permutation (list 1 2 4) (list 2 1 4))) (make-permutation empty empty))
(check-expect (compose-two-permutations (make-permutation empty empty) (make-permutation empty empty)) (make-permutation empty empty))


;; Type: (list of permutation) -> (list of permutation)
;; Precondition: Die Komposition der Permutationen muss definiert sein.
;; Returns: Komposition der Permutationen als Listenelement
(define (compose-permutations-helper permutations)
  (cond
    [(equal? (length permutations) 1) permutations]
    [else
     (compose-permutations-helper
      (append
       (list (compose-two-permutations
        (first permutations)
        (first (rest permutations))
        ))
       (rest (rest permutations))
       )
      )
    ]
  )
)
;;Tests:
(check-expect
 (compose-permutations-helper
  (list p3 p2 p1)
  )
 (list (make-permutation (list 2 4 6 8) (list 4 6 2 8)))
 )
(check-expect
  (compose-permutations-helper
   (list p6 p5 p4)
   )
  (list (make-permutation (list 1 2 3)(list 1 3 2)))
  )
(check-expect
 (compose-permutations-helper
  (list (make-permutation empty empty) (make-permutation empty empty) (make-permutation empty empty))
  )
 (list (make-permutation empty empty))
 )


;; Type: (list of permutation) -> permutation
;; Precondition: Die Komposition der Permutationen muss definiert sein.
;; Returns: Komposition der Permutationen
(define (compose-permutations permutations)
  (first (compose-permutations-helper (reverse permutations)))
)
;; Tests:
(check-expect
 (compose-permutations
  (list p1 p2 p3)
  )
 (make-permutation (list 2 4 6 8) (list 4 6 2 8))
 )
(check-expect
  (compose-permutations
   (list p4 p5 p6)
   )
  (make-permutation (list 1 2 3)(list 1 3 2))
  )
(check-expect
 (compose-permutations
  (list (make-permutation empty empty) (make-permutation empty empty) (make-permutation empty empty))
  )
 (make-permutation empty empty)
 )
  

;
; H4
;

;; Type: (list of number) (list of number) -> (list of (list of number))
;; Returns: Zerlegt eine Permutation in Zyklenschreibweise
(define (decompose-permutation-into-cycles input output)
  empty)
;; Tests:



;
; H5
;

;; Type: (list of number) string -> string
;; Returns: Schreibt eine Permutation in einen String
(define (cycle-to-string lst output)
  (cond
    [(and
      (empty? lst)
      (equal? (string-length output) 0))
     "()"
     ]
    [(empty? lst)
     (string-append output ")")
    ]
    [(equal? (string-length output) 0)
     (cycle-to-string lst "(")
    ]
    [else (cycle-to-string
           (rest lst)
           (string-append output (number->string (first lst)))
           )
    ]
                      
   )
 )
;; Tests
(check-expect (cycle-to-string (list 1 2 3) "") "(123)")
(check-expect (cycle-to-string empty "(12") "(12)")
(check-expect (cycle-to-string (list 2 9) "(") "(29)")

;; Type: (list of (list of number)) -> string
;; Returns: Schreibt alle Zyklen in einen String hintereinander.
(define (cycles-to-string lstlst)
  (cond
    [(equal? lstlst empty) ""]
    [else (string-append
           (cycle-to-string (first lstlst) "")
           (cycles-to-string (rest lstlst))
           )
    ]
   )
  )
;;Tests:
(check-expect (cycles-to-string (list (list 1 3) (list 2 4))) "(13)(24)")
(check-expect (cycles-to-string empty) "")
(check-expect (cycles-to-string (list (list 1) (list 1 2 3) (list 1))) "(1)(123)(1)")

;; Type: (list of number) (list of number) -> string
;; Returns: Gibt alle Zyklen einer Permutation in einem String aus.
(define (two-line-to-cycle-notation input output)
  (cycles-to-string (decompose-permutation-into-cycles input output))
)
;; Tests:
;(check-expect (two-line-to-cycle-notation (list 1 2 3)(list 3 1 2)) "(13)(2)")
;(check-expect (two-line-to-cycle-notation empty empty) "")
;(check-expect (two-line-to-cycle-notation (list 1 2 3 4)(list 4 3 2 1)) "(14)(23)")
; -> H4 nicht implementiert, daher würden diese Tests fehlschlagen
