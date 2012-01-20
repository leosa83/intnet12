<?php
//phpinfo();
session_start();

class Guess{
var $secretNumber;
var $numberOfGuesses;
var $guess;

function __construct(){
	$this->reset();        
}

function reset(){
	srand(time());
	$this->secretNumber = (rand()%100);
	$this->numberOfGuesses = 0;
}

function setGuess($var){
	$this->guess = $var;
	$this->numberOfGuesses++;
}

function success(){
	if($this->secretNumber==$this->guess){
		return "You made it!!!";
       }
       else if($this->secretNumber<$this->guess)
              return "Nope, guess lower<br>";
       else if($this->secretNumber>$this->guess)
              return "Nope, guess higher<br>";
}
}
?>

<html>
<head><title>Number Guess Game</title>
<script type = "text/javascript">
function inputfocus(form){
form.guess.focus()
}
</script>
</head>

<body onLoad="inputfocus(document.guessform);"> 

<?php
if(!session_is_registered('guessObject')){
$_SESSION['guessObject'] = new Guess();
print "Welcome to the Number Guess Game. Guess a number between 1 and 100.";
}
else{
$_SESSION['guessObject']->setGuess($_GET['guess']);
//print $_SESSION['guessObject']->secretNumber;
print $_SESSION['guessObject']->success();
print "You have made {$_SESSION['guessObject']->numberOfGuesses} guess(es)<br>";
}
?>


<form name="guessform">
<input type=text name=guess>
<input type=submit value="Guess">
</form>

</body></html>
