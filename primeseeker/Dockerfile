FROM clojure:temurin-24-tools-deps-alpine AS build

RUN mkdir -p /build

# TODO: Add dependencies before adding code (so they are cached)

COPY . /build

WORKDIR /build

RUN make build-uberjar

FROM eclipse-temurin:24-alpine

# TODO: Create non-root user to run service

RUN mkdir -p /service

COPY --from=build /build/target/primeseeker.jar /service/primeseeker.jar

WORKDIR /service

# TODO: Move to subdir
RUN touch primes.db

EXPOSE 3000

# TODO: Add healthcheck

CMD ["java", "-jar", "primeseeker.jar"]
