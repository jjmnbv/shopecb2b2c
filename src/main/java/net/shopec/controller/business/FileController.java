package net.shopec.controller.business;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import net.shopec.FileType;
import net.shopec.Results;
import net.shopec.service.FileService;

/**
 * Controller - 文件
 * 
 */
@Controller("businessFileController")
@RequestMapping("/business/file")
public class FileController extends BaseController {

	@Inject
	private FileService fileService;

	/**
	 * 上传
	 */
	@PostMapping("/upload")
	public ResponseEntity<?> upload(FileType fileType, MultipartFile file) {
		Map<String, Object> data = new HashMap<>();
		if (fileType == null || file == null || file.isEmpty()) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (!fileService.isValid(fileType, file)) {
			return Results.unprocessableEntity("business.upload.invalid");
		}
		String url = fileService.upload(fileType, file, false);
		if (StringUtils.isEmpty(url)) {
			return Results.unprocessableEntity("business.upload.error");
		}

		data.put("url", url);
		return ResponseEntity.ok(data);
	}

}