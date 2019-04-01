package net.shopec.restful;
import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.entity.Ad;
import net.shopec.exception.ServiceException;
import net.shopec.service.AdPositionService;
import net.shopec.service.AdService;

/**
* Created by CodeGenerator on 2018/09/11.
*/
@RestController
@RequestMapping("/api/ad")
public class AdController {
	
    @Inject
    private AdService adService;

    @Inject
	private AdPositionService adPositionService;
    
    @PostMapping("/add")
    public Result<Ad> add(Ad ad, Long adPositionId) {
    	ad.setAdPosition(adPositionService.find(adPositionId));
        adService.save(ad);
        throw new ServiceException("该手机号已被注册");
    }

    @PostMapping("/delete")
    public Result<Ad> delete(Long id) {
        adService.deleteById(id);
        return ResultUtils.success();
    }

    @PostMapping("/update")
    public Result<Ad> update(Ad ad, Long adPositionId) {
    	ad.setAdPosition(adPositionService.find(adPositionId));
    	
    	if (ad.getBeginDate() != null && ad.getEndDate() != null && ad.getBeginDate().after(ad.getEndDate())) {
    		throw new ServiceException("开始日期，结束日期不能为空!");
		}
		if (Ad.Type.text.equals(ad.getType())) {
			ad.setPath(null);
		} else {
			ad.setContent(null);
		}
		adService.update(ad);
        return ResultUtils.success();
    }

    @GetMapping("/detail")
    public Result<Ad> detail(Long id) {
        Ad ad = adService.selectById(id);
        return ResultUtils.success(ad);
    }

    @GetMapping("/list")
    public Result<Page<Ad>> list(@RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "20") Integer pageSize) {
        Pageable pageable = new Pageable(pageNumber,pageSize);
        Page<Ad> page = adService.findPage(pageable);
        return ResultUtils.success(page);
    }
}
