use std::ops::Index;

mod board
{

struct Point {

    x: i32;
    y: i32;
}

impl Point {
    pub fn new(x: i32, y: i32) {
        self.x = x;
        self.y = y;
    }

    pub fn description() -> String {
        return format!("{}-{}", x, y);
    }
}

struct Board {

    board: [[i32;];];
}

impl Board {
    pub fn new(size: i32 = 3) {
        board = [];

        for _ in 0...size-1 {
            var line = [i32]();
            for _ in 0...size-1 {
                line.append(0);
            }
            board.append(line);
        }
    }

    pub fn new(copy: Board) {
        board = [];

        for i in 0...size-1 {
            var line = [Int]();
            for j in 0...size-1 {
                line.append(copy[i, j]);
            }
            board.append(line);
        }
    }

    pub fn size: i32 {
        return board.count;
    }

    pub fn hasWonDiagnal(player: i32) -> bool {

        let mut found = true;

        // check the first diagnal
        for i in 0...board.count-1 {
            // if not found
            if(board[i][i] != player) {
                found = false;
                break;
            }
        }

        if (found) {
            return true;
        }

        found = true;
	let mut j = 0;
	let i = board.count-1;
	for i in stride(from: i, to: -1, by: -1) {
            if (j >= board.count || board[i][j] != player) {
                found = false;
                break;
            }
	    j += 1;
        }

        return found;
    }

    pub fn hasWon(player: i32) -> bool {
        // check diagnal
        if (self.hasWonDiagnal(player: player)) {
            return true;
        }

        // check each row
        for i in 0...board.count-1 {

            let mut found = true;
            for j in 0...board.count-1 {
                if(board[i][j] != player) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return true;
            }
        }

        for i in 0...board.count-1 {
            let mut found = true;

            for j in 0...board.count-1 {
                if(board[j][i] != player) {
                    found = false;
                    break;
                }
            }

            if (found) {
                return true;
            }
        }

        return false;
    }

    pub fn availablePoints() -> [Point] {
        let availablePoints = [Point]();

        for i in 0...board.count-1 {
            for j in 0...board[i].count-1 {
                if (board[i][j] == 0) {
                    availablePoints.append(Point(x: i, y: j));
                }
            }
        }
        return availablePoints;
    }

    pub fn placeMove(point: Point, player: i32) -> bool {

        if (point.x < 0 || point.x >= board.count ||
        point.y < 0 || point.y >= board.count) {
            return false;
        }

        if (self.board[point.x][point.y] != 0) {
            return false;
        }

        self.board[point.x][point.y] = player;
        return true;
    }

}

}
