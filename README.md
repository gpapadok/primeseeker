# Primeseeker
Distributed Fermat primality test for finding large prime numbers implemented in Clojure with master/slave architecture.<br/>

Primeseeker is the master. A REST API that serves integers to workers, accepts the result of the primality test and stores it.<br/>
Primeworker is the slave. It sequentially requests integers, runs the Fermat primality test and sends back the results.

Run the server with `clj -M:run` in the primeseeker folder.<br/>
Run workers with `clj -M:run` in the primeworker folder.<br/>
Run tests with `clj -M:test`.
