package com.nosbielc.reactive.products.domains;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class Product {

  @NotBlank
  @Id
  private String code;
  @NotBlank
  private String brand;
  @NotBlank
  private String description;
  private String color;
  private long price;

}
