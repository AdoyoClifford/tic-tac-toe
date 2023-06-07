import java.lang.IllegalArgumentException
import kotlin.system.exitProcess

class Game {

    private val board = MutableList<Cell>(size = 9) { Cell.Empty }
    private var status: GameStatus = GameStatus.Idle
    private lateinit var player: Player

    fun start() {
        status = GameStatus.Running
        val welcomeMessage = """
               ------------------------
        | Welcome to Tic Tac Toe! |
        | Pick a number from 0..8 |
          ------------------------
        """.trimIndent()
        println(welcomeMessage)
        getName()
        while (status is GameStatus.Running) {
            getCell()
        }

    }

    private fun getCell() {
        val input = readlnOrNull()
        try {
            requireNotNull(value = input)
            val cellNumber = input.toInt()
            require(value = cellNumber in 1..8)
            setCell(selectedCell = cellNumber)
        } catch (e: Exception) {
            println("Invalid number")
        }
    }

    private fun setCell(selectedCell: Int) {
        val cell = board[selectedCell]
        if (cell is Cell.Empty) {
            board.set(
                index = selectedCell,
                element = Cell.Filled(player = Player())
            )
            checkBoard()
            generateComputerMove()
        }

        try {

        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun generateComputerMove() {
        try {
            val availableCells = mutableListOf<Int>()
            board.forEachIndexed { index, cell ->
                if (cell is Cell.Empty) availableCells.add(index)
            }
            if (availableCells.isNotEmpty()) {
                val randomCell = availableCells.random()
                board.set(
                    index = randomCell,
                    element = Cell.Filled(player = Player())
                )
            }
        } catch (e: Exception) {
            println("Error ${e.message}")
        }
    }

    private fun checkBoard() {
        val winningCombination = listOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8),
            listOf(2, 4, 6)
        )

        val player1Cells = mutableListOf<Int>()
        val player2Cells = mutableListOf<Int>()
        board.forEachIndexed { index, cell ->
            if (cell.placeHolder == 'X') player1Cells.add(index)
            if (cell.placeHolder == 'O') player2Cells.add(index)
        }
        println("YOUR MOVES $player1Cells")
        println("COMPUTER MOVES $player2Cells")

        run CombinationLoop@{
            winningCombination.forEach { combination ->
                if (player1Cells.containsAll(elements = combination)) {
                    won()
                    return@CombinationLoop
                }
                if (player2Cells.containsAll(elements = combination)) {
                    lost()
                    return@CombinationLoop
                }
            }
            if (board.none{it is Cell.Empty} && status is GameStatus.Running){
                draw()
            }
            if (status is GameStatus.GameOver) {
                finish()
                playAgain()
            }

        }
    }

    private fun lost() {
        status = GameStatus.GameOver
        displayBoard()
        println("Sorry ${player.name} lost")
    }

    private fun won() {
        status = GameStatus.GameOver
        displayBoard()
        println("Congratulations ${player.name} won")
    }
    private fun draw() {
        status = GameStatus.GameOver
        displayBoard()
        println("Draw!")
    }

    private fun finish() {
        status = GameStatus.Idle
        board.replaceAll { Cell.Empty }
    }

    private fun playAgain() {
        print("Do you wish to play again? Y/N:")
        val input = readlnOrNull()
        try {
            requireNotNull(value = input)
            val capitalizedInput = input.replaceFirstChar(Char::titlecase)
            val positive = capitalizedInput.contains(other = "Y")
            val negative = capitalizedInput.contains(other = "N")
            require(value = positive || negative)
            if (positive) {
                start()
            } else if (negative) {
                exitProcess(status = 0)
            }
        } catch (e: IllegalArgumentException) {
            println("Wrong option type either Y or N")
            playAgain()
        }
    }

    private fun displayBoard() {
        val boardDisplay = """
             ------- 
            | ${board[0].placeHolder} ${board[1].placeHolder} ${board[2].placeHolder} |
            | ${board[3].placeHolder} ${board[4].placeHolder} ${board[5].placeHolder} |
            | ${board[6].placeHolder} ${board[7].placeHolder} ${board[8].placeHolder} |
             ------- 
        """.trimIndent()
        println(boardDisplay)
    }

    private fun getName() {
        print("Enter your name: ")
        val name = readlnOrNull()
        try {
            requireNotNull(value = name)
            player = Player(name = name, symbol = 'X')
            println("Its your move $name")
        } catch (e: Throwable) {
            println("Invalid name")
        }
    }

}

data class Player(
    val name: String = "Computer",
    val symbol: Char = 'O'
)

sealed class GameStatus() {
    object Idle : GameStatus()
    object Running : GameStatus()
    object GameOver : GameStatus()
}

sealed class Cell(val placeHolder: Char) {
    object Empty : Cell(placeHolder = '_')
    data class Filled(val player: Player) : Cell(placeHolder = player.symbol)
}