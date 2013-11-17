var makeQuery = function(db, coll, start, matches, groups, reducers) {
    var json = {
        db: db,
        coll: coll
    };

    if(start !== undefined){
        json.start = start;
    }

    json.match = matches;
    json.group = groups;
    json.reduce = reducers

    return json;
}

var createMatch = function(entity, op, value) {
    var matchOp = {};
    matchOp[op] = value;
    var match = {};
    match[entity] = matchOp;
    return matchOp;
};

var createReducer = function(entity, op) {
    var reduce = {};
    reduce[op] = entity;
    var reducer = {};
    reducer[(entity + "-" + op)] = reduce;
    return reducer;
};

var createDurationGroup = function(duration) {
    return {duration: duration};
}

var createSegmentGroup = function(segment) {
    return {segment: segment};
}