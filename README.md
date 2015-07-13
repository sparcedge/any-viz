Any Viz
=======

A simple, query builder for [TurbineDB](https://github.com/sparcedge/turbinedb) written in [Clojure](http://clojure.org/).  

Allows you to construct queries, graph results, and see the JSON needed to execute the query.

## Prerequisites

* [TurbineDB](https://github.com/sparcedge/turbinedb)
* [Leiningen](http://leiningen.org/)

## Usage

```sh
$ cd <project folder>
$ lein run <optional config>
```

Also, you can use Leiningen's uberjar command to compile to a jar file

```sh
$ cd <project folder>
$ lein uberjar
$ java -jar ./target/anyviz-0.1.0-SNAPSHOT-standalone.jar
```

**Note**: the default [config file](resources/config.json) will run the server listening on port 9000.  
So, just load your browser and navigate to the server and port on which it's listening.

## Configuration

A default [configuration file](resources/config.json) is supplied which you can tune to your needs.  
This is what it looks like for reference:

```sh
{
  "mode": "dev", // dev, prod
  "display-name": "AnyViz", // branding
  "turbinedb-url": "http://localhost:8080",
  "server-port": 9000
}
```

## License

This project is Copyright (c) 2015 [SPARC](https://github.com/sparcedge/) and open sourced under the [MIT License](LICENSE.md).
