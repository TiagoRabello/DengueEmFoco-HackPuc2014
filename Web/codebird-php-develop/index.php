<?php

require_once ('codebird.php');
\Codebird\Codebird::setConsumerKey('YOURKEY', 'YOURSECRET'); // static, see 'Using multiple Codebird instances'

$cb = \Codebird\Codebird::getInstance();

?>