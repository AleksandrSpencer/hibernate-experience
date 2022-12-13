package org.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@Entity
@Table(name = "users_products")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Embeddable
    public static class Id implements Serializable {
        static final Long serialVersionUID = 1L;
        @Column(name = "user_id")
        Long userId;

        @Column(name = "product_id")
        Long productId;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id = (Id) o;
            return userId.equals(id.userId) &&
                    productId.equals(id.productId);
        }


        @Override
        public int hashCode() {
            return Objects.hash(userId * 31, productId * 73);
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }
    }


    @EmbeddedId
    private Id id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @Column(name = "price")
    private BigDecimal price;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user_id=" + id.userId +
                ", product_id=" + id.productId +
                ", price=" + price +
                '}';
    }
}