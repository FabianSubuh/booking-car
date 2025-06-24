package org.bian.exc.carcatalog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import lombok.Data; // From Lombok
import lombok.NoArgsConstructor; // From Lombok

@Table(name = "CAR")
@Entity
@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Generates a no-argument constructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Model name is required")
    private String modelName;

    @NotBlank(message = "Brand is required")
    private String brand; // e.g., Hyundai

    @NotNull(message = "Production year is required") // Updated message
    @Positive(message = "Production year must be a positive number") // Updated message
    @Column(name = "prod_year")
    private Integer prodYear;

    private String color;
    private String features; // Comma-separated list or JSON string

    // For simplicity, we'll store image URLs. In a real app, this would point to S3/GCS.
    @Column(name = "image_url")
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL")
    private String imageUrl;

    public Car(String modelName, String brand, Integer prodYear, String color, String features, String imageUrl) {
        this.modelName = modelName;
        this.brand = brand;
        this.prodYear = prodYear;
        this.color = color;
        this.features = features;
        this.imageUrl = imageUrl;
    }
}
