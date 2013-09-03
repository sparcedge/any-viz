any-viz
=======

Configurable query builder for turbinedb

## Usage

    lein run <optional config>

## Configuration

    {
        "mode": "dev", // dev, prod
        "display-name": "AnyViz", // branding
        "turbinedb-url": "http://localhost:8080",
        "turbinedb-database": "db",
        "turbinedb-collection": "coll",
        "turbinedb-segments": ["segment1","segment2","segment3"], // segment avaiable for query
        "server-port": 9000
    }