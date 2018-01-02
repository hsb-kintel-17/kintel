# kintel
## TODO

- [ ] reale Bewertungsfunktion implementieren
- [ ] Promotion implementieren
- [ ] Eingabe von UML Koordinaten (bspw. e3d4) zu internen Koordinaten umwandeln (bspw. 5344)

## Problems
# Can't create copy of board
Since the board contains the fields which are not of primitive or boxable types it's not easy to create a copy for restoration later.
`Object#clone()` on the board creates a shallow copy but a deep copy is required in order to copy the whole state the board.
There are multiple ways to fix this:
- copy constructors
- own clone-like method that creates a deep copy
- defensive copying
- rebuild board to use primitives only  

Benefits: Putting the board on a stack before making a move and pop from the stack after a move. This would make undo logic of moves obsolete and might be faster at an expense of memory.
