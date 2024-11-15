1. **User**
    - One-to-Many with **Address**: A user can have multiple addresses.
    - Many-to-Many with **Coupon**: Users can use multiple coupons, and a coupon can be used by multiple users.
    - One-to-Many with **Cart**: Each user has one cart.
    - One-to-Many with **Order**: A user can have multiple orders.
    - One-to-Many with **Review**: A user can leave multiple reviews.
    - One-to-Many with **Transaction**: A user can have multiple transactions.
    - One-to-One with **Wishlist**: Each user has one wishlist.
2. **Address**
    - Many-to-One with **User**: An address belongs to one user.
    - Many-to-One with **Order**: An order has one shipping address.
3. **Cart**
    - One-to-One with **User**: Each user has one cart.
    - One-to-Many with **CartItem**: A cart can contain multiple cart items.
4. **CartItem**
    - Many-to-One with **Cart**: A cart item belongs to one cart.
    - Many-to-One with **Product**: A cart item refers to one product.
5. **Product**
    - Many-to-One with **Category**: A product belongs to one category.
    - Many-to-One with **Seller**: A product is sold by one seller.
    - One-to-Many with **Review**: A product can have multiple reviews.
6. **Category**
    - Many-to-One with **Category**: A category can have a parent category (for subcategories).
7. **Coupon**
    - Many-to-Many with **User**: A coupon can be used by multiple users.
8. **Order**
    - Many-to-One with **User**: An order belongs to one user.
    - One-to-Many with **OrderItem**: An order can have multiple order items.
    - Many-to-One with **Address**: An order has one shipping address.
9. **OrderItem**
    - Many-to-One with **Order**: An order item belongs to one order.
    - Many-to-One with **Product**: An order item refers to one product.
10. **PaymentOrder**
    - Many-to-One with **User**: A payment order belongs to one user.
    - One-to-Many with **Order**: A payment order can include multiple orders.
11. **Seller**
    - One-to-One with **Address**: A seller has one pickup address.
    - One-to-Many with **Product**: A seller can sell multiple products.
    - One-to-Many with **Transaction**: A seller can be involved in multiple transactions.
12. **Transaction**
    - Many-to-One with **User**: A transaction is associated with one user.
    - Many-to-One with **Seller**: A transaction is associated with one seller.
    - One-to-One with **Order**: A transaction corresponds to one order.
13. **Review**
    - Many-to-One with **Product**: A review is for one product.
    - Many-to-One with **User**: A review is written by one user.
14. **Wishlist**
    - One-to-One with **User**: Each user has one wishlist.
    - Many-to-Many with **Product**: A wishlist can contain multiple products.
15. **VerificationCode**
    - One-to-One with **User**: A verification code can be associated with one user.
    - One-to-One with **Seller**: A verification code can be associated with one seller.
16. **SellerReport**
    - One-to-One with **Seller**: A report corresponds to one seller.
