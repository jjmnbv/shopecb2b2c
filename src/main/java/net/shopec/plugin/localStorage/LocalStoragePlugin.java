package net.shopec.plugin.localStorage;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import net.shopec.config.UploadConfig;
import net.shopec.plugin.StoragePlugin;

/**
 * Plugin - 本地文件存储
 * 
 */
@Component("localStoragePlugin")
public class LocalStoragePlugin extends StoragePlugin {

//	@Inject
//	private ServletContext servletContext;
	
	@Inject
	private UploadConfig uploadConfig;

	@Override
	public String getName() {
		return "本地文件存储";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "SHOPEC";
	}

	@Override
	public String getSiteUrl() {
		return "http://www.shopec.net";
	}

	@Override
	public String getInstallUrl() {
		return null;
	}

	@Override
	public String getUninstallUrl() {
		return null;
	}

	@Override
	public String getSettingUrl() {
		return "local_storage/setting";
	}

	@Override
	public void upload(String path, File file, String contentType) {
		//File destFile = new File(servletContext.getRealPath(path));
		File destFile = new File(uploadConfig.getUploadPath() + path);
		try {
			FileUtils.moveFile(file, destFile);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public String getUrl(String path) {
//		Setting setting = SystemUtils.getSetting();
//		return setting.getSiteUrl() + path;
		return path;
	}

}