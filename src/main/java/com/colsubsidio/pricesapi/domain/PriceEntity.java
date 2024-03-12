package com.colsubsidio.pricesapi.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
//2.147.483.647 signed
//4.294.967.295 unsigned
@Data
@Entity
@Table(name="prices")
public class PriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    long brandId; //foreign key de la cadena del grupo (1 = ZARA).
    Date startDate;
    Date endDate; //rango de fechas en el que aplica el precio tarifa indicado.
    int priceList; //Identificador de la tarifa de precios aplicable.
    long productId; // Identificador código de producto.
    short priority; //Desambiguador de aplicación de precios. Si dos tarifas coinciden en un rago de fechas se aplica la de mayor prioridad (mayor valor numérico).
    BigDecimal price; //precio final de venta.
    String curr; // iso de la moneda.
}