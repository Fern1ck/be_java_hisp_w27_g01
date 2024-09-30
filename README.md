# Social MELI Grupo 01

El objetivo de este sprint es aplicar los contenidos dados hasta el momento durante el BOOTCAMP MeLi (Git, Java y Spring), con la finalidad de poder implementar una API REST a partir de un enunciado propuesto, una especificación de requisitos y documentación anexada

## Desarrolladores:
- [@Delfina Glavas](https://github.com/delfi85)
- [@Emilia Lascano](https://github.com/EmiLascano)
- [@Matias Gregorat](https://github.com/81866-Gregorat-Matias)
- [@Stephanie Castillo](https://github.com/Stephaaniie)
- [@Fernando Baldrich](https://github.com/Fern1ck)

La fecha de entrega y cierre es: Martes 01/10/2024, 16:00hs ARG.

## Funcionalidades del proyecto

## Dev:

- [@Stephanie Castillo](https://github.com/Stephaaniie)
  
- `Funcionalidad 1`: Poder realizar la acción de “Follow” (seguir) a un determinado usuario.

#### Metodo POST

```
  http://localhost:8080/users/{userId}/follow/{userIdToFollow}
```

```http
  Ejemplo:  /users/123/follow/234
```

| Response  |
| :-------- | 
| `Status Code 200 (todo OK) - bodyless or dto` | 
| `Status Code 400 (Bad Request) - bodyless or dto` | 

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `userId`      | `int` | **Required**. Número que identifica al usuario actual |
| `userIdToFollow`      | `int` | **Required**. Número que identifica al usuario a seguir |


## Dev:

- [@Matias Gregorat](https://github.com/81866-Gregorat-Matias)
  
#### Metodo GET

- `Funcionalidad 2`: Obtener el resultado de la cantidad de usuarios que siguen a un determinado vendedor.
```
  http://localhost:8080/users/{userId}/followers/count}
```

```http
  Ejemplo: /users/234/followers/count/
```

| Response  |
| :-------- | 
    
    "user_id": 234, 
    "user_name": "vendedor1",
    "followers_count": 35

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `userId`      | `int` | **Required**. Número que identifica a cada usuario. |

## Dev:

- [@Matias Gregorat](https://github.com/81866-Gregorat-Matias)
  
#### Metodo GET

- `Funcionalidad 3`: Obtener un listado de todos los usuarios que siguen a un determinado vendedor (¿Quién me sigue?).
```
  http://localhost:8080/users/{userId}/followers/list
```
```http
  Ejemplo: /users/234/followers/list
```

| Response  |
| :-------- | 
    
    "user_id": 234, 
    "user_name": "vendedor1", 
    "followers": [
     {
        "user_id": 4698,
        "user_name": "usuario1"
      },
      {
        "user_name": "usuario2" 
       },
       {
         "user_id": 2236,
         "user_name": "usuario3"
       }
    ]

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `userId`  | `int`    | **Required**. Número que identifica a cada usuario. |

## Dev:

- [@Delfina Glavas](https://github.com/delfi85)
  
#### Metodo GET

- `Funcionalidad 4`: Obtener  un listado de todos los vendedores a los cuales sigue un determinado usuario (¿A quién sigo?)
```
  http://localhost:8080/users/{userId}/followed/list
```

```http
  Ejemplo: /users/4698/followed/list
```

| Response  |
| :-------- | 
    
    "user_id": 4698,
    "user_name": "usuario1",
    "followed": [
                  {
                    "user_id": 234,
                    "user_name": "vendedor1"
                  },
                  {
                    "user_name": "vendedor2" 
                  },
                  {
                    "user_id": 6631,
                    "user_name": "vendedor3"
                  }
                ]
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `userId`      | `int` | **Required**. Número que identifica a cada usuario. |

## Dev:

- [@Stephanie Castillo](https://github.com/Stephaaniie)

#### Metodo POST
- `Funcionalidad 5`: Dar de alta una nueva publicación.
```
  http://localhost:8080/products/post
```
  | PAYLOAD  |
  | :-------- | 
      
        "user_id": 123,
        "date": "29-04-2021",
        "product": {
                    "product_id": 1,
                    "product_name": "Silla Gamer",
                    "type": "Gamer",
                    "brand": "Racer",
                    "color": "Red & Black",
                    "notes": "Special Edition"
                  },
        "category": 100, 
        "price": 1500.50
      
    
| Response  |
| :-------- | 
| `Status Code 200 (todo OK) - bodyless or dto` | 
| `Status Code 400 (Bad Request) - bodyless or dto` | 

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `userId`      | `int` | **Required**. Número que identifica a cada usuario. |
| `date`      | `LocalDate` | **Required**. Fecha de la publicación en formato dd-MM-yyyy. |
| `product_id`      | `int` | **Required**. Número identificatorio de un producto asociado a una publicación. |
| `product_name`      | `String` | **Required**. Cadena de caracteres que representa el nombre de un producto. |
| `type`      | `String` | **Required**. Cadena de caracteres que representa el tipo de un producto|
| `brand`      | `String` | **Required**. Cadena de caracteres que representa el tipo de un producto. |
| `color`      | `String` | **Required**. Cadena de caracteres que representa el color de un producto notes.|
| `note`      | `String` | **Required**. Cadena de caracteres para colocar notas u observaciones de un producto.|
| `category`      | `int` | **Required**. Identificador que sirve para conocer la categoría a la que pertenece un producto. Por ejemplo: 100: Sillas, 58: Teclados|
| `price`      | `double` | **Required**. Precio del producto.|

## Dev:

- [@Emilia Lascano](https://github.com/EmiLascano)

#### Metodo GET

- `Funcionalidad 6`: Obtener un listado de las publicaciones realizadas por los vendedores que un usuario sigue en las últimas dos semanas (para esto tener en cuenta ordenamiento por fecha, publicaciones más recientes primero).
```
  http://localhost:8080/products/followed/{userId}/list
```
```http
  Ejemplo: /products/followed/4698/list
```
| Response  |
| :-------- | 

        "user_id": 4698, 
        "posts": 
        [
          {
            “user_id”: 123,
            "post_id": 32,
            "date": "01-05-2021",
            "product": 
            {
              "product_id": 62,
              "product_name": "Headset RGB Inalámbrico",
              "type": "Gamer",
              "brand": "Razer",
              "color": "Green with RGB",
              "notes": "Sin Batería"
            },
            "category": 120,
            "price": 2800.69
            },
            {
              “user_id”: 234, 
              "post_id": 18,
              "date": "29-04-2021",
              "product":
                {
                  "product_id": 1,
                  "productName": "Silla Gamer",
                  "type": "Gamer",
                  "brand": "Racer",
                  "color": "Red & Black",
                  "notes": "Special Edition"
                  },
              "category": 100,
              "price": 15000.50
           } 
        ]

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `userId`      | `int` | **Required**. Número que identifica a cada usuario. |

## Dev:

- [@Emilia Lascano](https://github.com/EmiLascano)
  
#### Metodo POST
- `Funcionalidad 7`: Poder realizar la acción de “Unfollow” (dejar de seguir) a un determinado vendedor.
```
  http://localhost:8080/users/{userId}/unfollow/{userIdToUnfollow}
```
```http
  Ejemplo: /users/234/unfollow/123
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `userId`      | `int` | **Required**. Número que identifica a cada usuario. |
| `userIdToUnfollow`      | `int` | **Required**. Número que identifica al usuario a dejar de seguir |

## Dev:
- [@Fernando Baldrich](https://github.com/Fern1ck)

#### Metodo GET

- `Funcionalidad 8`: Ordenamiento alfabético ascendente y descendente.
  
```
  http://localhost:8080/users/{UserID}/followers/list?order=name_asc
  http://localhost:8080/users/{UserID}/followers/list?order=name_desc
  http://localhost:8080/users/{UserID}/followed/list?order=name_asc
  http://localhost:8080/users/{UserID}/followed/list?order=name_desc
```

| Order       | Description                       |
| :-----------| :-------------------------------- |
| `name_asc`  | **Alfabético ascendente.**        |
| `name_desc` | **Alfabético descendente.**       |

Nota: Este ordenamiento aplica solo para la funcionalidad 3 y 4.

## Dev:

- [@Delfina Glavas](https://github.com/delfi85)

#### Metodo GET
- `Funcionalidad 9`: Ordenamiento por fecha ascendente y descendente.
```
  http://localhost:8080//products/followed/{userId}/list?order=date_asc
  http://localhost:8080/products/followed/{userId}/list?order=date_desc
```
| Order       | Description                                          |
| :-----------| :--------------------------------------------------- |
| `date_asc`  | **Fecha ascendente (de más antigua a más nueva).**   |
| `date_desc` | **Fecha descendente (de más nueva a más antigua).**  |

Nota: Este ordenamiento aplica solo para la funcionalidad 6.

## Dev:
- [@Fernando Baldrich](https://github.com/Fern1ck)

#### Metodo POST
- `Funcionalidad 10`: Llevar a cabo la publicación de un nuevo producto en promoción.
```
  http://localhost:8080/products/promo-post
```
| PAYLOAD  |
| :-------- | 
    
    "user_id": 234,
    "date": "29-04-2021",
    "product":
    {
      "product_id": 1,
      "product_name": "Silla Gamer",
      "type": "Gamer",
      "brand": "Racer",
      "color": "Red & Black",
      "notes": "Special Edition"
    },
    "category": 100,
    "price": 1500.50,
    "has_promo": true,
    "discount": 0.25
    
| Response  |
| :-------- | 
| `Status Code 200 (todo OK) - bodyless or dto` | 
| `Status Code 400 (Bad Request) - bodyless or dto` | 

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `userId`      | `int` | **Required**. Número que identifica a cada usuario. |
| `date`      | `LocalDate` | **Required**. Fecha de la publicación en formato dd-MM-yyyy. |
| `product_id`      | `int` | **Required**. Número identificatorio de un producto asociado a una publicación. |
| `product_name`      | `String` | **Required**. Cadena de caracteres que representa el nombre de un producto. |
| `type`      | `String` | **Required**. Cadena de caracteres que representa el tipo de un producto. |
| `brand`      | `String` | **Required**. Cadena de caracteres que representa el tipo de un producto. |
| `color`      | `String` | **Required**. Cadena de caracteres que representa el color de un producto notes. |
| `note`      | `String` | **Required**. Cadena de caracteres para colocar notas u observaciones de un producto. |
| `category`      | `int` | **Required**. Identificador que sirve para conocer la categoría a la que pertenece un producto. Por ejemplo: 100: Sillas, 58: Teclados. |
| `price`      | `double` | **Required**. Precio del producto.|
| `has_promo`      | `boolean` | **Required**. Campo true o false para determinar si un producto está en promoción o no. |
| `discount`      | `double` | **Required**. En caso de que un producto estuviese en promoción ,establece el monto de descuento. |

## Devs:

- [@Delfina Glavas](https://github.com/delfi85)
- [@Emilia Lascano](https://github.com/EmiLascano)
- [@Matias Gregorat](https://github.com/81866-Gregorat-Matias)
- [@Stephanie Castillo](https://github.com/Stephaaniie)
- [@Fernando Baldrich](https://github.com/Fern1ck)
  
#### Metodo GET
- `Funcionalidad 11`: Obtener la cantidad de productos en promoción de un determinado vendedor.
```
  http://localhost:8080/products/promo-post/count?user_id={userId}
```
| Response  |
| :-------- | 

    "user_id" : 234, 
    "user_name": "vendedor1",
    "promo_products_count": 23

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `user_id`      | `int` | **Required**. Número que identifica a cada usuario. |
| `user_name`      | `String` | **Required**. Cadena de caracteres que representa el nombre del usuario. |
| `promo_products_count`      | `int` | **Required**. Cantidad numérica de productos en promoción de un determinado usuario. |


  
#### Metodo GET
- `Funcionalidad 12`: Obtener un listado de todos los productos en promoción de un determinado vendedor (OPCIONAL).
```
  http://localhost:8080/products/promo-post/list?user_id={userId}
```
| Response  |
| :-------- | 

    "user_id": 234,
    "user_name": "vendedor1",
    "posts": [
    {
      “user_id”: 234,
      "post_id": 18,
      "date": "29-04-2021",
      "product": 
      {
          "product_id": 1,
          "product_name": "Silla Gamer",
          "type": "Gamer",
          "brand": "Racer",
          "color": "Red & Black",
          "notes": "Special Edition"
      },
    "category": "100", 
    "price": 15000.50, 
    "has_promo": true,
    "discount": 0.25
    }]
    
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `userId`      | `int` | **Required**. Número que identifica a cada usuario. |
| `user_name`      | `int` | **Required**. Cadena de caracteres que representa el nombre del usuario. |
| `post_id`      | `int` | **Required**. Número identificatorio de cada una de las publicaciones. |
| `date`      | `LocalDate` | **Required**. Fecha de la publicación en formato dd-MM-yyyy. |
| `product_id`      | `int` | **Required**. Número identificatorio de un producto asociado a una publicación. |
| `product_name`      | `String` | **Required**. Cadena de caracteres que representa el nombre de un producto. |
| `type`      | `String` | **Required**. Cadena de caracteres que representa el tipo de un producto. |
| `brand`      | `String` | **Required**. Cadena de caracteres que representa el tipo de un producto. |
| `color`      | `String` | **Required**. Cadena de caracteres que representa el color de un producto notes. |
| `note`      | `String` | **Required**. Cadena de caracteres para colocar notas u observaciones de un producto. |
| `category`      | `int` | **Required**. Identificador que sirve para conocer la categoría a la que pertenece un producto. Por ejemplo: 100: Sillas, 58: Teclados. |
| `price`      | `double` | **Required**. Precio del producto.|
| `has_promo`      | `boolean` | **Required**. Campo true o false para determinar si un producto está en promoción o no. |
| `discount`      | `double` | **Required**. En caso de que un producto estuviese en promoción ,establece el monto de descuento. |


<details>
<summary> Funcionalidad 15: Activar una promocion de un posteo existente. </summary> 

## Dev:

- [@Matias Gregorat](https://github.com/81866-Gregorat-Matias)

#### Metodo PUT
```
  http://localhost:8080/products/posts/activate-promo
```

| Parameter  | Type     | Description                                         |
|:-----------|:---------|:----------------------------------------------------|
| `user_id`  | `int`    | **Required**. Número que identifica a cada usuario. |
| `post_id`  | `int`    | **Required**. Número que identifica al posteo.      |
| `discount` | `double` | **Required**. Número que el descuento a aplicar.    |

| Response  |
| :-------- | 
| `Status Code 200 (todo OK) - bodyless or dto` | 
| `Status Code 400 (Bad Request) - bodyless or dto` | 
</details>