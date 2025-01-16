# 1. Sintaxis básica de Thymeleaf

La principal diferencia entre Thymeleaf y el HTML estándar es el uso de atributos especiales que Thymeleaf interpreta y reemplaza cuando procesa el archivo. 

Estos atributos empiezan con el **prefijo th:**, que es la forma en que Thymeleaf indica que un atributo debe ser procesado dinámicamente.

Ejemplo de un archivo HTML estándar:
```
<a href="/productos/nuevo">Nuevo Producto</a>
```

Ejemplo de un archivo Thymeleaf:

```
<a th:href="@{/productos/nuevo}">Nuevo Producto</a>
```

Aquí, el atributo th:href indica que el valor de href será procesado por Thymeleaf. 

El **@{}** es una forma de construir URLs dinámicamente.

# 2. Expresiones de Thymeleaf
Aquí están las expresiones más comunes que se usan en las plantillas Thymeleaf.

## 2.1 Enlaces dinámicos con @{}
Usamos la expresión @{} para generar URLs relativas, especialmente cuando estamos trabajando con rutas de Spring MVC.

```
<a th:href="@{/productos/editar/{id}(id=${producto.id})}">Editar</a>
```

- **@{}:** Indica que el contenido dentro de los corchetes {} debe ser interpretado y transformado en una URL.
- **id=${producto.id}:** Esta es una expresión de Thymeleaf que reemplaza ${producto.id} con el valor del campo id del objeto producto.
  
## 2.2 Expresiones de Thymeleaf para variables (${})
La expresión ${...} se usa para evaluar y mostrar datos dinámicos. En el caso de Thymeleaf, producto.nombre, producto.precio, etc., son propiedades de un objeto Java que pasas desde el controlador a la vista.

```
<td th:text="${producto.nombre}"></td>
```

**${producto.nombre}:** Thymeleaf reemplaza esta expresión por el valor de la propiedad nombre del objeto producto.

## 2.3 Condicionales con th:if y th:unless
Estas son expresiones condicionales que puedes usar para mostrar u ocultar elementos HTML basados en ciertas condiciones.

**th:if:** Solo renderiza el contenido si la expresión es true.

```
<p th:if="${producto.precio > 1000}">Este producto es caro</p>
```

**th:unless:** Es lo opuesto de th:if, renderiza el contenido si la expresión es false.

```
<p th:unless="${producto.precio > 1000}">Este producto es barato</p>
```

## 2.4 Iteración con th:each
La expresión th:each se utiliza para iterar sobre colecciones y generar múltiples elementos basados en una lista de objetos.

```
<tr th:each="producto : ${productos}">
    <td th:text="${producto.nombre}"></td>
    <td th:text="${producto.precio}"></td>
    <td>
        <a th:href="@{/productos/editar/{id}(id=${producto.id})}">Editar</a>
        <a th:href="@{/productos/eliminar/{id}(id=${producto.id})}">Eliminar</a>
    </td>
</tr>
```

**th:each="producto : ${productos}":** Itera sobre la lista de productos y para cada producto, crea una nueva fila (<tr>).

**producto.nombre, producto.precio, etc.**, son los valores de cada producto dentro de la iteración.

## 2.5 Formularios con th:action y th:field
Cuando usas formularios con Thymeleaf, **th:action y th:field** son muy comunes. 

Te permiten enlazar el formulario a una URL específica y manejar los datos del formulario respectivamente.

```
<form th:action="@{/productos/guardar}" th:object="${producto}" method="post">
    <label for="nombre">Nombre:</label>
    <input type="text" th:field="*{nombre}" required />
    <label for="precio">Precio:</label>
    <input type="text" th:field="*{precio}" required />
    <label for="sku">SKU:</label>
    <input type="text" th:field="*{sku}" required />
    <button type="submit">Guardar</button>
</form>
```

- **th:action="@{/productos/guardar}":** Define la URL a la que se enviará el formulario cuando se envíe.
- **th:object="${producto}":** Especifica el objeto que se utilizará para enlazar los campos del formulario. Aquí, el objeto producto se pasa desde el controlador y se usa para completar el formulario con valores predeterminados si es necesario.
- **th:field="*{nombre}":** Enlaza el campo de entrada con la propiedad nombre del objeto producto. Cuando el formulario se envíe, el valor del campo nombre se guardará en el objeto producto.
  
## 2.6 Valores por defecto con th:value
Si necesitas establecer un valor inicial o predeterminado en un campo, puedes usar th:value.

```
<input type="text" th:value="${producto.nombre}" />
```

Esto establece el valor del campo de entrada en el valor de producto.nombre.

## 2.7. Utility Objects

El símbolo # indica que estás accediendo a un utility object. Algunos de los más comunes en Thymeleaf son:

- **#fields:** Para trabajar con formularios y validaciones.
- #dates:** Para manejar fechas y operaciones relacionadas.
- **#numbers: Para trabajar con números y formateo.
- **#strings:** Para manipular cadenas (por ejemplo, concatenar, dividir, etc.).
- **#lists:** Para trabajar con listas.
- **#maps:** Para manejar mapas (diccionarios).
- **#temporals:** Para trabajar con fechas y tiempos usando la API de Java 8+.

# 3. Resumen de las expresiones clave

- **${...}:** Se utiliza para mostrar el valor de una propiedad del modelo.
- **@{...}:** Se utiliza para generar URLs dinámicas, especialmente para hacer referencia a rutas en Spring MVC.
- **th:each="...":** Se usa para iterar sobre colecciones, como listas de objetos.
- **th:if="..." y th:unless="...":** Se usan para renderizar contenido basado en condiciones.
- **th:action="..." y th:field="...":** Se usan en formularios para enlazar datos y establecer acciones de envío.

# 4. Flujo básico de Thymeleaf
- El controlador Spring MVC pasa datos al modelo (por ejemplo, una lista de productos).
- Thymeleaf procesa el archivo HTML y sustituye las expresiones dinámicas (${...}, @{...}, etc.) con valores reales.
- El navegador recibe el HTML renderizado y lo muestra al usuario.
