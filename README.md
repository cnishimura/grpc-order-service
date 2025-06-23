# Proyecto: Procesamiento de Pedidos HACOM

## Objetivo del Proyecto
Este proyecto tiene como objetivo crear una aplicación simple de procesamiento de pedidos, integrando diversas tecnologías comunes en proyectos tipo Telco en HACOM. [cite_start]El propósito es evaluar la capacidad de poner en marcha un proyecto simple.

## Requisitos y Tecnologías Utilizadas

El proyecto se construirá cumpliendo con los siguientes requisitos:

* [cite_start]**Spring Boot**: Framework principal para el desarrollo de la aplicación.
* [cite_start]**Gradle**: Herramienta de automatización de construcción.
* [cite_start]**Java 17**: Versión del lenguaje de programación.
* [cite_start]**Spring Data MongoDB Reactive**: Para la interacción reactiva con MongoDB.
* [cite_start]**Spring Webflux**: Para construir APIs reactivas.
* [cite_start]**Spring Log4j2**: Para el manejo de logs, utilizando `log4j2.yml` en lugar de `log4j2.xml`.
* [cite_start]**Spring Actuator**: Para monitoreo y métricas Prometheus, incluyendo al menos un contador.
* [cite_start]**gRPC**: Para la creación de un servicio de inserción de pedidos, debe contar con ID del pedido, ID de cliente, número de teléfono del cliente y lista de ítems del pedido. [cite_start]La respuesta debe contar con el ID del pedido y un estado.
* [cite_start]**Akka Classic Actors**: Para el procesamiento asíncrono de pedidos. [cite_start]Se debe crear un actor que procese los pedidos ingresados por gRPC, y el actor debe enviar la respuesta gRPC cuando finalice de procesar el pedido.
* [cite_start]**MongoDB**: Base de datos NoSQL para almacenar la información de los pedidos. [cite_start]El actor finaliza el pedido insertando la información del pedido en MongoDB, con la siguiente estructura para la clase `Order`: `_id` (ObjectId), `orderId`, `customerId`, `customerPhoneNumber`, `status`, `items` (List<String>), y `ts` (OffsetDateTime).
* [cite_start]**Librería SMPP (fizzed/cloudhopper-smpp)**: Para el envío de SMS por SMPP. [cite_start]Se debe crear un cliente SMPP y enviar un SMS con el texto: "Your order " + `request.getOrderld()` + " has been processed", una vez el actor termina de procesar el pedido.
* [cite_start]**API REST (Spring Webflux)**: Un endpoint para consultar el estado del pedido, y un endpoint para consultar el total de pedidos por rango de fecha, usando `OffsetDateTime` para el rango.
* [cite_start]**Logs**: Insertar logs convenientemente en cualquier parte del código.

## Arquitectura del Proyecto

La arquitectura del proyecto se basa en un enfoque de microservicios y reactivo, utilizando los siguientes componentes:

1.  **Capa de Entrada (gRPC)**: Un servicio gRPC actúa como punto de entrada para la inserción de nuevos pedidos, recibiendo los datos definidos en el `.proto`.
2.  **Capa de Procesamiento (Akka Actors)**: Los pedidos recibidos por gRPC son enviados a un actor de Akka que los procesa de manera asíncrona.
3.  **Capa de Persistencia (MongoDB)**: Una vez que el actor finaliza el procesamiento, la información se persiste en MongoDB.
4.  **Capa de Notificación (SMPP)**: Se envía un SMS al cliente con el estado del pedido después de su procesamiento.
5.  **Capa de API (Spring Webflux)**: Expone endpoints RESTful para consultas de pedidos.
6.  **Monitoreo (Spring Actuator)**: Permite el monitoreo del estado de la aplicación y la exposición de métricas Prometheus.
7.  **Logging (Log4j2)**: Gestiona los logs de la aplicación.

## Estructura de Carpetas (Propuesta)

