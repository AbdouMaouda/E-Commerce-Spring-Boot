package com.ecommerce.Project.repositories;

import com.ecommerce.Project.model.Cart;
import com.ecommerce.Project.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query("SELECT c FROM  Cart c where c.user.email=?1")
    Cart findCartByEmail(String email);
}
