package net.shopec.config;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;

/**
 * 注入公共字段自动填充,任选注入方式即可
 */
@Component
public class MyMetaObjectHandler extends MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		Object createdDate = getFieldValByName("createdDate", metaObject);
		Object version = getFieldValByName("version", metaObject);
		if (createdDate == null && version == null) {
			setFieldValByName("createdDate", new Date(), metaObject);
			setFieldValByName("version", 0L, metaObject);
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		setFieldValByName("lastModifiedDate", new Date(), metaObject);
	}

}
