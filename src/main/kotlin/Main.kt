import kotlin.random.Random
import kotlin.random.nextInt

fun main(args: Array<String>) {


    // computer moves O players moves X
    // Enter a number between 0..8 throw exception if you enter an invalid number
    //congratulatory message

    val welcomeMessage = """
          ------------------------
        | Welcome to Tic Tac Toe! |
        | Pick a number from 0..8 |
          ------------------------
    """.trimIndent()
    println(welcomeMessage)
    print("Choose your name: ")
    val name = readlnOrNull()
    println("Its your move $name")

    val board = Array(3) { Array(3) { '_' } }

    val human = Human()
    val computer = Computer()

    while (!checkWinner(board)) {
        human.makeMove(board)
        computer.makeMove(board)
        for (row in board.indices) {
            print("|")
            for (colum in board[row].indices) {
                print(board[row][colum])
                print(' ')
            }
            print("|")
            println()
        }

    }
}


fun checkWinner(board: Array<Array<Char>>): Boolean {
    for (i in board.indices) {
        // check for row winner
        if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != '_') {
            return true
        }
        //check for column winner
        if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != '_') {
            return true
        }
        //check for diagonal winner
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != '_') {
            return true
        } else if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != '_') {
            return true
        }
    }
    return false
}

interface Player {
    fun makeMove(board: Array<Array<Char>>)
}

class Human : Player {
    override fun makeMove(board: Array<Array<Char>>) {
        // get user input
        while (true) {
            val input = readln().toInt()

            if (input in 1..9) {
                val location = (input - 1) / 3 * 3 + (input - 1) % 3
                board[location / 3][location % 3] = 'X'
                break
            } else {
                println("Please enter a number between 1 and 9.")
            }
        }
    }
}


class Computer : Player {
    override fun makeMove(board: Array<Array<Char>>) {
        var foundSpot = false

        while (!foundSpot) {
            val input = Random.nextInt(1..9)
            println(input)
            val location = (input - 1) / 3 * 3 + (input - 1) % 3
            if (board[location / 3][location % 3] == '_') {
                board[location / 3][location % 3] = 'O'
                foundSpot = true
            }
        }
    }
}



