<?php
if (session_status() === PHP_SESSION_NONE) session_start();
function require_login(): void { if (empty($_SESSION['user_id'])) { header('Location: login.php'); exit; } }
function e($value): string { return htmlspecialchars((string)$value, ENT_QUOTES, 'UTF-8'); }
