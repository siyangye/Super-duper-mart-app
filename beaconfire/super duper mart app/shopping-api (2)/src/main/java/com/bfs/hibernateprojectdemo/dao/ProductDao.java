package com.bfs.hibernateprojectdemo.dao;

import com.bfs.hibernateprojectdemo.domain.Product;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class ProductDao extends AbstractHibernateDao<Product> {

    public ProductDao() {
        setClazz(Product.class);
    }

    public List<Product> getAllAvailableProducts() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
        Root<Product> root = criteria.from(Product.class);
        criteria.select(root).where(builder.greaterThan(root.get("quantity"), 0));
        return session.createQuery(criteria).getResultList();
    }

    public List<Product> getProductsByIds(List<Integer> ids) {
        return sessionFactory.getCurrentSession().createQuery("from Product where product_id in :ids", Product.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    public void updateProductQuantity(Product product, int quantity) {
        Session session = sessionFactory.getCurrentSession();
        Product currentProduct = session.get(Product.class, product.getProductId());
        if (currentProduct != null) {
            currentProduct.setQuantity(currentProduct.getQuantity() - quantity);
            session.update(currentProduct);
        }
    }

    public void updateProduct(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.update(product);
    }

    public Product getProductById(int id) {
        return this.findById(id);
    }

    public List<Product> getAllProducts() {
        return this.getAll();
    }

    public void addProduct(Product product) {
        this.add(product);
    }

}