package code.with.cal.tictactoe // kods aizguts no: https://www.youtube.com/watch?v=POFvcoRo3Vw&ab_channel=CodeWithCal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import code.with.cal.tictactoe.databinding.ActivityMainBinding
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

// kods aizguts no https://www.youtube.com/watch?v=POFvcoRo3Vw&ab_channel=CodeWithCal
class MainActivity : AppCompatActivity()
{
    enum class Turn
    {
        NOUGHT,
        CROSS
    } // viegls veids, kā noteikt/kontrolet kura gājiens ir nākamais


    private var firstTurn = Turn.CROSS  // gajienu mainigais, kas nosaka kas iet pirmais
    private var currentTurn = Turn.CROSS  // gajienu mainigais, kas nosaka, kas iet paslaik

    private var crossesScore = 0  // rezultatu glabasana/mainiga izveide
    private var noughtsScore = 0

    private var cpuEnable = 0 //izsledz opciju spelet pret datoru
    private var cpuCrosses = false //izveido mainigo, kas nosaka vai CPU ir krustini vai nulites

    private var boardList = mutableListOf<Button>()  //izveido sarakstu ar pogam

    private lateinit var binding : ActivityMainBinding // piesaista aktivitates koda fragmentiem un mainigajiem

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState) //aktivitates izveidosana
        binding = ActivityMainBinding.inflate(layoutInflater) //izveido binding objektu
            setContentView(binding.root)
        initBoard()
        greetings()
    }

    private fun greetings()
    {
        val textIn = EditText(baseContext) //isti nezinu kas tas ir un ka tas precizi jaizmanto, bet ja jau strada tad gan jau nekas slikts
        val message = "\nHello and welcome to the game of Tic-Tac-Toe! \nPlease input your name:"
        AlertDialog.Builder(this)
            .setTitle("Greetings")
            .setMessage(message)
            .setView(textIn) //atlauj ievadit textu speletajam
            .setPositiveButton("Lets start!")  //izveido pogu reset, kas restarte speli un notira speles laukumu
            { _,_ ->
                val nameText = textIn.text.toString()
                binding.textView.text = "Player name: $nameText" //uzstada speletaja vardu uz ekrana
            }
            .setCancelable(false)
            .show()  //parada bridinajuma logu speletajiem

    }

    private fun initBoard() // inicialize spelu laukumu, jeb pievieno visas pogas pie pogu saraksta
    {
        boardList.add(binding.a1)
        boardList.add(binding.a2)
        boardList.add(binding.a3)
        boardList.add(binding.b1)
        boardList.add(binding.b2)
        boardList.add(binding.b3)
        boardList.add(binding.c1)
        boardList.add(binding.c2)
        boardList.add(binding.c3)

    }

    fun cpuTurn()
    {
        if (cpuEnable == 1 && !fullBoard()) { //parliecinas vai cpu ir izvelets un vai ir briva vieta nakamajam gajienam
            if ((currentTurn == Turn.CROSS && cpuCrosses) || (currentTurn == Turn.NOUGHT && !cpuCrosses)) {
                var move = 0
                var chosenButton: Button
                var possible = false
                while (!possible) // kamer nav atrasts iespejam gajiens, tiek veikts loops, kas izvelas random vertibu no 0 lidz 8, kas nosaka kur notiks nakamais cpu gajiens
                {
                    move = (0 until 9).random()
                    chosenButton = boardList.get(move)
                    if(chosenButton.text == "") //parliecinas vai poga nav jau vienreiz nospiesta
                        possible = true
                }
                chosenButton = boardList.get(move) //cpu atlasa nepieciesamo pogu
                addToBoard(chosenButton) //cpu nospiez izveleto pogu
            }
        }
    }

    fun boardTapped(view: View)
    {
        if(view !is Button) //parliecinas vai uzspiestais ir kada no iespejajam pogam
            return
        addToBoard(view) // ja poga patiesam bija nospiesta tad ta tiek attiecigi parverts par x vai o vai nekas nenotiek, ja poga jau kadreiz ir tikusi nospiesta

        if(checkForVictory(NOUGHT)) //parliecinas vai kads nav uzvarejis
        {
            cpuEnable = 0
            noughtsScore++  //palielina rezultatu "o" speletajam
            result("Noughts Win!") // izvada pazinojumu par uzvaru
        }
        else if(checkForVictory(CROSS))
        {
            cpuEnable = 0
            crossesScore++  //palielina rezultatu "x" speletajam
            result("Crosses Win!") // izvada pazinojumu par uzvaru
        }

        if(fullBoard() && !checkForVictory(CROSS) && !checkForVictory(NOUGHT))
        {
            cpuEnable = 0
            result("Draw")  //izavada pazinojumu par neizskirtu
        }

    }

    private fun checkForVictory(s: String): Boolean
    {
        //parliecinas vai horizontali nav treis vienadu simbolu
        if(match(binding.a1,s) && match(binding.a2,s) && match(binding.a3,s))
            return true
        if(match(binding.b1,s) && match(binding.b2,s) && match(binding.b3,s))
            return true
        if(match(binding.c1,s) && match(binding.c2,s) && match(binding.c3,s))
            return true

        //parliecinas vai vertikali nav treis vienadu simbolu
        if(match(binding.a1,s) && match(binding.b1,s) && match(binding.c1,s))
            return true
        if(match(binding.a2,s) && match(binding.b2,s) && match(binding.c2,s))
            return true
        if(match(binding.a3,s) && match(binding.b3,s) && match(binding.c3,s))
            return true

        //parliecinas vai pa diagonali nav treis vienadu simbolu
        if(match(binding.a1,s) && match(binding.b2,s) && match(binding.c3,s))
            return true
        if(match(binding.a3,s) && match(binding.b2,s) && match(binding.c1,s))
            return true

        return false //atgriez negativu vertibu pec katra gajienu, kura kads nav uzvarejis
    }

    private fun match(button: Button, symbol : String): Boolean = button.text == symbol  // parliecinas vai noteikta poga satur noteiktu simbolu (o vai x)

    private fun result(title: String)
    {
        val message = "\nNoughts $noughtsScore\n\nCrosses $crossesScore"
        AlertDialog.Builder(this)
            .setTitle(title) // uzstada pazinojuma virsrakstu par uzvaretaju
            //.setMessage(message) // izvada pazinojumu par pasreizejo rezultatu
            .setPositiveButton("Reset")  //izveido pogu reset, kas restarte speli un notira speles laukumu
            { _,_ ->
                cpuEnable = 0
                resetBoard()  //notira speles laukumu
            }
            .setNegativeButton("Play vs. CPU")  //izveido pogu, kas lauj nakamo speli spelet pret datoru
            { _,_ ->
                resetBoard() //notira speles laukumu
                cpuEnable = 1
                cpuChoose("Choose CPU player");
            }
            .setCancelable(false)
            .show()  //parada bridinajuma logu speletajiem
    }

    private fun cpuChoose(title: String) //izveido jaunu alertdialogu, lai lautu speletajam izveleties pret kadu simboli vins speles
    {
        val message = "\nChoose your CPU player:"
        AlertDialog.Builder(this)
            .setTitle(title)
            //.setMessage(message)
            .setPositiveButton("CPU: X")  //izveido pogu reset, kas restarte speli un notira speles laukumu
            { _,_ ->
                cpuCrosses = true //maina vertibu, kas reprezente kadu simbolu speles cpu
                resetBoard() //notira speles laukumu
                cpuTurn() //padod gajienu cpu
            }
            .setNegativeButton("CPU: O")  //izveido pogu, kas lauj nakamo speli spelet pret datoru
            { _,_ ->
                cpuCrosses = false
                resetBoard() //notira speles laukumu
                cpuTurn() //padod gajienu cpu
            }
            .setCancelable(false)
            .show()  //parada bridinajuma logu speletajiem
    }

    private fun resetBoard()
    {
        for(button in boardList)  // for cikls, kas iziet cauri visam pogam uz speles laukuma
        {
            button.text = ""  // padara visas pogas 'tuksas', jeb uz tam vairs nav rakstits x vai o
        }

        if(firstTurn == Turn.NOUGHT)  //samaina gajienu secibu, tas kurs saka pirmais, tagad saks otrais
            firstTurn = Turn.CROSS
        else if(firstTurn == Turn.CROSS)
            firstTurn = Turn.NOUGHT

        currentTurn = firstTurn  // nomaina veco gajienu secibu pret jauno
        binding.textView3.text = "Crosses - $crossesScore"
        binding.textView4.text = "Noughts - $noughtsScore"
        setTurnLabel()
    }

    private fun fullBoard(): Boolean
    {
        for(button in boardList) // for cikls, kas iziet cauri visam pogam uz speles laukuma
        {
            if(button.text == "")  //parliecinas vai nav palikusas pogas, uz kuram nav paradijies x vai o
                return false  // ja patiesam visas pogas ir "brivas', tad atgriez false vertibu
        }
        return true
        cpuEnable = 0// drosibas gadijumam izsledz cpu
        //pretejaja gadijuma atgriez true un spele apstajas
    }

    private fun addToBoard(button: Button) // funkcija lai mainitu pogu stavokli ("" vai "x" vai "o")
    {
        if(button.text == "reset"){
            crossesScore = 200
            noughtsScore = 0
            cpuEnable = 0
            resetBoard()
        }
        if(button.text != "") //parliecinas vai poga nav jau vienreiz nospiesta
            return

        if(currentTurn == Turn.NOUGHT)  // nosaka kura speletaja gajiens ir paslaik
        {
            button.text = NOUGHT  //uzraksta uz pogas o un padod gajienu nakamajam speletajam
            currentTurn = Turn.CROSS
        }
        else if(currentTurn == Turn.CROSS)  // nosaka kura speletaja gajiens ir pasalaik
        {
            button.text = CROSS // uzraksta uz pogas x  un padod gajienu nakamajam speletajam
            currentTurn = Turn.NOUGHT
        }
        setTurnLabel()  //atjauno speletaja gajiena datus
    }

    private fun setTurnLabel()
    {
        var turnText = ""  //izveido jaunu mainigo
        if(currentTurn == Turn.CROSS)
            turnText = "Turn $CROSS" // nomaina tekstu, lai paraditu ka gajiens ir x
        else if(currentTurn == Turn.NOUGHT)
            turnText = "Turn $NOUGHT" // nomaina tekstu lai paraditu ka gajien ir o
        binding.turnTV.text = turnText
        if (cpuEnable == 1)
            cpuTurn() //nodod gajienu cpu
    }

    companion object
    {
        const val NOUGHT = "O"  // pieskir vertibu o un x, lai lieki nenaktos atkartot "X" vai "O"
        const val CROSS = "X"
    }
// kods aizguts no https://www.youtube.com/watch?v=POFvcoRo3Vw&ab_channel=CodeWithCal
}











