package com.sahil.ecom.repository;

import com.sahil.ecom.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(String name);

    List<Category> findAllByName(String name);

    boolean existsByName(String name);


    @Query(value = """
            SELECT COUNT(category.name)
            FROM category
            WHERE category.name =:catName  and category.parent_id is NULL;""",nativeQuery = true)
    int checkUniqueAtRoot(String catName);


    @Query(value = """
            SELECT * FROM category WHERE parent_id IS NULL
            """,nativeQuery = true)
    List<Category> findRootCategories();


    @Modifying
    @Query(value = """
            UPDATE Category SET name =:categoryName WHERE Id =:id
            """)
    int updateCategoryName(@Param("categoryName") String categoryName,@Param("id")Long id);
}
