<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="author" content="SHOPEC Team">
	<meta name="copyright" content="SHOPEC">
	<title>${message("admin.index.title")} - Powered By SHOPEC</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/adminlte/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/adminlte/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/adminlte/css/animate.css" rel="stylesheet">
	<link href="${base}/resources/adminlte/css/adminLTE.css" rel="stylesheet">
	<link href="${base}/resources/adminlte/css/common.css" rel="stylesheet">
	<link href="${base}/resources/adminlte/css/index.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/adminlte/js/html5shiv.js"></script>
		<script src="${base}/resources/adminlte/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/adminlte/js/jquery.js"></script>
	<script src="${base}/resources/adminlte/js/bootstrap.js"></script>
	<script src="${base}/resources/adminlte/js/moment.js"></script>
	<script src="${base}/resources/adminlte/js/g2.js"></script>
	<script src="${base}/resources/adminlte/js/adminLTE.js"></script>
	<script src="${base}/resources/admin/js/common.js"></script>
	<script type="text/javascript">
		function setIframeHeight(iframe) {
    		var iframe = document.getElementById("iframe");
           if (iframe) {
               var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
               if (iframeWin.document.body) {
               	var header = document.getElementById("header");
				var h = header.offsetHeight;  //高度
                   iframe.height = document.body.clientHeight-h-5;
               }
           }
       };
	</script>
</head>
<body class="index hold-transition sidebar-mini">
	<div class="wrapper">
		[#include "/admin/include/main_header.ftl" /]
		[#include "/admin/include/main_sidebar.ftl" /]
		<div class="content-wrapper">
			<iframe src="main/list" frameborder="0" scrolling="auto" id="iframe" name="iframe"  onload="setIframeHeight(this)" height=99%  width=100%></iframe>
		</div>
		[#include "/admin/include/main_footer.ftl" /]
	</div>
</body>
</html>