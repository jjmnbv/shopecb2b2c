package net.shopec.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.shopec.FileType;
import net.shopec.Message;
import net.shopec.service.FileService;

/**
 * Controller - 文件
 * 
 */
@Controller("adminFileController")
@RequestMapping("/admin/file")
public class FileController extends BaseController {

	@Inject
	private FileService fileService;

	/**
	 * 上传
	 */
	@PostMapping("/upload")
	public @ResponseBody Map<String, Object> upload(FileType fileType, MultipartFile file) {
		Map<String, Object> data = new HashMap<>();
		if (fileType == null || file == null || file.isEmpty()) {
			data.put("message", ERROR_MESSAGE);
			data.put("state", ERROR_MESSAGE);
			return data;
		}
		if (!fileService.isValid(fileType, file)) {
			data.put("message", Message.warn("admin.upload.invalid"));
			data.put("state", message("admin.upload.invalid"));
			return data;
		}
		String url = fileService.upload(fileType, file, false);
		if (StringUtils.isEmpty(url)) {
			data.put("message", Message.warn("admin.upload.error"));
			data.put("state", message("admin.upload.error"));
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("state", "SUCCESS");
		data.put("url", url);
		return data;
	}

}