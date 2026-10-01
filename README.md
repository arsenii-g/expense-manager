# Expense Manager

Aplicación de escritorio para registrar gastos, organizarlos por categorías, consultar estadísticas y repartir cuentas entre varias personas. Incluye calendario, límites de gasto, alertas e importación CSV/JSON. Desarrollada con Java, JavaFX y Jackson.

## Requisitos

- JDK 21 y un entorno de escritorio.
- Conexión a Internet durante la primera ejecución para descargar Maven y las dependencias.

## Ejecutar

```bash
./mvnw javafx:run
```

En Windows, utiliza `mvnw.cmd javafx:run`.

Crea una categoría en la pestaña **Categorías** y registra un gasto en **Mis Gastos**. Utiliza las demás pestañas para consultar estadísticas, crear cuentas compartidas o configurar alertas. En **Archivo → Importar** puedes probar `examples/gastos.csv`.

La aplicación empieza sin datos y guarda tus cambios en archivos JSON en la carpeta del proyecto.

También dispone de una consola de texto:

```bash
./mvnw javafx:run "-Djavafx.args=-console"
```

Para registrar gastos desde la consola, crea primero alguna categoría en la interfaz gráfica.

## Pruebas

```bash
./mvnw test
```

La prueba incluida es básica; no cubre todas las funciones de la aplicación.

## Autor

Arsenii Gladkykh — [arsenii-g](https://github.com/arsenii-g).
