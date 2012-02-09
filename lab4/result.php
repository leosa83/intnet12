<html>
	<head><title>Resultat</title></head>
	<body>

<?php
	$lan = $_GET['lan'];
	$objekttyp = $_GET['objekttyp'];
	$rum = $_GET['rum'];
	$adress = $_GET['adress'];
	
	$sort = $_GET['sort'];
	if (!isset($sort)) {
		$sort = "pris";
	}
	
	$link = pg_connect("host=psql-vt2012.csc.kth.se dbname=emilarf user=emilarf
	password=GhfHZCSM");
	
	$query = "SELECT * FROM bostader ";
	$added = False;
	if ($lan != "" && $lan != "alla") {
		//$lan = pg_escape_literal($lan);
		$query = $query . "WHERE lan='{$lan}' ";
		$added = True;
	}
	if ($objekttyp != "" && $objekttyp != "alla") {
		//$objekttyp = pg_escape_literal($objekttyp);
		$query = $query . ($added ? "AND" : "WHERE") . " objekttyp='{$objekttyp}' ";
		$added = True;
	}
	if ($rum != "" && $rum != "alla") {
		//$rum = pg_escape_literal($rum);
		$query = $query . ($added ? "AND" : "WHERE") . " rum={$rum} ";
		$added = True;
	}
	if ($adress != "" && $adress != "") {
		//$adress = pg_escape_literal($adress);
		$query = $query . ($added ? "AND" : "WHERE") . " adress='{$adress}' ";
		$added = True;
	}

	$query = $query . "ORDER BY " . $sort . ";";
	
	$result = pg_exec($link, $query);
	$numrows = pg_numrows($result);
	
	$q = "";
	foreach ($_GET as $k => $v) {
		if ($k != "sort")
			$q .= $k . "=" . $v . "&";
	}
	$q = substr($q, 0, strlen($q) - 1);
?>

	<table border="1">
		<tr>
			<td>Län</td>
			<td>Objekttyp</td>
			<td>Adress</td>
			<td><a href="result.php?<?php echo $q ?>&sort=area">Area</a></td>
			<td><a href="result.php?<?php echo $q ?>&sort=rum">Rum</a></td>
			<td><a href="result.php?<?php echo $q ?>&sort=pris">Pris</a></td>
			<td>Avgift</td>
		</tr>

<?php
	for($ri = 0; $ri < $numrows; $ri++) {
		echo "<tr>\n";
		$row = pg_fetch_array($result, $ri);
		echo " <tr>";

		echo "<td>" . $row["lan"] . "</td>";
		echo "<td>" . $row["objekttyp"] . "</td>";
		echo "<td>" . $row["adress"] . "</td>";
		echo "<td>" . $row["area"] . "</td>";
		echo "<td>" . $row["rum"] . "</td>";
		echo "<td>" . $row["pris"] . "</td>";
		echo "<td>" . $row["avgift"] . "</td>";
		
		echo " </tr>";
	}
	
	pg_close($link);
?>

	</body>
</html>