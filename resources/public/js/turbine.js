var makeQuery = function(db, coll, start, matches, groups, reducers) {
    var json = {
        db: db,
        coll: coll,
        match: matches,
        group: groups,
        reduce: reducers
    };

    if(start !== undefined){
        json.start = start;
    }

    return json;
}

var createMatch = function(entity, op, value) {
    var matchOp = {};
    matchOp[op] = value;
    var match = {};
    match[entity] = matchOp;
    return match;
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