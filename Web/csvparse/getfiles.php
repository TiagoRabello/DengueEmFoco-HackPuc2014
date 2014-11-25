<?php

$filesString = shell_exec("ls *.csv");
echo json_encode(explode("\n", $filesString));

