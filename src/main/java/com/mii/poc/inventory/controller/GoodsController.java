package com.mii.poc.inventory.controller;

import com.mii.poc.inventory.RestResponse;
import com.mii.poc.inventory.dao.GoodsCategoryRepository;
import com.mii.poc.inventory.dao.GoodsRepository;
import com.mii.poc.inventory.domain.Goods;
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
 * @author ErwinSn
 */
@RestController
@RequestMapping("/api/inventory/goods")
public class GoodsController {
    
    @Autowired
    private GoodsCategoryRepository categoryRepository;
    
    @Autowired
    private GoodsRepository goodsRepository;
    
    @GetMapping(path = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<List<Goods>>> getAll() {
        List<Goods> accounts = goodsRepository.findAll();
        return ResponseEntity.ok().body(RestResponse.success(accounts));
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<Goods>> get(@PathVariable Long id) {
        Goods acc = goodsRepository.findById(id)
                .map(goods -> {
                    return goods;
                })
                .orElseThrow(() -> new DataNotFoundException(id));
        return ResponseEntity.ok().body(RestResponse.success(acc));
    }

    @PostMapping(path = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<Goods>> save(@RequestBody Goods goods) {
        Goods existingGoods = goodsRepository.findByGoodsCode(goods.getGoodsCode());
        if (existingGoods != null) {
            throw new DuplicateException(goods.getGoodsCode());
        }
        LocalDateTime nowDateTime = LocalDateTime.now();
        goods.setUpdateAt(nowDateTime);
        goods.setCreatedAt(nowDateTime);
        Goods savedGoods = goodsRepository.save(goods);
        return ResponseEntity.ok().body(RestResponse.success(savedGoods));
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<Goods>> update(@RequestBody Goods newGoods, @PathVariable Long id) {
        Goods goods = goodsRepository.findById(id)
                .map(g -> {
                    g.setGoodsCode(newGoods.getGoodsCode());
                    g.setGoodsName(newGoods.getGoodsName());
                    g.setUpdateAt(LocalDateTime.now());
                    return g;
                })
                .orElseThrow(() -> new DataNotFoundException(id));
        goodsRepository.save(goods);
        return ResponseEntity.ok().body(RestResponse.success(goods));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<RestResponse<Goods>> delete(@PathVariable Long id) {
        Goods goods = goodsRepository.findById(id)
                .map(g -> {
                    return g;
                })
                .orElseThrow(() -> new DataNotFoundException(id));
        goodsRepository.delete(goods);
        return ResponseEntity.ok().body(RestResponse.success("Delete Successfully", goods));
    }
    
    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<List<Goods>>> search(@RequestParam String keywords) {
        List<Goods> goods = goodsRepository.search(keywords);
        return ResponseEntity.ok().body(RestResponse.success(goods));
    }
    
    @GetMapping(path = "/bycategory/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<List<Goods>>> getByCategory(@PathVariable Long categoryId) {
        GoodsCategory category = categoryRepository.findById(categoryId)
                .map(goodsCategory -> {
                    return goodsCategory;
                })
                .orElseThrow(() -> new DataNotFoundException(categoryId));
        List<Goods> goods = goodsRepository.findByCategory(category);
        return ResponseEntity.ok().body(RestResponse.success(goods));
    }
    
}
