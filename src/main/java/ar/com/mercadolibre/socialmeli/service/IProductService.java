package ar.com.mercadolibre.socialmeli.service;

import ar.com.mercadolibre.socialmeli.dto.CreatePromoRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.CreatePromoResponseDTO;

public interface IProductService {
    CreatePromoResponseDTO createPromo(CreatePromoRequestDTO dto);
}
