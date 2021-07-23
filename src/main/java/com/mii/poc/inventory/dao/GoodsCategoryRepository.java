package com.mii.poc.inventory.dao;

import com.mii.poc.inventory.domain.GoodsCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Erwin
 */
@Repository
public interface GoodsCategoryRepository extends JpaRepository<GoodsCategory, Long> {
    
    GoodsCategory findByCategoryCode(String categoryCode);
    
    @Query("select g from GoodsCategory g " +
          "where lower(g.categoryName) like lower(concat('%', :searchTerm, '%')) "
            + "or g.categoryCode like concat('%', :searchTerm, '%')")
    List<GoodsCategory> search(String searchTerm);
    
}
