package com.bfs.hibernateprojectdemo.domain.dto.product;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreationRequest {

    @NotBlank(message = "Name cannot be blank")
    String name;

    @NotBlank(message = "Description cannot be blank")
    String description;

    @NotNull(message = "Wholesale price cannot be null")
    Double wholesalePrice;

    @NotNull(message = "Retail price cannot be null")
    Double retailPrice;

    @NotNull(message = "Quantity cannot be null")
    Integer quantity;
}