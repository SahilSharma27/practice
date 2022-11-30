package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(String name);

    List<Category> findAllByName(String name);

    boolean existsByName(String name);


    @Query(value = """
            SELECT COUNT(CATEGORY.NAME)
            FROM CATEGORY
            WHERE CATEGORY.NAME =:catName  and CATEGORY.PARENT_ID is Null;""",nativeQuery = true)
    int checkUniqueAtRoot(String catName);


    @Query(value = """
            SELECT * FROM CATEGORY WHERE PARENT_ID IS NULL
            """,nativeQuery = true)
    List<Category> findRootCategories();
}
