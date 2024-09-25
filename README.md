# Social MELI Grupo 01

El objetivo de este sprint es aplicar los contenidos dados hasta el momento durante el BOOTCAMP MeLi (Git, Java y Spring), con la finalidad de poder implementar una API REST a partir de un enunciado propuesto, una especificación de requisitos y documentación anexada

## Desarrolladores:
- Delfina Brenda Glavas
- Maria Emilia Lascano
- Matias Gregorat
- Stephanie Castillo
- Fernando Nicolas Baldrich

La fecha de entrega y cierre es: Martes 01/10/2024, 16:00hs ARG.

## Funcionalidades del proyecto

- `Funcionalidad 1`: Poder realizar la acción de “Follow” (seguir) a un determinado usuario.

```
  http://localhost:8080/users/{userId}/follow/{userIdToFollow}
```

- `Funcionalidad 2`: Obtener el resultado de la cantidad de usuarios que siguen a un determinado vendedor.
```
  http://localhost:8080/users/{userId}/followers/count}
```

- `Funcionalidad 3`: Obtener un listado de todos los usuarios que siguen a un determinado vendedor (¿Quién me sigue?).
```
  http://localhost:8080/users/{userId}/followers/list
```

- `Funcionalidad 4`: Obtener  un listado de todos los vendedores a los cuales sigue un determinado usuario (¿A quién sigo?)
```
  http://localhost:8080/users/{userId}/followed/list
```

- `Funcionalidad 5`: Dar de alta una nueva publicación.
```
  http://localhost:8080/products/post
```

- `Funcionalidad 6`: Obtener un listado de las publicaciones realizadas por los vendedores que un usuario sigue en las últimas dos semanas (para esto tener en cuenta ordenamiento por fecha, publicaciones más recientes primero).
```
  http://localhost:8080/products/followed/{userId}/list
```
- `Funcionalidad 7`: Poder realizar la acción de “Unfollow” (dejar de seguir) a un determinado vendedor.
```
  http://localhost:8080/users/{userId}/unfollow/{userIdToUnfollow}
```
- `Funcionalidad 8`: Ordenamiento alfabético ascendente y descendente.
```
  http://localhost:8080/users/{UserID}/followers/list?order=name_asc
  http://localhost:8080/users/{UserID}/followers/list?order=name_desc
  http://localhost:8080/users/{UserID}/followed/list?order=name_asc
  http://localhost:8080/users/{UserID}/followed/list?order=name_desc
```
- `Funcionalidad 9`: Ordenamiento por fecha ascendente y descendente.
```
  http://localhost:8080//products/followed/{userId}/list?order=date_asc
   http://localhost:8080/products/followed/{userId}/list?order=date_desc
```
- `Funcionalidad 10`: Llevar a cabo la publicación de un nuevo producto en promoción.
```
  http://localhost:8080/products/promo-post
```
- `Funcionalidad 11`: Obtener la cantidad de productos en promoción de un determinado vendedor.
```
  http://localhost:8080/products/promo-post/count?user_id={userId}
```
- `Funcionalidad 12`: Obtener un listado de todos los productos en promoción de un determinado vendedor (OPCIONAL).
```
  http://localhost:8080/products/promo-post/list?user_id={userId}
```
