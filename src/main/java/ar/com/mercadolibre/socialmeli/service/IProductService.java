package ar.com.mercadolibre.socialmeli.service;

import ar.com.mercadolibre.socialmeli.dto.request.PostDTO;
import ar.com.mercadolibre.socialmeli.dto.response.PostOkDTO;

public interface IProductService {
    PostOkDTO registerANewPublication(PostDTO postDTO);
}
