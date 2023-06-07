class Game {

    private val board = MutableList<Cell>(size = 9) { Cell.Empty }
    private var status = GameStatus.Idle
    private lateinit var player: Player

    fun start() {
        val status = GameStatus.Running
        val welcomeMessage = """
               ------------------------
        | Welcome to Tic Tac Toe! |
        | Pick a number from 0..8 |
          ------------------------
        """.trimIndent()
        println(welcomeMessage)
        getName()
        displayBoard()

    }

    private fun getCell() {
        TODO("Not yet implemented")
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
            board.forEachIndexed{index, cell ->
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