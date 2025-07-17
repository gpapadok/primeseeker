.PHONY: build-uberjar run-uberjar build-docker

build-uberjar:
	$(info -- Build uberjar --)
	clj -T:build uberjar

run-uberjar: build-uberjar
	$(info -- Run uberjar --)
	java -jar target/primeseeker.jar

build-docker:
	$(info -- Building docker image --)
	docker build . -t primeseeker
