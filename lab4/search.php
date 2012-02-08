<?php
$link = pg_connect("host=psql-vt2012.csc.kth.se dbname=emilarf user=emilarf password=GhfHZCSM");
?>

<html>
	<head>
		<title>Bövision - Sök</title>
 	</head>
 	<body>
 		<form action="result.php" method="get">
			<table>
				<tr>
					<td>Län</td>
					<td><select name="lan">
						<option value="alla">Alla</a>
<?php
$result = pg_exec($link, "select DISTINCT(lan) from bostader");
$numrows = pg_numrows($result);
for($ri = 0; $ri < $numrows; $ri++) {
	$row = pg_fetch_array($result, $ri);
	echo '<option value="' . $row["lan"] . '">' . $row["lan"] . '</option>';
}
?>
					</select></td>
				</tr>
				<tr>
					<td>Adress</td>
					<td><input type="text" name="adress"></td>
				</tr>
				<tr>
					<td>Objekttyp</td>
					<td><select name="objekttyp">
						<option value="alla">Alla</a>
<?php
$result = pg_exec($link, "select DISTINCT(objekttyp) from bostader");
$numrows = pg_numrows($result);
for($ri = 0; $ri < $numrows; $ri++) {
	$row = pg_fetch_array($result, $ri);
	echo '<option value="' . $row["objekttyp"] . '">' . $row["objekttyp"] . '</option>';
}
?>
					</select></td>
				</tr>
				<tr>
					<td>Rum</td>
					<td><select name="rum">
						<option value="alla">Alla</a>
<?php
$result = pg_exec($link, "select DISTINCT(rum) from bostader");
$numrows = pg_numrows($result);
for($ri = 0; $ri < $numrows; $ri++) {
	$row = pg_fetch_array($result, $ri);
	echo '<option value="' . $row["rum"] . '">' . $row["rum"] . '</option>';
}
?>
					</select></td>
				</tr>
			</table>
			<input type="submit" value="Sök">
		</form>
	</body>
</html>

<?php
	pg_close($link);
?>