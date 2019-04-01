<script type="text/javascript">

	$().ready(function() {
		var $includeForm = $("#includeForm");
		var $searchValue = $("input[name='searchValue']");
		
		$includeForm.submit(function() {
			if($.trim($searchValue.val()) == "") {
				return false;
			}
		})
	});

</script>
<aside class="main-sidebar">
	<section class="sidebar">
		<form id="includeForm" class="sidebar-form" action="${base}/admin/product/list" target="iframe" method="get">
			<input name="searchProperty" type="hidden" value="name">
			<div class="active input-group">
				<input name="searchValue" class="form-control" type="text" placeholder="商品搜索">
				<span class="input-group-btn">
					<button class="btn btn-flat" type="submit">
						<i class="fa fa-search"></i>
					</button>
				</span>
			</div>
		</form>
		<ul class="sidebar-menu">
			[#list ["admin:business", "admin:store", "admin:storeCategory", "admin:storeRank", "admin:businessAttribute", "admin:cash", "admin:categoryApplication", "admin:businessDeposit"] as permission]
				[@shiro.hasPermission name = permission]
					<li class="treeview[#if .main_template_name?matches(".*/business/.*|.*/stock/.*|.*/product_category/.*|.*/product_tag/.*|.*/review/.*")] active[/#if]">
						<a href="javascript:;">
							<i class="fa fa-bank"></i>
							<span>${message("admin.index.storeGroup")}</span>
							<span class="pull-right-container">
								<i class="fa fa-angle-left pull-right"></i>
							</span>
						</a>
						<ul class="treeview-menu">
							<li[#if .main_template_name?matches(".*/business/.*")] class="active"[/#if]>
								 [@shiro.hasPermission name = "admin:business"]
									<a href="${base}/admin/business/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.business")}
									</a>
								 [/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/store/.*")] class="active"[/#if]>
								 [@shiro.hasPermission name = "admin:store"]
									<a href="${base}/admin/store/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.store")}
									</a>
								 [/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/store_category/.*")] class="active"[/#if]>
								 [@shiro.hasPermission name = "admin:storeCategory"]
									<a href="${base}/admin/store_category/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.storeCategory")}
									</a>
								 [/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/store_rank/.*")] class="active"[/#if]>
								 [@shiro.hasPermission name = "admin:storeRank"]
									<a href="${base}/admin/store_rank/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.storeRank")}
									</a>
								 [/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/business_attribute/.*")] class="active"[/#if]>
								 [@shiro.hasPermission name = "admin:businessAttribute"]
									<a href="${base}/admin/business_attribute/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.businessAttribute")}
									</a>
								 [/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/cash/.*")] class="active"[/#if]>
								 [@shiro.hasPermission name = "admin:cash"]
									<a href="${base}/admin/cash/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.cash")}
									</a>
								 [/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/category_application/.*")] class="active"[/#if]>
								 [@shiro.hasPermission name = "admin:categoryApplication"]
									<a href="${base}/admin/category_application/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.categoryApplication")}
									</a>
								 [/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/category_application/.*")] class="active"[/#if]>
								 [@shiro.hasPermission name = "admin:businessDeposit"]
									<a href="${base}/admin/business_deposit/log" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.businessDeposit")}
									</a>
								 [/@shiro.hasPermission]
							</li>
						</ul>
					</li>
					[#break /]
				[/@shiro.hasPermission]
			[/#list]
			[#list ["admin:product", "admin:stock", "admin:productCategory", "admin:productTag", "admin:parameter", "admin:attribute", "admin:specification", "admin:brand"] as permission]
				[@shiro.hasPermission name = permission]
					<li class="treeview[#if .main_template_name?matches(".*/product/.*|.*/stock/.*|.*/product_category/.*|.*/product_tag/.*|.*/review/.*")] active[/#if]">
						<a href="javascript:;">
							<i class="fa fa-clone"></i>
							<span>${message("admin.index.productGroup")}</span>
							<span class="pull-right-container">
								<i class="fa fa-angle-left pull-right"></i>
							</span>
						</a>
						<ul class="treeview-menu">
							<li[#if .main_template_name?matches(".*/product/.*")] class="active"[/#if]>
								 [@shiro.hasPermission name = "admin:product"]
									<a href="${base}/admin/product/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.product")}
									</a>
								 [/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/stock/.*")] class="active"[/#if]>
								 [@shiro.hasPermission name = "admin:stock"]
									<a href="${base}/admin/stock/log" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.stock")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/product_category/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:productCategory"]
									<a href="${base}/admin/product_category/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.productCategory")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/product_tag/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:productTag"]
									<a href="${base}/admin/product_tag/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.productTag")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/parameter/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:parameter"]
									<a href="${base}/admin/parameter/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.parameter")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/attribute/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:attribute"]
									<a href="${base}/admin/attribute/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.attribute")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/specification/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:specification"]
									<a href="${base}/admin/specification/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.specification")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/brand/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:brand"]
									<a href="${base}/admin/brand/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.brand")}
									</a>
								[/@shiro.hasPermission]
							</li>
						</ul>
					</li>
					[#break /]
				[/@shiro.hasPermission]
			[/#list]
			[#list ["admin:order", "admin:orderPayment", "admin:orderRefunds", "admin:orderShipping", "admin:orderReturns", "admin:deliveryCenter", "admin:deliveryTemplate"] as permission]
				[@shiro.hasPermission name = permission]
					<li class="treeview[#if .main_template_name?matches(".*/order/.*|.*/delivery_template/.*|.*/delivery_center/.*")] active[/#if]">
						<a href="javascript:;">
							<i class="fa fa-list-ul"></i>
							<span>${message("admin.index.orderGroup")}</span>
							<span class="pull-right-container">
								<i class="fa fa-angle-left pull-right"></i>
							</span>
						</a>
						<ul class="treeview-menu">
							<li[#if .main_template_name?matches(".*/order/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:order"]
									<a href="${base}/admin/order/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.order")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/order_payment/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:orderPayment"]
									<a href="${base}/admin/order_payment/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.orderPayment")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/order_refunds/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:orderRefunds"]
									<a href="${base}/admin/refunds/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.orderRefunds")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/order_shipping/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:orderShipping"]
									<a href="${base}/admin/shipping/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.orderShipping")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/order_returns/.*")] class="active"[/#if]>
								 [@shiro.hasPermission name = "admin:orderReturns"]
									<a href="${base}/admin/returns/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.orderReturns")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/delivery_center/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:deliveryCenter"]
									<a href="${base}/admin/delivery_center/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.deliveryCenter")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/delivery_template/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:deliveryTemplate"]
									<a href="${base}/admin/delivery_template/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.deliveryTemplate")}
									</a>
								[/@shiro.hasPermission]
							</li>
						</ul>
					</li>
					[#break /]
				[/@shiro.hasPermission]
			[/#list]
			[#list ["admin:member", "admin:memberRank", "admin:memberAttribute", "admin:point", "admin:memberDeposit", "admin:review", "admin:consultation", "admin:messageConfig"] as permission]
				[@shiro.hasPermission name = permission]
					<li class="treeview[#if .main_template_name?matches(".*/store/setting.*|.*/store_product_category/.*|.*/store_product_tag/.*|.*/category_application/.*|.*/store/payment.*|.*/shipping_method/.*|.*/area_freight_config/.*|.*/store_ad_image/.*")] active[/#if]">
						<a href="javascript:;">
							<i class="fa fa-users"></i>
							<span>${message("admin.index.memberGroup")}</span>
							<span class="pull-right-container">
								<i class="fa fa-angle-left pull-right"></i>
							</span>
						</a>
						<ul class="treeview-menu">
							<li[#if .main_template_name?matches(".*/member/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:member"]
									<a href="${base}/admin/member/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.member")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/member_rank/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:memberRank"]
									<a href="${base}/admin/member_rank/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.memberRank")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/member_attribute/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:memberAttribute"]
									<a href="${base}/admin/member_attribute/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.memberAttribute")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/point/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:point"]
									<a href="${base}/admin/point/log" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.point")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/member_attribute/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:memberDeposit"]
									<a href="${base}/admin/member_deposit/log" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.memberDeposit")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/review/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:review"]
									<a href="${base}/admin/review/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.review")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/consultation/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:consultation"]
									<a href="${base}/admin/consultation/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.consultation")}
									</a>
								[/@shiro.hasPermission]
							</li>
							[#--<li[#if .main_template_name?matches(".*/consultation/.*")] class="active"[/#if]>--]
								[#--[@shiro.hasPermission name = "admin:messageConfig"]--]
									[#--<a href="${base}/admin/message_config/list" target="iframe">--]
										[#--<i class="fa fa-circle-o"></i>--]
										[#--${message("admin.index.messageConfig")}--]
									[#--</a>--]
								[#--[/@shiro.hasPermission]--]
							[#--</li>--]
						</ul>
					</li>
					[#break /]
				[/@shiro.hasPermission]
			[/#list]
			[#list ["admin:navigation", "admin:article", "admin:articleCategory", "admin:articleTag", "admin:friendLink", "admin:adPosition", "admin:ad", "admin:template", "admin:cache"] as permission]
				[@shiro.hasPermission name = permission]
					<li class="treeview[#if .main_template_name?matches(".*/navigation/.*|.*/full_reduction_promotion/.*|.*/coupon/.*")] active[/#if]">
						<a href="javascript:;">
							<i class="fa fa-heart-o"></i>
							<span>${message("admin.index.contentGroup")}</span>
							<span class="pull-right-container">
								<i class="fa fa-angle-left pull-right"></i>
							</span>
						</a>
						<ul class="treeview-menu">
							<li[#if .main_template_name?matches(".*/navigation/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:navigation"]
									<a href="${base}/admin/navigation/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.navigation")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/article/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:article"]
									<a href="${base}/admin/article/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.article")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/article_category/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:articleCategory"]
									<a href="${base}/admin/article_category/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.articleCategory")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/article_tag/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:articleTag"]
									<a href="${base}/admin/article_tag/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.articleTag")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/friend_link/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:friendLink"]
									<a href="${base}/admin/friend_link/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.friendLink")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/ad_position/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:adPosition"]
									<a href="${base}/admin/ad_position/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.adPosition")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/ad/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:ad"]
									<a href="${base}/admin/ad/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.ad")}
									</a>
								[/@shiro.hasPermission]
							</li>
							[#--<li[#if .main_template_name?matches(".*/template/.*")] class="active"[/#if]>--]
								[#--[@shiro.hasPermission name = "admin:template"]--]
									[#--<a href="${base}/admin/template/list" target="iframe">--]
										[#--<i class="fa fa-circle-o"></i>--]
										[#--${message("admin.index.template")}--]
									[#--</a>--]
								[#--[/@shiro.hasPermission]--]
							[#--</li>--]
							<li[#if .main_template_name?matches(".*/cache/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:cache"]
									<a href="${base}/admin/cache/clear" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.cache")}
									</a>
								[/@shiro.hasPermission]
							</li>
						</ul>
					</li>
					[#break /]
				[/@shiro.hasPermission]
			[/#list]
			[#list ["admin:promotion", "admin:coupon", "admin:seo"] as permission]
				[@shiro.hasPermission name = permission]
					<li class="treeview[#if .main_template_name?matches(".*/promotion/.*|.*/coupon/.*|.*/seo/.*")] active[/#if]">
						<a href="javascript:;">
							<i class="fa fa-credit-card"></i>
							<span>${message("admin.index.marketingGroup")}</span>
							<span class="pull-right-container">
								<i class="fa fa-angle-left pull-right"></i>
							</span>
						</a>
						<ul class="treeview-menu">
							<li[#if .main_template_name?matches(".*/promotion/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:promotion"]
									<a href="${base}/admin/promotion/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.promotion")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/coupon/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:coupon"]
									<a href="${base}/admin/coupon/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.coupon")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/seo/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:seo"]
									<a href="${base}/admin/seo/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.seo")}
									</a>
								[/@shiro.hasPermission]
							</li>
						</ul>
					</li>
					[#break /]
				[/@shiro.hasPermission]
			[/#list]
			[#list ["admin:setting", "admin:area", "admin:paymentMethod", "admin:shippingMethod", "admin:deliveryCorp", "admin:paymentPlugin", "admin:storagePlugin", "admin:loginPlugin", "admin:promotionPlugin", "admin:admin", "admin:role", "admin:message", "admin:auditLog"] as permission]
				[@shiro.hasPermission name = permission]
					<li class="treeview[#if .main_template_name?matches(".*/setting/.*|.*/area/.*|.*/payment_method/.*")] active[/#if]">
						<a href="javascript:;">
							<i class="fa fa-wrench"></i>
							<span>${message("admin.index.systemGroup")}</span>
							<span class="pull-right-container">
								<i class="fa fa-angle-left pull-right"></i>
							</span>
						</a>
						<ul class="treeview-menu">
							<li[#if .main_template_name?matches(".*/setting/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:setting"]
									<a href="${base}/admin/setting/edit" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.setting")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/area/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:area"]
									<a href="${base}/admin/area/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.area")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/payment_method/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:paymentMethod"]
									<a href="${base}/admin/payment_method/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.paymentMethod")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/shipping_method/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:shippingMethod"]
									<a href="${base}/admin/shipping_method/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.shippingMethod")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/delivery_corp/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:deliveryCorp"]
									<a href="${base}/admin/delivery_corp/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.deliveryCorp")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/payment_plugin/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:paymentPlugin"]
									<a href="${base}/admin/payment_plugin/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.paymentPlugin")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/storage_plugin/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:storagePlugin"]
									<a href="${base}/admin/storage_plugin/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.storagePlugin")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/login_plugin/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:loginPlugin"]
									<a href="${base}/admin/login_plugin/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.loginPlugin")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/promotion_plugin/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:promotionPlugin"]
									<a href="${base}/admin/promotion_plugin/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.promotion_plugin")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/admin/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:admin"]
									<a href="${base}/admin/admin/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.admin")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/role/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:role"]
									<a href="${base}/admin/role/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.role")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/message/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:message"]
									<a href="${base}/admin/message/send" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.send")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/message/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:message"]
									<a href="${base}/admin/message/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.message")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/message/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:message"]
									<a href="${base}/admin/message/draft" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.draft")}
									</a>
								[/@shiro.hasPermission]
							</li>
							<li[#if .main_template_name?matches(".*/audit_log/.*")] class="active"[/#if]>
								[@shiro.hasPermission name = "admin:auditLog"]
									<a href="${base}/admin/audit_log/list" target="iframe">
										<i class="fa fa-circle-o"></i>
										${message("admin.index.auditLog")}
									</a>
								[/@shiro.hasPermission]
							</li>
						</ul>
					</li>
				[#break /]
				[/@shiro.hasPermission]
			[/#list]
		</ul>
	</section>
</aside>