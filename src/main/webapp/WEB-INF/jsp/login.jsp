<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>로그인</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="flex justify-center items-center min-h-screen bg-gray-100">
<div class="bg-white p-8 rounded shadow-md w-full max-w-sm">
    <h2 class="text-2xl font-bold mb-6 text-center">로그인</h2>

    <a href="/login/github"
       class="flex items-center justify-center gap-3 bg-black text-white py-2 px-4 rounded hover:bg-gray-800 transition">
        <img src="https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"
             alt="GitHub Logo" class="w-6 h-6">
        <span>GitHub로 로그인</span>
    </a>

    <a href="/oauth2/authorization/google"
       class="bg-red-500 text-white text-center py-2 rounded block hover:bg-red-600 mt-4">
        <img src="https://developers.google.com/identity/images/g-logo.png"
             alt="Google Logo" class="inline w-5 h-5 mr-2">
        Google로 로그인
    </a>

</div>
</body>
</html>
