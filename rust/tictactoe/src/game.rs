
use std::option;

mod game
{
    // the player X/O defines
    static PlayerX: i32 = 1;
    static PlayerO: i32 = 2;

    struct Game
    {
      humanPlayer: i32,
      multiPlayer: Option<Multiplayer>,
      board: Board,
    }

    impl Game
    {
    // need a board to start a isGameOver
    pub fn new(board: Board) -> Game {
      self.board = board;
      self.humanPlayer = 0;
    }

    fn isGameOver() -> bool {
        //Game is over is someone has won, or board is full (draw)
        return (board.hasWon(player: PlayerX) || board.hasWon(player: PlayerO) || board.availablePoints().count == 0);
    }

    pub fn isMultiplayer() -> bool {
      return multiPlayer != nil;
    }

    // reads input and determines human player (X or O)
    pub fn setHumanPlayerFromInput() -> bool {
        let choice = readPlayer();

        match choice {
          Some(x) => self.humanPlayer = x,
          None => return false
        }

        // do the computers first move
        if (self.humanPlayer == Game.PlayerO) {
            let p = Point(x: rand::random() % 3, y: rand::random() % 3);

       	    if(!b.placeMove(point: p, player: computerPlayer())) {
      		      return false;
      	    }
        }

        return true;
    }

     fn printError(value: S, newline: bool) where S: Into<String> {
      if (!newline) {
        print!("\033[A\r\033[2K{}", value.into());
      } else {
        println!("\u{1B}[A\r\u{1B}[2K{}", value.into());
      }
    }

  pub fn setMultiPlayerFromInput() -> bool {
        let choice = readYesNo(); 
        let mut addr: Optional<String>;

        match choice {
          Some(x) => if (!x) { 
            return false;
          },
          None => return false
        }

        if (humanPlayer == Game.PlayerO) {

          loop {
            print!("What network address? ");

            let addr = readNetworkAddr();

            match addr {
              None => { printError(value: "Invalid network address.  ", newline: false); },
              Some(x) => {
                self.multiPlayer = try Multiplayer(game: self);

                if (!self.multiPlayer!.connect(addr: x)) {
                  printError(value: "unable to connect to network", newline: true);
                  return false;
                }

                break;
              }
            }
          } 
        }
        return true;
    }

    // reads a point from input and makes a move if available
    pub fn placeHumanMoveFromInput() -> bool {
        print!("Your move: ");

        let userMove = readPoint();

        // will use VT100 to replace the last line for error messages

        match userMove {
        None => {
            printError(value: "Invalid point.  Try 'x-y' or 'x y'.  ", newline: false);
            return false;
        },

        Some(x) => {
          if (!b.placeMove(point: x, player: self.humanPlayer) ) { //2 for O and O is the user
              printError(value: "That move is already taken.  ", newline: false);
              return false;
          }

          if (isMultiplayer) {
            if (!multiPlayer!.send(value: x.description)) {
              printError(value: "That move is already taken.  ", newline: false);
              return false;
            }
          }
        }
    }

        return true;
    }

    pub fn placeMultiPlayerMove() -> bool {

        if (!isMultiplayer) {
          return false;
        }

        let userMove = readPoint(source: multiPlayer);

        // will use VT100 to replace the last line for error messages

        if (userMove == nil) {
            printError(value: "Invalid response from network player");
            return false;
        }

        if (!b.placeMove(point: userMove!, player: self.humanPlayer) ) { //2 for O and O is the user
            printError(value: "Invalid move from network player");
            return false;
        }

        return true;
    }

    // calculate a computer move and place
    pub fn placeComputerMove() -> bool {

        if (isGameOver) {
          return false;
        }

        // get a list of available moves
        let pointsAvailable = board.availablePoints();

        // no more moves?
        if (pointsAvailable.count == 0) {
            return false;
        }

        let mut move: Optional<Point>;
        let mut imin = 2;
	      let alpha = 2;
	      let beta = -2;

        // find the best move
        for point in pointsAvailable {

            if(!board.placeMove(point: point, player: self.computerPlayer)) {
                continue;
            }

            let val = minmax(depth: 0, turn: self.computerPlayer, alpha: alpha, beta: beta);

            board[point.x, point.y] = 0;

            if (imin > val) {
                imin = val;
                move = point;
            }

            if (val == -1) {
                break;
            }
        }

        if (move != nil) {
            return board.placeMove(point: move!, player: self.computerPlayer);
        }

        return false;
    }


    // display the board to output
    pub fn displayBoard() {

        // dislay the y coords
        print!("\033[0m  y 1   2   3");

        // draw the top of the board
        printLine(length: 13, leftCorner: "x \033[1;37m┌", rightCorner: "┐", interval: "┬");


        for i in 0...board.size-1 {
            // draw each line
            print!("\033[0m{} ", i);

            for j in 0...board.size-1 {

                print!("\033[1;37m\\u2502\033[1;33m");

                switch(board[i, j]) {
                    case Game.PlayerO:
                        print!(" ◯ ");
                        break;
                    case Game.PlayerX:
                        print!(" ╳ ");
                        break;
                    default:
                        print!("   ");
                        break;
                }
            }
            print!("\033[1;37m│");

            if ((i + 1) != board.size) {
                printLine(length: 13, leftCorner: "  \033[1;37m├", rightCorner: "┤", interval: "┼");
            }
        }

        // draw the bottom border
        printLine(length: 13, leftCorner: "  \033[1;37m└", rightCorner: "┘\033[0m", interval: "┴");
    }

    // display the game status
    pub fn displayWinner() {
        if (board.hasWon(player: self.humanPlayer)) {
            println!("You win!"); //Can't happen
        } else if (board.hasWon(player: self.computerPlayer)) {
            println!("Unfortunately, you lost!");
        } else {
            println!("It's a draw!");
        }
    }

    // get the opposing piece (X or O)
    pub fn computerPlayer() -> i32 {
      return self.humanPlayer == Game.PlayerX ? Game.PlayerO : Game.PlayerX
    }

    pub fn currentPlayer() -> i32 {
      return self.humanPlayer
    }

    // the minmax algorithm to recursively determine the best move
    fn minmax( depth: i32, turn: i32, alpha: i32, beta: i32) -> i32 {

	     let mut alphaVal = alpha;
	     let mut betaVal = beta;

        if (board.hasWon(player: self.humanPlayer)) {
            return 1;
        }

        if (board.hasWon(player: self.computerPlayer)) {
            return -1;
        }

        let pointsAvailable = board.availablePoints();

        if (pointsAvailable.count == 0) {
            return 0;
        }

        let mut imin = 2;
        let mut imax = -2;

        for point in pointsAvailable {

            if (turn == self.computerPlayer) {

                if (!board.placeMove(point: point, player: self.humanPlayer)) {
                    board[point.x, point.y] = 0;
                    continue;
                }

                let val = minmax(depth: depth + 1, turn: self.humanPlayer, alpha: alphaVal, beta: betaVal);

                if (val > imax) {
                    imax = val;
                    betaVal = imax;
                }
                if (val == 1 || imax >= alphaVal) {
                    board[point.x, point.y] = 0;
                    break;
                }

            } else if (turn == self.humanPlayer) {

                if (!board.placeMove(point: point, player: self.computerPlayer)) {
                    board[point.x, point.y] = 0;
                    continue;
                }

                let val = minmax(depth: depth + 1, turn: self.computerPlayer, alpha: alphaVal, beta: betaVal);

                if (val < imin) {
                    imin = val;
                    alphaVal = imin;
                }

                if (val == -1 || imin <= betaVal) {
                    board[point.x, point.y] = 0;
                    break;
                }

            }

            board[point.x, point.y] = 0;
        }
        return (turn == self.computerPlayer) ? imax : imin;
    }

    // handy function to print a line for the board
    fn printLine(length: i32, leftCorner: String = "+", rightCorner:String = "+", interval:String = "-") {
        print(leftCorner,  terminator:"");
        for i in 0...length-3 {
            if ((i+1) % 4 == 0) {
                print!(interval);
            } else {
                print!("─")
            }
        }
        print(rightCorner);
    }


    // read a line from input
    // hard to believe straight swift has not much for this
    fn recieve() -> Optional<String> {
      var cstr: [UInt8] = []
      var c: Int32 = 0
      while c != 4 {
          c = getchar()
          if (c == 10 || c == 13) || c > 255 { break }
          cstr.append(UInt8(c))
      }
        // always add trailing zero
      cstr.append(0)

      return String.init(cString: UnsafePointer<CChar>(cstr))
  }

    // read a player from input
  fn readPlayer() -> Optional<i32> {
      if let str = recieve() {
        let c = str[str.startIndex]

          if c == "X" || c == "x" {
            return Game.PlayerX;
          } else if c == "O" || c == "o" {
            return Game.PlayerO;
          }
      }
      return nill;
  }

  fn readNetworkAddr() -> Optional<String> {
    if let str = recieve() {
    
        let regex = try Regex(pattern: "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$")
        if regex.matches(str) {
          return Some(str);
        }
    }
    return None;
  }

  fn readYesNo() -> Optional<bool> {
      if let str = recieve() {
          let c = str[str.startIndex]

          return c == "Y" || c == "y";
      }
      return nil;
  }

    // read a point from input
  fn readPoint(source: Optional<InputSource> = nil) -> Optional<Point> {
      if let str = (source ?? self).recieve() {
            // split on space and '-'
          let mut coords = str.characters.split { $0 == Character(" ") || $0 == Character("-") }.map{ String($0) }

          if (coords.count < 2) {
              return nil;
          }

          let x = i32(coords[0]);
          let y = i32(coords[1]);

          if (x == nil || y == nil) {
              return nil;
          }

          if (x < 1 || x > 3 || y < 1 || y > 3) {
              return nil;
          }

          return Point(x: x! - 1, y: y! - 1);
      }
      return nil;
  }

}
}
