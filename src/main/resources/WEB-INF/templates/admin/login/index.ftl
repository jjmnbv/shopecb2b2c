<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="author" content="SHOPEC Team">
	<meta name="copyright" content="SHOPEC">
	<title>${message("admin.login.title")} - Powered By SHOPEC</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/adminlte/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/adminlte/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/adminlte/css/adminLTE.css" rel="stylesheet">
	<link href="${base}/resources/adminlte/css/common.css" rel="stylesheet">
	<link href="${base}/resources/adminlte/css/login.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/adminlte/js/html5shiv.js"></script>
		<script src="${base}/resources/adminlte/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/adminlte/js/jquery.js"></script>
	<script src="${base}/resources/adminlte/js/bootstrap.js"></script>
	<script src="${base}/resources/adminlte/js/jquery.validate.js"></script>
	<script src="${base}/resources/adminlte/js/icheck.js"></script>
	<script src="${base}/resources/adminlte/js/adminLTE.js"></script>
	<script src="${base}/resources/adminlte/js/jquery.validate.js"></script>
	
	<script src="${base}/resources/admin/js/common.js"></script>
	<script type="text/javascript">
	$().ready( function() {
	    
		var $loginForm = $("#loginForm");
		var $username = $("input[name='username']");
		var $password = $("input[name='password']");
		var $captcha = $("#captcha");
		var $isRememberUsername = $("input[name='isRememberUsername']");
		var $submit = $("button[type='submit']");
		
		// 记住用户名
		if (getCookie("adminUsername") != null) {
			$isRememberUsername.iCheck("check");
			$username.val(getCookie("adminUsername"));
			$password.focus();
		} else {
			$isRememberUsername.iCheck("uncheck");
			$username.focus();
		}
		
		// 验证码图片
		$captcha.captchaImage();
		
		// 表单验证、记住用户名
		$loginForm.validate({
			rules: {
				username: "required",
				password: "required"
				[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("adminLogin")]
					,captcha: "required"
				[/#if]
			},
			submitHandler: function(form) {
				$.ajax({
					url: $loginForm.attr("action"),
					type: $loginForm.attr("method"),
					data: $loginForm.serialize(),
					dataType: "json",
					cache: false,
					beforeSend: function() {
						$submit.prop("disabled", true);
					},
					success: function(data) {
						if ($isRememberUsername.prop("checked")) {
							addCookie("adminUsername", $username.val(), {expires: 7 * 24 * 60 * 60});
						} else {
							removeCookie("adminUsername");
						}
						if (data.redirectUrl != null) {
							location.href = data.redirectUrl;
						} else {
							location.href = "${base}/";
						}
					},
					error: function(xhr, textStatus, errorThrown) {
						$submit.prop("disabled", false);
						[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("adminLogin")]
							$captcha.captchaImage("refresh", true);
						[/#if]
					}
				});
			}
		});
	  });
	</script>
</head>
<body class="hold-transition login-page">
<div class="login-box">
  <div class="login-logo">
    <a href="#"><b>SHOPEC</b> B2B2C</a>
  </div>
  <div class="login-box-body">
    <p class="login-box-msg">登 录</p>
		<form id="loginForm" action="login" method="post">
			<div class="form-group">
				<div class="input-group">
					<span class="input-group-addon">
						<i class="glyphicon glyphicon-user gray-lighter"></i>
					</span>
					<input name="username" class="form-control" type="text" maxlength="200" placeholder="${message("admin.login.username")}">
				</div>
			</div>
			<div class="form-group">
				<div class="input-group">
					<span class="input-group-addon">
						<i class="glyphicon glyphicon-lock gray-lighter"></i>
					</span>
					<input name="password" class="form-control" type="password" maxlength="200" placeholder="${message("admin.login.password")}" autocomplete="off">
				</div>
			</div>
			[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("adminLogin")]
				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">
							<span class="glyphicon glyphicon-picture gray-lighter"></span>
						</div>
						<input id="captcha" name="captcha" class="captcha form-control" type="text" maxlength="4" placeholder="${message("common.captcha.name")}" autocomplete="off">
						<div class="input-group-btn"></div>
					</div>
				</div>
			[/#if]
			<div class="form-group">
				<input id="isRememberUsername" name="isRememberUsername" class="icheck" type="checkbox" value="true">
				<label for="isRememberUsername">${message("admin.login.isRememberUsername")}</label>
			</div>
			<div class="form-group">
				<button class="btn btn-primary btn-block" type="submit">${message("admin.login.login")}</button>
			</div>
		</form>
  </div>
</div>

</body>
</html>
