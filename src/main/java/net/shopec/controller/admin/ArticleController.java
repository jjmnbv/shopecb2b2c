package net.shopec.controller.admin;

import java.util.HashSet;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.shopec.Message;
import net.shopec.Pageable;
import net.shopec.entity.Article;
import net.shopec.service.ArticleCategoryService;
import net.shopec.service.ArticleService;
import net.shopec.service.ArticleTagService;

/**
 * Controller - 文章
 * 
 */
@Controller("adminArticleController")
@RequestMapping("/admin/article")
public class ArticleController extends BaseController {

	@Inject
	private ArticleService articleService;
	@Inject
	private ArticleCategoryService articleCategoryService;
	@Inject
	private ArticleTagService articleTagService;

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		model.addAttribute("articleTags", articleTagService.findAll());
		return "admin/article/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public String save(Article article, Long articleCategoryId, Long[] articleTagIds, RedirectAttributes redirectAttributes) {
		article.setArticleCategory(articleCategoryService.find(articleCategoryId));
		article.setArticleTags(new HashSet<>(articleTagService.findList(articleTagIds)));
		if (!isValid(article)) {
			return ERROR_VIEW;
		}
		article.setHits(0L);
		articleService.save(article);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list";
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		model.addAttribute("articleTags", articleTagService.findAll());
		model.addAttribute("article", articleService.find(id));
		return "admin/article/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public String update(Article article, Long articleCategoryId, Long[] articleTagIds, RedirectAttributes redirectAttributes) {
		article.setArticleCategory(articleCategoryService.find(articleCategoryId));
		article.setArticleTags(new HashSet<>(articleTagService.findList(articleTagIds)));
		if (!isValid(article)) {
			return ERROR_VIEW;
		}
		articleService.update(article, "hits");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list";
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", articleService.findPage(pageable));
		return "admin/article/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public @ResponseBody Message delete(Long[] ids) {
		articleService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}