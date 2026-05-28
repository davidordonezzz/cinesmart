# compilo con la version de java 21 con jdk
FROM eclipse-temurin:21-jdk AS build
# como es docker (linux) trabajo con /app
WORKDIR /app
# copio estos archivos para que docker pueda ejecutar este programa
COPY .mvn/ .mvn/
# copio estos archivos 
COPY mvnw pom.xml ./
# le doy permisoso
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B
COPY src/ src/
RUN ./mvnw package -DskipTests -B

# ejecuto en el puerto 8080
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copia el .jar generado en la etapa de compilacion y lo renombra como app.jar
COPY --from=build /app/target/*.jar app.jar
# Indicamos que la aplicacion usara el puerto 8080
EXPOSE 8080
# Al arrancar el contenedor, ejecutamos la aplicacion con java -jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
