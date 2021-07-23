package com.mii.poc.inventory.dao;

import com.mii.poc.inventory.domain.Goods;
import com.mii.poc.inventory.domain.GoodsCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ErwinSn
 */
@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {
    
    List<Goods> findByCategory(GoodsCategory goodsCategory);
    
    Goods findByGoodsCode(String goodsCode);
    
    @Query("select g from Goods g " +
          "where lower(g.goodsName) like lower(concat('%', :searchTerm, '%')) "
            + "or g.goodsCode like concat('%', :searchTerm, '%') "
            + "or g.category.categoryName like concat('%', :searchTerm, '%')")
    List<Goods> search(String searchTerm);
    
}
