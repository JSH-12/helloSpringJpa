package kr.ac.hansung.cse.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import kr.ac.hansung.cse.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * =====================================================================
 * ProductRepository - 데이터 접근 계층 (Repository Layer)
 * =====================================================================
 */
@Repository
public class ProductRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 모든 상품 목록 조회
     */
    public List<Product> findAll() {
        TypedQuery<Product> query = entityManager
                .createQuery("SELECT p FROM Product p LEFT JOIN FETCH p.category ORDER BY p.id ASC", Product.class);
        return query.getResultList();
    }

    /**
     * 상품명 키워드 검색 (부분 일치)
     */
    public List<Product> findByNameContaining(String keyword) {
        return entityManager.createQuery(
                        "SELECT p FROM Product p LEFT JOIN FETCH p.category " +
                                "WHERE p.name LIKE :keyword ORDER BY p.id ASC",
                        Product.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }

    /**
     * 카테고리 ID로 상품 조회
     */
    public List<Product> findByCategoryId(Long categoryId) {
        return entityManager.createQuery(
                        "SELECT p FROM Product p LEFT JOIN FETCH p.category " +
                                "WHERE p.category.id = :cid ORDER BY p.id ASC",
                        Product.class)
                .setParameter("cid", categoryId)
                .getResultList();
    }

    /**
     * ID로 단일 상품 조회
     */
    public Optional<Product> findById(Long id) {
        List<Product> result = entityManager
                .createQuery("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.id = :id", Product.class)
                .setParameter("id", id)
                .getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    /**
     * 상품 저장 (신규 생성)
     */
    public Product save(Product product) {
        entityManager.persist(product);
        return product;
    }

    /**
     * 상품 수정
     */
    public Product update(Product product) {
        return entityManager.merge(product);
    }

    /**
     * 상품 삭제
     */
    public void delete(Long id) {
        Product product = entityManager.find(Product.class, id);
        if (product != null) {
            entityManager.remove(product);
        }
    }
}