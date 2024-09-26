package ar.com.mercadolibre.socialmeli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@Data
public class ExceptionDTO {
    private String message;
}