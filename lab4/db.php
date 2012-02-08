<html>
	<title>PHP/PostgreSQL-test</title></head>  	
	<body>

<?php
		$link = pg_connect("host=psql-vt2012.csc.kth.se dbname=emilarf user=emilarf
		password=GhfHZCSM");
		$result = pg_exec($link, "select * from bostader");
		$numrows = pg_numrows($result);
?>

		<a href="search.php">Sök</a>
		<table border="1">
			<tr>
				<th>Adress</th>
			</tr>

<?php
			for($ri = 0; $ri < $numrows; $ri++) {
			echo "<tr>\n";
			$row = pg_fetch_array($result, $ri);
			echo " <td>", $row["adress"], "</td>";
			}
			pg_close($link);
?>

		</table>
	</body>
</html>
