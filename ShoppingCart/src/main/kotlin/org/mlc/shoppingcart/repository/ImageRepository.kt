package org.mlc.shoppingcart.repository

import org.mlc.shoppingcart.model.Image
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository: JpaRepository<Image, Long> {

}