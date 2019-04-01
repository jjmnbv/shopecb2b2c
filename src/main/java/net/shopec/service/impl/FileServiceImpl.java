package net.shopec.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import net.shopec.FileType;
import net.shopec.Setting;
import net.shopec.plugin.StoragePlugin;
import net.shopec.service.FileService;
import net.shopec.service.PluginService;
import net.shopec.util.SystemUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service - 文件
 * 
 */
@Service
public class FileServiceImpl implements FileService {

	@Inject
	private ServletContext servletContext;
	@Inject
	private TaskExecutor taskExecutor;
	@Inject
	private PluginService pluginService;

	/**
	 * 添加文件上传任务
	 * 
	 * @param storagePlugin
	 *            存储插件
	 * @param path
	 *            上传路径
	 * @param file
	 *            上传文件
	 * @param contentType
	 *            文件类型
	 */
	private void addUploadTask(final StoragePlugin storagePlugin, final String path, final File file, final String contentType) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				upload(storagePlugin, path, file, contentType);
			}
		});
	}

	/**
	 * 上传文件
	 * 
	 * @param storagePlugin
	 *            存储插件
	 * @param path
	 *            上传路径
	 * @param file
	 *            上传文件
	 * @param contentType
	 *            文件类型
	 */
	private void upload(StoragePlugin storagePlugin, String path, File file, String contentType) {
		Assert.notNull(storagePlugin, "notNull");
		Assert.hasText(path, "hasText");
		Assert.notNull(file, "notNull");
		Assert.hasText(contentType, "hasText");

		try {
			storagePlugin.upload(path, file, contentType);
		} finally {
			FileUtils.deleteQuietly(file);
		}
	}

	public boolean isValid(FileType fileType, MultipartFile multipartFile) {
		Assert.notNull(fileType, "notNull");
		Assert.notNull(multipartFile, "notNull");
		Assert.state(!multipartFile.isEmpty(), "state");

		Setting setting = SystemUtils.getSetting();
		if (setting.getUploadMaxSize() != null && setting.getUploadMaxSize() != 0 && multipartFile.getSize() > setting.getUploadMaxSize() * 1024L * 1024L) {
			return false;
		}
		String[] uploadExtensions;
		switch (fileType) {
		case media:
			uploadExtensions = setting.getUploadMediaExtensions();
			break;
		case file:
			uploadExtensions = setting.getUploadFileExtensions();
			break;
		default:
			uploadExtensions = setting.getUploadImageExtensions();
			break;
		}
		if (ArrayUtils.isNotEmpty(uploadExtensions)) {
			return FilenameUtils.isExtension(StringUtils.lowerCase(multipartFile.getOriginalFilename()), uploadExtensions);
		}
		return false;
	}

	public String upload(FileType fileType, MultipartFile multipartFile, boolean async) {
		Assert.notNull(fileType, "notNull");
		Assert.notNull(multipartFile, "notNull");
		Assert.state(!multipartFile.isEmpty(), "state");

		Setting setting = SystemUtils.getSetting();
		String uploadPath;
		Map<String, Object> model = new HashMap<>();
		model.put("uuid", UUID.randomUUID().toString());
		switch (fileType) {
		case media:
			uploadPath = setting.resolveMediaUploadPath(model);
			break;
		case file:
			uploadPath = setting.resolveFileUploadPath(model);
			break;
		default:
			uploadPath = setting.resolveImageUploadPath(model);
			break;
		}
		try {
			String destPath = uploadPath + UUID.randomUUID() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			for (StoragePlugin storagePlugin : pluginService.getStoragePlugins(true)) {
				File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");
				multipartFile.transferTo(tempFile);
				String contentType = multipartFile.getContentType();
				if (async) {
					addUploadTask(storagePlugin, destPath, tempFile, contentType);
				} else {
					upload(storagePlugin, destPath, tempFile, contentType);
				}
				return storagePlugin.getUrl(destPath);
			}
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return null;
	}

	public String upload(FileType fileType, MultipartFile multipartFile) {
		Assert.notNull(fileType, "notNull");
		Assert.notNull(multipartFile, "notNull");
		Assert.state(!multipartFile.isEmpty(), "state");

		return upload(fileType, multipartFile, true);
	}

	public String uploadLocal(FileType fileType, MultipartFile multipartFile) {
		Assert.notNull(fileType, "notNull");
		Assert.notNull(multipartFile, "notNull");
		Assert.state(!multipartFile.isEmpty(), "state");

		Setting setting = SystemUtils.getSetting();
		String uploadPath;
		Map<String, Object> model = new HashMap<>();
		model.put("uuid", UUID.randomUUID().toString());
		switch (fileType) {
		case media:
			uploadPath = setting.resolveMediaUploadPath(model);
			break;
		case file:
			uploadPath = setting.resolveFileUploadPath(model);
			break;
		default:
			uploadPath = setting.resolveImageUploadPath(model);
			break;
		}
		try {
			String destPath = uploadPath + UUID.randomUUID() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			File destFile = new File(servletContext.getRealPath(destPath));
			File destDir = destFile.getParentFile();
			if (destDir != null) {
				destDir.mkdirs();
			}
			multipartFile.transferTo(destFile);
			return destPath;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}