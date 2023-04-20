<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="../app.css" rel="stylesheet">
<title>メッセージの累積</title>
</head>
<body>
	${loginUser.name}さん、こんにちは！
	<hr>
	<form action="list" method="POST">
		メッセージ：<input type="text" name="message">
		<button>送信</button>
	</form>
	<form action="search" method="POST">
		検索語：<input type="text" name="keyword">
		<button>検索</button>
	</form>
	<form action="clear" method="GET">
		<button>Clear</button>
	</form>
	<hr>
	<h1>メッセージ一覧</h1>
	<c:forEach var="mes" items="${messages}">
		<div>${mes.name}:${mes.message}</div>
	</c:forEach>
	<p>
		<a href="./users">管理ページへ</a>
	</p>
	<p>
		<a href="./login">ログアウト</a>
	</p>
</body>
</html>
