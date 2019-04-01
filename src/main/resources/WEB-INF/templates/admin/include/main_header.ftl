<header class="main-header" id="header">
	<a class="logo" href="${base}/admin/index">
		<span class="logo-mini">
			<img class="img-circle" src="${base}/upload/image/header_logo.png" alt="shopec b2c">
		</span>
		<span class="logo-lg"><strong>SHOPEC</strong> B2B2C</span>
	</a>
	<nav class="navbar navbar-static-top">
		<div class="container-fluid">
			<a class="sidebar-toggle" href="javascript:;" data-toggle="offcanvas"></a>
			<div class="navbar-custom-menu">
				<ul class="nav navbar-nav">
					<li>
						<a href="${base}/admin/profile/edit" target="iframe">
							<span class="fa fa-user"></span>
							<span class="hidden-xs"><strong>[@shiro.principal property="displayName" /]</strong> ${message("admin.index.hello")}!</span>
						</a>
					</li>
					<li class="dropdown notifications-menu">
						<a class="dropdown-toggle" href="javascript:;" data-toggle="dropdown">
							<span class="fa fa-bell-o"></span>
						</a>
						<ul class="dropdown-menu">
						</ul>
					</li>
					<li>
						<a class="logout" href="${base}/admin/logout">
							<span class="fa fa-sign-out"></span>
							${message("admin.index.logout")}
						</a>
					</li>
				</ul>
			</div>
		</div>
	</nav>
</header>