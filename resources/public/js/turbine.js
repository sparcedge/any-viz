var makeQuery = function(db, coll, matches, groups, reducers) {
    return {
        db: db,
        coll: coll,
        match: matches,
        group: groups,
        reduce: reducers
    }
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