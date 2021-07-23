package com.mii.poc.inventory.controller;

import com.mii.poc.inventory.RestResponse;
import com.mii.poc.inventory.dao.GoodsCategoryRepository;
import com.mii.poc.inventory.domain.GoodsCategory;
import com.mii.poc.inventory.util.DataNotFoundException;
import com.mii.poc.inventory.util.DuplicateException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vinch
 */
@RestController
@RequestMapping("/api/inventory/goodscategory")
public class GoodsCategoryController {
    
    @Autowired
    private GoodsCategoryRepository categoryRepository;
    
    @GetMapping(path = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<List<GoodsCategory>>> getAll() {
        List<GoodsCategory> goodsCategories = categoryRepository.findAll();
        return ResponseEntity.ok().body(RestResponse.success(goodsCategories));
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<GoodsCategory>> get(@PathVariable Long id) {
        GoodsCategory category = categoryRepository.findById(id)
                .map(goods -> {
                    return goods;
                })
                .orElseThrow(() -> new DataNotFoundException(id));
        return ResponseEntity.ok().body(RestResponse.success(category));
    }

    @PostMapping(path = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<GoodsCategory>> save(@RequestBody GoodsCategory goodsCategory) {
        GoodsCategory existingGoods = categoryRepository.findByCategoryCode(goodsCategory.getCategoryCode());
        if (existingGoods != null) {
            throw new DuplicateException(goodsCategory.getCategoryCode());
        }
        LocalDateTime nowDateTime = LocalDateTime.now();
        goodsCategory.setUpdateAt(nowDateTime);
        goodsCategory.setCreatedAt(nowDateTime);
        GoodsCategory savedGoods = categoryRepository.save(goodsCategory);
        return ResponseEntity.ok().body(RestResponse.success(savedGoods));
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<GoodsCategory>> update(@RequestBody GoodsCategory newGoods, @PathVariable Long id) {
        GoodsCategory goodsCategory = categoryRepository.findById(id)
                .map(g -> {
                    g.setCategoryCode(newGoods.getCategoryCode());
                    g.setCategoryName(newGoods.getCategoryName());
                    g.setUpdateAt(LocalDateTime.now());
                    return g;
                })
                .orElseThrow(() -> new DataNotFoundException(id));
        categoryRepository.save(goodsCategory);
        return ResponseEntity.ok().body(RestResponse.success(goodsCategory));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<RestResponse<GoodsCategory>> delete(@PathVariable Long id) {
        GoodsCategory goods = categoryRepository.findById(id)
                .map(g -> {
                    return g;
                })
                .orElseThrow(() -> new DataNotFoundException(id));
        categoryRepository.delete(goods);
        return ResponseEntity.ok().body(RestResponse.success("Delete Successfully", goods));
    }
    
    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<List<GoodsCategory>>> search(@RequestParam String keywords) {
        List<GoodsCategory> goodsCategories = categoryRepository.search(keywords);
        return ResponseEntity.ok().body(RestResponse.success(goodsCategories));
    }
    
}
